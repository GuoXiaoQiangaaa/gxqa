package com.pwc.modules.output.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

/**
 * 商品
 * 
 * @author zk
 * @email 
 * @date 2020-06-01 18:17:05
 */
@Data
@TableName("output_goods")
public class OutputGoodsEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId
	private Long goodsId;
	/**
	 * 商品编号
	 */
	private String goodsNo;
	/**
	 * 商品名称
	 */
	private String goodsName;
	/**
	 * 所属企业id
	 */
	private Long deptId;
	/**
	 * 商品税率
	 */
	private String taxRate;
	/**
	 * 商品单价
	 */
	private BigDecimal price;
	/**
	 * 是否享受优惠政策0：否 1:是
	 */
	private Integer preferential;
	/**
	 * sku编码
	 */
	private String skuCode;
	/**
	 * 税收分类id
	 */
	private Long taxCategory;
	/**
	 * 商品科目
	 */
	private String goodsSubject;
	/**
	 * 商品简码
	 */
	private String goodsCode;
	/**
	 * 规格型号
	 */
	private String specifications;
	/**
	 * 计算单位
	 */
	private String unit;
	/**
	 * 优惠类型
	 */
	private String preferentialType;
	/**
	 * nis的单价计量单位
	 */
	private String nisUnit;
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

}
