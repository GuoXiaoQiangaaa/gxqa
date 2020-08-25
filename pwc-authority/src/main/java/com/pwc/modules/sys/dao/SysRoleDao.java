

package com.pwc.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pwc.modules.sys.entity.SelectVo;
import com.pwc.modules.sys.entity.SysRoleEntity;
import com.pwc.modules.sys.entity.SysUserEntity;
import com.pwc.modules.sys.entity.SysUserRoleEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 角色管理
 *
 * @author
 */
@Mapper
public interface SysRoleDao extends BaseMapper<SysRoleEntity> {

    List<SelectVo> queryVOList(List<Long> list);

    SysUserEntity findDeptIdByUserId(Long userId);

    List<Long> findMenuListByDeptId(Long deptId);

    SysUserRoleEntity findRoleIdByUserId(Long userId);

}
