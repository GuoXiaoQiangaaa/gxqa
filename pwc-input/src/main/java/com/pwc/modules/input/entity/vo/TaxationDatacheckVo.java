package com.pwc.modules.input.entity.vo;

import com.pwc.common.utils.excel.annotation.ExcelField;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author gxw
 * @date 2020/6/29 13:46
 */
@Data
public class TaxationDatacheckVo {
    @ExcelField(title = "入账税额",align =1,sort = 20)
    private BigDecimal tax;// 入账税额
    @ExcelField(title = "认证税额",align =1,sort = 30)
    private BigDecimal taxr; // 认证税额
    @ExcelField(title = "差异",align =1,sort = 40)
    private BigDecimal checkTax; // 核对
    @ExcelField(title = "月份",align =1,sort = 10)
    private String month; // 月份

}
