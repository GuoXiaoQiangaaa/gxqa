

package com.pwc.modules.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pwc.common.exception.RRException;
import com.pwc.common.utils.Constant;
import com.pwc.common.utils.R;
import com.pwc.modules.sys.entity.SysMenuEntity;
import com.pwc.modules.sys.service.SysMenuService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统菜单
 *
 * @author
 */
@RestController
@RequestMapping("/sys/menu")
public class SysMenuController extends AbstractController {
	@Autowired
	private SysMenuService sysMenuService;

	/**
	 * 导航菜单
	 */
	@GetMapping("/nav")
	public R nav(){
		List<SysMenuEntity> menuList = sysMenuService.getUserMenuList(getUserId());
		return R.ok().put("menuList", menuList);
	}

	@GetMapping("/list")
	@RequiresPermissions("sys:menu:list")
	public R list(String name){
		List<SysMenuEntity> menuList = sysMenuService.list(
				new QueryWrapper<SysMenuEntity>()
						.like(StringUtils.isNotBlank(name), "name", name));
		List<SysMenuEntity> allChildList = new ArrayList<>();
		for(SysMenuEntity sysMenuEntity : menuList){
			SysMenuEntity parentMenuEntity = sysMenuService.getById(sysMenuEntity.getParentId());
			if(parentMenuEntity != null){
				sysMenuEntity.setParentName(parentMenuEntity.getName());
				if (StringUtils.isNotBlank(name)) {
					List<SysMenuEntity> menuChildList = sysMenuService.list(new QueryWrapper<SysMenuEntity>()
							.eq("parent_id", sysMenuEntity.getMenuId()));
					for (SysMenuEntity childMenu : menuChildList) {
						childMenu.setParentName(sysMenuEntity.getName());
					}
					allChildList.addAll(menuChildList);
				}
			}
		}
		menuList.addAll(allChildList);
		return R.ok().put("menuList", menuList);
	}
	/**
	 * 所有菜单列表
	 */
	@GetMapping("/alllist")
	@RequiresPermissions("sys:menu:list")
	public R list2(){

		List<SysMenuEntity> permissions = new ArrayList<SysMenuEntity>();
		//1 查询查询所有的节点
		List<SysMenuEntity> ps = sysMenuService.queryAll();
		//2 将集合转换成HashMap key = id value =Permission对象
		Map<Long,SysMenuEntity> permissionMap = new HashMap();
		for(SysMenuEntity permission:ps) {
			permissionMap.put(permission.getMenuId(),permission);
		}
		for(SysMenuEntity p:ps) {
			SysMenuEntity child = p;
			if(child.getParentId() == 0) {
				//根节点
				permissions.add(p);
			}else {
				SysMenuEntity parent =  permissionMap.get(child.getParentId());
				parent.getChildren().add(child);
			}
		}
		return R.ok().put("menuList", permissions);
	}


	/**
	 * 选择菜单(添加、修改菜单)
	 */
	@GetMapping("/select")
	@RequiresPermissions("sys:menu:select")
	public R select(){
		//查询列表数据
		List<SysMenuEntity> menuList = sysMenuService.queryNotButtonList();
		
		//添加顶级菜单
		SysMenuEntity root = new SysMenuEntity();
		root.setMenuId(0L);
		root.setName("一级菜单");
		root.setParentId(-1L);
		root.setOpen(true);
		menuList.add(root);
		
		return R.ok().put("menuList", menuList);
	}
	
	/**
	 * 菜单信息
	 */
	@GetMapping("/info/{menuId}")
	@RequiresPermissions("sys:menu:info")
	public R info(@PathVariable("menuId") Long menuId){
		SysMenuEntity menu = sysMenuService.getById(menuId);
		return R.ok().put("menu", menu);
	}
	
	/**
	 * 保存
	 */
	@PutMapping("/save")
	@RequiresPermissions("sys:menu:save")
	public R save(@RequestBody SysMenuEntity menu){
		//数据校验
		verifyForm(menu);
		
		sysMenuService.save(menu);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@PostMapping("/update")
	@RequiresPermissions("sys:menu:update")
	public R update(@RequestBody SysMenuEntity menu){
		//数据校验
		verifyForm(menu);
				
		sysMenuService.updateById(menu);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@DeleteMapping("/delete")
	@RequiresPermissions("sys:menu:delete")
	public R delete(long menuId){
		if(menuId <= 31){
			return R.error("系统菜单，不能删除");
		}

		//判断是否有子菜单或按钮
		List<SysMenuEntity> menuList = sysMenuService.queryListParentId(menuId);
		if(menuList.size() > 0){
			return R.error("请先删除子菜单或按钮");
		}

		sysMenuService.delete(menuId);

		return R.ok();
	}
	
	/**
	 * 验证参数是否正确
	 */
	private void verifyForm(SysMenuEntity menu){
		if(StringUtils.isBlank(menu.getName())){
			throw new RRException("菜单名称不能为空");
		}
		
		if(menu.getParentId() == null){
			throw new RRException("上级菜单不能为空");
		}
		
		//菜单
		if(menu.getType() == Constant.MenuType.MENU.getValue()){
			if(StringUtils.isBlank(menu.getUrl())){
				throw new RRException("菜单URL不能为空");
			}
		}
		
		//上级菜单类型
		int parentType = Constant.MenuType.CATALOG.getValue();
		if(menu.getParentId() != 0){
			SysMenuEntity parentMenu = sysMenuService.getById(menu.getParentId());
			parentType = parentMenu.getType();
		}
		
		//目录、菜单
		if(menu.getType() == Constant.MenuType.CATALOG.getValue() ||
				menu.getType() == Constant.MenuType.MENU.getValue()){
			if(parentType != Constant.MenuType.CATALOG.getValue()){
				throw new RRException("上级菜单只能为目录类型");
			}
			return ;
		}
		
		//按钮
		if(menu.getType() == Constant.MenuType.BUTTON.getValue()){
			if(parentType != Constant.MenuType.MENU.getValue() && parentType != Constant.MenuType.SUB_MENU.getValue()){
				throw new RRException("上级菜单只能为菜单类型");
			}
			return ;
		}
	}
}
