package com.pwc.modules.input.entity;



import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 旅客票据Entity
 */
@Data
@TableName("input_invoice_taxation")
public class InputInvoiceTaxationEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId
    private Integer id;
    private Integer invoiceId;
    private String invoiceCode;        // 发票代码
    private String invoiceNumber;        // 发票号
    private BigDecimal invoiceTotalPrice; // 票面金额（含税）
    private BigDecimal invoiceTaxPrice;        // 税额
    private String invoiceStatus;        // 发票状态
    private String entryDate; // 入账日期
    private String invoiceAuthDate; //认证日期
    private Date taxationDate;// 计税查询日期
    private String taxationFlag;//  计税查询标志
    private String taxationDataFlag;//数据处理标志
    private String adjustmentReason;//新增调整原因
    private String accountNumber;// 科目编号
    private String accountDescription;// 科目描述
    private String adjustmentTax;//调整金额
    private String voucherNumber;// 凭证号
    private BigDecimal tolerance;// 容差额
    private Integer toleranceFlag;// 调差状态

}

