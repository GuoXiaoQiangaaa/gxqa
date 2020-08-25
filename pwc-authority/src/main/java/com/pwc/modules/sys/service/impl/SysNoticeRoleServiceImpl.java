package com.pwc.modules.sys.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;

import com.pwc.modules.sys.dao.SysNoticeRoleDao;
import com.pwc.modules.sys.entity.SysNoticeRoleEntity;
import com.pwc.modules.sys.service.SysNoticeRoleService;


@Service("sysNoticeRoleService")
public class SysNoticeRoleServiceImpl extends ServiceImpl<SysNoticeRoleDao, SysNoticeRoleEntity> implements SysNoticeRoleService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SysNoticeRoleEntity> page = this.page(
                new Query<SysNoticeRoleEntity>().getPage(params),
                new QueryWrapper<SysNoticeRoleEntity>()
        );

        return new PageUtils(page);
    }

}
