package com.pwc.modules.input.entity;



import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 
 * 
 * @author jojo
 * @email wangjiaojiao640801@163.com
 * @date 2019-12-23 15:15:03
 */
@Data
@TableName("input_oa_expense_info")
public class InputOaExpenseInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private String expenseNumber;
	/**
	 * 
	 */
	@TableId
	private Integer id;

	/**
	 * 
	 */
	private String applyUser;
	/**
	 * 
	 */
	private String endUser;
	/**
	 * 
	 */
	private String applyTime;
	/**
	 * 
	 */
	private BigDecimal amount;
	/**
	 * 
	 */
	private Integer expenseStatus;
	private String expenseType;

	private BigDecimal invoicesAmount;

	/**
	 * 关联企业
	 */
	private Long companyId;

	@TableField(exist = false)
	private List<InputExpenseInvoiceMappingBean> expenseInvoiceMappingBeanList;

	@TableField(exist = false)
	private List<InputInvoiceEntity> invoiceList;




}
