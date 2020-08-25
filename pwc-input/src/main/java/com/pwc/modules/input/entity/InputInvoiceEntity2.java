package com.pwc.modules.input.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pwc.common.utils.excel.annotation.ExcelField;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@TableName("input_invoice")
public class InputInvoiceEntity2 implements Serializable {
    @TableId
    private Integer id;
    @ExcelField(title="发票代码", align=1, sort=10)
    private String invoiceCode;        // 发票代码
    @ExcelField(title="发票号码", align=1, sort=20)
    private String invoiceNumber;        // 发票号码
    @ExcelField(title="界面金额（含税）", align=1, sort=70)
    private BigDecimal invoiceTotalPrice;        // 界面金额（含税）
    @ExcelField(title="界面金额（不含税）", align=1, sort=80)
    private BigDecimal invoiceFreePrice;        // 界面金额（不含税）
    @ExcelField(title="税额", align=1, sort=90)
    private BigDecimal invoiceTaxPrice;        // 税额
    @ExcelField(title="校验码", align=1, sort=100)
    private String invoiceCheckCode;        // 校验码
    @ExcelField(title="开票日期", align=1, sort=110)
    private String invoiceCreateDate;        // 开票日期
    @ExcelField(title="发票类型", align=1, sort=120)
    private String invoiceEntity;        // 发票类型
    @ExcelField(title="发票状态", align=1, sort=150)
    private String invoiceStatus;        // 发票状态
    @ExcelField(title="上传日期", align=1, sort=130)
    private String invoiceUploadDate;   //上传日期
    @ExcelField(title="购方企业名称", align=1, sort=30)
    private String invoicePurchaserCompany; //购方企业名称
    @ExcelField(title="购方税号", align=1, sort=40)
    private String invoicePurchaserParagraph;//购方税号
    @ExcelField(title="销方企业名称", align=1, sort=50)
    private String invoiceSellCompany; //销方企业名称
    @ExcelField(title="销方税号", align=1, sort=60)
    private String invoiceSellParagraph; //销方税号
//    @ExcelField(title="上传人姓名", align=1, sort=30)
    private String createUserName; // 上传人姓名
    @ExcelField(title="手工备注", align=1, sort=140)
    private String invoiceRemarks; // 备注
    @ExcelField(title="详情", align=1, sort=160)
    private String invoiceErrorDescription; //详情

}
