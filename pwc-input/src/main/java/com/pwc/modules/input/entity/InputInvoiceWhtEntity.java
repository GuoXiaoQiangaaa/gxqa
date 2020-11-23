package com.pwc.modules.input.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pwc.common.excel.annotation.ExcelField;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

/**
 * WHT(代扣代缴单据)表
 *
 * @author fanpf
 * @date 2020/9/4
 */
@Data
@TableName("input_invoice_wht")
public class InputInvoiceWhtEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * wht表主键
	 */
	@TableId
	private Long whtId;
	/**
	 * 公司代码
	 */
	@ExcelField(title = "公司编码（Legal Entity）", align = 1, sort = 1)
    @NotBlank(message = "公司代码不能为空")
	private String deptCode;
	/**
	 * 公司名称
	 */
	private String deptName;
	/**
	 * 代扣代缴编号
	 */
	@ExcelField(title = "代扣代缴编号", align = 1, sort = 1)
    @NotBlank(message = "代扣代缴编号不能为空")
	private String whtCode;
	/**
	 * 税额
	 */
	@ExcelField(title = "税额（CNY）", align = 1, sort = 1)
    @NotNull(message = "税额不能为空")
	private BigDecimal taxPrice;
	/**
	 * 金额
	 */
	@ExcelField(title = "金额（CNY）", align = 1, sort = 1)
    @NotNull(message = "金额不能为空")
	private BigDecimal totalPrice;
	/**
	 * 认证所属期
	 */
	@ExcelField(title = "认证所属期", align = 1, sort = 1)
    @NotBlank(message = "认证所属期不能为空")
	private String authDate;
	/**
	 * 部门id
	 */
	private Integer deptId;
	/**
	 * 创建人
	 */
	@TableField(fill = FieldFill.INSERT)
	private Integer createBy;
	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private Date createTime;
	/**
	 * 修改人
	 */
	@TableField(fill = FieldFill.UPDATE)
	private Integer updateBy;
	/**
	 * 更新时间
	 */
	@TableField(fill = FieldFill.UPDATE)
	private Date updateTime;

	/**
	 * 删除标识:0:删除; 1:正常
	 */
	private String delFlag;

}
