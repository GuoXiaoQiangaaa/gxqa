

package com.pwc.modules.sys.service.impl;


import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.annotation.DataFilter;
import com.pwc.common.utils.Constant;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;
import com.pwc.modules.sys.dao.SysUserDao;
import com.pwc.modules.sys.entity.SysDeptEntity;
import com.pwc.modules.sys.entity.SysRoleEntity;
import com.pwc.modules.sys.entity.SysUserEntity;
import com.pwc.modules.sys.service.SysDeptService;
import com.pwc.modules.sys.service.SysRoleService;
import com.pwc.modules.sys.service.SysUserRoleService;
import com.pwc.modules.sys.service.SysUserService;
import com.pwc.modules.sys.shiro.ShiroUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 系统用户
 *
 * @author
 */
@Service("sysUserService")
public class SysUserServiceImpl extends ServiceImpl<SysUserDao, SysUserEntity> implements SysUserService {
	@Autowired
	private SysUserRoleService sysUserRoleService;
	@Autowired
	private SysDeptService sysDeptService;
	@Autowired
	private SysRoleService sysRoleService;

	@Override
	public List<Long> queryAllMenuId(Long userId) {
		return baseMapper.queryAllMenuId(userId);
	}

	@Override
	@DataFilter(subDept = true)
	public PageUtils queryPage(Map<String, Object> params) {
		String username = (String)params.get("username");
		String mobile = (String)params.get("mobile");
		String deptName = (String)params.get("deptName");

		//曲线救国，分页查询
		List<Object> deptIds = sysDeptService.listObjs(
				new QueryWrapper<SysDeptEntity>()
						.select("dept_id")
						.like(StringUtils.isNotBlank(deptName),"name", deptName));

		IPage<SysUserEntity> page = this.page(
			new Query<SysUserEntity>().getPage(params),
			new QueryWrapper<SysUserEntity>()
				.like(StringUtils.isNotBlank(username),"username", username)
				.like(StringUtils.isNotBlank(mobile),"mobile", mobile)
				.in(CollUtil.isNotEmpty(deptIds),"dept_id", deptIds)
			//	.apply(params.get(Constant.SQL_FILTER) != null, (String)params.get(Constant.SQL_FILTER))
					.orderByDesc("user_id")
		);

		for(SysUserEntity sysUserEntity : page.getRecords()){
			SysDeptEntity sysDeptEntity = sysDeptService.getById(sysUserEntity.getDeptId());
			sysUserEntity.setDeptName(sysDeptEntity.getName());
			List<Long> roleIdList = sysUserRoleService.queryRoleIdList(sysUserEntity.getUserId());
			if (CollUtil.isNotEmpty(roleIdList)) {
				SysRoleEntity roleEntity = sysRoleService.getById(roleIdList.get(0));
				if (null != roleEntity) {
					sysUserEntity.setRoleName(roleEntity.getRoleName());
				}
			}
		}

		return new PageUtils(page);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveUser(SysUserEntity user) {
		user.setCreateTime(new Date());
		user.setDeptName(sysDeptService.getById(user.getDeptId()).getName());
		//sha256加密
		String salt = RandomStringUtils.randomAlphanumeric(20);
		user.setSalt(salt);
		user.setPassword(ShiroUtils.sha256(user.getPassword(), user.getSalt()));
		this.save(user);
		
		//保存用户与角色关系
		sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update(SysUserEntity user) {
		if(StringUtils.isBlank(user.getPassword())){
			user.setPassword(null);
		}else{
			SysUserEntity userEntity = this.getById(user.getUserId());
			user.setPassword(ShiroUtils.sha256(user.getPassword(), userEntity.getSalt()));
		}
		this.updateById(user);
		
		//保存用户与角色关系
		sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
	}


	@Override
	public boolean updatePassword(Long userId, String password, String newPassword) {
        SysUserEntity userEntity = new SysUserEntity();
        userEntity.setPassword(newPassword);
        return this.update(userEntity,
        	new QueryWrapper<SysUserEntity>().eq("user_id", userId).eq("password", password));
    }

	@Override
	public boolean checkUserName(String userName) {
		List<SysUserEntity> userlist = baseMapper.queryByUsername(userName);
		if (CollUtil.isNotEmpty(userlist)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean updateStatus(Long userId, Integer status) {
		SysUserEntity sysUserEntity = new SysUserEntity();
		sysUserEntity.setStatus(status);
		return this.update(sysUserEntity, new QueryWrapper<SysUserEntity>().eq("user_id", userId));
	}

	@Override
	public Long getIdByName(String username){
		SysUserEntity sysUserEntity = this.getOne(new QueryWrapper<SysUserEntity>().eq("username", username));
		return null != sysUserEntity ? sysUserEntity.getUserId() : null;
	}
}
