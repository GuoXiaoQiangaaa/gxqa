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
	/**公司代码*/
	private String companyCode;
	/**科目*/
	private String account;
	/**参考*/
	private String reference;
	/**凭证编号*/
	private String documentNo;
	/**凭证类型*/
	private String documentType;
	/**记账日期*/
	private String docDate;
	/**入账日期*/
	private String pstngDate;
	/**当地金额*/
	private BigDecimal amountInLocal;
	/**当地币种*/
	private String lcurr;
	/**金额*/
	private BigDecimal amountInDoc;
	/**币种*/
	private String curr;
	/**用户名*/
	private String userName;
	/**分配*/
	private String assignment;
	/**行文本*/
	private String text;
	/**税码*/
	private String tx;
	/**合作伙伴*/
	private String tradingPartner;
	/**
	 * 记账码
	 */
	private String postingKey;
	/**
	 * 年月
	 */
	private String yearMonth;
	/**
	 * 摘要
	 */
	private String headerText;
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
