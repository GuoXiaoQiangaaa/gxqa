package com.pwc.modules.input.entity.vo;

import com.pwc.common.excel.annotation.ExcelField;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @description: 红字发票差异清单
 * @author: myz
 * @create: 2020-12-11 14:54
 **/
@Data
public class RedInvoiceDifferenceMatch {
    /**
     * 月份
     */
    @ExcelField(title = "月份", align = 1, sort = 10)
    private String mouth;

    @ExcelField(title = "入账凭证号", align = 1, sort = 10)
    private String documentNo;
    /**
     * 入账日期
     */
    @ExcelField(title = "入账日期", align = 1, sort = 10)
    private String pstngDate;
    /**
     * 入账税额
     */
    @ExcelField(title = "入账税额", align = 1, sort = 10)
    private String amountInLocal;
    /**
     * 红字通知单编号
     */
    @ExcelField(title = "红字通知单编号", align = 1, sort = 1)
    private String redNoticeNumber;
    /**
     * 蓝字发票号码
     */
    @ExcelField(title = "蓝字发票号码", align = 1, sort = 1)
    private String blueInvoiceNumber;
    /**
     * 蓝字发票代码
     */
    @ExcelField(title = "蓝字发票代码", align = 1, sort = 1)
    private String blueInvoiceCode;
    /**
     * 税额
     */
    @ExcelField(title = "税额（CNY）", align = 1, sort = 1)
    private BigDecimal taxPrice;
    /**
     * 填写日期
     */
    @ExcelField(title = "填开日期", align = 1, sort = 1)
    private Date writeDate;
    /**
     * 票账差异金额
     */
    @ExcelField(title = "票账差异金额", align = 1, sort = 10)
    private String differenceFee;

}
