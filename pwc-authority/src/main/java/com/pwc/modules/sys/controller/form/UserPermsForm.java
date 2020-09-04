package com.pwc.modules.sys.controller.form;

import com.pwc.modules.sys.entity.SysUserMenuEntity;
import lombok.Data;

@Data
public class UserPermsForm {
    /**
     * 模块id
     */
    private Long modules;
    /**
     * 权限关联id
     */
    private SysUserMenuEntity perms;
}

