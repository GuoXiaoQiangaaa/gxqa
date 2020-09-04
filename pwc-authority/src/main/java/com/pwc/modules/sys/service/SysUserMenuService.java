package com.pwc.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.sys.entity.SysUserMenuEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author zk
 * @email 
 * @date 2020-08-19 16:06:55
 */
public interface SysUserMenuService extends IService<SysUserMenuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 根据用户id查询
     * @param userId
     * @return
     */
    List<SysUserMenuEntity> getByUserId(Long userId);

    boolean deleteByUserId(Long userId);
}

