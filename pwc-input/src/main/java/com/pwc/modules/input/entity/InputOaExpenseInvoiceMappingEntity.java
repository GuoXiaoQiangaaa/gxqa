package com.pwc.modules.input.entity;




import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 
 * 
 * @author jojo
 * @email wangjiaojiao640801@163.com
 * @date 2019-12-23 15:15:03
 */
@TableName("input_oa_expense_invoice_mapping")
@Data
public class InputOaExpenseInvoiceMappingEntity implements Serializable {
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
	private Integer invoiceNo;

	private Integer unformatNo;

	private String  invoiceType;

	private BigDecimal  money;

	/**
	 * g
	 */
	private Long companyId;




}
