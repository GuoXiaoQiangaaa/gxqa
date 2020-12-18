package com.pwc.modules.filing.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.WorkbookUtil;
import cn.hutool.poi.excel.sax.handler.RowHandler;
import cn.hutool.setting.Setting;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.pwc.common.annotation.DataFilter;
import com.pwc.common.third.TtkTaxUtil;
import com.pwc.common.third.common.ExcelExtractUtil;
import com.pwc.common.third.common.ExcelHandlerUtil;
import com.pwc.common.third.request.FilingVat;
import com.pwc.common.utils.*;
import com.pwc.modules.filing.controller.FilingController;
import com.pwc.modules.filing.dao.FilingRecordDao;
import com.pwc.modules.filing.entity.*;
import com.pwc.modules.filing.service.*;
import com.pwc.modules.sys.entity.SysDeptEntity;
import com.pwc.modules.sys.entity.SysFileEntity;
import com.pwc.modules.sys.service.*;
import com.pwc.modules.sys.shiro.ShiroUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;


/**
 * @author zk
 */
@Service("filingRecordService")
public class FilingRecordServiceImpl extends ServiceImpl<FilingRecordDao, FilingRecordEntity> implements FilingRecordService {

    @Autowired
    private FilingProcessService filingProcessService;
    @Autowired
    private FilingNodeService filingNodeService;
    @Autowired
    private FilingVatService filingVatService;
    @Autowired
    private SysDeptService sysDeptService;
    @Autowired
    private FilingDistrictService filingDistrictService;
    @Autowired
    private SysFileService sysFileService;
    @Autowired
    private FilingRecordFileService filingRecordFileService;
    @Autowired
    private FilingOperateLogService filingOperateLogService;
    @Autowired
    private SysNoticeService sysNoticeService;
    @Autowired
    private SysUserRoleService sysUserRoleService;
    @Autowired
    private FilingThirdCityCodeService filingThirdCityCodeService;

    private Log log = Log.get(this.getClass());

    @Override
    @DataFilter(subDept = true, user = false, tableAlias = "f")
    public PageUtils queryPage(Map<String, Object> params) {
        String globalDate = (String) params.get("globalDate");
        if (StringUtils.isBlank(globalDate)) {
            globalDate = DateUtil.format(DateUtil.date(), "yyyy-MM");
        }
        String deptName = (String) params.get("deptName");
        String nodeName = (String) params.get("processNodeName");
        String processNodeStatus = (String) params.get("processNodeStatus");
        Integer nodeStatus = StringUtils.isNotBlank(processNodeStatus) && !"null".equals(processNodeStatus) ? Integer.parseInt(processNodeStatus) : null;
        QueryWrapper<FilingRecordEntity> queryWrapper = new QueryWrapper<>();
        //根据节点名区分查询节点状态
        if (StringUtils.isNotBlank(nodeName) && !Objects.isNull(nodeStatus)) {
            if (nodeName.equalsIgnoreCase(FilingConstants.ProcessNodeName.UPLOAD.name())) {
                queryWrapper.eq("upload_status", nodeStatus);
            } else if (nodeName.equalsIgnoreCase(FilingConstants.ProcessNodeName.CONFIRM.name())) {
                queryWrapper.eq("confirm_report_status", nodeStatus);
            } else if (nodeName.equalsIgnoreCase(FilingConstants.ProcessNodeName.DECLARE.name())) {
                queryWrapper.eq("declare_status", nodeStatus);
            } else if (nodeName.equalsIgnoreCase(FilingConstants.ProcessNodeName.DEDUCTION.name())) {
                queryWrapper.eq("deduction_status", nodeStatus);
            }
        }

        queryWrapper.like(StringUtils.isNotBlank(deptName), "dept_name", deptName)
                .like(StringUtils.isNotBlank(globalDate), "create_time", globalDate);
                //.apply(params.get(Constant.SQL_FILTER) != null, (String) params.get(Constant.SQL_FILTER));
//        IPage<FilingRecordEntity> page = this.page(
//                new Query<FilingRecordEntity>().getPage(params),
//                queryWrapper);
        int pageSise = MapUtil.getInt(params, "limit");
        int currPage = MapUtil.getInt(params, "page");
        Page<FilingRecordEntity> page1 = new Page<>(currPage, pageSise);
        page1.setRecords(baseMapper.queryPage(page1, queryWrapper));
//        PageUtils pageUtils = new PageUtils(baseMapper.queryPage(page1,queryWrapper),currPage, pageSise);

        return new PageUtils(page1);
    }

