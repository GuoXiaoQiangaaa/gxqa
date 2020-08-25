package com.pwc.modules.input.entity;

import java.math.BigDecimal;

public class InputExpenseInvoiceMappingBean {

    private String invoiceType;
    private BigDecimal totalMoney;
    private Integer countNumber;

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public BigDecimal getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(BigDecimal totalMoney) {
        this.totalMoney = totalMoney;
    }

    public Integer getCountNumber() {
        return countNumber;
    }

    public void setCountNumber(Integer countNumber) {
        this.countNumber = countNumber;
    }
}
