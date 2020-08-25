package com.pwc.modules.output.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

/**
 * 开票规则
 * 
 * @author zk
 * @email 
 * @date 2020-06-01 18:17:06
 */
@Data
@TableName("output_invoice_rules")
public class OutputInvoiceRulesEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键开票规则ID
	 */
	@TableId
	private Long rulesId;
	/**
	 * 客户id
	 */
	private Long customerId;
	/**
	 * 商品id ：针对特定产品的规则
	 */
	private Long goodsId;
	/**
	 * 不含税金额
	 */
	private BigDecimal amount;
	/**
	 * 是否自动开票0:否 1：是
	 */
	private Integer autoInvoicing;
	/**
	 * 是否预制发票 0:否1:是
	 */
	private Integer preformedInvoice;
	/**
	 * 是否需要销售清单0:否1:是
	 */
	private Integer needSalesList;
	/**
	 * 规则有效期
	 */
	private Date expireDate;
	/**
	 * 发票类型
	 */
	private String invoiceType;
	/**
	 * 发票载体
	 */
	private String invoiceEntity;
	/**
	 * 发票备注
	 */
	private String remark;
	/**
	 * 开票频率：多个以，分开
	 */
	private String invoicingFrequency;
	/**
	 * 开票时间
	 */
	private String invoicingTime;
	/**
	 * 规则类型：1:特殊规则 2:普通规则
	 */
	private Integer type;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 创建人
	 */
	private String createBy;
	/**
	 * 更新时间
	 */
	private Date updateTime;
	/**
	 * 更新人
	 */
	private String updateBy;

	/**
	 * 删除 0正常 -1删除
	 */
	@TableLogic
	private Integer delFlag;
	/**
	 * 商品名称
	 */
	@TableField(exist = false)
	private String goodsName;

}
