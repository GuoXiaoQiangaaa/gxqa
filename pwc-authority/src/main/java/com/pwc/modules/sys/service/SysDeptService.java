

package com.pwc.modules.sys.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.sys.entity.SysDeptEntity;
import com.pwc.modules.sys.entity.TreeSelectVo;

import java.util.List;
import java.util.Map;

/**
 * 部门管理
 *
 * @author
 */
public interface SysDeptService extends IService<SysDeptEntity> {

	List<SysDeptEntity> queryList(Map<String, Object> map);

	/**
	 * 查询子部门ID列表
	 * @param parentId  上级部门ID
	 */
	List<Long> queryDetpIdList(Long parentId);

	/**
	 * 获取子部门ID，用于数据过滤
	 */
	List<Long> getSubDeptIdList(Long deptId);

	/**
	 * 分页查询部门列表
	 * @param params
	 * @return
	 */
	PageUtils queryPage(Map<String, Object> params);

	/**
	 * 修改部门状态
	 * @param deptId
	 * @param status
	 * @return
	 */
	boolean updateStatus(Long deptId, Integer status);

	/**
	 * 查询子部门列表
	 * @param parentId  上级部门ID
	 */
	List<SysDeptEntity> queryDeptList(Long parentId);

	/**
	 * 树
	 * @param deptId
	 * @return
	 */
	List<SysDeptEntity> getTreeDeptList(Long deptId);

	List<TreeSelectVo> getTreeSelectList(Long deptId);

	SysDeptEntity getByTaxCode(String taxCode);

	/**
	 * 保存
	 * @param deptEntity
	 * @return
	 */
	boolean saveDept(SysDeptEntity deptEntity);

	/**
	 * 修改
	 * @param deptEntity
	 * @return
	 */
	boolean updateDept(SysDeptEntity deptEntity);

	/**
	 * 根据税号和名称查询数量
	 * @param taxCode
	 * @param name
	 * @return
	 */
	int getCountByTaxCodAndName(String taxCode, String name);

	/**
	 * 根据名称获取企业
	 * @param name
	 * @return
	 */
	SysDeptEntity getByName(String name);

	/**
	 * 获取税号
	 * @return
	 */
	List<String> getTaxCodeByIds(Map<String, Object> params);

	/**
	 * 分页查询部门列表
	 * @param params
	 * @return
	 */
	PageUtils queryPageForStatistics(Map<String, Object> params);
}
