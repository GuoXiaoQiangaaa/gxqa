package com.pwc.modules.filing.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pwc.common.third.TtkTaxUtil;
import com.pwc.common.utils.*;
import com.pwc.common.validator.ValidatorUtils;
import com.pwc.modules.filing.entity.FilingRecordEntity;
import com.pwc.modules.filing.entity.FilingRecordFileEntity;
import com.pwc.modules.filing.entity.FilingVatEntity;
import com.pwc.modules.filing.service.*;
import com.pwc.modules.sys.controller.AbstractController;
import com.pwc.modules.sys.entity.SysDeptEntity;
import com.pwc.modules.sys.entity.SysFileEntity;
import com.pwc.modules.sys.service.SysDeptService;
import com.pwc.modules.sys.service.SysDeptTaxService;
import com.pwc.modules.sys.service.SysFileService;
import com.pwc.modules.sys.service.SysNoticeService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * 申报流程信息
 *
 * @author zk
 * @email
 * @date 2019-11-08 15:11:47
 */
@RestController
@RequestMapping("filing/record")
public class FilingRecordController extends AbstractController {
    @Autowired
    private FilingRecordService filingRecordService;
    @Autowired
    private SysDeptService sysDeptService;
    @Autowired
    private FilingDistrictService filingDistrictService;
    @Autowired
    private SysFileService sysFileService;
    @Autowired
    private FilingVatService filingVatService;
    @Autowired
    private FilingRecordFileService filingRecordFileService;
    @Autowired
    private SysDeptTaxService sysDeptTaxService;
    @Autowired
    private FilingOperateLogService filingOperateLogService;
    @Autowired
    private SysNoticeService sysNoticeService;


    /**
     * 上传
     *
     * @param file     文件
     * @param filingId 申报id
     * @param type     上传类型
     */
    @PostMapping(value = "/uploadAuditAttachment", consumes = "multipart/*", headers = "content-type=multipart/form-data")
    public R uploadAuditAttachment(MultipartFile file, Long filingId, Integer type) {
        String uploadUrl = ClassUtil.getClassPath() + "statics/upload/";
        FilingRecordEntity filingRecordEntity = filingRecordService.getById(filingId);
        if (null == filingRecordEntity) {
            return R.error("申报流程不存在");
        }
        UploadFileEntity uploadFile = UploadKitUtil.uploadFile(file, uploadUrl, true, true);
        SysFileEntity fileEntity = new SysFileEntity();
        BeanUtil.copyProperties(uploadFile, fileEntity);
        sysFileService.save(fileEntity);
        //上传完成保存文件和filing关联关系
        FilingRecordFileEntity filingRecordFileEntity = new FilingRecordFileEntity();
        filingRecordFileEntity.setFileId(fileEntity.getId().longValue());
        filingRecordFileEntity.setFilingId(filingId);
        filingRecordFileEntity.setType(type);
        filingRecordFileEntity.setFileType("AUDIT");
        filingRecordFileService.save(filingRecordFileEntity);
        // 操作记录申报流程
        filingOperateLogService.save(filingRecordEntity, FilingConstants.OperateLogType.UPLOAD_AUDIT.getValue(), getUserId(), type);
        return R.ok().put("file", fileEntity);
    }


