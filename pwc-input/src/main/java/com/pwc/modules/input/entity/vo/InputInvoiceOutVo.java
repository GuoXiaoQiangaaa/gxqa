package com.pwc.modules.input.entity.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Gxw
 * @date 2020/7/22 18:44
 */
@Data
public class InputInvoiceOutVo {
    private Integer id;
    private String month; // 月份
    private String objectName; // 标签分类
    private Integer proportion; // 比例
    private BigDecimal outAmount;  // 金额
    private BigDecimal amountSum;  // 转出入账金额
    private BigDecimal outAmountSum;  // 转出入账金额（税方）
    private List<OutReasonVo> resaon;
    @Data
    public static class OutReasonVo{
        private BigDecimal Amount; // 差异额
        private String  ReasonNote; // 原因备注
    }

}
