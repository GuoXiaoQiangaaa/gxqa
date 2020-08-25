package com.pwc.modules.filing.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.WorkbookUtil;
import cn.hutool.poi.excel.sax.handler.RowHandler;
import cn.hutool.setting.Setting;
import com.google.common.collect.Maps;
import com.pwc.common.third.common.ExcelHandlerUtil;
import com.pwc.common.third.common.ExcelTitle;
import com.pwc.common.third.request.FilingVat;
import com.pwc.common.utils.*;
import com.pwc.modules.filing.entity.FilingRecordEntity;
import com.pwc.modules.filing.entity.FilingRecordFileEntity;
import com.pwc.modules.filing.service.FilingRecordFileService;
import com.pwc.modules.filing.service.FilingRecordService;
import com.pwc.modules.sys.entity.SysDeptEntity;
import com.pwc.modules.sys.entity.SysFileEntity;
import com.pwc.modules.sys.service.SysDeptService;
import com.pwc.modules.sys.service.SysFileService;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.pwc.modules.filing.dao.FilingVatDao;
import com.pwc.modules.filing.entity.FilingVatEntity;
import com.pwc.modules.filing.service.FilingVatService;
import org.springframework.web.multipart.MultipartFile;


/**
 * @author zk
 */
@Service("filingVatService")
public class FilingVatServiceImpl extends ServiceImpl<FilingVatDao, FilingVatEntity> implements FilingVatService {

