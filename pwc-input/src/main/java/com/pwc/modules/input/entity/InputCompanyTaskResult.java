package com.pwc.modules.input.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

@TableName("input_company_task_result")
public class InputCompanyTaskResult implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId
    private Integer id;

    private Integer companyId;

    private String type;

    private String invoiceType;

    private String deductibleNum;

    private String deductibleAmount;

    private String deductibleTax;

    private String unDeductibleNum;

    private String unDeductibleAmount;

    private String unDeductibleTax;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getDeductibleNum() {
        return deductibleNum;
    }

    public void setDeductibleNum(String deductibleNum) {
        this.deductibleNum = deductibleNum;
    }

    public String getDeductibleAmount() {
        return deductibleAmount;
    }

    public void setDeductibleAmount(String deductibleAmount) {
        this.deductibleAmount = deductibleAmount;
    }

    public String getDeductibleTax() {
        return deductibleTax;
    }

    public void setDeductibleTax(String deductibleTax) {
        this.deductibleTax = deductibleTax;
    }

    public String getUnDeductibleNum() {
        return unDeductibleNum;
    }

    public void setUnDeductibleNum(String unDeductibleNum) {
        this.unDeductibleNum = unDeductibleNum;
    }

    public String getUnDeductibleAmount() {
        return unDeductibleAmount;
    }

    public void setUnDeductibleAmount(String unDeductibleAmount) {
        this.unDeductibleAmount = unDeductibleAmount;
    }

    public String getUnDeductibleTax() {
        return unDeductibleTax;
    }

    public void setUnDeductibleTax(String unDeductibleTax) {
        this.unDeductibleTax = unDeductibleTax;
    }
}
