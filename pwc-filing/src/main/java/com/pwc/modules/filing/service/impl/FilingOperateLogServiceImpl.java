package com.pwc.modules.filing.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.pwc.common.annotation.DataFilter;
import com.pwc.common.utils.Constant;
import com.pwc.common.utils.FilingConstants;
import com.pwc.common.utils.Query;
import com.pwc.modules.filing.entity.FilingRecordEntity;
import com.pwc.modules.filing.service.FilingRecordFileService;
import com.pwc.modules.filing.service.FilingRecordService;
import com.pwc.modules.sys.entity.SysDeptEntity;
import com.pwc.modules.sys.entity.SysFileEntity;
import com.pwc.modules.sys.entity.SysUserEntity;
import com.pwc.modules.sys.service.SysDeptService;
import com.pwc.modules.sys.service.SysFileService;
import com.pwc.modules.sys.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.utils.PageUtils;

import com.pwc.modules.filing.dao.FilingOperateLogDao;
import com.pwc.modules.filing.entity.FilingOperateLogEntity;
import com.pwc.modules.filing.service.FilingOperateLogService;


/**
 * @author zk
 */
@Service("filingOperateLogService")
public class FilingOperateLogServiceImpl extends ServiceImpl<FilingOperateLogDao, FilingOperateLogEntity> implements FilingOperateLogService {

    @Autowired
    private SysDeptService sysDeptService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private FilingRecordService filingRecordService;

    @Override
    @DataFilter(subDept = true, user = false)
    public PageUtils queryPage(Map<String, Object> params) {
        String filingId = (String) params.get("filingId");
        IPage<FilingOperateLogEntity> page = this.page(
                new Query<FilingOperateLogEntity>().getPage(params),
                new QueryWrapper<FilingOperateLogEntity>()
                        .eq(StrUtil.isNotBlank(filingId), "filing_id", filingId)
                        .apply(params.get(Constant.SQL_FILTER) != null, (String)params.get(Constant.SQL_FILTER))
                        .orderByDesc("create_time")
        );

        for (FilingOperateLogEntity filingOperateLogEntity : page.getRecords()) {
            SysDeptEntity sysDeptEntity = sysDeptService.getById(filingOperateLogEntity.getDeptId());
            if (null != sysDeptEntity) {
                filingOperateLogEntity.setDeptName(sysDeptEntity.getName());
                filingOperateLogEntity.setTaxCode(sysDeptEntity.getTaxCode());
            }
            SysUserEntity sysUserEntity = sysUserService.getById(filingOperateLogEntity.getUserId());
            if (null != sysUserEntity) {
                filingOperateLogEntity.setUserName(sysUserEntity.getUsername());
            }
        }
        return new PageUtils(page);
    }

    @Override
    public boolean save(FilingRecordEntity filingRecordEntity, Integer logType, Long userId, Integer fileType) {

        FilingOperateLogEntity filingOperateLogEntity = new FilingOperateLogEntity();
        filingOperateLogEntity.setFilingId(filingRecordEntity.getFilingId());
        filingOperateLogEntity.setDeptId(filingRecordEntity.getDeptId());
        filingOperateLogEntity.setUserId(userId);
        if (FilingConstants.OperateLogType.UPLOAD.getValue().equals(logType)) {
            filingOperateLogEntity.setDescr("上传了申报文件");
            List<SysFileEntity> fileEntityList = filingRecordService.getFilingFiles(filingRecordEntity.getFilingId(), fileType);
            String filename = "";
            if (CollUtil.isNotEmpty(fileEntityList)) {
                SysFileEntity f = fileEntityList.get(0);
//            for (SysFileEntity f : fileEntityList) {
                filename = filename + "[" + f.getOrigName() + "] ";
                filingOperateLogEntity.setFileName(filename);
                filingOperateLogEntity.setUrl(f.getServerPath());
//            }
            }
        } else if (FilingConstants.OperateLogType.FILING.getValue().equals(logType)) {
            filingOperateLogEntity.setDescr("提交了申报文件");
        } else if (FilingConstants.OperateLogType.AUDIT.getValue().equals(logType)) {
            String descr;
            if (FilingConstants.ProcessNodeStatus.AUDITED.getValue().equals(filingRecordEntity.getConfirmReportStatus())) {
                descr = "审核通过，备注："+ filingRecordEntity.getReportAuditMemo();
            } else {
                descr = "审核拒绝，备注："+ filingRecordEntity.getReportAuditMemo();
            }
            filingOperateLogEntity.setDescr(descr);
        } else if (FilingConstants.OperateLogType.COMFIRM.getValue().equals(logType)) {
            filingOperateLogEntity.setDescr("确认报告提交");
        } else if (FilingConstants.OperateLogType.DECLARE.getValue().equals(logType)) {
            filingOperateLogEntity.setDescr("确认了申报回执");
        } else if (FilingConstants.OperateLogType.DEDUCTION.getValue().equals(logType)) {
            filingOperateLogEntity.setDescr("确认了扣款回执");
        } else if (FilingConstants.OperateLogType.FINISHED.getValue().equals(logType)) {
            filingOperateLogEntity.setDescr("完成了申报");
        } else if (FilingConstants.OperateLogType.DESTROYED.getValue().equals(logType)) {
            filingOperateLogEntity.setDescr("销毁了文件");
        } else if (FilingConstants.OperateLogType.CREATE.getValue().equals(logType)) {
            filingOperateLogEntity.setDescr("创建了申报流程");
        } else if (FilingConstants.OperateLogType.UPLOAD_AUDIT.getValue().equals(logType)) {
            filingOperateLogEntity.setDescr("上传了审核附件");
        }

        return this.save(filingOperateLogEntity);
    }
}
