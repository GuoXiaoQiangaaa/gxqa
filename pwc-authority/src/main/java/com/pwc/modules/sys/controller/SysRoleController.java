

package com.pwc.modules.sys.controller;

import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.R;
import com.pwc.common.validator.ValidatorUtils;
import com.pwc.modules.sys.entity.SelectVo;
import com.pwc.modules.sys.entity.SysRoleEntity;
import com.pwc.modules.sys.entity.SysUserEntity;
import com.pwc.modules.sys.entity.SysUserRoleEntity;
import com.pwc.modules.sys.service.SysRoleDeptService;
import com.pwc.modules.sys.service.SysRoleMenuService;
import com.pwc.modules.sys.service.SysRoleService;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 角色管理
 *
 * @author
 */
@RestController
@RequestMapping("/sys/role")
public class SysRoleController extends AbstractController {
	@Autowired
	private SysRoleService sysRoleService;
	@Autowired
	private SysRoleMenuService sysRoleMenuService;
	@Autowired
	private SysRoleDeptService sysRoleDeptService;

	/**
	 * 角色列表
	 */
	@GetMapping("/list")
	@RequiresPermissions("sys:role:list")
	public R list(@RequestParam Map<String, Object> params){
		PageUtils page = sysRoleService.queryPage(params);

		return R.ok().put("page", page);
	}

	/**
	 * 角色列表
	 */
	@GetMapping("/select")
	@RequiresPermissions("sys:role:select")
	public R select(){
//		List<SysRoleEntity> list = sysRoleService.list();

		List<SelectVo> list = sysRoleService.roleSelect();
		return R.ok().put("list", list);
	}




	/**
	 * 角色信息
	 */
	@GetMapping("/info/{roleId}")
	@RequiresPermissions("sys:role:info")
	public R info(@PathVariable("roleId") Long roleId){
		SysRoleEntity role = sysRoleService.getById(roleId);

		//查询角色对应的菜单
		List<Long> menuIdList = sysRoleMenuService.queryMenuIdList(roleId);
		role.setMenuIdList(menuIdList);

		//查询角色对应的部门
		List<Long> deptIdList = sysRoleDeptService.queryDeptIdList(new Long[]{roleId});
		role.setDeptIdList(deptIdList);

		return R.ok().put("role", role);
	}

	/**
	 * 回显角色权限信息
	 * @param userId
	 * @return
	 */
	@GetMapping("/info2")
	@RequiresPermissions("sys:role:info")
	public R info2( Long userId ){

		//1.通过userId查询查询deptID,再通过deptID查询对应的menuList
		SysUserEntity user = sysRoleService.findDeptIdByUserId(userId);
		List<Long> menuIdList1 = sysRoleService.findMenuListByDeptId(user.getDeptId());
		//2.通过userID查询对应的roleId，进而查询roleId对应的menuList
		SysUserRoleEntity role = sysRoleService.findRoleIdByUserId(userId);
		List<Long> menuIdList2 = sysRoleMenuService.queryMenuIdList(role.getRoleId());
		menuIdList1.addAll(menuIdList2);

		return R.ok().put("meunId", menuIdList1);
	}
	
	/**
	 * 保存角色
	 */
	@PutMapping("/save")
	@RequiresPermissions("sys:role:save")
	public R save(@RequestBody SysRoleEntity role){
		ValidatorUtils.validateEntity(role);
		
		sysRoleService.saveRole(role);
		
		return R.ok();
	}
	
	/**
	 * 修改角色
	 */
	@PostMapping("/update")
	@RequiresPermissions("sys:role:update")
	public R update(@RequestBody SysRoleEntity role){
		ValidatorUtils.validateEntity(role);
		
		sysRoleService.update(role);
		
		return R.ok();
	}
	
	/**
	 * 删除角色
	 */
	@DeleteMapping("/delete")
	@RequiresPermissions("sys:role:delete")
	public R delete(String roleIds){
		Long[] ids = (Long[]) ConvertUtils.convert(roleIds.split(","), Long.class);

		sysRoleService.deleteBatch(ids);
		
		return R.ok();
	}
}
