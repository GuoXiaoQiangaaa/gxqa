package com.pwc.modules.input.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pwc.common.excel.annotation.ExcelField;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 进项转出明细表
 *
 * @author fanpf
 * @date 2020/9/17
 */
@Data
@TableName("input_export_detail")
public class InputExportDetailEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 转出明细主键
	 */
	@TableId
	private Long exportId;
	/**
	 * 公司代码
	 */
	@ExcelField(title = "Company code", align = 1, sort = 1)
	private String companyCode;
	/**
	 * 科目
	 */
	@ExcelField(title = "Account", align = 1, sort = 1)
	private String account;
	/**
	 * 参考
	 */
	@ExcelField(title = "Reference", align = 1, sort = 1)
	private String reference;
	/**
	 * 凭证编号
	 */
	@ExcelField(title = "Document No", align = 1, sort = 1)
	private String documentNo;
	/**
	 * 凭证类型
	 */
	@ExcelField(title = "Type", align = 1, sort = 1)
	private String type;
	/**
	 * 记账日期
	 */
	@ExcelField(title = "Doc. Date", align = 1, sort = 1)
	private String docDate;
	/**
	 * 入账日期
	 */
	@ExcelField(title = "Pstng Date", align = 1, sort = 1)
	private String pstngDate;
	/**
	 * 当地金额
	 */
	@ExcelField(title = "Amount in local cur.", align = 1, sort = 1)
	private BigDecimal amountLocal;
	/**
	 * 当地币种(默认CNY)
	 */
	@ExcelField(title = "Lcurr", align = 1, sort = 1)
	private String currencyLocal;
	/**
	 * 金额
	 */
	@ExcelField(title = "Amount in doc. curr", align = 1, sort = 1)
	private BigDecimal amount;
	/**
	 * 币种(默认CNY)
	 */
	@ExcelField(title = "Curr.", align = 1, sort = 1)
	private String currency;
	/**
	 * 用户名
	 */
	@ExcelField(title = "User name", align = 1, sort = 1)
	private String userName;
	/**
	 * 分配
	 */
	@ExcelField(title = "Assignment", align = 1, sort = 1)
	private String assignment;
	/**
	 * 行文本
	 */
	@ExcelField(title = "Text", align = 1, sort = 1)
	private String text;
	/**
	 * 税率
	 */
	@ExcelField(title = "Tx", align = 1, sort = 1)
	private String taxRate;
	/**
	 * 合作伙伴
	 */
	@ExcelField(title = "Trading Partner", align = 1, sort = 1)
	private String tradingPartner;
	/**
	 * 转出类型: 0:红字转出; 1:海关免税转出; 2:福利转出; 3:其他转出
	 */
	private String exportType;
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
	 * 更新人
	 */
	@TableField(fill = FieldFill.UPDATE)
	private Integer updateBy;
	/**
	 * 更新时间
	 */
	@TableField(fill = FieldFill.UPDATE)
	private Date updateTime;

}
