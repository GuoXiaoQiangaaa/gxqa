package com.pwc.modules.input.entity.rpa;

public class ZInvoiceDetailInfo {
    public String PO_NUMBER;  //采购订单编号
    public String PO_ITEM;  //采购凭证的项目编号
    public String MAT_DOC;  //物料凭证编号
    public String MAT_ITEM;  //物料凭证中的项目
    public Double QUANTITY;  //数量
    public Double ITEM_AMOUNT;  //凭证货币金额
    public String UNIT;  //订单单位
    public String TAX_CODE;  //税码

    @Override
    public String toString() {
        return "ZInvoiceDetailInfo{" +
                "PO_NUMBER='" + PO_NUMBER + '\'' +
                ", PO_ITEM='" + PO_ITEM + '\'' +
                ", MAT_DOC='" + MAT_DOC + '\'' +
                ", MAT_ITEM='" + MAT_ITEM + '\'' +
                ", QUANTITY=" + QUANTITY +
                ", ITEM_AMOUNT=" + ITEM_AMOUNT +
                ", UNIT='" + UNIT + '\'' +
                ", TAX_CODE='" + TAX_CODE + '\'' +
                '}';
    }
}
