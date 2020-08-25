

package com.pwc.modules.sys.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.db.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pwc.common.third.TtkOrgUtil;
import com.pwc.common.utils.Constant;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.R;
import com.pwc.common.utils.StatusDefine;
import com.pwc.common.validator.ValidatorUtils;
import com.pwc.modules.sys.entity.SysDeptEntity;
import com.pwc.modules.sys.entity.SysDeptMenuEntity;
import com.pwc.modules.sys.entity.SysMenuEntity;
import com.pwc.modules.sys.entity.TreeSelectVo;
import com.pwc.modules.sys.service.SysDeptMenuService;
import com.pwc.modules.sys.service.SysDeptService;
import com.pwc.modules.sys.service.SysUserRoleService;
//import oracle.jdbc.proxy.annotation.Post;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 公司模块管理
 *
 * @author
 */
@RestController
@RequestMapping("/sys/dept/menu")
public class SysDeptMenuController extends AbstractController {
	@Autowired
	private SysDeptMenuService sysDeptMenuService;



	/**
	 * 公司模块管理列表
	 */
	@PostMapping("/list")
	//@RequiresPermissions("sys:deptMenu:list")
	public R list(@RequestBody SysDeptMenuEntity sysDeptMenuEntity){
		// super admin 拥有所有权限
		//PageUtils page = sysDeptMenuService.queryPage(params);
		Integer page = sysDeptMenuEntity.getPage();
		Integer size = sysDeptMenuEntity.getSize();
		Page<SysDeptMenuEntity> page1 = sysDeptMenuService.selectByPage(new Page<SysDeptMenuEntity>(page,size),sysDeptMenuEntity);

		return R.ok().put("page1", page1);
	}

	/**
	 * 菜单以及列表
	 */
	@GetMapping("/menuList")
	//@RequiresPermissions("sys:menu:list")
	public R menuList(){

		List<SysMenuEntity> menuList = sysDeptMenuService.getMenuList();

		return R.ok().put("data", menuList);
	}

	/**
	 * 模块管理编辑回显
	 */
	@GetMapping("/info/{deptId}")
	//@RequiresPermissions("sys:dept:info")
	public R info(@PathVariable("deptId") Long deptId){

		 List menuList = sysDeptMenuService.getDeptMenuById(deptId);

		return R.ok().put("menuList", menuList);
	}
	/**
	 * 模块管理修改(编辑)
	 */
	@PostMapping("/update")
	//@RequiresPermissions("sys:menu:update")
	public R update(@RequestBody SysDeptMenuEntity sysDeptMenuEntity){
		String menuIds = sysDeptMenuEntity.getMenuIds();
		Long[] ids = (Long[]) ConvertUtils.convert(menuIds.split(","), Long.class);
		 sysDeptMenuService.update(sysDeptMenuEntity,ids);

		return R.ok();
	}






}
