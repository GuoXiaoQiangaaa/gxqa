package com.pwc.modules.sys.service.impl;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pwc.common.annotation.DataFilter;
import com.pwc.common.utils.Constant;
import com.pwc.modules.sys.entity.SysNoticeRoleEntity;
import com.pwc.modules.sys.service.SysNoticeRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;

import com.pwc.modules.sys.dao.SysNoticeDao;
import com.pwc.modules.sys.entity.SysNoticeEntity;
import com.pwc.modules.sys.service.SysNoticeService;


/**
 * @author zk
 */
@Service("sysNoticeService")
public class SysNoticeServiceImpl extends ServiceImpl<SysNoticeDao, SysNoticeEntity> implements SysNoticeService {

    @Autowired
    SysNoticeRoleService sysNoticeRoleService;

    @Override
    @DataFilter(subDept = true, user = false, tableAlias = "f")
    public PageUtils queryPage(Map<String, Object> params) {

        QueryWrapper<SysNoticeEntity> queryWrapper = new QueryWrapper<SysNoticeEntity>()
                .apply(params.get(Constant.SQL_FILTER) != null, (String)params.get(Constant.SQL_FILTER));


        int pageSise = MapUtil.getInt(params, "limit");
        int currPage = MapUtil.getInt(params, "page");
        Page<SysNoticeEntity> page = new Page<>(currPage,pageSise);
        page.setRecords(baseMapper.queryPage(page,queryWrapper));
        return new PageUtils(page);
    }

    @Override
    public void save(Long filingId, Long deptId, List<Long> roleIds, String descr) {
        SysNoticeEntity noticeEntity = new SysNoticeEntity();
        noticeEntity.setDeptId(deptId);
        noticeEntity.setFilingId(filingId);
        noticeEntity.setDescr(descr);
        this.save(noticeEntity);
        for (Long roleId:roleIds) {
            SysNoticeRoleEntity sysNoticeRoleEntity = new SysNoticeRoleEntity();
            sysNoticeRoleEntity.setNoticeId(noticeEntity.getNoticeId());
            sysNoticeRoleEntity.setRoleId(roleId);
            sysNoticeRoleService.save(sysNoticeRoleEntity);
        }
    }
}
