package com.pwc.modules.data.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

/**
 * 商品信息
 *
 * @author fanpf
 * @date 2020/8/24
 */
@Data
@TableName("output_goods")
public class OutputGoodsNewEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId
	private Long goodsId;
	/**
	 * 商品编号
	 */
	private String goodsNumber;
	/**
	 * 商品名称
	 */
	private String goodsName;
	/**
	 * 所属企业id
	 */
	private Long deptId;
	/**
	 * 所属企业名称
	 */
	@TableField(exist = false)
	private String deptName;
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
	 * 税收分类编码
	 */
	private String taxCategoryCode;
	/**
	 * 税收分类名称
	 */
	private String taxCategoryName;
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
	 * 是否停用(0:停用;1:正常)
	 */
	private String delFlag;
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
