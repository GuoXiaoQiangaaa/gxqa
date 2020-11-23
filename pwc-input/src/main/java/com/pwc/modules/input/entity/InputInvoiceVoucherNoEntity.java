package com.pwc.modules.input.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 发票入账 凭证号和发票对应表
 * 
 * @author zlb
 * @email 
 * @date 2020-08-19 12:08:28
 */
@Data
@TableName("input_invoice_voucher_no")
public class InputInvoiceVoucherNoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 发票代码
	 */
	private String invoiceCode;
	/**
	 * 
	 */
	private String invoiceNumber;
	/**
	 * 凭证编号
	 */
	private String voucherNumber;
	/**
	 * 入账类型1入账2冲销
	 */
	private String entryState;
	/**
	 * 入账方式1.人工 2.sss 3.sap
	 */
	private String entryType;
	/**
	 * 
	 */
	private String createDate;

}
