package com.pwc.modules.job.task;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.pwc.common.utils.FilingConstants;
import com.pwc.modules.filing.entity.FilingNodeEntity;
import com.pwc.modules.filing.service.FilingNodeService;
import com.pwc.modules.sys.entity.SysDeptEntity;
import com.pwc.modules.sys.service.SysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 继承上个月节点设置
 * @author zk
 */
@Component("filingNodeTask")
public class FilingNodeTask implements ITask {
    @Autowired
    private FilingNodeService filingNodeService;
    @Autowired
    private SysDeptService sysDeptService;

    @Override
    public void run(String params) {
        List<SysDeptEntity> deptList = sysDeptService.list();
        // 上个月
        String lastMonth = DateUtil.format(DateUtil.lastMonth(), "yyyy-MM");
        for (SysDeptEntity dept:deptList) {
            FilingNodeEntity lastMonthFilingNode = filingNodeService.queryByDeptAndCreateTime(dept.getDeptId(), lastMonth);
            if (null != lastMonthFilingNode) {
                FilingNodeEntity currNode = new FilingNodeEntity();
                currNode.setUploadDate(lastMonthFilingNode.getUploadDate());
                currNode.setReportConfirmDate(lastMonthFilingNode.getReportConfirmDate());
                currNode.setDeclareDate(lastMonthFilingNode.getDeclareDate());
                currNode.setDeductionDate(lastMonthFilingNode.getDeductionDate());
                currNode.setDeptId(dept.getDeptId());
                currNode.setDeptName(dept.getName());
                currNode.setCreateTime(DateUtil.date());
                currNode.setCreateBy("by Task");
                currNode.setStatus(FilingConstants.NodeStatus.UNMODIFIED.getValue());
                filingNodeService.save(currNode);
            }

        }
    }
}
