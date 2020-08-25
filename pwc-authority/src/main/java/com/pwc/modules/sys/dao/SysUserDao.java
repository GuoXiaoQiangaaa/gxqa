

package com.pwc.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pwc.modules.sys.entity.SysUserEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 系统用户
 *
 * @author
 */
@Mapper
public interface SysUserDao extends BaseMapper<SysUserEntity> {
	
	/**
	 * 查询用户的所有权限
	 * @param userId  用户ID
	 */
	List<String> queryAllPerms(Long userId);
	
	/**
	 * 查询用户的所有菜单ID
	 */
	List<Long> queryAllMenuId(Long userId);

	List<SysUserEntity> queryByUsername(String username);

	/**
	 * 查询到企业id
	 * @param userId
	 * @return
	 */
	SysUserEntity findDeptIdByUserId(Long userId);

	/**
	 * 查找公司id对应的权限列表
	 * @param deptId
	 * @return
	 */
	List<String> findPermsListByDeptId(Long deptId);

}
