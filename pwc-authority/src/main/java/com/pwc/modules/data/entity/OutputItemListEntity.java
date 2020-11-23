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
 * 科目清单表
 *
 * @author fanpf
 * @date 2020/8/27
 */
@Data
@TableName("output_item_list")
public class OutputItemListEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 科目主键
	 */
	@TableId
	private Long itemId;
	/**
	 * 科目编码
	 */
	@ExcelField(title = "科目编码", align = 1, sort = 1)
	@NotBlank(message = "科目编码不能为空")
	private String itemCode;
	/**
	 * 科目类型
	 */
	@ExcelField(title = "科目类型", align = 1, sort = 1)
	@NotBlank(message = "科目类型不能为空")
	private String itemType;
	/**
	 * 科目描述
	 */
	@ExcelField(title = "科目描述", align = 1, sort = 1)
	private String description;
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
