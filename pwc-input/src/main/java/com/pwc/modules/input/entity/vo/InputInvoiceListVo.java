package com.pwc.modules.input.entity.vo;

import com.pwc.common.utils.excel.annotation.ExcelField;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @description: 发票总览列表
 * @author: Gxw
 * @create: 2020-09-17 19:46
 **/
@Data
public class InputInvoiceListVo {
    /** 发票代码 */
    @ExcelField(title="发票代码", align=1, sort=10)
    private String invoiceCode;
    /**发票号码 */
    @ExcelField(title="发票号码", align=1, sort=20)
    private String invoiceNumber;
    /**  批次号 */
    @ExcelField(title="批次号", align=1, sort=30)
    private String invoiceBatchNumber;
    /**发票来源*/
    @ExcelField(title="发票来源", align=1, sort=40,dictType ="invoiceStyle")
    private String invoiceStyle;
    /**红字通知单编号*/
    @ExcelField(title="红字通知单编号", align=1, sort=50)
    private String redNoticeNumber;
    /**PO号码*/
    @ExcelField(title="PO号码", align=1, sort=60)
    private String poNumber;
    /**Report ID  关联报销单号*/
    @ExcelField(title="Report ID", align=1, sort=70)
    private String invoiceExpense;
    /** 购方名称 */
    @ExcelField(title="购方企业名称", align=1, sort=80)
    private String invoicePurchaserCompany;
    /** 购方税号*/
    @ExcelField(title="购方税号", align=1, sort=90)
    private String invoicePurchaserParagraph;
    /** 销方名称*/
    @ExcelField(title="销方企业名称", align=1, sort=100)
    private String invoiceSellCompany;
    /** 销方税号*/
    @ExcelField(title="销方税号", align=1, sort=110)
    private String invoiceSellParagraph;
    /**是否多税率*/
    @ExcelField(title="是否多税率", align=1, sort=120)
    private String manyTax;
    /**税率*/
    @ExcelField(title="税率", align=1, sort=130)
    private String tax;
    /**总金额（含税）*/
    @ExcelField(title="总金额（含税）", align=1, sort=140)
    private BigDecimal invoiceTotalPrice;
    /**总金额（不含税）*/
    @ExcelField(title="总金额（不含税）", align=1, sort=150)
    private BigDecimal invoiceFreePrice;
    /**总税额*/
    @ExcelField(title="总税额", align=1, sort=160)
    private BigDecimal invoiceTaxPrice;
    /** 发票分类*/
    @ExcelField(title="发票分类", align=1, sort=170,dictType = "invoiceClass")
    private String invoiceClass;
    /** 发票实体*/
    @ExcelField(title="发票类型", align=1, sort=180,dictType = "invoiceEntity")
    private String invoiceEntity;
    /**
     * 金税发票状态 （0 正常，1 失控，2 作废，3 红冲)
     */
    @ExcelField(title="金税发票状态", align=1, sort=190,dictType = "goldenTaxStatus")
    private String invoiceRecognition;
    /**开票日期*/
    @ExcelField(title="开票日期", align=1, sort=200)
    private String invoiceCreateDate;
    /**上传日期*/
    @ExcelField(title="上传日期", align=1, sort=210)
    private String invoiceUploadDate;

    /**更新日期*/
    @ExcelField(title="更新日期", align=1, sort=220)
    private Date updateTime;


}
