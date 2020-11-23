package com.pwc.modules.output.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

/**
 * 开票申请表
 *
 * @author fanpf
 * @date 2020/9/24
 */
@Data
@TableName("output_invoice_apply")
public class OutputInvoiceApplyEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId
	private Long applyId;
	/**
	 * 开票申请号
	 */
	private String applyNumber;
	/**
	 * 所属企业ID
	 */
	private Long deptId;
	/**
	 * 订单id,多个为合并开票以，分开 
	 */
	private String orderId;
	/**
	 * 订单号
	 */
	private String orderNumber;
	/**
	 * 纳税人类型
	 */
	private String taxpayerType;
	/**
	 * 发票类型
	 */
	private String invoiceType;
	/**
	 * 发票实体
	 */
	private String invoiceEntity;
	/**
	 * 发票备注
	 */
	private String invoiceRemark;
	/**
	 * 业务类型 
	 */
	private String businessType;
	/**
	 * 赊销开票 0 否 1 是
	 */
	private Integer creditInvoicing;
	/**
	 * 显示折扣 0 否 1 是
	 */
	private Integer showDiscount;
	/**
	 * 折扣显示规则 1 单独显示总折扣值 2 按订单中货物或应税劳务总额进行折扣分摊
	 */
	private Integer showDiscountRule;
	/**
	 * 拆分方式 0 确定单张发票金额 1 确定发票张数
	 */
	private Integer splitMethod;
	/**
	 * 订单总金额（不含税）
	 */
	private BigDecimal orderAmount;
	/**
	 * 票面金额上限（不含税）
	 */
	private BigDecimal invoiceAmountLimit;
	/**
	 * 拆分发票张数
	 */
	private Integer splitInvoiceNumber;
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
	/**
	 * 申请人名字，可操作
	 */
	private String applyUser;
	/**
	 * 申请人ID，当前操作用户ID
	 */
	private Long applyUserId;
	/**
	 * 申请时间
	 */
	private Date applyTime;
	/**
	 * 购方ID
	 */
	private Long purchaseId;
	/**
	 * 购方
	 */
	private String purchaseName;
	/**
	 * 销方ID
	 */
	private Long sellerId;
	/**
	 * 销方
	 */
	private String sellerName;
	/**
	 * 是否预制发票 0 否 1 是
	 */
	private Integer preformedInvoice;
	/**
	 * 入账凭证号
	 */
	private String accountingVouchers;
	/**
	 * 申请类型 0.订单开票 1.无订单开票 2.提前开票
	 */
	private Integer applyType;
	/**
	 * 不含税金额
	 */
	private BigDecimal excludingTaxAmount;
	/**
	 * 税率
	 */
	private String taxRate;
	/**
	 * 税额
	 */
	private BigDecimal taxAmount;
	/**
	 * 价税合计
	 */
	private BigDecimal totalAmount;

}
