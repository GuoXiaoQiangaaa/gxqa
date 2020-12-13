package com.pwc.modules.input.entity.vo;

import com.pwc.common.excel.annotation.ExcelField;
import lombok.Data;

import java.io.Serializable;

/**
 * @description: 发票差异清单
 * @author: myz
 * @create: 2020-12-11 14:54
 **/
@Data
public class InvoiceDifferenceMatch implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 月份
     */
    @ExcelField(title = "月份", align = 1, sort = 1)
    private String mouth;

    @ExcelField(title = "入账凭证号", align = 1, sort = 1)
    private String documentNo;
    /**
     * 入账日期
     */
    @ExcelField(title = "入账日期", align = 1, sort = 1)
    private String pstngDate;
    /**
     * 入账税额
     */
    @ExcelField(title = "入账税额", align = 1, sort = 1)
    private String amountInLocal;
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
     * 发票税额
     */
    @ExcelField(title = "发票税额", align = 1, sort =1)
    private String invoiceTaxPrice;
    /**
     * 开票日期
     */
    @ExcelField(title = "开票日期", align = 1, sort = 1)
    private String invoiceCreateDate;
    /**
     * 票账差异金额
     */
    @ExcelField(title = "票账差异金额", align = 1, sort = 1)
    private String differenceFee;
}
