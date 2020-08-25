package com.pwc.modules.input.entity.vo;

import com.pwc.common.utils.excel.annotation.ExcelField;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author gxw
 * @date 2020/6/29 13:46
 */
@Data
public class TaxationToleranceVo {
    private Integer Id;// ID
    @ExcelField(title = "月份",align = 1,sort = 1)
    private String month; // 月份
    @ExcelField(title = "入账凭证号",align = 1,sort = 2)
    private String voucherNumber;// 凭证号
    @ExcelField(title = "入账日期",align = 1,sort = 3)
    private String entryDate;//入账日期
    @ExcelField(title = "入账税额",align =1,sort = 4)
    private BigDecimal tax;// 入账税额
    @ExcelField(title = "发票代码",align =1,sort = 5)
    private String invoiceCode; // 发票代码
    @ExcelField(title = "发票号码",align =1,sort = 6)
    private String invoiceNumber;//发票号码
    @ExcelField(title = "发票税额",align =1,sort = 7)
    private BigDecimal taxr; // 发票税额
    @ExcelField(title = "票帐差异金额",align =1,sort = 8)
    private BigDecimal checkTax; // 票帐差异金额
    @ExcelField(title = "调差状态",align =1,sort = 9,dictType = "toleranceFlag")
    private Integer toleranceFlag;// 调差状态





}
