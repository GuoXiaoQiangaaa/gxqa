package com.pwc.modules.data.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 客户信息
 *
 * @author fanpf
 * @date 2020/8/24
 */
@Data
@TableName("output_customer_new")
public class OutputCustomerNewEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 客户主键
	 */
	@TableId
	private Long customerId;
	/**
	 * 客户SAP代码
	 */
	private String sapCode;
	/**
	 * 公司代码
	 */
	private Long deptCode;
	/**
	 * 英文客户名称
	 */
	private String name;
	/**
	 * 中文客户名称
	 */
	private String nameCn;
	/**
	 * 纳税人识别号
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
	 * 银行账号
	 */
	private String bankAccount;
	/**
	 * 客户邮箱
	 */
	private String email;
	/**
	 * 是否停用(0:停用;1:正常)
	 */
	@TableLogic(value = "1", delval = "0")
	private String delFlag;
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
