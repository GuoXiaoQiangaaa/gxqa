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
 * @date 2019-12-29 17:15:57
 */
@TableName("input_unformat_invoice")
@Data
public class InputUnformatInvoiceEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Integer id;
	/**
	 * 
	 */
	private String fields;
	/**
	 * 
	 */
	private String content;
	/**
	 * 
	 */
	private BigDecimal amount;
	/**
	 * 
	 */
	private String issueDate;
	/**
	 * 
	 */
	private String createTime;

	private String imageUrl;

	private Integer createBy;

	private Long companyId;




}
