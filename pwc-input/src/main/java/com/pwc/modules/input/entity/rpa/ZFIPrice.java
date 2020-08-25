package com.pwc.modules.input.entity.rpa;


import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;

public class ZFIPrice {

    static String tabChar = "    ";

    public static void main(String[] args) throws Exception
    {
        ZFIPrice zprice = new ZFIPrice();
        zprice.execute();
    }

    public void execute() throws Exception {
        JCoFunction function = RfcManager.getFunction("ZFM_FI_TAXPLATFORM_PRICE");
        if (function == null){
            System.out.println("获取函数失败，您可能没有调用RFC的权限!");
            return;
        }

        System.out.println("SAP连接OK!");
        System.out.println("ZFM_FI_TAXPLATFORM_PRICE 调用开始...");

        System.out.println("RFC传递参数...");
        JCoTable IT_MBLNRS = function.getTableParameterList().getTable("IT_MBLNR");
        IT_MBLNRS.appendRow();
        IT_MBLNRS.setRow(0);
        IT_MBLNRS.setValue("MBLNR", "5001827062");
        IT_MBLNRS.setValue("RCODE", "");

        RfcManager.execute(function);

        System.out.println("RFC调用成功...");

        System.out.println("输出返回结果集...");
        JCoTable ET_DATAS = function.getTableParameterList().getTable("ET_DATA");
        for(int i=0;i<ET_DATAS.getNumRows();i++)
        {
            ET_DATAS.setRow(i);

            System.out.println("("+i+"):"+ET_DATAS.getValue("BUKRS")+tabChar
                    +ET_DATAS.getValue("LIFNR")+tabChar + ET_DATAS.getValue("NAME1") +tabChar
                    +ET_DATAS.getValue("SORTL")+tabChar + ET_DATAS.getValue("EBELN") +tabChar
                    +ET_DATAS.getValue("BSART")+tabChar + ET_DATAS.getValue("EBELP") +tabChar
                    +ET_DATAS.getValue("EKGRP")+tabChar + ET_DATAS.getValue("MBLNR") +tabChar
                    +ET_DATAS.getValue("ZEILE")+tabChar + ET_DATAS.getValue("WERKS") +tabChar
                    +ET_DATAS.getValue("MATNR")+tabChar + ET_DATAS.getValue("MAKTX") +tabChar);
        }

        System.out.println("ZFM_FI_TAXPLATFORM_PRICE 调用结束...");
    }
}
