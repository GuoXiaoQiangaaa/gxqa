package com.pwc.modules.input.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

@TableName("input_invoice_rule")
public class InputInvoiceRuleEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId
    private Integer id;
    private String erpMaterial;
    private String invoiceCreditProduct;
    private String ruleDelete;

    public String getRuleDelete() {
        return ruleDelete;
    }

    public void setRuleDelete(String ruleDelete) {
        this.ruleDelete = ruleDelete;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getErpMaterial() {
        return erpMaterial;
    }

    public void setErpMaterial(String erpMaterial) {
        this.erpMaterial = erpMaterial;
    }

    public String getInvoiceCreditProduct() {
        return invoiceCreditProduct;
    }

    public void setInvoiceCreditProduct(String invoiceCreditProduct) {
        this.invoiceCreditProduct = invoiceCreditProduct;
    }
}
