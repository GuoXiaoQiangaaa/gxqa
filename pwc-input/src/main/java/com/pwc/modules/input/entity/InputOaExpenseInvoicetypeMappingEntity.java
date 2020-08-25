package com.pwc.modules.input.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 
 * 
 * @author jojo
 * @email wangjiaojiao640801@163.com
 * @date 2019-12-29 02:09:58
 */
@TableName("input_oa_expense_invoicetype_mapping")
public class InputOaExpenseInvoicetypeMappingEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Integer id;
	/**
	 * 
	 */
	private Integer expenseNo;
	/**
	 * 
	 */
	private BigDecimal invoicesAmount;
	/**
	 * 
	 */
	private String avaliableAmount;
	/**
	 * 
	 */
	private String invoiceType;
	private BigDecimal invoicesCount;



	/**
	 * 0 异常；1 正常
	 */
	private String status;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getExpenseNo() {
		return expenseNo;
	}

	public void setExpenseNo(Integer expenseNo) {
		this.expenseNo = expenseNo;
	}

	public BigDecimal getInvoicesAmount() {
		return invoicesAmount;
	}

	public void setInvoicesAmount(BigDecimal invoicesAmount) {
		this.invoicesAmount = invoicesAmount;
	}

	public String getAvaliableAmount() {
		return avaliableAmount;
	}

	public void setAvaliableAmount(String avaliableAmount) {
		this.avaliableAmount = avaliableAmount;
	}

	public String getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BigDecimal getInvoicesCount() {
		return invoicesCount;
	}

	public void setInvoicesCount(BigDecimal invoicesCount) {
		this.invoicesCount = invoicesCount;
	}
}