    @Override
    public R filingUpload(FilingRecordEntity filingRecord) {

        FilingProcessEntity filingProcessEntity = this.getCurrentProcess(filingRecord.getDeptId());
        if (null == filingProcessEntity) {
            return R.error("未设置申报流程");
        }
        if (checkExpired(filingRecord.getDeptId())) {
            return R.error("未设置节点或已超出申报上传节点时间");
        }
        filingRecord.setUploadStatus(FilingConstants.ProcessNodeStatus.FINISHED.getValue());
        if (filingProcessEntity.getConfirmReportStatus().equals(FilingConstants.CommonStatus.ENABLED.getValue())) {
            filingRecord.setConfirmReportStatus(FilingConstants.ProcessNodeStatus.PENDING_UPLOAD.getValue());
        } else {
            filingRecord.setDeclareStatus(FilingConstants.ProcessNodeStatus.PENDING_UPLOAD.getValue());
        }
        filingRecord.setUpdateTime(DateUtil.date());
        if (super.updateById(filingRecord)) {
            filingRecord = getById(filingRecord.getFilingId());
            Long userId = ShiroUtils.getUserId();
            filingOperateLogService.save(filingRecord, FilingConstants.OperateLogType.FILING.getValue(), userId, null);
            this.writeThirdTaxData(filingRecord);
            return R.ok().put("filingRecord", filingRecord);
        }
        return R.error("申报提交失败");
    }


    @Override
    public R confirmReport(FilingRecordEntity filingRecord) {

        if (checkExpired(filingRecord.getDeptId())) {
            return R.error("未设置节点或已超出确认报告节点时间");
        }
        filingRecord.setConfirmReportStatus(FilingConstants.ProcessNodeStatus.PENDING_AUDIT.getValue());
        filingRecord.setComfirmReportTime(DateUtil.date());
        if (super.updateById(filingRecord)) {
            filingRecord = getById(filingRecord.getFilingId());
            Long userId = ShiroUtils.getUserId();
            // 操作记录
            filingOperateLogService.save(filingRecord, FilingConstants.OperateLogType.COMFIRM.getValue(), userId, null);

            // 通知代办
            List<Long> roleIds = new ArrayList<>();
            roleIds.add(StatusDefine.RoleIds.ADMIN.getValue());
            roleIds.add(StatusDefine.RoleIds.GROUP_MANAGER.getValue());
            roleIds.add(StatusDefine.RoleIds.GROUP_AUDIT_USER.getValue());
            sysNoticeService.save(filingRecord.getFilingId(), filingRecord.getDeptId(), roleIds, "有申报待您审核");
            return R.ok().put("filingRecord", filingRecord);
        }
        return R.error("确认报告提交失败");
    }

    @Override
    public boolean confirmReportAudit(FilingRecordEntity filingRecord) {
        //审核通过修改申报节点为待上传状态
        if (filingRecord.getConfirmReportStatus().equals(FilingConstants.ProcessNodeStatus.FINISHED.getValue())) {
            filingRecord.setDeclareStatus(FilingConstants.ProcessNodeStatus.PENDING_UPLOAD.getValue());
        }
        Long userId = ShiroUtils.getUserId();
        filingOperateLogService.save(filingRecord, FilingConstants.OperateLogType.AUDIT.getValue(), userId, null);
        return super.updateById(filingRecord);
    }

    @Override
    public R filing(FilingRecordEntity filingRecord) {

        if (checkExpired(filingRecord.getDeptId())) {
            return R.error("未设置节点或已超出申报回执节点时间");
        }
        filingRecord.setDeclareStatus(FilingConstants.ProcessNodeStatus.FINISHED.getValue());
        filingRecord.setDeductionStatus(FilingConstants.ProcessNodeStatus.PENDING_UPLOAD.getValue());
        filingRecord.setDeclareTime(DateUtil.date());
        SysDeptEntity deptEntity = sysDeptService.getById(filingRecord.getDeptId());
        //是否支持第三方网报
//        if (checkThirdSupport(deptEntity.getCityCode())) {
//            //网报后下载申报回执
//            String filepath = TtkTaxUtil.downloadTaxReportPDF(deptEntity.getThirdOrgId());
//            SysFileEntity fileEntity = new SysFileEntity();
//            fileEntity.setUploadTime(DateUtil.date());
//            fileEntity.setServerPath(filepath);
//            sysFileService.save(fileEntity);
//            filingRecord.setDeductionAttachment(fileEntity.getId().toString());
//        }

        if (super.updateById(filingRecord)) {
            filingRecord = getById(filingRecord.getFilingId());
            Long userId = ShiroUtils.getUserId();
            filingOperateLogService.save(filingRecord, FilingConstants.OperateLogType.DECLARE.getValue(), userId, null);
            return R.ok().put("filingRecord", filingRecord);
        }
        return R.error("申报提交失败");
    }

    @Override
    public R deduction(FilingRecordEntity filingRecord) {

        if (checkExpired(filingRecord.getDeptId())) {
            return R.error("未设置节点或已超出扣款回执节点时间");
        }
        filingRecord.setDeductionStatus(FilingConstants.ProcessNodeStatus.FINISHED.getValue());
        filingRecord.setDeductionTime(DateUtil.date());
        if (super.updateById(filingRecord)) {
            filingRecord = getById(filingRecord.getFilingId());
            Long userId = ShiroUtils.getUserId();
            // 操作日志
            filingOperateLogService.save(filingRecord, FilingConstants.OperateLogType.DEDUCTION.getValue(), userId, null);
            // 通知代办
            List<Long> roleIds = new ArrayList<>();
            roleIds.add(StatusDefine.RoleIds.ADMIN.getValue());
            roleIds.add(StatusDefine.RoleIds.GROUP_MANAGER.getValue());
            roleIds.add(StatusDefine.RoleIds.GROUP_FILING_USER.getValue());
            roleIds.add(StatusDefine.RoleIds.BRANCH_MANAGER.getValue());
            roleIds.add(StatusDefine.RoleIds.BRANCH_FILING_USER.getValue());
            sysNoticeService.save(filingRecord.getFilingId(), filingRecord.getDeptId(), roleIds, "有申报等待结束");
            return R.ok().put("filingRecord", filingRecord);
        }
        return R.error("扣款提交失败");
    }

