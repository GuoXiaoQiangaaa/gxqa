package com.pwc.modules.input.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fapiao.neon.model.in.SyncInvoice;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author zk
 * @email 
 * @date 2020-01-19 18:27:48
 */
@Data
@TableName("input_invoice_sync")
public class InputInvoiceSyncEntity extends SyncInvoice implements Serializable {
	private static final long serialVersionUID = 1L;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	/**
	 * 
	 */
	@TableId
	private Integer id;
	/**
	 * 
	 */
	@JSONField(name = "billingDate")
	private String billingDate;
	/**
	 * 
	 */
	@JSONField(name = "deductible")
	private String deductible;
	/**
	 * 
	 */
	@JSONField(name = "deductibleDate")
	private String deductibleDate;
	/**
	 * 
	 */
	@JSONField(name = "deductiblePeriod")
	private String deductiblePeriod;
	/**
	 * 
	 */
	@JSONField(name = "deductibleType")
	private String deductibleType;
	/**
	 * 
	 */
	@JSONField(name = "deductibleMode")
	private String deductibleMode;
	/**
	 * 
	 */
	@JSONField(name = "invoiceCode")
	private String invoiceCode;
	/**
	 * 
	 */
	@JSONField(name = "invoiceNumber")
	private String invoiceNumber;
	/**
	 * 
	 */
	@JSONField(name = "invoiceType")
	private String invoiceType;
	/**
	 * 
	 */
	@JSONField(name = "purchaserName")
	private String purchaserName;
	/**
	 * 
	 */
	@JSONField(name = "purchaserTaxNo")
	private String purchaserTaxNo;
	/**
	 * 
	 */
	@JSONField(name = "salesTaxName")
	private String salesTaxName;
	/**
	 * 
	 */
	@JSONField(name = "salesTaxNo")
	private String salesTaxNo;
	/**
	 * 
	 */
	@JSONField(name = "state")
	private String state;
	/**
	 * 
	 */
	@JSONField(name = "totalAmount")
	private String totalAmount;
	/**
	 * 
	 */
	@JSONField(name = "totalTax")
	private String totalTax;
	/**
	 * 
	 */
	@JSONField(name = "validTax")
	private String validTax;
	/**
	 * 
	 */
	@JSONField(name = "checkStatus")
	private String checkStatus;
	/**
	 * 
	 */
	@JSONField(name = "checkDate")
	private String checkDate;
	/**
	 * 
	 */
	@JSONField(name = "managementStatus")
	private String managementStatus;
	/**
	 * 
	 */
	@JSONField(name = "abnormalType")
	private String abnormalType;
	/**
	 * 
	 */
	@JSONField(name = "checkCode")
	private String checkCode;

	/**
	 * 是否代办退税 0：否 1：是
	 */
	@JSONField(name = "agencyDrawback")
	private String agencyDrawback;

	/**
	 * 信息来源 0：扫描认证 1：系统推送 2：不予退税
	 */
	@JSONField(name = "infoSources")
	private String infoSources;

	/**
	 * 有效税额 当invoiceType=01、14 and deductibleType=2、3时可为空
	 */
	@JSONField(name = "effectiveTaxAmount")
	private String effectiveTaxAmount;

	/**
	 * 逾期可勾选标志 逾期可勾选标志 0：默认值 1：可勾选逾期
	 */
	@JSONField(name = "overdueCheckMark")
	private String overdueCheckMark;

	/**
	 * 转内销证明编号
	 */
	@JSONField(name = "resaleCertificateNumber")
	private String resaleCertificateNumber;
	/**
	 * 状态
	 */
	private String status;
	/**
	 *  插入日期
	 */
	private Date createTime;

	private Long deptId;
}
