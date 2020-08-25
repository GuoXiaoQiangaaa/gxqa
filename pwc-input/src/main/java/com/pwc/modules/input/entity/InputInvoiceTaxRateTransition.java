package com.pwc.modules.input.entity;


import com.baomidou.mybatisplus.annotation.TableName;

@TableName("input_invoice_taxrate_transition")
public class InputInvoiceTaxRateTransition {
    private Integer id;

    private String taxCode;

    private String taxRate;

    private String isDelete;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(String taxRate) {
        this.taxRate = taxRate;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }
}
