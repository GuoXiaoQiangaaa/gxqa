package com.pwc.modules.input.entity.rpa;


//import com.chunqiu.BootApplication;
import com.sap.conn.jco.*;

public class ZMIRO {

    static String tabChar = "    ";

    public ZMiroOutput execute(ZInvoiceInfo invoice) throws Exception {

//        com.chunqiu.modules.operation.controller.CustomDestinationDataProvider.MyDestinationDataProvider myProvider = new com.chunqiu.modules.operation.controller.CustomDestinationDataProvider.MyDestinationDataProvider();
        String destName = "ABAP_AS";
        try {
//            com.sap.conn.jco.ext.Environment.registerDestinationDataProvider(myProvider);


            //set properties for the destination and ...

//            BootApplication.myProvider.changeProperties(destName, CustomDestinationDataProvider.getDestinationPropertiesFromUI2());
            //... work with it
//            JCoDestination destination = test.executeCalls(destName);
        } catch (Exception e) {
            e.printStackTrace();
//            return null;
        }
        JCoDestination destination = JCoDestinationManager.getDestination(destName);
        JCoFunction function = destination.getRepository().getFunctionTemplate("ZFM_FI_TAXPLATFORM_MIRO").getFunction();
//        JCoFunction function = RfcManager.getFunction("ZFM_FI_TAXPLATFORM_MIRO");
//        JCoDestination destination = JCoDestinationManager.getDestination("ABAP_AS");
//        JCoFunction function = destination.getRepository().getFunctionTemplate("ZFM_FI_TAXPLATFORM_MIRO").getFunction();

        if (function == null){
            System.out.println("获取函数失败，您可能没有调用RFC的权限!");
            return null;
        }
        System.out.println("SAP连接OK!");
        System.out.println("ZFM_FI_TAXPLATFORM_MIRO 调用开始...");

        System.out.println("RFC传递参数...");

        System.out.println("RFC传导入参数，结构体HEADERDATA...");
        JCoStructure HEADERDATA = function.getImportParameterList().getStructure("HEADERDATA");
        HEADERDATA.setValue("BATCH", invoice.BATCH);  //批号
        HEADERDATA.setValue("INV_NEW",invoice.INV_NEW); // 凭着组中最近一张发票的发票号码
        HEADERDATA.setValue("COMP_CODE", invoice.COMP_CODE);  //公司代码
        HEADERDATA.setValue("PMNTTRMS", invoice.PMNTTRMS);  //付款条件代码
        HEADERDATA.setValue("DOC_DATE", invoice.DOC_DATE);  //凭证中的凭证日期
        HEADERDATA.setValue("PSTNG_DATE", invoice.PSTNG_DATE);  //凭证中的过帐日期
        HEADERDATA.setValue("BLINE_DATE", invoice.BLINE_DATE);  //用于到期日计算的基准日期
        HEADERDATA.setValue("VEND_NAME", invoice.VEND_NAME);  //供应商名称
        HEADERDATA.setValue("GROSS_AMOUNT", invoice.GROSS_AMOUNT.toString());  //凭证货币的总发票金额


        System.out.println("RFC传导入参数，表ITEMDATA...");
        JCoTable ITEMDATA = function.getTableParameterList().getTable("ITEMDATA");
        for (int i =0; i< invoice.Details.size(); i++)
        {
            ZInvoiceDetailInfo invoiceDetail = invoice.Details.get(i);
            ITEMDATA.appendRow();
            ITEMDATA.setRow(i);
            ITEMDATA.setValue("PO_NUMBER", invoiceDetail.PO_NUMBER);  //采购订单编号
            ITEMDATA.setValue("PO_ITEM", invoiceDetail.PO_ITEM);  //采购凭证的项目编号
            ITEMDATA.setValue("MAT_DOC", invoiceDetail.MAT_DOC);  //物料凭证编号
            ITEMDATA.setValue("MAT_ITEM", invoiceDetail.MAT_ITEM);  //物料凭证中的项目
            ITEMDATA.setValue("QUANTITY", invoiceDetail.QUANTITY.toString());  //数量
            ITEMDATA.setValue("ITEM_AMOUNT", invoiceDetail.ITEM_AMOUNT.toString());  //凭证货币金额
            ITEMDATA.setValue("UNIT", invoiceDetail.UNIT);  //订单单位
            ITEMDATA.setValue("TAX_CODE", invoiceDetail.TAX_CODE);  //税码
        }

        System.out.println("RFC传导入参数，表GLACCOUNTDATA...");
        JCoTable GLACCOUNTDATA = function.getTableParameterList().getTable("GLACCOUNTDATA");
        for (int i =0; i< invoice.Accounts.size(); i++)
        {
            ZInvoiceAccount account = invoice.Accounts.get(i);
            GLACCOUNTDATA.appendRow();
            GLACCOUNTDATA.setRow(i);
            GLACCOUNTDATA.setValue("INVOICE_NO", account.INVOICE_NO);  //分配编号
            GLACCOUNTDATA.setValue("TAX_AMOUNT", account.TAX_AMOUNT.toString());  //凭证货币金额
        }

        function.execute(destination);
//        RfcManager.execute(function);

        System.out.println("RFC调用成功...");

        ZMiroOutput miroOutput = new ZMiroOutput();

        JCoStructure DOCDATA = function.getExportParameterList().getStructure("DOCDATA");
        //INV_DOC_NO:发票凭证的凭证编号, FISCALYEAR:会计年度
        miroOutput.INV_DOC_NO = DOCDATA.getValue("INV_DOC_NO").toString();
        miroOutput.FISCALYEAR = DOCDATA.getValue("FISCALYEAR").toString();
        miroOutput.BELNR = DOCDATA.getValue("BELNR").toString();
        //TYPE:消息类型: S 成功,E 错误,W 警告,I 信息,A 中断
        //ID:消息类
        //NUMBER:消息编号
        //MESSAGE:消息文本
        JCoTable dtRETURN = function.getTableParameterList().getTable("RETURN");
        for(int i=0;i<dtRETURN.getNumRows();i++)
        {
            dtRETURN.setRow(i);

            ZRFCReturn zReturn = new ZRFCReturn();

            zReturn.ZTYPE = dtRETURN.getValue("TYPE").toString();
            zReturn.ID = dtRETURN.getValue("ID").toString();
            zReturn.NUMBER = dtRETURN.getValue("NUMBER").toString();
            zReturn.MESSAGE = dtRETURN.getValue("MESSAGE").toString();
            miroOutput.listReturns.add(zReturn);
        }

        System.out.println("ZFM_FI_TAXPLATFORM_MIRO 调用结束...");
//          destination = null;
//        JCoContext.end(destination);
//        Environment.unregisterDestinationDataProvider(myProvider);
//        Environment.unregisterDestinationDataProvider(BootApplication.myProvider);
//        BootApplication.myProvider = new com.chunqiu.modules.operation.controller.CustomDestinationDataProvider.MyDestinationDataProvider();
//        com.sap.conn.jco.ext.Environment.registerDestinationDataProvider(BootApplication.myProvider);
        return miroOutput;
    }
}
