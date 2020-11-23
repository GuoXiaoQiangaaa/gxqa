package com.pwc.modules.input.entity.vo;

import com.pwc.common.utils.excel.annotation.ExcelField;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @description: 账票匹配页面下载
 * @author: Gxw
 * @create: 2020-09-25 18:44
 **/
@Data
public class InvoiceMatchVo {
    /**
     * 批次号
     */
    @ExcelField(title = "批次号", align = 1, sort = 10)
    private String invoiceBatchNumber;
    /**
     * 发票代码
     */
    @ExcelField(title = "发票代码", align = 1, sort = 20)
    private String invoiceCode;
    /**
     * 发票号码
     */
    @ExcelField(title = "发票号码", align = 1, sort = 30)
    private String invoiceNumber;
    /**
     * 发票来源
     */
    @ExcelField(title = "发票类型", align = 1, sort = 50, dictType = "invoiceStyle")
    private String invoiceStyle;

    /**
     * 购方名称
     */
    @ExcelField(title = "购方企业名称", align = 1, sort = 60)
    private String invoicePurchaserCompany;
    /** 购方税号 */
    /**
     * @ExcelField(title="购方税号", align=1, sort=90)
     */
    private String invoicePurchaserParagraph;
    /**
     * 销方名称
     */
    @ExcelField(title = "销方企业名称", align = 1, sort = 70)
    private String invoiceSellCompany;
    /** 销方税号*/
    /**
     * @ExcelField(title="销方税号", align=1, sort=110)
     */
    private String invoiceSellParagraph;
    /** 货品简码&名称*/
    /**
     * 总金额（含税）
     */
    @ExcelField(title = "总金额", align = 1, sort = 80)
    private BigDecimal invoiceTotalPrice;
    /**
     * 总金额（不含税）
     */
    @ExcelField(title = "不含税金额", align = 1, sort = 90)
    private BigDecimal invoiceFreePrice;
    /**
     * 总税额
     */
    @ExcelField(title = "税额", align = 1, sort = 100)
    private BigDecimal invoiceTaxPrice;
    /**
     * 税率
     */
    @ExcelField(title = "税率", align = 1, sort = 110)
    private String tax;
    /**SAP税额*/

    /**
     * 金税发票状态 （0 正常，1 失控，2 作废，3 红冲)
     */
    @ExcelField(title = "金税发票状态", align = 1, sort = 120, dictType = "goldenTaxStatus")
    private String invoiceRecognition;
    /**
     * 发票分类
     */
    @ExcelField(title = "发票分类", align = 1, sort = 130, dictType = "invoiceClass")
    private String invoiceClass;
    /** 发票实体*/
    /**
     * @ExcelField(title="发票类型", align=1, sort=180,dictType = "invoiceEntity")
     */
    private String invoiceEntity;
    /**
     * 匹配日期
     */
    @ExcelField(title = "匹配日期", align = 1, sort = 140)
    private String matchDate;
    /**
     * SAP凭证号
     */
    @ExcelField(title = "SAP凭证号", align = 1, sort = 150)
    private String entrySuccessCode;
    /**会计期间*/

    /**
     * 认证所属期
     */
    @ExcelField(title = "认证所属期", align = 1, sort = 160)
    private String invoiceDeductiblePeriod;


}
