

package com.pwc.modules.sys.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pwc.modules.sys.entity.*;

import org.apache.ibatis.annotations.*;


import java.util.List;
import java.util.Map;

/**
 * 部门管理
 *
 * @author
 */
@Mapper
public interface SysDeptMenuDao extends BaseMapper<SysDeptMenuEntity> {


    /**
     * 公司模块管理列表
     * @param sysDeptMenuEntityPage
     * @param sysDeptMenuEntity
     * @return
     */
    List<SysDeptMenuEntity> selectByPage(@Param("sysDeptMenuEntityPage") Page<SysDeptMenuEntity> sysDeptMenuEntityPage, @Param("sysDeptMenuEntity") SysDeptMenuEntity sysDeptMenuEntity);

    /**
     * 一级菜单列表
     * @return
     */
    List<SysMenuEntity> getMenuList();
    /**
     * 模块管理编辑回显
     * @param deptId
     */
    List getDeptMenuById(Long deptId);

    //1.先将该公司下的菜单清空
    @Delete("delete from sys_dept_menu where dept_id=#{deptId}")
    void clearMenuIdByDeptId(SysDeptMenuEntity sysDeptMenuEntity);

    //2.重新选择公司对应一级菜单权限
    @Insert("INSERT into sys_dept_menu set dept_id=#{deptId},menu_id=#{id}")
    void update(@Param("deptId") Long deptId,@Param("id") Long id);


    List selectDeptList();

}