    /**
     * 上传
     *
     * @param file     文件
     * @param filingId 申报id
     * @param type     上传类型
     */
    @PostMapping(value = "/upload", consumes = "multipart/*", headers = "content-type=multipart/form-data")
    public R fileUpload(MultipartFile file, Long filingId, Integer type) {
        String uploadUrl = ClassUtil.getClassPath() + "statics/upload/";
        String fileName = file.getOriginalFilename();
        String[] infos = StrUtil.split(fileName, "_");
        String taxCode = infos[1];
        String taxType = infos[2].toUpperCase();

        FilingRecordEntity filingRecordEntity = filingRecordService.getById(filingId);
        SysDeptEntity sysDeptEntity = sysDeptService.getById(filingRecordEntity.getDeptId());
        if (!taxCode.equals(sysDeptEntity.getTaxCode())) {
            return R.error("请确认税号是否是本公司报税信息");
        }
        List<Long> taxIds = sysDeptTaxService.getTaxIdByDeptId(filingRecordEntity.getDeptId());
        // 上传文件名是否匹配
        if (taxType.contains(FilingConstants.Tax.VAT.name()) || taxType.contains(FilingConstants.Tax.CIT.name())
                || taxType.contains(FilingConstants.Tax.FS.name()) || taxType.contains(FilingConstants.Tax.SD.name())) {
            // 企业是否包含此税种
            if (!taxIds.contains(FilingConstants.Tax.VAT.getValue().longValue()) && !taxIds.contains(FilingConstants.Tax.CIT.getValue().longValue())
                    && !taxIds.contains(FilingConstants.Tax.FS.getValue().longValue()) && !taxIds.contains(FilingConstants.Tax.SD.getValue().longValue())) {
                return R.error("企业不支持此税种 " + fileName);
            }
            UploadFileEntity uploadFile = UploadKitUtil.uploadFile(file, uploadUrl, true, true);
            SysFileEntity fileEntity = new SysFileEntity();
            BeanUtil.copyProperties(uploadFile, fileEntity);
            sysFileService.save(fileEntity);
            //上传完成保存文件和filing关联关系
            FilingRecordFileEntity filingRecordFileEntity = new FilingRecordFileEntity();
            filingRecordFileEntity.setFileId(fileEntity.getId().longValue());
            filingRecordFileEntity.setFilingId(filingId);
            filingRecordFileEntity.setType(type);
            filingRecordFileEntity.setFileType(taxType);
            filingRecordFileService.save(filingRecordFileEntity);
            // 操作记录申报流程
            filingOperateLogService.save(filingRecordEntity, FilingConstants.OperateLogType.UPLOAD.getValue(), getUserId(), type);
            return R.ok().put("file", fileEntity);
        }
        return R.error("未匹配到文件名，请确认文件命名规范！");
    }

    /**
     * 流程上传文件列表
     *
     * @param filingId
     * @return
     */
    @GetMapping("/files")
    @RequiresPermissions("filing:record:list")
    public R filingFiles(Long filingId, Integer type) {

        return R.ok().put("files", filingRecordService.getFilingFiles(filingId, type));
    }

    /**
     * 列表
     */
    @GetMapping("/list")
    @RequiresPermissions("filing:record:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = filingRecordService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{filingId}")
    @RequiresPermissions("filing:record:info")
    public R info(@PathVariable("filingId") Long filingId) {
        FilingRecordEntity filingRecord = filingRecordService.getById(filingId);
        SysDeptEntity sysDeptEntity = sysDeptService.getById(filingRecord.getDeptId());
        //是否支持第三方
        if (CollUtil.isEmpty(filingDistrictService.getByCityCode(sysDeptEntity.getCityCode()))) {
            filingRecord.setThird(0);
        } else {
            filingRecord.setThird(1);
        }
//        filingRecord.setUploadFile(sysFileService.getById(filingRecord.getUploadAttachment()));
//        filingRecord.setReportFile(sysFileService.getById(filingRecord.getComfirmReportAttachment()));
//        filingRecord.setDeclareFile(sysFileService.getById(filingRecord.getDeclareAttachment()));
//        filingRecord.setDeductionFile(sysFileService.getById(filingRecord.getDeductionAttachment()));
        return R.ok().put("filingRecord", filingRecord);
    }

