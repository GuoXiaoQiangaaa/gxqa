package com.pwc.modules.input.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

@TableName("input_oa_invoice_refund")
public class InputOAInvoiceRefund {
    /**
     *
     */
    @TableId
    private Integer id;
    /**
     * 发票id
     */
    private Integer invoiceId;
    /**
     * 退票原因
     */
    private String refundReason;
    /**
     * 详细原因
     */
    private String detailedReason;
    /**
     * 快递公司
     */
    private String expressCompany;
    /**
     * 快递单号
     */
    private String expressNo;
    /**
     * 退票执行人
     */
    private String refundUser;
    /**
     *
     */
    private Date refundTime;

    @TableField(exist = false)
    private String invoiceIds;

    public String getInvoiceIds() {
        return invoiceIds;
    }

    public void setInvoiceIds(String invoiceIds) {
        this.invoiceIds = invoiceIds;
    }

    /**
     * 设置：
     */
    public void setId(Integer id) {
        this.id = id;
    }
    /**
     * 获取：
     */
    public Integer getId() {
        return id;
    }
    /**
     * 设置：发票id
     */
    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }
    /**
     * 获取：发票id
     */
    public Integer getInvoiceId() {
        return invoiceId;
    }
    /**
     * 设置：退票原因
     */
    public void setRefundReason(String refundReason) {
        this.refundReason = refundReason;
    }
    /**
     * 获取：退票原因
     */
    public String getRefundReason() {
        return refundReason;
    }
    /**
     * 设置：详细原因
     */
    public void setDetailedReason(String detailedReason) {
        this.detailedReason = detailedReason;
    }
    /**
     * 获取：详细原因
     */
    public String getDetailedReason() {
        return detailedReason;
    }
    /**
     * 设置：快递公司
     */
    public void setExpressCompany(String expressCompany) {
        this.expressCompany = expressCompany;
    }
    /**
     * 获取：快递公司
     */
    public String getExpressCompany() {
        return expressCompany;
    }
    /**
     * 设置：快递单号
     */
    public void setExpressNo(String expressNo) {
        this.expressNo = expressNo;
    }
    /**
     * 获取：快递单号
     */
    public String getExpressNo() {
        return expressNo;
    }
    /**
     * 设置：退票执行人
     */
    public void setRefundUser(String refundUser) {
        this.refundUser = refundUser;
    }
    /**
     * 获取：退票执行人
     */
    public String getRefundUser() {
        return refundUser;
    }
    /**
     * 设置：
     */
    public void setRefundTime(Date refundTime) {
        this.refundTime = refundTime;
    }
    /**
     * 获取：
     */
    public Date getRefundTime() {
        return refundTime;
    }
}
