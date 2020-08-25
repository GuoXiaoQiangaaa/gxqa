package com.pwc.modules.output.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 客户规则关联
 * 
 * @author zk
 * @email 
 * @date 2020-06-11 15:41:24
 */
@Data
@TableName("output_customer_rules")
public class OutputCustomerRulesEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId
	private Long id;
	/**
	 * 客户id
	 */
	private Long customerId;
	/**
	 * 规则id
	 */
	private Long rulesId;
	/**
	 * 
	 */
	private String createBy;
	/**
	 * 
	 */
	private Date createTime;
	/**
	 * 
	 */
	private String updateBy;
	/**
	 * 
	 */
	private Date updateTime;

}
