package com.pwc.modules.data.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pwc.common.excel.annotation.ExcelField;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * 进项税率校验表
 *
 * @author fanpf
 * @date 2020/8/29
 */
@Data
@TableName("input_tax_check")
public class InputTaxCheckEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 税率校验主键
	 */
	@TableId
	private Long checkId;
	/**
	 * 货品名称
	 */
	@ExcelField(title = "货品名称", align = 1, sort = 1)
	@NotBlank(message = "货品名称不能为空")
	private String goodsName;
	/**
	 * 税收分类编码
	 */
	@ExcelField(title = "税收分类编码", align = 1, sort = 1)
	@NotBlank(message = "税收分类编码不能为空")
	private String taxTypeCode;
	/**
	 * 非法税率
	 */
	@ExcelField(title = "非法税率", align = 1, sort = 1)
	@NotBlank(message = "非法税率不能为空")
	private String taxRate;
	/**
	 * 是否停用(0:停用;1:正常)
	 */
	private String delFlag;
	/**
	 * 创建人
	 */
	@TableField(fill = FieldFill.DEFAULT)
	private String createBy;
	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.DEFAULT)
	private Date createTime;
	/**
	 * 修改人
	 */
	@TableField(fill = FieldFill.DEFAULT)
	private String updateBy;
	/**
	 * 更新时间
	 */
	@TableField(fill = FieldFill.DEFAULT)
	private Date updateTime;

}
