package com.pwc.modules.filing.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pwc.common.annotation.DataFilter;
import com.pwc.common.utils.Constant;
import com.pwc.common.utils.FilingConstants;
import com.pwc.modules.filing.entity.FilingNodeEntity;
import com.pwc.modules.sys.entity.SysDeptEntity;
import com.pwc.modules.sys.service.SysDeptService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;

import com.pwc.modules.filing.dao.FilingProcessDao;
import com.pwc.modules.filing.entity.FilingProcessEntity;
import com.pwc.modules.filing.service.FilingProcessService;


/**
 * @author zk
 */
@Service("filingProcessService")
public class FilingProcessServiceImpl extends ServiceImpl<FilingProcessDao, FilingProcessEntity> implements FilingProcessService {

    @Autowired
    private SysDeptService sysDeptService;

    @Override
    @DataFilter(subDept = true, user = false, tableAlias = "f")
    public PageUtils queryPage(Map<String, Object> params) {
        String deptName = (String)params.get("deptName");
        String globalDate = (String)params.get("globalDate");
        if (StringUtils.isBlank(globalDate)) {
            globalDate = DateUtil.format(DateUtil.date(), "yyyy-MM");
        }
        QueryWrapper<FilingProcessEntity> queryWrapper = new QueryWrapper<FilingProcessEntity>()
                .like(StringUtils.isNotBlank(deptName),"dept_name", deptName)
                .like(StringUtils.isNotBlank(globalDate), "create_time", globalDate)
                .apply(params.get(Constant.SQL_FILTER) != null, (String)params.get(Constant.SQL_FILTER));
        int pageSise = MapUtil.getInt(params, "limit");
        int currPage = MapUtil.getInt(params, "page");
        Page<FilingProcessEntity> page = new Page<>(currPage,pageSise);
        page.setRecords(baseMapper.queryPage(page,queryWrapper));
        return new PageUtils(page);
    }

    @Override
    public boolean saveProcess(FilingProcessEntity process) {
        //当前月日期
        Date currentDate = DateUtil.date();
        String datePattern = "yyyy-MM";
        String currentDateStr = DateUtil.format(DateUtil.date(), datePattern);

        FilingProcessEntity filingProcess = queryByDeptAndCreateTime(process.getDeptId(), currentDateStr);
        //本月设置
        if (null != filingProcess) {
            return false;
        }

        //所有下级部门ID
        List<Long> subDeptIds = sysDeptService.getSubDeptIdList(process.getDeptId());

        List<SysDeptEntity> depts = (List<SysDeptEntity>) sysDeptService.listByIds(subDeptIds);
        depts.forEach(f -> {
            FilingProcessEntity subDeptProcess = queryByDeptAndCreateTime(f.getDeptId(), currentDateStr);
            if (null != subDeptProcess) {
                //子公司已做修改不覆盖
                if (subDeptProcess.getStatus().equals(FilingConstants.NodeStatus.MODIFIED.getValue())) {
                    return;
                }
            } else {
                subDeptProcess = new FilingProcessEntity();
            }
            subDeptProcess.setDeptId(f.getDeptId());
            subDeptProcess.setDeptName(f.getName());
            subDeptProcess.setUploadStatus(process.getUploadStatus());
            subDeptProcess.setConfirmReportStatus(process.getConfirmReportStatus());
            subDeptProcess.setReportUploadStatus(process.getReportUploadStatus());
            subDeptProcess.setDeclareStatus(process.getDeclareStatus());
            subDeptProcess.setDeductionStatus(process.getDeductionStatus());
            subDeptProcess.setEffectTime(process.getEffectTime());
            super.save(subDeptProcess);
        });
        process.setDeptName(sysDeptService.getById(process.getDeptId()).getName());
        process.setEffectTime(currentDate);
        process.setStatus(FilingConstants.NodeStatus.MODIFIED.getValue());
        return super.save(process);
    }


    /**
     * 查询时间内部门是否设置过流程
     * @param deptId
     * @param createTime
     * @return
     */
    @Override
    public FilingProcessEntity queryByDeptAndCreateTime(Long deptId, String createTime) {
        List<FilingProcessEntity> list = this.list(new QueryWrapper<FilingProcessEntity>().eq("dept_id", deptId)
                .like("create_time", createTime));
        return CollUtil.isNotEmpty(list) ? list.get(0) : null;
    }

}
