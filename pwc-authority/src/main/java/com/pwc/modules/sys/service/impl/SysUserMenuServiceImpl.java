package com.pwc.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;
import com.pwc.modules.sys.dao.SysUserMenuDao;
import com.pwc.modules.sys.entity.SysUserMenuEntity;
import com.pwc.modules.sys.service.SysUserMenuService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("sysUserMenuService")
public class SysUserMenuServiceImpl extends ServiceImpl<SysUserMenuDao, SysUserMenuEntity> implements SysUserMenuService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SysUserMenuEntity> page = this.page(
                new Query<SysUserMenuEntity>().getPage(params),
                new QueryWrapper<SysUserMenuEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<SysUserMenuEntity> getByUserId(Long userId) {
        return this.list(new QueryWrapper<SysUserMenuEntity>().eq("user_id", userId));
    }

    @Override
    public boolean deleteByUserId(Long userId) {
        return this.remove(new QueryWrapper<SysUserMenuEntity>().eq("user_id", userId));
    }
}
