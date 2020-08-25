package com.pwc.modules.input.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
@Data
@TableName("input_company_task_invoices")
public class InputCompanyTaskInvoices implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *id
     */
    @TableId
    private Integer id;
    /**
     *companyId
     */
    private Integer companyId;
    /**
     *申请统计业务执行状态
     */
    private String type;
    /**
     *统计月份
     */
    private String statisticsMonth;
    /**
     *发票类型
     */
    private String invoiceType;
    /**
     *发票代码
     */
    private String invoiceCode;
    /**
     *发票号码
     */
    private String invoiceNumber;
    /**
     *合计金额
     */
    private String amount;
    /**
     *有效税额
     */
    private String validTax;
    /**
     *转内销编号（出口转内销发票返回）
     */
    private String exportRejectNo;
    /**
     *发票状态 0：正常 1：失控2：作废 3：红冲 4异常
     */
    private String invoiceStatus;
    /**
     *管理状态 0正常1异常
     */
    private String manageStatus;
    /**
     *勾选类型 0抵扣1不抵扣
     */
    private String deductType;
    /**
     *税额
     */
    private String tax;
    /**
     *海关缴款书号码
     */
    private String paymentCertificateNo;
}
