package com.pwc.modules.output.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 客户基本信息
 * 
 * @author zk
 * @email 
 * @date 2020-06-01 18:17:06
 */
@Data
@TableName("output_customer")
public class OutputCustomerEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId
	private Long customerId;
	/**
	 * 企业id
	 */
	private Long deptId;
	/**
	 * 客户名称
	 */
	private String name;
	/**
	 * 纳税人类型
	 */
	private String taxpayerType;
	/**
	 * 税号
	 */
	private String taxCode;
	/**
	 * 地址
	 */
	private String address;
	/**
	 * 联系电话
	 */
	private String contact;
	/**
	 * 开户行
	 */
	private String bank;
	/**
	 * 银行账户
	 */
	private String bankAccount;
	/**
	 * 创建人
	 */
	private String createBy;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 修改人
	 */
	private String updateBy;
	/**
	 * 更新时间
	 */
	private Date updateTime;

}
