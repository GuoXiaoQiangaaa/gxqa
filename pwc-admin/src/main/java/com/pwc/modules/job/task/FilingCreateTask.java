package com.pwc.modules.job.task;

import cn.hutool.core.date.DateUtil;
import com.pwc.common.utils.FilingConstants;
import com.pwc.common.utils.StatusDefine;
import com.pwc.modules.filing.entity.FilingRecordEntity;
import com.pwc.modules.filing.service.FilingRecordService;
import com.pwc.modules.sys.entity.SysDeptEntity;
import com.pwc.modules.sys.service.SysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *  启用filing申报流程
 * @author zk
 */
@Component("filingCreateTask")
public class FilingCreateTask implements ITask {

    @Autowired
    private SysDeptService sysDeptService;
    @Autowired
    private FilingRecordService filingRecordService;

    @Override
    public void run(String params) {
        System.out.println("-----------------------进入执行-------------------------");
        List<SysDeptEntity> deptList = sysDeptService.list();
        for (SysDeptEntity dept : deptList) {
            if (StatusDefine.DeptType.BRANCH_OFFICE.getValue() == dept.getType()) {
                // 如果当月已有申报流程跳过
                if (null != filingRecordService.getCurrentMonthFiling(dept.getDeptId())){
                    continue;
                }
                FilingRecordEntity filingRecord = new FilingRecordEntity();
                filingRecord.setUploadStatus(FilingConstants.ProcessNodeStatus.PENDING_UPLOAD.getValue());
                filingRecord.setConfirmReportStatus(FilingConstants.ProcessNodeStatus.NOT_STARTED.getValue());
                filingRecord.setDeclareStatus(FilingConstants.ProcessNodeStatus.NOT_STARTED.getValue());
                filingRecord.setDeductionStatus(FilingConstants.ProcessNodeStatus.NOT_STARTED.getValue());
                filingRecord.setDeptName(dept.getName());
                filingRecord.setDeptId(dept.getDeptId());
                filingRecord.setCreateTime(DateUtil.date());
                filingRecord.setCreateBy("by Task");
                filingRecordService.save(filingRecord);
            }
        }
    }
}
