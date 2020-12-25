package com.pwc.modules.output.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

/**
 * 销项发票表
 *
 * @author fanpf
 * @date 2020/9/24
 */
@Data
@TableName("output_invoice")
public class OutputInvoiceEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId
	private Long invoiceId;
	/**
	 * 所属企业id
	 */
	private Long deptId;
	/**
	 * 订单编号
	 */
	private String orderNumber;
	/**
	 * 订单ID
	 */
	private Long orderId;
	/**
	 * 发票代码
	 */
	private String invoiceCode;
	/**
	 * 发票号码
	 */
	private String invoiceNumber;
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
	 * 折扣金额（不含税）
	 */
	private BigDecimal excludingTaxDiscountAmount;
	/**
	 * 合计（含税）
	 */
	private BigDecimal totalAmount;
	/**
	 * 业务类型
	 */
	private String businessType;
	/**
	 * 开票方式
	 */
	private String invoicingMethod;
	/**
	 * 开票时间
	 */
	private Date invoicingTime;
	/**
	 * 发票载体
	 */
	private String invoiceEntity;
	/**
	 * 票据类型
	 */
	private String invoiceType;
	/**
	 * 购方名称
	 */
	private String purchaseName;
	/**
	 * 购方税号
	 */
	private String purchaseTaxCode;
	/**
	 * 购方地址
	 */
	private String purchaseAddress;
	/**
	 * 购方电话
	 */
	private String purchaseContact;
	/**
	 * 购方开户账号
	 */
	private String purchaseBankAccount;
	/**
	 * 购方开户行
	 */
	private String purchaseBank;
	/**
	 * 销方名称
	 */
	private String sellerName;
	/**
	 * 销方税号
	 */
	private String sellerTaxCode;
	/**
	 * 销方地址
	 */
	private String sellerAddress;
	/**
	 * 销方电话
	 */
	private String sellerContact;
	/**
	 * 销方开户账号
	 */
	private String sellerBankAccount;
	/**
	 * 销方开户行
	 */
	private String sellerBank;
	/**
	 * 开票申请状态：1.待提交 2.待审核 3.已撤销 4.审批驳回
	 */
	private Integer InvoiceRequisitionStatus;
	/**
	 * 发票状态：1.正常 2.失控 3.作废 4.红冲 5.异常
	 */
	private Integer invoiceStatus;
	/**
	 * 合同号
	 */
	private String contractNumber;
	/**
	 * 入账凭证号
	 */
	private String accountingVouchers;
	/**
	 * 关联会计凭证
	 */
	private String accountingDocument;
	/**
	 * 发票描述
	 */
	private String remark;
	/**
	 * 
	 */
	private Date createTime;
	/**
	 * 
	 */
	private String createBy;
	/**
	 * 
	 */
	private Date updateTime;
	/**
	 * 
	 */
	private String updateBy;
	/**
	 * 是否预制 0 否 1是
	 */
	private Integer preformedInvoice;
	/**
	 * 驳回意见
	 */
	private String rejectSuggestion;
	/**
	 * 关联的申请id
	 */
	private Long applyId;
	/**
	 * 申请号
	 */
	private String applyNumber;
	/**
	 * 申请人
	 */
	private String applyUser;
	/**
	 * 申请人ID，当前登陆操作用户
	 */
	private Long applyUserId;
	/**
	 * 申请日期
	 */
	private Date applyTime;
	/**
	 * 业务类型 0.订单开票 1.无订单开票 2.提前开票
	 */
	private Integer applyType;
	/**
	 * 核销状态： 0 否 1 是
	 */
	private Integer writeOff;
	/**
	 * 收款人
	 */
	private String payee;
	/**
	 * 复核人
	 */
	private String reviewer;
	/**
	 * 开票人
	 */
	private String drawer;
	/**
	 * 作废原因
	 */
	private String invalidReason;

}
