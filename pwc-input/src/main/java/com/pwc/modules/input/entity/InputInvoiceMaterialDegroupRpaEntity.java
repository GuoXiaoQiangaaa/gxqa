package com.pwc.modules.input.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@TableName("input_invoice_material_degroup_rpa")
public class InputInvoiceMaterialDegroupRpaEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    private Integer id;
    private String groupId; //发票号（日期最新的那张发票号）
    private Date gruopDate; //为组名的那张发票的日期
    private String taxCode; //税码
    private String invoiceQuantity; //发票数量
    private BigDecimal totalTax; //组内所有发票税额总计
    private String paymentTerm;
    private BigDecimal taxInludedPrice; //组内所有发票价税合计
    private Integer invoiceMaterialRpaId; //与公司代码表对应的Id

    public String getPaymentTerm() {
        return paymentTerm;
    }

    public void setPaymentTerm(String paymentTerm) {
        this.paymentTerm = paymentTerm;
    }

    private InputInvoiceMaterialInvoicegroupRpa invoiceMaterialInvoicegroupRpa;

    public InputInvoiceMaterialInvoicegroupRpa getInvoiceMaterialInvoicegroupRpa() {
        return invoiceMaterialInvoicegroupRpa;
    }

    public void setInvoiceMaterialInvoicegroupRpa(InputInvoiceMaterialInvoicegroupRpa invoiceMaterialInvoicegroupRpa) {
        this.invoiceMaterialInvoicegroupRpa = invoiceMaterialInvoicegroupRpa;
    }

    public Integer getInvoiceMaterialRpaId() {
        return invoiceMaterialRpaId;
    }

    public void setInvoiceMaterialRpaId(Integer invoiceMaterialRpaId) {
        this.invoiceMaterialRpaId = invoiceMaterialRpaId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Date getGruopDate() {
        return gruopDate;
    }

    public void setGruopDate(Date gruopDate) {
        this.gruopDate = gruopDate;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getInvoiceQuantity() {
        return invoiceQuantity;
    }

    public void setInvoiceQuantity(String invoiceQuantity) {
        this.invoiceQuantity = invoiceQuantity;
    }

    public BigDecimal getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(BigDecimal totalTax) {
        this.totalTax = totalTax;
    }

    public BigDecimal getTaxInludedPrice() {
        return taxInludedPrice;
    }

    public void setTaxInludedPrice(BigDecimal taxInludedPrice) {
        this.taxInludedPrice = taxInludedPrice;
    }

}
