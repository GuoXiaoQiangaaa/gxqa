package com.pwc.modules.input.entity.rpa;

public class ZMiroExcute {

    static String tabChar = "    ";

    public static void main(String[] args) throws Exception
    {
        try {
            ZInvoiceInfo invoice = GetInvoiceInfo();

            ZMIRO zmiro = new ZMIRO();
            ZMiroOutput miroOutput = zmiro.execute(invoice);
//            ZMiroOutput miroOutput = StepClient.testPassEntry(invoice);

            if (miroOutput == null) {
                System.out.println("RFC调用失败!");
                return;
            }

            if (miroOutput.listReturns.size() > 0) {
                System.out.println("发票校验失败!");
                for (int i = 0; i < miroOutput.listReturns.size(); i++) {
                    ZRFCReturn zReturn = miroOutput.listReturns.get(i);

                    System.out.println("(" + i + "):" + zReturn.ZTYPE + tabChar
                            + zReturn.ID + tabChar + zReturn.NUMBER + tabChar
                            + zReturn.MESSAGE + tabChar);
                }
            } else {
                System.out.println("发票校验成功!");
                System.out.println("发票凭证号为：" + miroOutput.INV_DOC_NO + tabChar
                        + "会计年度为:" + miroOutput.FISCALYEAR);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            System.out.println("RFC函数调用失败!" + ex.getMessage());
        }
    }

    public static ZInvoiceInfo GetInvoiceInfo()
    {
        ZInvoiceInfo invoice = new ZInvoiceInfo();

        //初始化发票抬头
        invoice.BATCH = "INVOICE";  //批号
        invoice.COMP_CODE = "6101";  //公司代码
        invoice.PMNTTRMS = "D030";  //付款条件代码
        invoice.DOC_DATE = "20190415";  //凭证中的凭证日期
        invoice.PSTNG_DATE = "20190416";  //凭证中的过帐日期
        invoice.BLINE_DATE = "20190217";  //用于到期日计算的基准日期
        invoice.VEND_NAME = "新卫";  //供应商名称
        invoice.GROSS_AMOUNT = 47.85;  //凭证货币的总发票金额

        //初始化Acctount
        ZInvoiceAccount account = new ZInvoiceAccount();
        account.INVOICE_NO = "INVOICE01";  //分配编号
        account.TAX_AMOUNT = 6.6;  //凭证货币金额
        invoice.Accounts.add(account);

        //初始化InvoiceDetails
        ZInvoiceDetailInfo invoiceDetail = new ZInvoiceDetailInfo();
        invoiceDetail.PO_NUMBER = "4500108187";  //采购订单编号
        invoiceDetail.PO_ITEM = "00020";  //采购凭证的项目编号
        invoiceDetail.MAT_DOC = "5001977438";  //物料凭证编号
        invoiceDetail.MAT_ITEM = "0001";  //物料凭证中的项目
        invoiceDetail.QUANTITY = 2.0;  //数量
        invoiceDetail.ITEM_AMOUNT = 27.5;  //凭证货币金额
        invoiceDetail.UNIT = "KG";  //订单单位
        invoiceDetail.TAX_CODE = "J7";  //税码
        invoice.Details.add(invoiceDetail);

        ZInvoiceDetailInfo invoiceDetail2 = new ZInvoiceDetailInfo();
        invoiceDetail2.PO_NUMBER = "4500108187";  //采购订单编号
        invoiceDetail2.PO_ITEM = "00020";  //采购凭证的项目编号
        invoiceDetail2.MAT_DOC = "5001977437";  //物料凭证编号
        invoiceDetail2.MAT_ITEM = "0001";  //物料凭证中的项目
        invoiceDetail2.QUANTITY = 1.0;  //数量
        invoiceDetail2.ITEM_AMOUNT = 13.75;  //凭证货币金额
        invoiceDetail2.UNIT = "KG";  //订单单位
        invoiceDetail2.TAX_CODE = "J7";  //税码
        invoice.Details.add(invoiceDetail2);

        return invoice;
    }


}
