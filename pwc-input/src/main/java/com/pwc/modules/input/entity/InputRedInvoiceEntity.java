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
 * 红字发票表
 *
 * @author fanpf
 * @date 2020/8/25
 */
@Data
@TableName("input_red_invoice")
public class InputRedInvoiceEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 红字发票主键
	 */
	@TableId
	private Long redId;
	/**
	 * 红字通知单编号
	 */
	@ExcelField(title = "红字通知单编号", align = 1, sort = 1)
	@NotBlank(message = "红字通知单编号不能为空")
	private String redNoticeNumber;
	/**
	 * 填写日期
	 */
	@ExcelField(title = "填开日期", align = 1, sort = 1)
	@NotBlank(message = "填开日期不能为空")
	private Date writeDate;
	/**
	 * 状态(0:红色通知单开具;1:红票已开;2作废)
	 */
	private String redStatus;
	/**
	 * 购方企业名称
	 */
	@ExcelField(title = "购方名称", align = 1, sort = 1)
	@NotBlank(message = "购方名称不能为空")
	private String purchaserCompany;
	/**
	 * 购方纳税人识别号
	 */
	@ExcelField(title = "购方纳税人识别号（税号）", align = 1, sort = 1)
	@NotBlank(message = "购方纳税人识别号不能为空")
	private String purchaserTaxCode;
	/**
	 * 销方企业名称
	 */
	@ExcelField(title = "销方名称", align = 1, sort = 1)
	@NotBlank(message = "销方名称不能为空")
	private String sellCompany;
	/**
	 * 销方纳税人识别号
	 */
	@ExcelField(title = "销方纳税人识别号（税号）", align = 1, sort = 1)
	@NotBlank(message = "销方纳税人识别号不能为空")
	private String sellTaxCode;
	/**
	 * 发票总额
	 */
	@ExcelField(title = "发票总额（CNY）", align = 1, sort = 1)
	@NotNull(message = "发票总额不能为空")
	private BigDecimal totalPrice;
	/**
	 * 发票总额(不含税)
	 */
	@ExcelField(title = "不含税金额（CNY）", align = 1, sort = 1)
	@NotNull(message = "不含税金额不能为空")
	private BigDecimal freePrice;
	/**
	 * 税额
	 */
	@ExcelField(title = "税额（CNY）", align = 1, sort = 1)
	@NotNull(message = "税额不能为空")
	private BigDecimal taxPrice;
	/**
	 * 税率
	 */
	@ExcelField(title = "税率", align = 1, sort = 1)
	@NotBlank(message = "税率不能为空")
	private String taxRate;
	/**
	 * 蓝字发票号码
	 */
	@ExcelField(title = "蓝字发票号码", align = 1, sort = 1)
	@NotBlank(message = "蓝字发票号码不能为空")
	private String blueInvoiceNumber;
	/**
	 * 蓝字发票代码
	 */
	@ExcelField(title = "蓝字发票代码", align = 1, sort = 1)
	@NotBlank(message = "蓝字发票代码不能为空")
	private String blueInvoiceCode;
	/**
	 * 红字发票号码
	 */
	@ExcelField(title = "红字发票号码", align = 1, sort = 1)
	private String redInvoiceNumber;
	/**
	 * 红字发票代码
	 */
	@ExcelField(title = "红字发票代码", align = 1, sort = 1)
	private String redInvoiceCode;
	/**
	 * 创建人
	 */
	@TableField(fill = FieldFill.DEFAULT)
	private String createBy;
	/**
	 * 创建时间
	 */
	@ExcelField(title = "创建时间", align = 1, sort = 1)
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

	/**
	 * 部门id
	 */
	@TableField(fill = FieldFill.DEFAULT)
	private Long deptId;
	/**
	 * 匹配日期
	 */
	@ExcelField(title = "匹配日期", align = 1, sort = 1)
	private String matchDate;
	/**
	 * 凭证编号
	 */
	@ExcelField(title = "凭证编号", align = 1, sort = 1)
	private String documentNo;
	/**
	 * 入账日期
	 */
	@ExcelField(title = "入账日期", align = 1, sort = 1)
	private String entryDate;
	/**
	 * 入账状态
	 */
	private String entryStatus;
	/**
	 * 年月
	 */
	@ExcelField(title = "会计期间", align = 1, sort = 1)
	private String yearAndMonth;

	/**
	 * SAP差异原因
	 */
	@ExcelField(title = "差异原因", align = 1, sort = 1)
	private String sapReason;
	/**
	 * SAP税额
	 */
	@ExcelField(title = "sap税额", align = 1, sort = 1)
	private String sapTax;
	/**
	 * SAP差异额
	 */
	@ExcelField(title = "sap差异额", align = 1, sort = 1)
	private String sapCheckTax;

}