    @Autowired
    FilingRecordService filingRecordService;
    @Autowired
    SysDeptService sysDeptService;
    @Autowired
    private SysFileService sysFileService;
    @Autowired
    private FilingRecordFileService filingRecordFileService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<FilingVatEntity> page = this.page(
                new Query<FilingVatEntity>().getPage(params),
                new QueryWrapper<FilingVatEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public boolean initUpload(MultipartFile multipartFile, Long deptId) {
        Setting setting = new Setting("taxJson/info.setting");
        Long appleId = Long.parseLong(setting.get("apple_id"));
        Long belleId = Long.parseLong(setting.get("belle_id"));
        String uploadUrl = ClassUtil.getClassPath()+ "statics/upload/";
        UploadFileEntity uploadFileEntity = UploadKitUtil.uploadFile(multipartFile,uploadUrl, true, true);
        if (deptId.equals(belleId)) {
            SysFileEntity fileEntity = new SysFileEntity();
            BeanUtil.copyProperties(uploadFileEntity,fileEntity);
            sysFileService.save(fileEntity);
            List<Long> detpIdList = sysDeptService.queryDetpIdList(deptId);
            if (CollUtil.isEmpty(detpIdList)) {
                detpIdList = new ArrayList<>();
                detpIdList.add(deptId);
            }
            for (Long id : detpIdList) {
                FilingRecordEntity filingRecord = filingRecordService.getCurrentMonthFiling(id);
                if (null == filingRecord) {
                    continue;
                }
                FilingRecordFileEntity filingRecordFileEntity = new FilingRecordFileEntity();
                filingRecordFileEntity.setType(1);
                filingRecordFileEntity.setFileId(fileEntity.getId().longValue());
                filingRecordFileEntity.setFileType("VAT");
                filingRecordFileEntity.setFilingId(filingRecord.getFilingId());
                filingRecordFileService.save(filingRecordFileEntity);
                filingRecord.setUploadTime(DateUtil.date());
                filingRecord.setUploadStatus(FilingConstants.ProcessNodeStatus.PENDING_CONFIRMED.getValue());
                filingRecordService.updateById(filingRecord);
            }
//            List<SysDeptEntity> deptList = (List<SysDeptEntity>) sysDeptService.listByIds(detpIdList);
//            for (SysDeptEntity deptEntity : deptList) {
//                FilingRecordEntity filingRecord = filingRecordService.listByIds(deptEntity);
//            }
        } else {
            ZipSecureFile.setMinInflateRatio(-1.0d);
            Workbook workbook = WorkbookUtil.createBook(uploadFileEntity.getServerPath());
            int sheetIndex = workbook.getSheetIndex("数据填写");
            ExcelUtil.readBySax(ResourceUtil.getStream(uploadFileEntity.getServerPath()), sheetIndex, vatRowHandler());
        }

        return false;
    }


    @Override
    public FilingVatEntity getVatByTaxCodeAndCreateTime(String taxCode, String createTime) {
        return this.getOne(new QueryWrapper<FilingVatEntity>().eq("stores_ein", taxCode)
                .like("create_time", createTime).orderByDesc("create_time").last("limit 1"));
    }

    /**
     * row handler
     * @return
     */
    private RowHandler vatRowHandler() {
        Map<String, Integer> map = Maps.newHashMap();
        AtomicReference<List<Object>> vatAtomicReference = new AtomicReference<>();
        return (sheetIndex, rowIndex, rowlist) -> {
            FilingVat filingVat = new FilingVat();
            if (rowIndex == 0) {
                vatAtomicReference.set(rowlist);
            }
            if (rowIndex == 1) {
                List<Object> headerList = vatAtomicReference.get();
                String name = "";
                for (int i = 0; i < headerList.size(); i ++) {
                    if(null != headerList.get(i)) {
                        name = headerList.get(i).toString();
                    }
                    map.put(name + "-" + rowlist.get(i), i);
                }
            }
            if (0 == sheetIndex) {
                if (rowIndex != 1 && rowIndex != 0) {

                    //处理vat
                    ExcelHandlerUtil.vatHandler(rowIndex, rowlist, filingVat, map);
                    if (StrUtil.isNotBlank(filingVat.getStoresEin())) {
                        FilingVatEntity vatEntity = new FilingVatEntity();
                        BeanUtil.copyProperties(filingVat,vatEntity);
                        //保存vat
                        baseMapper.insert(vatEntity);

                        //写入
                        vatExcelWriter(filingVat, rowlist);
                    }
                }
            }
        };
    }



    /**
     * 写文件
     * @param vat
     */
    private void vatExcelWriter(FilingVat vat, List rowlist) {
        // 取id
        Setting setting = new Setting("taxJson/info.setting");
        Long smId = Long.parseLong(setting.get("sm_id"));
        SysDeptEntity deptEntity = sysDeptService.getByTaxCode(vat.getStoresEin());
            if(null != deptEntity) {
                FilingRecordEntity filingRecordEntity = filingRecordService.getCurrentMonthFiling(deptEntity.getDeptId());
                //存储路径
                String uploadUrl = ClassUtil.getClassPath()+ "statics/upload/";
                //文件名 要写入的路径
                String writeUrl = uploadUrl + deptEntity.getName() + "-" + IdUtil.objectId() +".xlsx";
                // 原文件 zara.xlsx
                String sourceUrl = ClassUtil.getClassPath()+ "statics/taxJson/zara.xlsx";
                // 等于smid smcp.xlsx
                if (deptEntity.getParentId().equals(smId)) {
                    sourceUrl = ClassUtil.getClassPath()+ "statics/taxJson/smcp.xlsx";
                }
                ExcelWriter excelWriter = ExcelUtil.getWriter(sourceUrl);

                excelWriter.setSheet(0);
                excelWriter.setCurrentRow(2);
                excelWriter.writeRow(rowlist);
                excelWriter.close();
                FileUtil.copy(sourceUrl, writeUrl, true);
                if (null != filingRecordEntity && filingRecordEntity.getUploadStatus().equals(FilingConstants.ProcessNodeStatus.PENDING_UPLOAD.getValue())) {
                    this.setFilingRecordFile(deptEntity, writeUrl,filingRecordEntity.getFilingId(), "VAT");
                    this.setFilingRecordFile(deptEntity, writeUrl,filingRecordEntity.getFilingId(), "SD");
                    this.setFilingRecordFile(deptEntity, writeUrl,filingRecordEntity.getFilingId(), "CIT");
                    filingRecordEntity.setUploadTime(DateUtil.date());
                    filingRecordEntity.setUploadStatus(FilingConstants.ProcessNodeStatus.PENDING_CONFIRMED.getValue());
                    filingRecordService.updateById(filingRecordEntity);
                }
            }
    }

    private void setFilingRecordFile(SysDeptEntity deptEntity, String writeUrl, Long filingId, String fileType){
        SysFileEntity fileEntity = new SysFileEntity();
        fileEntity.setOrigName(deptEntity.getName()+"_"+deptEntity.getTaxCode()+"_"+fileType+"_"+DateUtil.format(DateUtil.date(), "yyyyMM")+".xlsx");
        fileEntity.setUploadTime(DateUtil.date());
        fileEntity.setServerPath(writeUrl);
        sysFileService.save(fileEntity);
        FilingRecordFileEntity filingRecordFileEntity = new FilingRecordFileEntity();
        filingRecordFileEntity.setFilingId(filingId);
        filingRecordFileEntity.setType(1);
        filingRecordFileEntity.setFileId(fileEntity.getId().longValue());
//        filingRecordFileEntity.setFileType("VAT");
        filingRecordFileEntity.setFileType(fileType);
        filingRecordFileService.save(filingRecordFileEntity);
    }
}