    /**
     * 保存
     */
    @PutMapping("/save")
    @RequiresPermissions("filing:record:save")
    public R save(Long deptId) {
        SysDeptEntity sysDeptEntity = sysDeptService.getById(deptId);
        if (StatusDefine.DeptType.BRANCH_OFFICE.getValue() == sysDeptEntity.getType()) {
            FilingRecordEntity filingRecordEntity = new FilingRecordEntity();
            filingRecordEntity.setUploadStatus(FilingConstants.ProcessNodeStatus.PENDING_UPLOAD.getValue());
            filingRecordEntity.setConfirmReportStatus(FilingConstants.ProcessNodeStatus.NOT_STARTED.getValue());
            filingRecordEntity.setDeclareStatus(FilingConstants.ProcessNodeStatus.NOT_STARTED.getValue());
            filingRecordEntity.setDeductionStatus(FilingConstants.ProcessNodeStatus.NOT_STARTED.getValue());
            filingRecordEntity.setDeptId(deptId);
            filingRecordEntity.setDeptName(sysDeptEntity.getName());
            filingRecordService.save(filingRecordEntity);
            // 操作日志
            filingOperateLogService.save(filingRecordEntity, FilingConstants.OperateLogType.CREATE.getValue(), getUserId(), null);
            // 通知代办
            List<Long> roleIds = new ArrayList<>();
            roleIds.add(StatusDefine.RoleIds.ADMIN.getValue());
            roleIds.add(StatusDefine.RoleIds.GROUP_MANAGER.getValue());
            roleIds.add(StatusDefine.RoleIds.GROUP_FILING_USER.getValue());
            roleIds.add(StatusDefine.RoleIds.BRANCH_MANAGER.getValue());
            roleIds.add(StatusDefine.RoleIds.BRANCH_FILING_USER.getValue());
            sysNoticeService.save(filingRecordEntity.getFilingId(), filingRecordEntity.getDeptId(), roleIds, "有申报文件待上传");
        }
        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("filing:record:update")
    public R update(@RequestBody FilingRecordEntity filingRecord) {
        ValidatorUtils.validateEntity(filingRecord);
        filingRecordService.updateById(filingRecord);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @RequiresPermissions("filing:record:delete")
    public R delete(@RequestBody Long[] filingIds) {
        filingRecordService.removeByIds(Arrays.asList(filingIds));

        return R.ok();
    }

    /**
     * 申报上传提交
     *
     * @param filingRecord
     * @return
     */
    @PostMapping("/uploadSubmit")
    @RequiresPermissions("filing:record:update")
    public R filingUpload(@RequestBody FilingRecordEntity filingRecord) {
        return filingRecordService.filingUpload(filingRecord);
    }

    /**
     * 确认报告提交
     *
     * @param filingRecord
     * @return
     */
    @PostMapping("/reportSubmit")
    @RequiresPermissions("filing:record:update")
    public R confirmReport(@RequestBody FilingRecordEntity filingRecord) {
        return filingRecordService.confirmReport(filingRecord);
    }

    /**
     * 确认报告审核
     *
     * @param filingRecord
     * @return
     */
    @PostMapping("/reportAudit")
    @RequiresPermissions("filing:record:audit")
    public R confirmReportAudit(@RequestBody FilingRecordEntity filingRecord) {
        if (filingRecordService.confirmReportAudit(filingRecord)) {
            return R.ok().put("filingRecord", filingRecordService.getById(filingRecord.getFilingId()));
        }
        return R.error("审核失败");
    }

    /**
     * 申报回执提交
     *
     * @param filingRecord
     * @return
     */
    @PostMapping("/declareSubmit")
    @RequiresPermissions("filing:record:update")
    public R filing(@RequestBody FilingRecordEntity filingRecord) {
        return filingRecordService.filing(filingRecord);
    }

    /**
     * 扣款回执提交
     *
     * @param filingRecord
     * @return
     */
    @PostMapping("/deductionSubmit")
    @RequiresPermissions("filing:record:update")
    public R deduction(@RequestBody FilingRecordEntity filingRecord) {
        return filingRecordService.deduction(filingRecord);
    }

    /**
     * 完成申报
     *
     * @param filingId
     * @return
     */
    @PostMapping("/finish")
    @RequiresPermissions("filing:record:update")
    public R finish(Long filingId) {
        boolean r = filingRecordService.updateStatus(filingId, FilingConstants.FilingRecordStatus.FINISHED.getValue());
        if (!r) {
            return R.error();
        }
        return R.ok();
    }

    /**
     * 销毁敏感信息
     *
     * @param filingId
     * @return
     */
    @PostMapping("/destroy")
    @RequiresPermissions("filing:record:destroy")
    public R destroy(Long filingId) {
        boolean r = filingRecordService.updateStatus(filingId, FilingConstants.FilingRecordStatus.DESTROYED.getValue());
        if (!r) {
            return R.error("销毁失败");
        }
        return R.ok();
    }

    /**
     * 第三方url
     *
     * @param deptId
     * @return
     */
    @PostMapping("/thirdReportUrl")
    @RequiresPermissions("filing:record:info")
    public R reportUrl(Long deptId) {
        SysDeptEntity deptEntity = sysDeptService.getById(deptId);

        String url = TtkTaxUtil.getWebUrlForShenBao(deptEntity.getThirdOrgId(), null, null, null);
        if ("310000".equals(deptEntity.getCityCode())) {
            url = url.replace("page=ttk-taxapply-app-taxlist", "page=ttk-tax-app-robot-declare-payment");
        }
        return R.ok().put("data", url);
    }

    /**
     * 重置状态
     *
     * @param filingId
     * @return
     */
    @PostMapping("/reset")
    @RequiresPermissions("filing:record:reset")
    public R reset(Long filingId) {
        String datePattern = "yyyy-MM";
        String currentDateStr = DateUtil.format(DateUtil.date(), datePattern);
        FilingRecordEntity filingRecordEntity = filingRecordService.getById(filingId);
        SysDeptEntity deptEntity = sysDeptService.getById(filingRecordEntity.getDeptId());
        filingRecordEntity.setUploadStatus(FilingConstants.ProcessNodeStatus.PENDING_UPLOAD.getValue());
        filingRecordEntity.setConfirmReportStatus(FilingConstants.ProcessNodeStatus.NOT_STARTED.getValue());
        filingRecordEntity.setDeclareStatus(FilingConstants.ProcessNodeStatus.NOT_STARTED.getValue());
        filingRecordEntity.setDeductionStatus(FilingConstants.ProcessNodeStatus.NOT_STARTED.getValue());
        filingRecordService.update(filingRecordEntity, new QueryWrapper<FilingRecordEntity>().eq("filing_id", filingId));
        FilingVatEntity beforeVat = filingVatService.getVatByTaxCodeAndCreateTime(deptEntity.getTaxCode(), currentDateStr);
        if (null != beforeVat) {
            filingVatService.removeById(beforeVat);
        }
        List<Object> fileIds = filingRecordFileService.listObjs(new QueryWrapper<FilingRecordFileEntity>().select("id")
                .eq("filing_id", filingId));
        if (CollUtil.isNotEmpty(fileIds)) {
            if (filingRecordFileService.removeByIds(Convert.toList(Long.class, fileIds))) {
                return R.ok();
            }
        }
        return R.ok();
    }

    /**
     * 上传
     *
     * @param file
     * @param request
     */
    @PostMapping(value = "/extractPDF", consumes = "multipart/*", headers = "content-type=multipart/form-data")
    @RequiresPermissions("filing:record:update")
    public R fileUpload(Long filingId, MultipartFile file, HttpServletRequest request) {
        try {
            return R.ok().put("data", filingRecordService.extractPdf(filingId, file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.error();
    }

    /**
     * 删除流程上传文件
     *
     * @param filingRecordFileId 关联id
     * @return
     */
    @DeleteMapping("/file/delete")
    @RequiresPermissions("filing:record:list")
    public R filingFileDelete(Long filingRecordFileId) {

        if (filingRecordFileService.removeById(filingRecordFileId)) {
            return R.ok();
        }
        return R.error("删除失败");
    }


    /**
     * 单独提交数据给第三方
     * @param filingRecordFileId
     * @param filingId
     * @return
     */
    @PostMapping(value = "/file/submit")
    public R submitByFileId(Long filingRecordFileId, Long filingId){
        if (filingRecordService.submitFileByFileId(filingRecordFileId, filingId)){
            return R.ok();
        }
        return R.error("提交失败");
    }

    /**
     * 列表
     */
    @GetMapping("/batch")
    @RequiresPermissions("filing:batch:url")
    public R batch(@RequestParam Map<String, Object> params) {
        PageUtils page = filingRecordService.queryPage(params);
        List<Long> orgIds = new ArrayList<>();
        List<FilingRecordEntity> list = (List<FilingRecordEntity>) page.getList();
        for (FilingRecordEntity recordEntity : list) {
            SysDeptEntity sysDeptEntity = sysDeptService.getById(recordEntity.getDeptId());
            if (null != sysDeptEntity) {
                orgIds.add(sysDeptEntity.getThirdOrgId());
            }
        }

        return R.ok().put("page", page).put("url", TtkTaxUtil.getWebUrlForShenBaoBatch(orgIds));
    }
}
