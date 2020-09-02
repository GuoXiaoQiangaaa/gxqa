package com.pwc.modules.input.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pwc.common.excel.annotation.ExcelField;
import lombok.Data;

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
	private String redNoticeNumber;
	/**
	 * 填写日期
	 */
	@ExcelField(title = "填写日期", align = 1, sort = 1)
	private Date writeDate;
	/**
	 * 状态(0:红色通知单开具;1:红票已开)
	 */
	private String redStatus;
	/**
	 * 购方企业名称
	 */
	@ExcelField(title = "购方名称", align = 1, sort = 1)
	private String purchaserCompany;
	/**
	 * 购方纳税人识别号
	 */
	@ExcelField(title = "购方纳税人识别号", align = 1, sort = 1)
	private String purchaserTaxCode;
	/**
	 * 销方企业名称
	 */
	@ExcelField(title = "销方名称", align = 1, sort = 1)
	private String sellCompany;
	/**
	 * 销方纳税人识别号
	 */
	@ExcelField(title = "销方纳税人识别号", align = 1, sort = 1)
	private String sellTaxCode;
	/**
	 * 发票总额
	 */
	@ExcelField(title = "发票总额", align = 1, sort = 1)
	private BigDecimal totalPrice;
	/**
	 * 发票总额(不含税)
	 */
	@ExcelField(title = "不含税金额", align = 1, sort = 1)
	private BigDecimal freePrice;
	/**
	 * 税额
	 */
	@ExcelField(title = "税额", align = 1, sort = 1)
	private BigDecimal taxPrice;
	/**
	 * 税率
	 */
	@ExcelField(title = "税率", align = 1, sort = 1)
	private String taxRate;
	/**
	 * 蓝字发票号码
	 */
	@ExcelField(title = "蓝字发票号码", align = 1, sort = 1)
	private String blueInvoiceNumber;
	/**
	 * 蓝字发票代码
	 */
	@ExcelField(title = "蓝字发票代码", align = 1, sort = 1)
	private String blueInvoiceCode;
	/**
	 * 红字发票号码
	 */
	private String redInvoiceNumber;
	/**
	 * 红字发票代码
	 */
	private String redInvoiceCode;
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
