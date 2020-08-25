

package com.pwc.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.sys.entity.SysUserEntity;

import java.util.List;
import java.util.Map;


/**
 * 系统用户
 *
 * @author
 */
public interface SysUserService extends IService<SysUserEntity> {

	PageUtils queryPage(Map<String, Object> params);
	
	/**
	 * 查询用户的所有菜单ID
	 */
	List<Long> queryAllMenuId(Long userId);
	
	/**
	 * 保存用户
	 */
	void saveUser(SysUserEntity user);
	
	/**
	 * 修改用户
	 */
	void update(SysUserEntity user);

	/**
	 * 修改密码
	 * @param userId       用户ID
	 * @param password     原密码
	 * @param newPassword  新密码
	 */
	boolean updatePassword(Long userId, String password, String newPassword);

	/**
	 * 验证用户名是否重复
	 * @param userName
	 * @return true 为重复 false 不重复
	 */
	boolean checkUserName(String userName);

	boolean updateStatus(Long userId, Integer status);

	/**
	 * input
	 * @param username
	 * @return
	 */
	Long getIdByName(String username);
}
