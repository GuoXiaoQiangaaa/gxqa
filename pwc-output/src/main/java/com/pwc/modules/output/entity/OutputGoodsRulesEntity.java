package com.pwc.modules.output.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

/**
 * 商品规则
 * 
 * @author zk
 * @email 
 * @date 2020-06-01 18:17:06
 */
@Data
@TableName("output_goods_rules")
public class OutputGoodsRulesEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId
	private Long rulesId;
	/**
	 * 商品id
	 */
	private Long goodsId;
	/**
	 * 客户id
	 */
	private Long customerId;
	/**
	 * 商品单价
	 */
	private BigDecimal amount;
	/**
	 * 创建人
	 */
	private String createBy;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 修改人
	 */
	private String updateBy;
	/**
	 * 更新时间
	 */
	private Date updateTime;
	/**
	 * 商品
	 */
	private OutputGoodsEntity goods;
}
