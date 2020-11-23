package com.pwc.modules.input.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 海关缴款书SAP入账或冲销凭证信息推送表
 * 
 * @author zlb
 * @email 
 * @date 2020-08-12 17:01:42
 */
@Data
@TableName("input_invoice_customs_push")
public class InputInvoiceCustomsPushEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 凭证编号
	 */
	private String voucherNumber;
	/**
	 * 公司代码
	 */
	private String companyId;
	/**
	 * 凭证类型 DR：手工凭证 RV：销售或退货凭证
	 */
	private String voucherType;
	/**
	 * 科目编号
	 */
	private String accountingNumber;
	/**
	 * 过账日期
	 */
    private String 	postingDate;
	/**
	 * 本币币种
	 */
	private String	currencyType;
	/**
	 * 本位币金额 +入账 -冲销
	 */
	private String	amount;
	/**
	 * 文本字段
	 */
	private String	textField;
	/**
	 * 分配字段 如果是海关缴款书凭证，则为18位编号）
	 */
	private String	distributeField;

	/**
	 * 会计期间
	 */
	private String period;
	/**
	 * 入账税金额
	 */
	private String entryTax;

	/**
	 * 抽取状态0未抽1已抽
	 */
	private String state;

	private String createDate;
	/**
	 * 记账码
	 */
	private String accountingCode;
	/**
	 * 参考代码2
	 */
	private String 	consultField;
}
