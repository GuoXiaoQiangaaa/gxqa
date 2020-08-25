

package com.pwc.modules.sys.shiro;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pwc.common.utils.Constant;
import com.pwc.modules.sys.dao.SysMenuDao;
import com.pwc.modules.sys.dao.SysUserDao;
import com.pwc.modules.sys.dao.SysUserRoleDao;
import com.pwc.modules.sys.entity.SysMenuEntity;
import com.pwc.modules.sys.entity.SysUserEntity;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 认证
 *
 * @author
 */
@Component
public class UserRealm extends AuthorizingRealm {
    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private SysMenuDao sysMenuDao;
    @Autowired
	private SysUserRoleDao sysUserRoleDao;

    /**
     * 授权(验证权限时调用)
     */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		SysUserEntity user = (SysUserEntity)principals.getPrimaryPrincipal();
		Long userId = user.getUserId();
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.setStringPermissions(this.getPerms(userId));
		return info;
	}

	/**
	 * 认证(登录时调用)
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken authcToken) throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken)authcToken;

		//查询用户信息
		SysUserEntity user = sysUserDao.selectOne(new QueryWrapper<SysUserEntity>().eq("username", token.getUsername()));
		//账号不存在
		if(user == null) {
			throw new UnknownAccountException("账号或密码不正确");
		}

		//账号锁定
		if(user.getStatus() == 0){
			throw new LockedAccountException("账号已被锁定,请联系管理员");
		}

		SecurityUtils.getSubject().getSession().setAttribute("perms", this.getPerms(user.getUserId()));
		return new SimpleAuthenticationInfo(user, user.getPassword(), ByteSource.Util.bytes(user.getSalt()), getName());
	}

	@Override
	public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
		HashedCredentialsMatcher shaCredentialsMatcher = new HashedCredentialsMatcher();
		shaCredentialsMatcher.setHashAlgorithmName(ShiroUtils.hashAlgorithmName);
		shaCredentialsMatcher.setHashIterations(ShiroUtils.hashIterations);
		super.setCredentialsMatcher(shaCredentialsMatcher);
	}

	/**
	 * 	权限码
	 * @param userId
	 * @return 权限列表
	 */
	private Set<String> getPerms(Long userId){
		List<Long> roleIds = sysUserRoleDao.queryRoleIdList(userId);
		Long roleId = roleIds.get(0);
		List<String> permsList;
		if(roleId == Constant.SUPER_ADMIN){
			List<SysMenuEntity> menuList = sysMenuDao.selectList(null);
			permsList = new ArrayList<>(menuList.size());
			for(SysMenuEntity menu : menuList){
				permsList.add(menu.getPerms());
			}
		}else{
			//通过user查询到企业id，根据企业id找到菜单权限
			SysUserEntity user = sysUserDao.findDeptIdByUserId(userId);
			//公司权限列表
			permsList =  sysUserDao.findPermsListByDeptId(user.getDeptId());
			System.out.println(permsList);
			//用户权限列表
			List<String> permsList2 = sysUserDao.queryAllPerms(userId);
			permsList.addAll(permsList2);
		}
		Set<String> permsSet = new HashSet<>();
		for(String perms : permsList){
			if(StringUtils.isBlank(perms)){
				continue;
			}
			permsSet.addAll(Arrays.asList(perms.trim().split(",")));
		}

		return permsSet;
	}


}
