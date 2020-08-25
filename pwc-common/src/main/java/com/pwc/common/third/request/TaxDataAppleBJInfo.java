package com.pwc.common.third.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Tax Data Apple BJ Info
 * @author louxin
 */
@Data
public class TaxDataAppleBJInfo {


    public static Integer[] QtyRow = new Integer[]{29, 31, 32, 34};
    public static Integer[] TaxBaseAmountRow = new Integer[]{10, 12, 14, 18, 20, 22, 29, 31, 32, 34};
    public static Integer[] TaxAmountRow = new Integer[]{10, 12, 14, 18, 20, 22, 29, 31, 32, 34, 42, 44, 51};
    public static Integer[] SpceRow = new Integer[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2};

    /**
     * Qty
     */
    private List<BigDecimal> Qty;

    /**
     * Tax Base Amount
     */
    private List<BigDecimal> TaxBaseAmount;

    /**
     * Tax Amount
     */
    private List<BigDecimal> TaxAmount;


    @Override
    public String toString() {
        return "TaxDataAppleBJInfo{" +
                "Qty=" + Qty +
                ", TaxBaseAmount=" + TaxBaseAmount +
                ", TaxAmount=" + TaxAmount +
                '}';
    }
}
