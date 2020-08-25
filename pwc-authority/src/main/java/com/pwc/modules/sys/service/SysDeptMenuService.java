

package com.pwc.modules.sys.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.modules.sys.entity.SysDeptMenuEntity;
import com.pwc.modules.sys.entity.SysMenuEntity;

import java.util.List;

/**
 * 公司模块管理
 *
 * @author
 */
public interface SysDeptMenuService extends IService<SysDeptMenuEntity> {


	/**
	 * 分页查询公司模块列表列表
	 * @param sysDeptMenuEntityPage
	 * @param sysDeptMenuEntity
	 * @return
	 */
	//PageUtils queryPage(Map<String, Object> params);
	Page<SysDeptMenuEntity> selectByPage(Page<SysDeptMenuEntity> sysDeptMenuEntityPage, SysDeptMenuEntity sysDeptMenuEntity);
	/**
	 * 菜单一级列表
	 * @return
	 */
	List<SysMenuEntity> getMenuList();

	/**
	 * 模块管理编辑回显
	 * @param deptId
	 * @return
	 */
	List getDeptMenuById(Long deptId);

	/**
	 * 修改模块管理
	 * @param sysDeptMenuEntity
	 * @param ids
	 */
	void update(SysDeptMenuEntity sysDeptMenuEntity, Long[] ids);

}
