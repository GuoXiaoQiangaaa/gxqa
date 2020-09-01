package com.pwc.modules.data.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pwc.common.excel.annotation.ExcelField;
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
	@ExcelField(title = "商品编码（必填）", align = 1, sort = 1)
	private String goodsNumber;
	/**
	 * 商品名称
	 */
	@ExcelField(title = "商品名称（必填）", align = 1, sort = 1)
	private String goodsName;
	/**
	 * 规格型号
	 */
	@ExcelField(title = "商品规格型号", align = 1, sort = 1)
	private String specifications;
	/**
	 * 计算单位
	 */
	@ExcelField(title = "计量单位", align = 1, sort = 1)
	private String unit;
	/**
	 * 商品单价
	 */
	@ExcelField(title = "商品价格", align = 1, sort = 1)
	private BigDecimal price;
	/**
	 * 所属企业名称
	 */
	@TableField(exist = false)
	@ExcelField(title = "所属机构", align = 1, sort = 1)
	private String deptName;
	/**
	 * 税收分类名称
	 */
	@ExcelField(title = "税收分类名称（必填）", align = 1, sort = 1)
	private String taxCategoryName;
	/**
	 * 税收分类编码
	 */
	@ExcelField(title = "税收分类编码（必填）", align = 1, sort = 1)
	private String taxCategoryCode;
	/**
	 * 所属企业id
	 */
	private Long deptId;
	/**
	 * 商品税率
	 */
	@ExcelField(title = "税率（必填）", align = 1, sort = 1)
	private String taxRate;
	/**
	 * 是否享受优惠政策0：否 1:是
	 */
	private Integer preferential;
	/**
	 * 是否享受优惠政策0：否 1:是
	 */
	@ExcelField(title = "是否享受优惠政策", align = 1, sort = 1)
	@TableField(exist = false)
	private String preferentialStr;
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
	 * 优惠政策类型(0:免税; 1:部分免税; 2:收税; 3:应税)
	 */
	@ExcelField(title = "优惠政策类型", align = 1, sort = 1)
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
