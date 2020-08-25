package com.pwc.modules.filing.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pwc.common.annotation.DataFilter;
import com.pwc.common.utils.Constant;
import com.pwc.common.utils.FilingConstants;
import com.pwc.common.utils.Query;
import com.pwc.modules.filing.entity.FilingRecordEntity;
import com.pwc.modules.sys.entity.SysDeptEntity;
import com.pwc.modules.sys.service.SysDeptService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.utils.PageUtils;

import com.pwc.modules.filing.dao.FilingNodeDao;
import com.pwc.modules.filing.entity.FilingNodeEntity;
import com.pwc.modules.filing.service.FilingNodeService;

/**
 *
 * @author zk
 */
@Service("filingNodeService")
public class FilingNodeServiceImpl extends ServiceImpl<FilingNodeDao, FilingNodeEntity> implements FilingNodeService {

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
        QueryWrapper<FilingNodeEntity> queryWrapper = new QueryWrapper<FilingNodeEntity>()
                        .like(StringUtils.isNotBlank(deptName),"dept_name", deptName)
                        .like(StringUtils.isNotBlank(globalDate), "create_time", globalDate)
                        .apply(params.get(Constant.SQL_FILTER) != null, (String)params.get(Constant.SQL_FILTER));
//        IPage<FilingNodeEntity> page = this.page(
//                new Query<FilingNodeEntity>().getPage(params),
//                new QueryWrapper<FilingNodeEntity>()
//                        .like(StringUtils.isNotBlank(deptName),"dept_name", deptName)
//                        .like(StringUtils.isNotBlank(globalDate), "create_time", globalDate)
//                        .apply(params.get(Constant.SQL_FILTER) != null, (String)params.get(Constant.SQL_FILTER))
//        );
        int pageSise = MapUtil.getInt(params, "limit");
        int currPage = MapUtil.getInt(params, "page");
        Page<FilingNodeEntity> page = new Page<>(currPage,pageSise);
        page.setRecords(baseMapper.queryPage(page,queryWrapper));

        return new PageUtils(page);
    }

    @Override
    public FilingNodeEntity queryByDeptAndCreateTime(Long deptId, String createTime) {
        if (StrUtil.isBlank(createTime)) {
            String datePattern = "yyyy-MM";
            createTime = DateUtil.format(DateUtil.date(), datePattern);
        }
        List<FilingNodeEntity> list = this.list(new QueryWrapper<FilingNodeEntity>().eq("dept_id", deptId)
                .like("create_time", createTime));
        return CollUtil.isNotEmpty(list) ? list.get(0) : null;
    }


    /**
     * 设置节点初始化子公司
     * @param entity
     * @return
     */
    @Override
    public boolean saveFilingNote(FilingNodeEntity entity) {
        //当前月日期
        Date currentDate = DateUtil.date();
        String datePattern = "yyyy-MM";
        String currentDateStr = DateUtil.format(DateUtil.date(), datePattern);

        FilingNodeEntity filingNode = queryByDeptAndCreateTime(entity.getDeptId(), currentDateStr);
        //本月设置
        if (null != filingNode) {
            return false;
        }

        //所有下级部门ID
        List<Long> subDeptIds = sysDeptService.getSubDeptIdList(entity.getDeptId());

        List<SysDeptEntity> depts = (List<SysDeptEntity>) sysDeptService.listByIds(subDeptIds);
        depts.forEach(f -> {
            FilingNodeEntity subDeptNode = queryByDeptAndCreateTime(f.getDeptId(), currentDateStr);
            if (null != subDeptNode) {
                //子公司已做修改不覆盖
                if (subDeptNode.getStatus().equals(FilingConstants.NodeStatus.MODIFIED.getValue())) {
                    return;
                }
            } else {
                subDeptNode = new FilingNodeEntity();
            }
            subDeptNode.setDeptId(f.getDeptId());
            subDeptNode.setDeptName(f.getName());
            subDeptNode.setDeclareDate(entity.getDeclareDate());
            subDeptNode.setUploadDate(entity.getUploadDate());
            subDeptNode.setDeductionDate(entity.getDeductionDate());
            subDeptNode.setReportConfirmDate(entity.getReportConfirmDate());
            subDeptNode.setEffectTime(currentDate);
            super.save(subDeptNode);
        });
        entity.setDeptName(sysDeptService.getById(entity.getDeptId()).getName());
        entity.setEffectTime(currentDate);
        entity.setStatus(FilingConstants.NodeStatus.MODIFIED.getValue());
        return super.save(entity);
    }
}
