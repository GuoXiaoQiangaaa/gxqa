package com.pwc.modules.input.entity.rpa;

public class ZInvoiceAccount {
    public String INVOICE_NO;  //分配编号
    public Double TAX_AMOUNT;  //凭证货币金额

    @Override
    public String toString() {
        return "ZInvoiceAccount{" +
                "INVOICE_NO='" + INVOICE_NO + '\'' +
                ", TAX_AMOUNT=" + TAX_AMOUNT +
                '}';
    }
}
