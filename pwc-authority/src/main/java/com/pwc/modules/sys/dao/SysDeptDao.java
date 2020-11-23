

package com.pwc.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pwc.modules.sys.entity.InputCompanyDto;
import com.pwc.modules.sys.entity.SysDeptEntity;
import com.pwc.modules.sys.entity.TreeSelectVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 部门管理
 *
 * @author
 */
@Mapper
public interface SysDeptDao extends BaseMapper<SysDeptEntity> {

    List<SysDeptEntity> queryList(Map<String, Object> params);

    /**
     * 查询子部门ID列表
     * @param parentId  上级部门ID
     */
    List<Long> queryDetpIdList(Long parentId);

    /**
     * 查询子部门列表
     * @param parentId  上级部门ID
     */
    List<SysDeptEntity> queryDeptList(Long parentId);

    List<TreeSelectVo> queryVOList(Long parentId);

    /**
     * 查询税号
     * @param deptIds
     * @return
     */
    List<String> queryTaxCodeByIds(Map<String, Object> params);

    /**
     * 根据部门id获取税号
     */
    String queryTaxCodeById(@Param("deptId")Long deptId);

    /**
     * 根据部门id查询拥有全部或个人数据权限的用户名
     */
    List<String> queryUsernameByDeptId(@Param("deptId") Long deptId, @Param("justOwn") int justOwn);

    /**
     * 新增部门时添加企业信息
     */
    void saveCompany(@Param("company")InputCompanyDto company);

    /**
     * 更新部门时更新企业信息
     */
    void updateCompany(@Param("company")InputCompanyDto company);

    /**
     * 根据dept_id获取company
     */
    InputCompanyDto queryCompanyByDeptId(@Param("deptId") Long deptId);

}
