package com.pwc.modules.filing.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.sax.handler.RowHandler;
import com.google.common.collect.Maps;
import com.pwc.common.third.common.ExcelHandlerUtil;
import com.pwc.common.third.common.ExcelTitle;
import com.pwc.common.third.request.FilingBelleVat;
import com.pwc.common.third.request.FilingVat;
import com.pwc.common.utils.*;
import com.pwc.modules.filing.entity.FilingRecordEntity;
import com.pwc.modules.filing.entity.FilingRecordFileEntity;
import com.pwc.modules.filing.service.FilingRecordService;
import com.pwc.modules.sys.entity.SysDeptEntity;
import com.pwc.modules.sys.entity.SysFileEntity;
import com.pwc.modules.sys.service.SysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.pwc.modules.filing.dao.FilingBelleVatDao;
import com.pwc.modules.filing.entity.FilingBelleVatEntity;
import com.pwc.modules.filing.service.FilingBelleVatService;
import org.springframework.web.multipart.MultipartFile;


@Service("filingBelleVatService")
public class FilingBelleVatServiceImpl extends ServiceImpl<FilingBelleVatDao, FilingBelleVatEntity> implements FilingBelleVatService {
    @Autowired
    FilingRecordService filingRecordService;
    @Autowired
    SysDeptService sysDeptService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<FilingBelleVatEntity> page = this.page(
                new Query<FilingBelleVatEntity>().getPage(params),
                new QueryWrapper<FilingBelleVatEntity>()
        );

        return new PageUtils(page);
    }


    @Override
    public boolean initUpload(MultipartFile multipartFile) {
        String uploadUrl = ClassUtil.getClassPath()+ "statics/upload/";
        UploadFileEntity uploadFileEntity = UploadKitUtil.uploadFile(multipartFile, uploadUrl, true,true);

        ExcelUtil.readBySax(ResourceUtil.getStream(uploadFileEntity.getServerPath()), 0, vatRowHandler() );

        return false;
    }

    /**
     * row handler
     * @return
     */
    private RowHandler vatRowHandler() {
        Map<String, Integer> map = Maps.newHashMap();
        AtomicReference<List<Object>> vatAtomicReference = new AtomicReference<>();
        return (sheetIndex, rowIndex, rowlist) -> {
            FilingBelleVat filingVat = new FilingBelleVat();
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
                        FilingBelleVatEntity vatEntity = new FilingBelleVatEntity();
                        BeanUtil.copyProperties(filingVat, vatEntity);
                        //保存vat
                        baseMapper.insert(vatEntity);
                    }
                }
                //写入
//                vatExcelWriter(filingVat);
            }
        };
    }

    /**
     * 写文件
     * @param vat
     */
    private void vatExcelWriter(FilingBelleVat vat) {
        SysDeptEntity deptEntity = sysDeptService.getByTaxCode(vat.getStoresEin());
        if(null != deptEntity) {

        }
    }

}
