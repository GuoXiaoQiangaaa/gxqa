package com.pwc.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 企业设置税种关联表
 * 
 * @author zk
 * @email 
 * @date 2019-12-31 17:21:04
 */
@Data
@TableName("sys_dept_tax")
public class SysDeptTaxEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 企业id
	 */
	private Long deptId;
	/**
	 * 税种id
	 */
	private Long taxId;
	/**
	 * 主键
	 */
	@TableId
	private Long id;

	/**
	 * 企业名称
	 */
	@TableField(exist = false)
	private String deptName;

	/**
	 * 税种列表
	 */
	@TableField(exist = false)
	private List<SysTaxEntity> taxList;

}
