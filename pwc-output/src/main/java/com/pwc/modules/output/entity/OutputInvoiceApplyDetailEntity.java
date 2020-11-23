package com.pwc.modules.output.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 开票申请明细表
 *
 * @author fanpf
 * @date 2020/9/24
 */
@Data
@TableName("output_invoice_apply_detail")
public class OutputInvoiceApplyDetailEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId
	private Long detailId;
	/**
	 * 开票申请ID
	 */
	private Long applyId;
	/**
	 * 货物或应税劳务名称
	 */
	private String name;
	/**
	 * 规格型号
	 */
	private String specifications;
	/**
	 * 单位
	 */
	private String unit;
	/**
	 * 数量
	 */
	private Double quantity;
	/**
	 * 单价
	 */
	private BigDecimal price;
	/**
	 * 金额
	 */
	private BigDecimal amount;
	/**
	 * 税率
	 */
	private String taxRate;
	/**
	 * 税额
	 */
	private BigDecimal taxAmount;
	/**
	 * 状态
	 */
	private Integer status;

}
