package com.pwc.modules.input.entity.vo;

import com.pwc.common.utils.excel.annotation.ExcelField;
import com.sun.xml.internal.bind.v2.model.core.ID;
import lombok.Data;

/**
 * @description: Te发票上传模版
 * @author: Gxw
 * @create: 2020-09-01 14:28
 **/
@Data
public class InvoiceTeVo {
    @ExcelField(title = "Legal Entity")
    private String legalEntity; // 公司代码
    @ExcelField(title = "Report ID")
    private String reportId; //Report ID
    @ExcelField(title = "LInvoice series number\n" +
            "发票代码 （左上角）")
    private String invoiceCode; // 发票代码
    @ExcelField(title = "Invoice number\n" +
            "发票号码（右上角）y")
    private String invoiceNumber; // 发票号码
    @ExcelField(title = "invoice amount without VAT\n" +
            "发票不含税金额")
    private String invoiceFreePrice;
    @ExcelField(title = "invoice date\n" +
            "发票日期")
    private String invoiceCreateDate;
    @ExcelField(title="Input Date\n" +
            "录入日期",  sort=70)
    private String invoiceUploadDate;
}
