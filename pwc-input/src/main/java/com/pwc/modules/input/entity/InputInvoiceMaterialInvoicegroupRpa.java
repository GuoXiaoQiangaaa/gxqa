package com.pwc.modules.input.entity;


import com.baomidou.mybatisplus.annotation.TableField;

import java.math.BigDecimal;

public class InputInvoiceMaterialInvoicegroupRpa {

    private Integer id;
    private String invoiceNo;
    private BigDecimal tax;
    private Integer degroupRpaId;
    @TableField(exist = false)
    private Integer invoiceId;

    public Integer getDegroupRpaId() {
        return degroupRpaId;
    }

    public void setDegroupRpaId(Integer degroupRpaId) {
        this.degroupRpaId = degroupRpaId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }
}