    @Override
    public FilingRecordEntity getCurrentMonthFiling(Long deptId) {
        return this.queryByDeptAndCreateTime(deptId, null);
    }

    @Override
    public FilingRecordEntity queryByDeptAndCreateTime(Long deptId, String createTime) {
        if (StrUtil.isBlank(createTime)) {
            String datePattern = "yyyy-MM";
            createTime = DateUtil.format(DateUtil.date(), datePattern);
        }
        return this.getOne(new QueryWrapper<FilingRecordEntity>().eq("dept_id", deptId)
                .like("create_time", createTime));
    }

    @Override
    public boolean updateStatus(Long filingId, Integer status) {
        FilingRecordEntity filingRecordEntity = getById(filingId);
        filingRecordEntity.setStatus(status);
        Integer operateLogType = null;
        Long userId = ShiroUtils.getUserId();
        if (2 == status) {
            operateLogType = FilingConstants.OperateLogType.FINISHED.getValue();
            // 通知代办
            List<Long> roleIds = new ArrayList<>();
            roleIds.add(StatusDefine.RoleIds.ADMIN.getValue());
            roleIds.add(StatusDefine.RoleIds.GROUP_MANAGER.getValue());
            sysNoticeService.save(filingRecordEntity.getFilingId(), filingRecordEntity.getDeptId(), roleIds, "有申报文件可销毁");
        } else if (-1 == status) {
            operateLogType = FilingConstants.OperateLogType.DESTROYED.getValue();
        }
        filingOperateLogService.save(filingRecordEntity, operateLogType, userId, null);
        return this.update(filingRecordEntity, new QueryWrapper<FilingRecordEntity>().eq("filing_id", filingId));
    }

    @Override
    public Integer countFilingByDeptIds(List<Long> deptIds, String query, Integer status, String date) {
        if (StrUtil.isBlank(date)) {
            String datePattern = "yyyy-MM";
            date = DateUtil.format(DateUtil.date(), datePattern);
        }
        QueryWrapper<FilingRecordEntity> wrapper = new QueryWrapper<FilingRecordEntity>().in("dept_id", deptIds).like("create_time", date);
        //不为空根据状态统计
        if (StrUtil.isNotBlank(query) && null != status) {
            wrapper.eq(query, status);
        }
        return count(wrapper);
    }


    @Override
    public Map<String, Object> extractPdf(Long filingId, MultipartFile file) throws IOException {
        Map<String, Object> result = MapUtil.newHashMap();
        String uploadUrl = ClassUtil.getClassPath() + "statics/upload/";
        String name = file.getOriginalFilename();
        String format = "VAT";
        assert name != null;
        if (name.contains("vat2")) {
            format = "VAT2";
        } else if (name.contains("vat1")) {
            format = "VAT1";
        } else if (name.contains("vat")) {
            format = "VAT";
        } else if (name.contains("sd")) {
            result.put("compare", "true");
            return result;
        }
        File pdfFile = FileUtil.file(uploadUrl, name);
        file.transferTo(pdfFile);
        List<SysFileEntity> filelist = this.getFilingFiles(filingId, 1);
        for (SysFileEntity fileEntity : filelist) {
            if (format.equals(fileEntity.getTaxType())) {
                Map<String, Object> params = Maps.newHashMap();
                params.put("format", format);
                params.put("type", "beijing");
                params.put("file", pdfFile);
                String pdf = HttpUtil.post("http://106.54.125.70:8101/pdf_extra/", params);
                if (JSONUtil.isJson(pdf)) {
                    JSONObject jsonObject = JSONUtil.parseObj(pdf);
                    result = ExcelExtractUtil.extractExcelForAppleBJ(jsonObject, fileEntity.getServerPath(), "VAT", format);
                }
            }
        }
        return result;
    }


    /**
     * 申报文件列表
     *
     * @param filingId
     * @param type
     * @return
     */
    @Override
    public List<SysFileEntity> getFilingFiles(Long filingId, int type) {
        List<FilingRecordFileEntity> fileIds = filingRecordFileService.list(new QueryWrapper<FilingRecordFileEntity>()
                .eq("filing_id", filingId).eq("type", type));
        List<SysFileEntity> fileEntityList = new ArrayList<>();
        for (FilingRecordFileEntity filingRecordFileEntity : fileIds) {
            SysFileEntity sysFileEntity = sysFileService.getById(filingRecordFileEntity.getFileId());
            sysFileEntity.setTaxType(filingRecordFileEntity.getFileType());
            sysFileEntity.setFilingRecordFileId(filingRecordFileEntity.getId());
            fileEntityList.add(sysFileEntity);
        }
        return fileEntityList;
    }


