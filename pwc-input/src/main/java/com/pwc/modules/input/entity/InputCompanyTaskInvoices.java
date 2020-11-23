package com.pwc.modules.input.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pwc.common.excel.annotation.ExcelField;
import lombok.Data;

import java.io.Serializable;
@Data
@TableName("input_company_task_invoices")
public class InputCompanyTaskInvoices implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId
    private Integer id;
    /**
     * companyId
     */
    private Integer companyId;

    /**
     * 企业名称
     */
    @TableField(exist = false)
    @ExcelField(title = "公司名称", align = 1, sort = 1)
    private String companyName;
    /**
     * 申请统计业务执行状态
     */
    private String type;
    /**
     * 统计月份
     */
    private String statisticsMonth;
    /**
     * 发票类型
     */
    @ExcelField(title = "发票类型", align = 1, sort = 1)
    private String invoiceType;
    /**
     * 发票代码
     */
    @ExcelField(title = "发票代码", align = 1, sort = 1)
    private String invoiceCode;
    /**
     * 发票号码
     */
    @ExcelField(title = "发票号码", align = 1, sort = 1)
    private String invoiceNumber;
    /**
     * 合计金额
     */
    @ExcelField(title = "合计金额", align = 1, sort = 1)
    private String amount;
    /**
     * 有效税额
     */
    @ExcelField(title = "有效税额", align = 1, sort = 1)
    private String validTax;
    /**
     * 转内销编号（出口转内销发票返回）
     */
    @ExcelField(title = "转内销编号（出口转内销发票返回）", align = 1, sort = 1)
    private String exportRejectNo;
    /**
     * 发票状态 0：正常 1：失控2：作废 3：红冲 4异常
     */
    @ExcelField(title = "发票状态", align = 1, sort = 1)
    private String invoiceStatus;
    /**
     * 管理状态 0正常1异常
     */
    @ExcelField(title = "管理状态", align = 1, sort = 1)
    private String manageStatus;
    /**
     * 勾选类型 0抵扣1不抵扣
     */
    @ExcelField(title = "勾选类型", align = 1, sort = 1)
    private String deductType;
    /**
     * 税额
     */
    @ExcelField(title = "税额", align = 1, sort = 1)
    private String tax;
    /**
     * 海关缴款书号码
     */
    @ExcelField(title = "海关缴款书号码", align = 1, sort = 1)
    private String paymentCertificateNo;
}
