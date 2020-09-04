

package com.pwc.modules.sys.controller;


import cn.hutool.core.collection.CollUtil;
import com.pwc.common.exception.RRException;
import com.pwc.common.utils.Constant;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.R;
import com.pwc.common.utils.StatusDefine;
import com.pwc.common.validator.Assert;
import com.pwc.common.validator.ValidatorUtils;
import com.pwc.common.validator.group.AddGroup;
import com.pwc.common.validator.group.UpdateGroup;
import com.pwc.modules.sys.controller.form.SetUserPermsForm;
import com.pwc.modules.sys.controller.form.UserPermsForm;
import com.pwc.modules.sys.entity.SysDeptEntity;
import com.pwc.modules.sys.entity.SysMenuEntity;
import com.pwc.modules.sys.entity.SysUserEntity;
import com.pwc.modules.sys.entity.SysUserMenuEntity;
import com.pwc.modules.sys.service.*;
import com.pwc.modules.sys.shiro.ShiroUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 系统用户
 *
 * @author
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController extends AbstractController {
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysUserRoleService sysUserRoleService;
	@Autowired
	private SysDeptService sysDeptService;
	@Autowired
	private SysUserMenuService sysUserMenuService;
	@Autowired
	private SysMenuService sysMenuService;

	@GetMapping("/checkName")
	public R checkName(String name) {
		if (sysUserService.checkUserName(name)) {
			return R.error("用户名已存在");
		}
		return R.ok();
	}

	/**
	 * 所有用户列表
	 */
	@GetMapping("/list")
	@RequiresPermissions("sys:user:list")
	public R list(@RequestParam Map<String, Object> params){
		PageUtils page = sysUserService.queryPage(params);

		return R.ok().put("page", page);
	}
	
	/**
	 * 获取登录的用户信息
	 */
	@GetMapping("/info")
	public R info(){
		SysUserEntity userEntity = getUser();
		SysDeptEntity deptEntity = sysDeptService.getById(userEntity.getDeptId());
		userEntity.setDeptName(deptEntity.getName());
		userEntity.setDeptType(deptEntity.getType());
		return R.ok().put("user", userEntity);
	}
	
	/**
	 * 修改登录用户密码
	 */
	@PostMapping("/password")
	public R password(String password, String newPassword){
		Assert.isBlank(newPassword, "新密码不为能空");

		//原密码
		password = ShiroUtils.sha256(password, getUser().getSalt());
		//新密码
		newPassword = ShiroUtils.sha256(newPassword, getUser().getSalt());
				
		//更新密码
		boolean flag = sysUserService.updatePassword(getUserId(), password, newPassword);
		if(!flag){
			return R.error("原密码不正确");
		}
		
		return R.ok();
	}

	/**
	 * 设置密码
	 */
	@PostMapping("/setPassword")
	@RequiresPermissions("sys:user:password")
	public R password(Long userId, String password, String confirmPassword){
		Assert.isBlank(password, "密码不为能空");
		Assert.isNull(userId, "用户ID不能为空");
		if (!Objects.equals(password,confirmPassword)) {
			throw new RRException("两次密码不一致");
		}
		SysUserEntity userEntity = sysUserService.getById(userId);
		userEntity.setPassword(ShiroUtils.sha256(password, userEntity.getSalt()));
		sysUserService.updateById(userEntity);
		return R.ok();
	}
	
	/**
	 * 用户信息
	 */
	@GetMapping("/info/{userId}")
	@RequiresPermissions("sys:user:info")
	public R info(@PathVariable("userId") Long userId){
		SysUserEntity user = sysUserService.getById(userId);
		
		//获取用户所属的角色列表
		List<Long> roleIdList = sysUserRoleService.queryRoleIdList(userId);
		user.setRoleIdList(roleIdList);
		
		return R.ok().put("user", user);
	}
	
	/**
	 * 保存用户
	 */
	@PutMapping("/save")
	@RequiresPermissions("sys:user:save")
	public R save(@RequestBody SysUserEntity user){
		ValidatorUtils.validateEntity(user, AddGroup.class);
		if (sysUserService.checkUserName(user.getUsername())) {
			return R.error("用户名已存在");
		}
		sysUserService.saveUser(user);
		
		return R.ok();
	}
	
	/**
	 * 修改用户
	 */
	@PostMapping("/update")
	@RequiresPermissions("sys:user:update")
	public R update(@RequestBody SysUserEntity user){
		ValidatorUtils.validateEntity(user, UpdateGroup.class);

		sysUserService.update(user);
		
		return R.ok();
	}
	
	/**
	 * 删除用户
	 */
	@DeleteMapping("/delete")
	@RequiresPermissions("sys:user:delete")
	public R delete(String userIds){

		String[] ids = userIds.split(",");

		if(ArrayUtils.contains(ids, 1L)){
			return R.error("系统管理员不能删除");
		}
		
		if(ArrayUtils.contains(ids, getUserId())){
			return R.error("当前用户不能删除");
		}

		sysUserService.removeByIds(Arrays.asList(ids));
		
		return R.ok();
	}

	/**
	 * 启用
	 */
	@PostMapping("/enable")
	@RequiresPermissions("sys:user:update")
	public R enable(long userId){

		if(!sysUserService.updateStatus(userId, StatusDefine.UserStatus.ENABLE.getValue())) {
			return R.error();
		}

		return R.ok();
	}

	/**
	 * 禁用用
	 */
	@PostMapping("/disable")
	@RequiresPermissions("sys:user:update")
	public R disable(long userId){

		if(!sysUserService.updateStatus(userId, StatusDefine.UserStatus.DISABLE.getValue())) {
			return R.error();
		}

		return R.ok();
	}


	/**
	 * 设置用户权限
	 * @param perms
	 * @return
	 */
	@PostMapping("/setPermission")
	public R setPermission(@RequestBody SetUserPermsForm perms) {
		List<SysUserMenuEntity> permsList = new ArrayList<>();
		List<UserPermsForm> userPermsForms = perms.getUserPerms();
		for (UserPermsForm permsForm : userPermsForms) {
			SysUserMenuEntity userMenuEntity = permsForm.getPerms();
			userMenuEntity.setMenuId(permsForm.getModules());
			userMenuEntity.setUserId(perms.getUserId());
			permsList.add(userMenuEntity);
		}
		if (CollUtil.isNotEmpty(permsList)) {
			// 先删除
			sysUserMenuService.deleteByUserId(perms.getUserId());
			// 再添加
			sysUserMenuService.saveBatch(permsList);
		}
		return R.ok();
	}

	/**
	 * 所有模块
	 * @return
	 */
	@GetMapping("/allModulesPermssions")
	public R allmodulePermissions() {
		List<SysMenuEntity> menuEntityList = sysMenuService.queryListType(Constant.MenuType.CATALOG.getValue());

		List<SysDeptEntity> deptEntityList = sysDeptService.getTreeDeptList(getDeptId());
		for (SysMenuEntity menuEntity : menuEntityList) {
			menuEntity.setDepts(deptEntityList);
		}
		return R.ok().put("data", menuEntityList);
	}

	/**
	 * 用户已设置的模块权限
	 * @return
	 */
	@GetMapping("/userModulePermissions")
	public R userModulePermissions(Long userId) {
		List<SysMenuEntity> menulist = new ArrayList<>();
		List<SysUserMenuEntity> userMenuEntityList = sysUserMenuService.getByUserId(userId);
		for (SysUserMenuEntity userMenu : userMenuEntityList) {
			SysMenuEntity menuEntity = sysMenuService.getById(userMenu.getMenuId());
			SysDeptEntity dept = sysDeptService.getById(userMenu.getDeptId());
			if (null != dept) {
				dept.setJustOwn(userMenu.getJustOwn());
			}
			List<SysDeptEntity> deptlist = new ArrayList<>();
			deptlist.add(dept);
			if (null != menuEntity) {
				menuEntity.setDepts(deptlist);
			}
			menulist.add(menuEntity);
		}
		return R.ok().put("data", menulist);
	}
}
