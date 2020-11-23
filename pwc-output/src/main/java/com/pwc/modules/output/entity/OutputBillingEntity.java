package com.pwc.modules.output.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

/**
 * 销项Billing表
 *
 * @author fanpf
 * @date 2020/9/25
 */
@Data
@TableName("output_billing")
public class OutputBillingEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * billing表主键
	 */
	@TableId
	private Long billId;
	/**
	 * 账单号码
	 */
	private String billingNo;
	/**
	 * 公司代码
	 */
	private String deptCode;
	/**
	 * 会计科目
	 */
	private String accountItem;
	/**
	 * 付款方式
	 */
	private String payType;
	/**
	 * 凭证编号
	 */
	private String documentNo;
	/**
	 * 摘要
	 */
	private String summary;
	/**
	 * 分配
	 */
	private String assignment;
	/**
	 * 记账日期
	 */
	private String docDate;
	/**
	 * 入账日期
	 */
	private String pstngDate;
	/**
	 * 税码
	 */
	private String taxCode;
	/**
	 * 金额
	 */
	private BigDecimal amount;
	/**
	 * 当地币种(默认CNY)
	 */
	private String currencyLocal;
	/**
	 * po号码
	 */
	private String poNo;
	/**
	 * cancel by
	 */
	private String cancelBy;
	/**
	 * 合作伙伴
	 */
	private String tradingPartner;
	/**
	 * 开票状态
	 */
	private String invoiceStatus;
	/**
	 * 创建人
	 */
	private Integer createBy;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 修改人
	 */
	private Integer updateBy;
	/**
	 * 更新时间
	 */
	private Date updateTime;

}
