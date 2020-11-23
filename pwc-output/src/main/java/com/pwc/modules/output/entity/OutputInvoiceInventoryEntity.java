package com.pwc.modules.output.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 发票库存表
 *
 * @author fanpf
 * @date 2020/9/25
 */
@Data
@TableName("output_invoice_inventory")
public class OutputInvoiceInventoryEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId
	private Long inventoryId;
	/**
	 * 发票类型: 0:纸质专票; 1:纸质普票; 2:电子普票
	 */
	private String invoiceType;
	/**
	 * 当期购进发票张数
	 */
	private Integer inputNum;
	/**
	 * 当期销项开具张数
	 */
	private Integer outputNum;
	/**
	 * 当前库存
	 */
	private Integer inventory;
	/**
	 * 库存状态 1 正常 2 不足 3 过多
	 */
	private Integer status;
	/**
	 * 
	 */
	private Long createBy;
	/**
	 * 
	 */
	private Date createTime;
	/**
	 * 
	 */
	private Long updateBy;
	/**
	 * 
	 */
	private Date updateTime;
	/**
	 * 所属企业id
	 */
	private Long deptId;
	/**
	 * 发票代码起始
	 */
	private String invoiceCodeBegin;
	/**
	 * 发票代码结束
	 */
	private String invoiceCodeEnd;
	/**
	 * 发票号码起始
	 */
	private String invoiceNumBegin;
	/**
	 * 发票号码结束
	 */
	private String invoiceNumEnd;
	/**
	 * 报警条件
	 */
	@TableField(exist = false)
	private String alarmCondition;
	/**
	 * 所属公司
	 */
	@TableField(exist = false)
	private String deptName;
	/**
	 * 所属公司税号
	 */
	@TableField(exist = false)
	private String taxCode;

}
