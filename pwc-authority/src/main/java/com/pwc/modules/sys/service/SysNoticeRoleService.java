package com.pwc.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.sys.entity.SysNoticeRoleEntity;

import java.util.Map;

/**
 * 通知角色关联
 *
 * @author zk
 * @email 
 * @date 2020-02-11 18:06:10
 */
public interface SysNoticeRoleService extends IService<SysNoticeRoleEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

