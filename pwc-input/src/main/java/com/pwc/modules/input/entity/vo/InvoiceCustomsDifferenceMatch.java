package com.pwc.modules.input.entity.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.pwc.common.excel.annotation.ExcelField;
import lombok.Data;

/**
 * @description: 海关通知单差异清单
 * @author: myz
 * @create: 2020-12-11 14:54
 **/
@Data
public class InvoiceCustomsDifferenceMatch {
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

    @ExcelField(title="海关缴款书编号", align=1, sort=10)
    private String payNo;

    /**
     * 税款金额
     */
    @ExcelField(title="税额", align=1, sort=50)
    private String totalTax;

    /**
     * 批次号(格式:yyyyMMdd-1)
     */
    @TableField(fill = FieldFill.DEFAULT)
    private String batchNo;

    /**
     * 填发日期
     */
    @ExcelField(title="填发日期", align=1, sort=70)
    private String billingDate;

    /**
     * 票账差异金额
     */
    @ExcelField(title = "票账差异金额", align = 1, sort = 10)
    private String differenceFee;

}
