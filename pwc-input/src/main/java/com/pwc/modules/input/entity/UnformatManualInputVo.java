package com.pwc.modules.input.entity;

public class UnformatManualInputVo {
    String amount;
    String issueDate;
    String expenseNo;
    String unformatType;
    String invoiceCode;
    String invoiceNumber;
    String invoiceType;
    String invoiceTotalPrice;

    public String getInvoiceTotalPrice() {
        return invoiceTotalPrice;
    }

    public void setInvoiceTotalPrice(String invoiceTotalPrice) {
        this.invoiceTotalPrice = invoiceTotalPrice;
    }

    public String getInvoiceCode() {
        return invoiceCode;
    }

    public void setInvoiceCode(String invoiceCode) {
        this.invoiceCode = invoiceCode;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getUnformatType() {
        return unformatType;
    }

    public void setUnformatType(String unformatType) {
        this.unformatType = unformatType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getExpenseNo() {
        return expenseNo;
    }

    public void setExpenseNo(String expenseNo) {
        this.expenseNo = expenseNo;
    }
}
