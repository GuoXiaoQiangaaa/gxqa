package com.pwc.modules.input.entity.rpa;


import java.util.ArrayList;
import java.util.List;

public class ZInvoiceInfo {
    public String BATCH;  //批号
    public String COMP_CODE;  //公司代码
    public String PMNTTRMS;  //付款条件代码
    public String DOC_DATE;  //凭证中的凭证日期
    public String PSTNG_DATE;  //凭证中的过帐日期s
    public String BLINE_DATE;  //用于到期日计算的基准日期
    public String VEND_NAME;  //供应商名称
    public Double GROSS_AMOUNT;  //凭证货币的总发票金额
    public String INV_NEW; // 凭着组中最近一张发票的发票号码

    public List<ZInvoiceDetailInfo> Details = new ArrayList<ZInvoiceDetailInfo>();
    public List<ZInvoiceAccount> Accounts = new ArrayList<ZInvoiceAccount>();

    @Override
    public String toString() {
        return "ZInvoiceInfo{" +
                "BATCH='" + BATCH + '\'' +
                ", COMP_CODE='" + COMP_CODE + '\'' +
                ", PMNTTRMS='" + PMNTTRMS + '\'' +
                ", DOC_DATE='" + DOC_DATE + '\'' +
                ", PSTNG_DATE='" + PSTNG_DATE + '\'' +
                ", BLINE_DATE='" + BLINE_DATE + '\'' +
                ", VEND_NAME='" + VEND_NAME + '\'' +
                ", GROSS_AMOUNT=" + GROSS_AMOUNT +
                ", INV_NEW='" + INV_NEW + '\'' +
                ", Details=" + Details +
                ", Accounts=" + Accounts +
                '}';
    }
}
