package com.pwc.modules.input.entity.vo;

import com.pwc.common.utils.excel.annotation.ExcelField;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gxw
 * @date 2020/6/29 13:46
 */
@Data
public class InputInvoiceTaxationVo {
    private Integer Id;// ID
    @ExcelField(title = "科目编号",align =1,sort = 10)
    private String accountNumber;// 科目编号
    @ExcelField(title = "科目描述",align =1,sort = 20)
    private String accountDescription;// 科目描述
    @ExcelField(title = "入账税额",align =1,sort = 30)
    private BigDecimal tax;// 入账税额
    @ExcelField(title = "认证税额",align =1,sort = 40)
    private BigDecimal taxr; // 认证税额
    @ExcelField(title = "本月入账未认证(新增)",align =1,sort = 80)
    private BigDecimal benTax; //本月入账未认证
    @ExcelField(title = "本月认证未入账(新增)",align =1,sort = 90)
    private BigDecimal benTaxr;//本月认证未入账
    @ExcelField(title = "前期入账本月认证(核销)",align =1,sort = 100)
    private BigDecimal qianTax; //前入账本认证
    @ExcelField(title = "前期认证本入账(新增)",align =1,sort = 110)
    private BigDecimal qianTaxr;//前认证本入账
    @ExcelField(title = "税会差异",align =1,sort = 50)
    private BigDecimal difference;// 税会差异
    @ExcelField(title = "差异调整合计",align =1,sort = 60)
    private BigDecimal adjustment;// 差异调整
    @ExcelField(title = "核对",align =1,sort = 70)
    private BigDecimal checkTax; // 核对
    private String invoiceNumber;//   认证发票号
    private String invoiceCode;        // 发票代码
    private String invoiceAuthDate; //认证日期
    private String entryDate;//入账日期
    private int aging;//账龄
    private String agingFlag;//入账标志 0 null 不打√ / 1 打√
    private String CertificationFlag;//认证标志 0 null 不打√ / 1 打√
    private String colourFlag;// 颜色标志 0 null 黑色 / 1 红色
    @ExcelField(title = "调整原因",align =1,sort = 120)
    private List<ReasonVo> reasonVo = new ArrayList<ReasonVo>(); // 调整原因
    private String voucherNumber;// 凭证号
    @ExcelField(title = "月份",align =1,sort = 1)
    private String month; // 月份

    @Data
    public static class ReasonVo {
        @ExcelField(title = "调整原因", align = 1, sort = 120)
        private String adjustmentReason;// 调整原因
        @ExcelField(title = "调整金额", align = 1, sort = 120)
        private String adjustmentTax;// 调整金额

    }

}
