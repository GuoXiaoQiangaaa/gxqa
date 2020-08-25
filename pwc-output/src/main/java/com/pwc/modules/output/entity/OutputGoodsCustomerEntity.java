package com.pwc.modules.output.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

/**
 * 商品客户关联价格
 * 
 * @author zk
 * @email 
 * @date 2020-06-01 18:17:06
 */
@Data
@TableName("output_goods_customer")
public class OutputGoodsCustomerEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId
	private Long priceId;
	/**
	 * 商品id
	 */
	private Long goodsId;
	/**
	 * 客户id
	 */
	private Long customerId;
	/**
	 * 价格
	 */
	private BigDecimal price;

}
