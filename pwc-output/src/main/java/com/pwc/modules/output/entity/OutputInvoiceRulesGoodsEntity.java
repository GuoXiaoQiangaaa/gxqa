package com.pwc.modules.output.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 开票规则商品关联
 * 
 * @author zk
 * @email 
 * @date 2020-06-11 17:00:13
 */
@Data
@TableName("output_invoice_rules_goods")
public class OutputInvoiceRulesGoodsEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId
	private Long id;
	/**
	 * 开票规则id
	 */
	private Long rulesId;
	/**
	 * 商品ID
	 */
	private Long goodsId;
	/**
	 * 是否自动开票 0否1是
	 */
	private Integer autoInvoicing;
	/**
	 * 是否预制发票 0否 1是
	 */
	private Integer preformedInvoice;
	/**
	 * 
	 */
	private String createBy;
	/**
	 * 
	 */
	private Date createTime;
	/**
	 * 
	 */
	private String updateBy;
	/**
	 * 
	 */
	private Date updateTime;

	/**
	 * 商品名
	 */
	@TableField(exist = false)
	private String goodsName;

}
