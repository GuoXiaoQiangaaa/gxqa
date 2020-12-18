

package com.pwc.modules.sys.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.annotation.DataFilter;
import com.pwc.common.utils.Constant;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;
import com.pwc.common.utils.StatusDefine;
import com.pwc.modules.sys.dao.SysRoleDao;
import com.pwc.modules.sys.entity.*;
import com.pwc.modules.sys.service.*;
import com.pwc.modules.sys.shiro.ShiroUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * 角色
 *
 * @author
 */
@Service("sysRoleService")
public class SysRoleServiceImpl extends ServiceImpl<SysRoleDao, SysRoleEntity> implements SysRoleService {
	@Autowired
	private SysRoleMenuService sysRoleMenuService;
	@Autowired
	private SysRoleDeptService sysRoleDeptService;
	@Autowired
	private SysUserRoleService sysUserRoleService;
	@Autowired
	private SysDeptService sysDeptService;

	@Override
	@DataFilter(subDept = true, user = false)
	public PageUtils queryPage(Map<String, Object> params) {
		String roleName = (String)params.get("roleName");
		List<Long> queryIds = queryRoleIds();
		IPage<SysRoleEntity> page = this.page(
			new Query<SysRoleEntity>().getPage(params),
			new QueryWrapper<SysRoleEntity>()
				.like(StringUtils.isNotBlank(roleName),"role_name", roleName)
				.in(CollUtil.isNotEmpty(queryIds), "role_id", queryIds)
				.apply(params.get(Constant.SQL_FILTER) != null, (String)params.get(Constant.SQL_FILTER)).orderByDesc("role_id")
		);

		for(SysRoleEntity sysRoleEntity : page.getRecords()){
			SysDeptEntity sysDeptEntity = sysDeptService.getById(sysRoleEntity.getDeptId());
			if(sysDeptEntity != null){
				sysRoleEntity.setDeptName(sysDeptEntity.getName());
			}
			sysRoleEntity.setMenuIdList(sysRoleMenuService.queryMenuIdList(sysRoleEntity.getRoleId()));
		}

		return new PageUtils(page);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveRole(SysRoleEntity role) {
		role.setCreateTime(new Date());
		this.save(role);

		//保存角色与菜单关系
		sysRoleMenuService.saveOrUpdate(role.getRoleId(), role.getMenuIdList());

		//保存角色与部门关系
		sysRoleDeptService.saveOrUpdate(role.getRoleId(), role.getDeptIdList());
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update(SysRoleEntity role) {
		this.updateById(role);

		//更新角色与菜单关系
		sysRoleMenuService.saveOrUpdate(role.getRoleId(), role.getMenuIdList());

		//保存角色与部门关系
		sysRoleDeptService.saveOrUpdate(role.getRoleId(), role.getDeptIdList());
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteBatch(Long[] roleIds) {
		//删除角色
		this.removeByIds(Arrays.asList(roleIds));

		//删除角色与菜单关联
		sysRoleMenuService.deleteBatch(roleIds);

		//删除角色与部门关联
		sysRoleDeptService.deleteBatch(roleIds);

		//删除角色与用户关联
		sysUserRoleService.deleteBatch(roleIds);
	}

	@Override
	public List<SelectVo> roleSelect() {
		return baseMapper.queryVOList(queryRoleIds());
	}

	@Override
	public SysUserEntity findDeptIdByUserId(Long userId) {
		return baseMapper.findDeptIdByUserId(userId);
	}

	@Override
	public List<Long> findMenuListByDeptId(Long deptId) {
		return baseMapper.findMenuListByDeptId(deptId);
	}

	@Override
	public SysUserRoleEntity findRoleIdByUserId(Long userId) {
		return baseMapper.findRoleIdByUserId(userId);
	}

	/**
	 *
	 * @return
	 */
	private List<Long> queryRoleIds(){
		List<Long> roleIds = sysUserRoleService.queryRoleIdList(ShiroUtils.getUserId());
		Long roleId = roleIds.get(0);
		SysDeptEntity sysDeptEntity = sysDeptService.getById(ShiroUtils.getUserEntity().getDeptId());
		List<Long> queryIds = new ArrayList<>();

		if (StatusDefine.DeptType.HEAD_OFFICE.getValue() == sysDeptEntity.getType()) {
			queryIds.add(StatusDefine.RoleIds.BRANCH_FILING_USER.getValue());
			queryIds.add(StatusDefine.RoleIds.BRANCH_MANAGER.getValue());
			queryIds.add(StatusDefine.RoleIds.GROUP_AUDIT_USER.getValue());
			queryIds.add(StatusDefine.RoleIds.GROUP_FILING_USER.getValue());
			queryIds.add(StatusDefine.RoleIds.GROUP_MANAGER.getValue());
		} else if (StatusDefine.DeptType.BRANCH_OFFICE.getValue() == sysDeptEntity.getType()) {
			queryIds.add(StatusDefine.RoleIds.BRANCH_FILING_USER.getValue());
			queryIds.add(StatusDefine.RoleIds.BRANCH_MANAGER.getValue());
		}
		if (roleId == Constant.SUPER_ADMIN) {
			queryIds = null;
		}
		return queryIds;
	}
}