    @Override
    @DataFilter(subDept = true, user = false)
    public String queryThirdFilingUrl(Map<String, Object> params) {
        String globalDate = (String) params.get("globalDate");
        if (StringUtils.isBlank(globalDate)) {
            globalDate = DateUtil.format(DateUtil.date(), "yyyy-MM");
        }
        String deptName = (String) params.get("deptName");
        String nodeName = (String) params.get("processNodeName");
        String processNodeStatus = (String) params.get("processNodeStatus");
        Integer nodeStatus = StringUtils.isNotBlank(processNodeStatus) && !"null".equals(processNodeStatus) ? Integer.parseInt(processNodeStatus) : null;
        QueryWrapper<FilingRecordEntity> queryWrapper = new QueryWrapper<>();
        //根据节点名区分查询节点状态
        if (StringUtils.isNotBlank(nodeName) && !Objects.isNull(nodeStatus)) {
            if (nodeName.equalsIgnoreCase(FilingConstants.ProcessNodeName.UPLOAD.name())) {
                queryWrapper.eq("upload_status", nodeStatus);
            } else if (nodeName.equalsIgnoreCase(FilingConstants.ProcessNodeName.CONFIRM.name())) {
                queryWrapper.eq("confirm_report_status", nodeStatus);
            } else if (nodeName.equalsIgnoreCase(FilingConstants.ProcessNodeName.DECLARE.name())) {
                queryWrapper.eq("declare_status", nodeStatus);
            } else if (nodeName.equalsIgnoreCase(FilingConstants.ProcessNodeName.DEDUCTION.name())) {
                queryWrapper.eq("deduction_status", nodeStatus);
            }
        }

        queryWrapper.like(StringUtils.isNotBlank(deptName), "dept_name", deptName)
                .like(StringUtils.isNotBlank(globalDate), "create_time", globalDate);
                //.apply(params.get(Constant.SQL_FILTER) != null, (String) params.get(Constant.SQL_FILTER)

        IPage<FilingRecordEntity> page = this.page(
                new Query<FilingRecordEntity>().getPage(params),
                queryWrapper);

        List<Long> orgIds = new ArrayList<>();
        List<FilingRecordEntity> list = page.getRecords();
        for (FilingRecordEntity recordEntity : list) {
            SysDeptEntity sysDeptEntity = sysDeptService.getById(recordEntity.getDeptId());
            if (null != sysDeptEntity) {
                orgIds.add(sysDeptEntity.getThirdOrgId());
            }
        }

        return TtkTaxUtil.getWebUrlForShenBaoBatch(orgIds);
    }

