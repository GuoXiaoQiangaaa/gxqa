package com.pwc.modules.input.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 发票退票表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2018-12-26 16:58:47
 */
@Data
@TableName("input_invoice_refund")
public class InputInvoiceRefundEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Integer id;
	/**
	 * 发票id
	 */
	private Integer invoiceId;
	/**
	 * 退票原因
	 */
	private String refundReason;
	/**
	 * 详细原因
	 */
	private String detailedReason;
	/**
	 * 快递公司
	 */
	private String expressCompany;
	/**
	 * 快递单号
	 */
	private String expressNo;
	/**
	 * 退票执行人
	 */
	private String refundUser;
	/**
	 * 
	 */
	private Date refundTime;

	private String refundStatus;

	/**
	 * 批量退票需要
	 */
	@TableField(exist = false)
	private String invoiceIds;
}
