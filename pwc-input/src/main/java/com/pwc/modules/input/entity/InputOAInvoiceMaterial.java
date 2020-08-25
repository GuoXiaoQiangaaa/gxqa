package com.pwc.modules.input.entity;


import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;

@TableName("input_oa_invoice_material")
public class InputOAInvoiceMaterial {

    private Integer id;

    private Integer invoiceId;

    private String sphXh;

    private String sphSpmc;

    private String sphGgxh;

    private String sphJldw;

    private BigDecimal sphSl;

    private BigDecimal sphDj;

    private BigDecimal sphJe;

    private String sphSlv;

    private BigDecimal sphSe;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getSphXh() {
        return sphXh;
    }

    public void setSphXh(String sphXh) {
        this.sphXh = sphXh;
    }

    public String getSphSpmc() {
        return sphSpmc;
    }

    public void setSphSpmc(String sphSpmc) {
        this.sphSpmc = sphSpmc;
    }

    public String getSphGgxh() {
        return sphGgxh;
    }

    public void setSphGgxh(String sphGgxh) {
        this.sphGgxh = sphGgxh;
    }

    public String getSphJldw() {
        return sphJldw;
    }

    public void setSphJldw(String sphJldw) {
        this.sphJldw = sphJldw;
    }

    public BigDecimal getSphSl() {
        return sphSl;
    }

    public void setSphSl(BigDecimal sphSl) {
        this.sphSl = sphSl;
    }

    public BigDecimal getSphDj() {
        return sphDj;
    }

    public void setSphDj(BigDecimal sphDj) {
        this.sphDj = sphDj;
    }

    public BigDecimal getSphJe() {
        return sphJe;
    }

    public void setSphJe(BigDecimal sphJe) {
        this.sphJe = sphJe;
    }

    public String getSphSlv() {
        return sphSlv;
    }

    public void setSphSlv(String sphSlv) {
        this.sphSlv = sphSlv;
    }

    public BigDecimal getSphSe() {
        return sphSe;
    }

    public void setSphSe(BigDecimal sphSe) {
        this.sphSe = sphSe;
    }
}