    @Override
    public boolean submitFileByFileId(Long filingRecordFileId, Long filingId) {
        FilingRecordEntity filingRecord = this.getById(filingId);
        SysDeptEntity deptEntity = sysDeptService.getById(filingRecord.getDeptId());
        String datePattern = "yyyy-MM";
        String currentDateStr = DateUtil.format(DateUtil.date(), datePattern);

        // TODO check the file in this filingRecord？
        FilingRecordFileEntity filingRecordFileEntity = this.filingRecordFileService.getById(filingRecordFileId);

        SysFileEntity file = sysFileService.getById(filingRecordFileEntity.getFileId());
        String fileName = file.getOrigName().toUpperCase();

        try {
            if (fileName.contains(FilingConstants.Tax.VAT.name())) {
                // 获取第三方城市编码，如果没有为当前企业编码
                String thirdCityCode = filingThirdCityCodeService.getByDeptCityCode(deptEntity.getCityCode());
                String cityCode = StrUtil.isNotBlank(thirdCityCode) ? thirdCityCode : deptEntity.getCityCode();
                // now
                FilingVatEntity vatEntity = filingVatService.getVatByTaxCodeAndCreateTime(deptEntity.getTaxCode(), currentDateStr);

                //提交推送到第三方
                if (null != vatEntity) {
                    Workbook workbook = WorkbookUtil.createBook(file.getServerPath());
                    int sheetIndex = workbook.getSheetIndex("数据填写");
                    ExcelUtil.readBySax(ResourceUtil.getStream(file.getServerPath()), sheetIndex, vatRowHandler(vatEntity));
                    FilingVatEntity entity = filingVatService.getVatByTaxCodeAndCreateTime(deptEntity.getTaxCode(), currentDateStr);
                    FilingVat filingVat = new FilingVat();
                    BeanUtil.copyProperties(entity, filingVat);
                    filingVat.setCityCode(cityCode);
                    if (FilingConstants.TaxPayerType.GENERAL.getValue().equals(deptEntity.getThirdVatTaxpayer())) {
                        TtkTaxUtil.writeValueAddedTaxDataForGeneral(deptEntity.getThirdOrgId(), filingVat);
                    } else if (FilingConstants.TaxPayerType.SMALL.getValue().equals(deptEntity.getThirdVatTaxpayer())) {
                        TtkTaxUtil.writeValueAddedTaxDataForSmall(deptEntity.getThirdOrgId(), filingVat,deptEntity.getThirdSmallPeriod());
                    }
                } else {
                    Workbook workbook = WorkbookUtil.createBook(file.getServerPath());
                    int sheetIndex = workbook.getSheetIndex("数据填写");
                    ExcelUtil.readBySax(ResourceUtil.getStream(file.getServerPath()), sheetIndex, vatRowHandler());
                    vatEntity = filingVatService.getVatByTaxCodeAndCreateTime(deptEntity.getTaxCode(), currentDateStr);
                    FilingVat filingVat = new FilingVat();
                    BeanUtil.copyProperties(vatEntity, filingVat);
                    filingVat.setCityCode(cityCode);
                    if (FilingConstants.TaxPayerType.GENERAL.getValue().equals(deptEntity.getThirdVatTaxpayer())) {
                        TtkTaxUtil.writeValueAddedTaxDataForGeneral(deptEntity.getThirdOrgId(), filingVat);
                    } else if (FilingConstants.TaxPayerType.SMALL.getValue().equals(deptEntity.getThirdVatTaxpayer())) {
                        TtkTaxUtil.writeValueAddedTaxDataForSmall(deptEntity.getThirdOrgId(), filingVat, deptEntity.getThirdSmallPeriod());
                    }
                }
                // 推送附加税导致前端界面显示为空 TODO：附加税
//                TtkTaxUtil.writeSupertaxFromExcel(deptEntity.getThirdOrgId(), file.getServerPath());
            } else if (fileName.contains(FilingConstants.Tax.CIT.name())) {
                TtkTaxUtil.writeCITFromExcel(deptEntity.getThirdOrgId(), file.getServerPath());
                // now
//                FilingVatEntity citEntity = filingVatService.getVatByTaxCodeAndCreateTime(deptEntity.getTaxCode(), currentDateStr);
//                //提交推送到第三方
//                if (null != citEntity) {
//                    FilingVat filingCit = new FilingVat();
//                    BeanUtil.copyProperties(citEntity, filingCit);
//                    TtkTaxUtil.writeCITFromExcelByCellName(deptEntity.getThirdOrgId(), file.getServerPath(), filingCit);
//                } else {
//                    Workbook workbook = WorkbookUtil.createBook(file.getServerPath());
//                    int sheetIndex = workbook.getSheetIndex("数据填写");
//                    ExcelUtil.readBySax(ResourceUtil.getStream(file.getServerPath()), sheetIndex, vatRowHandler());
//                    citEntity = filingVatService.getVatByTaxCodeAndCreateTime(deptEntity.getTaxCode(), currentDateStr);
//                    FilingVat filingCit = new FilingVat();
//                    BeanUtil.copyProperties(citEntity, filingCit);
//                    TtkTaxUtil.writeCITFromExcelByCellName(deptEntity.getThirdOrgId(), file.getServerPath(), filingCit);
//                }

            } else if (fileName.contains(FilingConstants.Tax.SD.name())) {
//                TtkTaxUtil.writeSDFromExcel(deptEntity.getThirdOrgId(), file.getServerPath(), deptEntity.getThirdTaxPeriodCode());
//                TtkTaxUtil.writeSDFromExcelByCellName(deptEntity.getThirdOrgId(), file.getServerPath(), deptEntity.getThirdTaxPeriodCode());

                // now
                FilingVatEntity sdEntity = filingVatService.getVatByTaxCodeAndCreateTime(deptEntity.getTaxCode(), currentDateStr);
                //提交推送到第三方
                if (null != sdEntity) {
                    FilingVat filingSd = new FilingVat();
                    BeanUtil.copyProperties(sdEntity, filingSd);
                    TtkTaxUtil.writeSDFromExcelByCellName(deptEntity.getThirdOrgId(), file.getServerPath(), deptEntity.getThirdTaxPeriodCode(), filingSd);
                } else {
                    Workbook workbook = WorkbookUtil.createBook(file.getServerPath());
                    int sheetIndex = workbook.getSheetIndex("数据填写");
                    ExcelUtil.readBySax(ResourceUtil.getStream(file.getServerPath()), sheetIndex, vatRowHandler());
                    sdEntity = filingVatService.getVatByTaxCodeAndCreateTime(deptEntity.getTaxCode(), currentDateStr);
                    FilingVat filingSd = new FilingVat();
                    BeanUtil.copyProperties(sdEntity, filingSd);
                    TtkTaxUtil.writeSDFromExcelByCellName(deptEntity.getThirdOrgId(), file.getServerPath(), deptEntity.getThirdTaxPeriodCode(), filingSd);
                }

            } else if (fileName.contains(FilingConstants.Tax.FS.name())) {
                if (FilingConstants.AccountingStandards.BUSINESS.getValue().equals(deptEntity.getThirdAccountingStandards())) {
                    TtkTaxUtil.writeFSFromExcel(deptEntity.getThirdOrgId(), deptEntity.getTaxCode(), deptEntity.getName(), file.getServerPath());
                } else if (FilingConstants.AccountingStandards.SMALL.getValue().equals(deptEntity.getThirdAccountingStandards())) {
                     TtkTaxUtil.writeFinancialReportDataForSmall(deptEntity.getThirdOrgId(), deptEntity.getTaxCode(), deptEntity.getName(), file.getServerPath());
                }

            }
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
/*------------------------------------------------------------------------------ private methods --------------------------------------------------------------------------*/

    /**
     * 推第三方
     *
     * @param filingRecord
     */
    private void writeThirdTaxData(FilingRecordEntity filingRecord) {
        SysDeptEntity deptEntity = sysDeptService.getById(filingRecord.getDeptId());
        String datePattern = "yyyy-MM";
        String currentDateStr = DateUtil.format(DateUtil.date(), datePattern);
        Setting setting = new Setting("taxJson/info.setting");
        List<SysFileEntity> filelist = this.getFilingFiles(filingRecord.getFilingId(), 1);
        boolean thirdSupport = checkThirdSupport(deptEntity.getCityCode());
        for (SysFileEntity file : filelist) {
            String fileName = file.getOrigName().toUpperCase();

            if (thirdSupport) {
                log.info("third support:----");
                //  TODO:dept parent_id 26为苹果推送第三方数据防止报错
                log.info("setting apple id :" + setting.get("apple_id"));
                log.info("setting belle id :" + setting.get("belle_id"));
                Long appleId = Long.parseLong(setting.get("apple_id"));
                Long belleId = Long.parseLong(setting.get("belle_id"));
                if (deptEntity.getParentId().equals(appleId)) {
                    if (fileName.contains(FilingConstants.Tax.VAT.name())) {
                        if ("310000".equals(deptEntity.getCityCode())) {
                            TtkTaxUtil.writeValueAddedTaxDataGeneralForAppleSH(deptEntity.getThirdOrgId(), file.getServerPath(), "VAT");
                        } else {
                            TtkTaxUtil.writeValueAddedTaxDataGeneralForAppleBJ(deptEntity.getThirdOrgId(), file.getServerPath(), "VAT");
                        }
                    } else if (fileName.contains(FilingConstants.Tax.SD.name())) {
//                        TtkTaxUtil.writeValueAddedSdDataForApple(deptEntity.getThirdOrgId(), file.getServerPath(), "SD");
                        // now
                        FilingVatEntity sdEntity = filingVatService.getVatByTaxCodeAndCreateTime(deptEntity.getTaxCode(), currentDateStr);
                        //提交推送到第三方
                        if (null != sdEntity) {
                            FilingVat filingSd = new FilingVat();
                            BeanUtil.copyProperties(sdEntity, filingSd);
                            TtkTaxUtil.writeSDFromExcelByCellName(deptEntity.getThirdOrgId(), file.getServerPath(), deptEntity.getThirdTaxPeriodCode(), filingSd);
                        } else {
                            ExcelUtil.readBySax(ResourceUtil.getStream(file.getServerPath()), 0, vatRowHandler());
                            sdEntity = filingVatService.getVatByTaxCodeAndCreateTime(deptEntity.getTaxCode(), currentDateStr);
                            FilingVat filingSd = new FilingVat();
                            BeanUtil.copyProperties(sdEntity, filingSd);
                            TtkTaxUtil.writeSDFromExcelByCellName(deptEntity.getThirdOrgId(), file.getServerPath(), deptEntity.getThirdTaxPeriodCode(), filingSd);
                        }

                    } else if (fileName.contains(FilingConstants.Tax.FS.name())) {
                        if (FilingConstants.AccountingStandards.BUSINESS.getValue().equals(deptEntity.getThirdAccountingStandards())) {
                            TtkTaxUtil.writeFSFromExcel(deptEntity.getThirdOrgId(), deptEntity.getTaxCode(), deptEntity.getName(), file.getServerPath());
                        } else if (FilingConstants.AccountingStandards.SMALL.getValue().equals(deptEntity.getThirdAccountingStandards())) {
                            TtkTaxUtil.writeFinancialReportDataForSmall(deptEntity.getThirdOrgId(), deptEntity.getTaxCode(), deptEntity.getName(), file.getServerPath());
                        }
                    }
                } else if (deptEntity.getParentId().equals(belleId)) {
                    if (fileName.contains(FilingConstants.Tax.VAT.name())) {
                        // TODO something

                        //                        TtkTaxUtil.writeValueAddedTaxDataGeneralForBeLLE(deptEntity.getThirdOrgId(), file.getServerPath(), "VAT", 3);
                    }
                } else {
                    if (fileName.contains(FilingConstants.Tax.VAT.name())) {
                        // 获取第三方城市编码，如果没有为当前企业编码
                        String thirdCityCode = filingThirdCityCodeService.getByDeptCityCode(deptEntity.getCityCode());
                        String cityCode = StrUtil.isNotBlank(thirdCityCode) ? thirdCityCode : deptEntity.getCityCode();
                        // now
                        FilingVatEntity vatEntity = filingVatService.getVatByTaxCodeAndCreateTime(deptEntity.getTaxCode(), currentDateStr);

                        //提交推送到第三方
                        if (null != vatEntity) {
                            FilingVat filingVat = new FilingVat();
                            BeanUtil.copyProperties(vatEntity, filingVat);
                            filingVat.setCityCode(cityCode);
                            if (FilingConstants.TaxPayerType.GENERAL.getValue().equals(deptEntity.getThirdVatTaxpayer())) {
                                TtkTaxUtil.writeValueAddedTaxDataForGeneral(deptEntity.getThirdOrgId(), filingVat);
                            } else if (FilingConstants.TaxPayerType.SMALL.getValue().equals(deptEntity.getThirdVatTaxpayer())) {
                                TtkTaxUtil.writeValueAddedTaxDataForSmall(deptEntity.getThirdOrgId(), filingVat,deptEntity.getThirdSmallPeriod());
                            }
                        } else {
                            Workbook workbook = WorkbookUtil.createBook(file.getServerPath());
                            int sheetIndex = workbook.getSheetIndex("数据填写");
                            ExcelUtil.readBySax(ResourceUtil.getStream(file.getServerPath()), sheetIndex, vatRowHandler());
                            vatEntity = filingVatService.getVatByTaxCodeAndCreateTime(deptEntity.getTaxCode(), currentDateStr);
                            FilingVat filingVat = new FilingVat();
                            BeanUtil.copyProperties(vatEntity, filingVat);
                            filingVat.setCityCode(cityCode);
                            if (FilingConstants.TaxPayerType.GENERAL.getValue().equals(deptEntity.getThirdVatTaxpayer())) {
                                TtkTaxUtil.writeValueAddedTaxDataForGeneral(deptEntity.getThirdOrgId(), filingVat);
                            } else if (FilingConstants.TaxPayerType.SMALL.getValue().equals(deptEntity.getThirdVatTaxpayer())) {
                                TtkTaxUtil.writeValueAddedTaxDataForSmall(deptEntity.getThirdOrgId(), filingVat,deptEntity.getThirdSmallPeriod());
                            }
                        }
                    } else if (fileName.contains(FilingConstants.Tax.CIT.name())) {
                        TtkTaxUtil.writeCITFromExcel(deptEntity.getThirdOrgId(), file.getServerPath());

                    } else if (fileName.contains(FilingConstants.Tax.SD.name())) {
//                        TtkTaxUtil.writeSDFromExcel(deptEntity.getThirdOrgId(), file.getServerPath(), deptEntity.getThirdTaxPeriodCode());
//                        TtkTaxUtil.writeSDFromExcelByCellName(deptEntity.getThirdOrgId(), file.getServerPath(), deptEntity.getThirdTaxPeriodCode());
                        // now
                        FilingVatEntity sdEntity = filingVatService.getVatByTaxCodeAndCreateTime(deptEntity.getTaxCode(), currentDateStr);
                        //提交推送到第三方
                        if (null != sdEntity) {
                            FilingVat filingSd = new FilingVat();
                            BeanUtil.copyProperties(sdEntity, filingSd);
                            TtkTaxUtil.writeSDFromExcelByCellName(deptEntity.getThirdOrgId(), file.getServerPath(), deptEntity.getThirdTaxPeriodCode(), filingSd);
                        } else {
                            Workbook workbook = WorkbookUtil.createBook(file.getServerPath());
                            int sheetIndex = workbook.getSheetIndex("数据填写");
                            ExcelUtil.readBySax(ResourceUtil.getStream(file.getServerPath()), sheetIndex, vatRowHandler());
                            sdEntity = filingVatService.getVatByTaxCodeAndCreateTime(deptEntity.getTaxCode(), currentDateStr);
                            FilingVat filingSd = new FilingVat();
                            BeanUtil.copyProperties(sdEntity, filingSd);
                            TtkTaxUtil.writeSDFromExcelByCellName(deptEntity.getThirdOrgId(), file.getServerPath(), deptEntity.getThirdTaxPeriodCode(), filingSd);
                        }

                    } else if (fileName.contains(FilingConstants.Tax.FS.name())) {

                        if (FilingConstants.AccountingStandards.BUSINESS.getValue().equals(deptEntity.getThirdAccountingStandards())) {
                            TtkTaxUtil.writeFSFromExcel(deptEntity.getThirdOrgId(), deptEntity.getTaxCode(), deptEntity.getName(), file.getServerPath());
                        } else if (FilingConstants.AccountingStandards.SMALL.getValue().equals(deptEntity.getThirdAccountingStandards())) {
                            TtkTaxUtil.writeFinancialReportDataForSmall(deptEntity.getThirdOrgId(), deptEntity.getTaxCode(), deptEntity.getName(), file.getServerPath());
                        }
                    }
                }

            } else {
                Long smId = Long.parseLong(setting.get("sm_id"));
                // 等于smid 自动生成excel
                if (deptEntity.getParentId().equals(smId)) {
                    // 生成的文件名
                    String filename = deptEntity.getName() + "_" + DateUtil.today() + ".xlsx";
                    String sourceUrl = ClassUtil.getClassPath() + "taxJson/smcp.xlsx";
//                    String sourceUrl = "/Users/zk/Downloads/smcp.xlsx";
                    // 生成文件路径
                    String destUrl = ClassUtil.getClassPath() + filename;
//                    String destUrl = "/Users/zk/Downloads/"+filename;
                    ExcelWriter writer = ExcelUtil.getWriter(sourceUrl, "数据填写");
                    ExcelReader reader = ExcelUtil.getReader(file.getServerPath(), 1);
                    List<Object> row = reader.readRow(2);
                    writer.setCurrentRow(2);
                    writer.writeRow(row);
                    writer.flush();
                    // 复制到新文件
                    FileUtil.copy(sourceUrl, destUrl, true);
                    filingRecord.setConfirmReportStatus(FilingConstants.ProcessNodeStatus.PENDING_AUDIT.getValue());
                    SysFileEntity fileEntity = new SysFileEntity();
                    fileEntity.setOrigName(filename);
                    fileEntity.setServerPath(destUrl);
                    fileEntity.setNewName(filename);
                    fileEntity.setUploadTime(DateUtil.date());
                    sysFileService.save(fileEntity);
                    FilingRecordFileEntity filingRecordFileEntity = new FilingRecordFileEntity();
                    filingRecordFileEntity.setFilingId(filingRecord.getFilingId());
                    filingRecordFileEntity.setFileId(fileEntity.getId().longValue());
                    filingRecordFileEntity.setFileType("VAT");
                    // 报告确认
                    filingRecordFileEntity.setType(2);
                    filingRecordFileService.save(filingRecordFileEntity);
                }
            }
        }

    }


    /**
     * row handler
     *
     * @return
     */
    private RowHandler vatRowHandler() {
        Map<String, Integer> map = Maps.newHashMap();
        AtomicReference<List<Object>> vatAtomicReference = new AtomicReference<>();
        return (sheetIndex, rowIndex, rowlist) -> {
            FilingVat filingVatEntity = new FilingVat();
            if (rowIndex == 0) {
                vatAtomicReference.set(rowlist);
                return;
            }
            if (rowIndex == 1) {
                List<Object> headerList = vatAtomicReference.get();
                String name = "";
                for (int i = 0; i < headerList.size(); i++) {
                    Object obj = headerList.get(i);
                    if (null != obj && StrUtil.isNotBlank(obj.toString())) {
                        name = obj.toString();
                    }
                    map.put(name + "-" + rowlist.get(i), i);
                }
                return;
            }
//            if (0 == sheetIndex) {
                if (rowIndex != 1 && rowIndex != 0) {
                    //处理vat
                    ExcelHandlerUtil.vatHandler(rowIndex, rowlist, filingVatEntity, map);
                    if (StrUtil.isNotBlank(filingVatEntity.getStoresEin())) {
                        FilingVatEntity vatEntity = new FilingVatEntity();
                        BeanUtil.copyProperties(filingVatEntity, vatEntity);
                        filingVatService.save(vatEntity);
                    }
                }
//            }
        };
    }

    /**
     * update filing VAT Entity
     *
     * @param entity
     * @return
     */
    private RowHandler vatRowHandler(FilingVatEntity entity) {
        Map<String, Integer> map = Maps.newHashMap();
        AtomicReference<List<Object>> vatAtomicReference = new AtomicReference<>();
        return (sheetIndex, rowIndex, rowlist) -> {
            FilingVat filingVatEntity = new FilingVat();
            BeanUtil.copyProperties(filingVatEntity, entity);

            if (rowIndex == 0) {
                vatAtomicReference.set(rowlist);
                return;
            }
            if (rowIndex == 1) {
                List<Object> headerList = vatAtomicReference.get();
                String name = "";
                for (int i = 0; i < headerList.size(); i++) {
                    if (null != headerList.get(i)) {
                        name = headerList.get(i).toString();
                    }
                    map.put(name + "-" + rowlist.get(i), i);
                }
                return;
            }
//            if (0 == sheetIndex) {
                if (rowIndex != 1 && rowIndex != 0) {
                    //处理vat
                    ExcelHandlerUtil.vatHandler(rowIndex, rowlist, filingVatEntity, map);
                    if (StrUtil.isNotBlank(filingVatEntity.getStoresEin())) {
                        FilingVatEntity vatEntity = new FilingVatEntity();
                        BeanUtil.copyProperties(filingVatEntity, vatEntity);
                        filingVatService.updateById(vatEntity);
                    }
                }
//            }
        };
    }

    /**
     * 是否超出节点时间
     *
     * @param deptId
     * @return
     */
    private boolean checkExpired(long deptId) {
        FilingNodeEntity filingNodeEntity = this.getCurrentNode(deptId);
        if (null == filingNodeEntity) {
            return false;
        }
        //当前月的第几天
        Integer day = DateUtil.dayOfMonth(DateUtil.date());
        //节点设置时间
        Integer nodeDay = Integer.parseInt(filingNodeEntity.getDeclareDate());
        return nodeDay < day;
    }

    /**
     * 获得当前流程设置
     *
     * @return
     */
    private FilingProcessEntity getCurrentProcess(long deptId) {
        String datePattern = "yyyy-MM";
        String currentDateStr = DateUtil.format(DateUtil.date(), datePattern);
        return filingProcessService.queryByDeptAndCreateTime(deptId, currentDateStr);
    }

    /**
     * 获得当前节点设置
     *
     * @return
     */
    private FilingNodeEntity getCurrentNode(long deptId) {
        String datePattern = "yyyy-MM";
        String currentDateStr = DateUtil.format(DateUtil.date(), datePattern);
        return filingNodeService.queryByDeptAndCreateTime(deptId, currentDateStr);
    }

    /**
     * 当前code是否支持网报
     *
     * @param cityCode
     * @return
     */
    private boolean checkThirdSupport(String cityCode) {
        //是否支持第三方
        if (CollUtil.isNotEmpty(filingDistrictService.getByCityCode(cityCode))) {
            return true;
        }
        return false;
    }


}
