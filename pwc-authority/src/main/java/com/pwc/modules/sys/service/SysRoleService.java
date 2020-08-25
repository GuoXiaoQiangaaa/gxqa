

package com.pwc.modules.sys.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.sys.entity.SelectVo;
import com.pwc.modules.sys.entity.SysRoleEntity;
import com.pwc.modules.sys.entity.SysUserEntity;
import com.pwc.modules.sys.entity.SysUserRoleEntity;

import java.util.List;
import java.util.Map;


/**
 * 角色
 *
 * @author
 */
public interface SysRoleService extends IService<SysRoleEntity> {

	PageUtils queryPage(Map<String, Object> params);

	void saveRole(SysRoleEntity role);

	void update(SysRoleEntity role);

	void deleteBatch(Long[] roleIds);

	List<SelectVo> roleSelect();

    SysUserEntity findDeptIdByUserId(Long userId);

	List<Long> findMenuListByDeptId(Long deptId);

	SysUserRoleEntity findRoleIdByUserId(Long userId);

}
