package com.pwc.modules.job.task;

import cn.hutool.core.date.DateUtil;
import com.pwc.common.utils.FilingConstants;
import com.pwc.modules.filing.entity.FilingProcessEntity;
import com.pwc.modules.filing.service.FilingProcessService;
import com.pwc.modules.sys.entity.SysDeptEntity;
import com.pwc.modules.sys.service.SysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 继承上个月流程设置
 * @author zk
 */
@Component("filingProcessTask")
public class FilingProcessTask implements ITask {
    @Autowired
    private FilingProcessService filingProcessService;
    @Autowired
    private SysDeptService sysDeptService;

    @Override
    public void run(String params) {
        List<SysDeptEntity> deptList = sysDeptService.list();
        // 上个月
        String lastMonth = DateUtil.format(DateUtil.lastMonth(), "yyyy-MM");
        for (SysDeptEntity dept:deptList) {
            FilingProcessEntity lastMonthProcess = filingProcessService.queryByDeptAndCreateTime(dept.getDeptId(), lastMonth);
            if (null != lastMonthProcess) {
                FilingProcessEntity currProcess = new FilingProcessEntity();
                currProcess.setUploadStatus(lastMonthProcess.getUploadStatus());
                currProcess.setConfirmReportStatus(lastMonthProcess.getConfirmReportStatus());
                currProcess.setReportUploadStatus(lastMonthProcess.getReportUploadStatus());
                currProcess.setDeclareStatus(lastMonthProcess.getDeclareStatus());
                currProcess.setDeductionStatus(lastMonthProcess.getDeductionStatus());
                currProcess.setDeptId(dept.getDeptId());
                currProcess.setDeptName(dept.getName());
                currProcess.setCreateTime(DateUtil.date());
                currProcess.setCreateBy("by Task");
                currProcess.setStatus(FilingConstants.NodeStatus.UNMODIFIED.getValue());
                filingProcessService.save(currProcess);
            }

        }
    }
}
