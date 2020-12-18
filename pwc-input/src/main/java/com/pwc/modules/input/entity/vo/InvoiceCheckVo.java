package com.pwc.modules.input.entity.vo;

import com.pwc.common.utils.excel.annotation.ExcelField;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @description: 发票勾选导出数据
 * @author: myz
 * @create: 2020-12-17 09:42
 **/
@Data
public class InvoiceCheckVo {
    /**
     * 发票代码
     */
    @ExcelField(title = "发票代码", align = 1, sort = 10)
    private String invoiceCode;
    /**
     * 发票号码
     */
    @ExcelField(title = "发票号码", align = 1, sort = 20)
    private String invoiceNumber;
    /**
     * 开票日期
     */
    @ExcelField(title = "开票日期", align = 1, sort = 30)
    private String invoiceCreateDate;
    /**
     * 购方名称
     */
    @ExcelField(title = "购方企业名称", align = 1, sort = 40)
    private String invoicePurchaserCompany;
    /**
     * 购方税号
     */
    @ExcelField(title = "购方税号", align = 1, sort = 50)
    private String invoicePurchaserParagraph;
    /**
     * 销方名称
     */
    @ExcelField(title = "销方企业名称", align = 1, sort = 60)
    private String invoiceSellCompany;
    /**
     * 销方税号
     */
    @ExcelField(title = "销方税号", align = 1, sort = 70)
    private String invoiceSellParagraph;
    @ExcelField(title = "上传方式", align = 1, sort = 80, dictType = "invoiceUploadType")
    private String invoiceUploadType;        // 上传方式 上传方式 0 抵账库上传 1：启动扫描仪上传 2： 手工上传电子发票
    @ExcelField(title = "总金额（含税）", align = 1, sort = 90)
    private BigDecimal invoiceTotalPrice;
    /**
     * 总金额（不含税）
     */
    @ExcelField(title = "总金额（不含税）", align = 1, sort = 100)
    private BigDecimal invoiceFreePrice;
    /**
     * 总税额
     */
    @ExcelField(title = "总税额", align = 1, sort = 110)
    private BigDecimal invoiceTaxPrice;
    /**
     * 批次号
     */
    @ExcelField(title = "批次号", align = 1, sort = 120)
    private String invoiceBatchNumber;
    /**
     * 发票来源
     */
    @ExcelField(title = "发票来源", align = 1, sort = 130, dictType = "invoiceStyle")
    private String invoiceStyle;
    @ExcelField(title = "认证月份", align = 1, sort = 140)
    private String invoiceDeductiblePeriod; // 税款所属期
    @ExcelField(title = "认证日期", align = 1, sort = 150)
    private String invoiceAuthDate; //认证日期
    /**
     * 发票实体
     */
    @ExcelField(title = "发票类型", align = 1, sort = 160, dictType = "invoiceEntity")
    private String invoiceEntity;
    @ExcelField(title = "金税发票状态", align = 1, sort = 170, dictType = "goldenTaxStatus")
    //（0 正常，1 失控，2 作废，3 红冲)
    private String invoiceRecognition;
    /**
     * 税率
     */
    @ExcelField(title = "税率", align = 1, sort = 180)
    private String tax;
    /**
     * 异常原因
     */
    @ExcelField(title = "异常原因", align = 1, sort = 190)
    private String invoiceErrorDescription;
    @ExcelField(title = "统计状态", align = 1, sort = 200, dictType = "applyStatus")
    private String applyStatus; // 统计状态 0-未统计 1-统计确认成功 2-统计确认失败
    /**
     * 发票状态
     */
    @ExcelField(title = "发票状态", align = 1, sort = 210)
    private String invoiceStatus;
}
