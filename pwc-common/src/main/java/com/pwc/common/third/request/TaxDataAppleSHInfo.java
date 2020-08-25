package com.pwc.common.third.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Tax Data Apple SH Info
 * @author louxin
 */
@Data
public class TaxDataAppleSHInfo {


    public static Integer[] QtyRow = new Integer[]{74, 88, 91, 94};
    public static Integer[] TaxBaseAmountRow = new Integer[]{11, 13, 15, 27, 29, 31, 44, 46, 48, 61, 64, 67, 74, 88, 91, 94};
    public static Integer[] TaxAmountRow = new Integer[]{11, 13, 15, 27, 29, 31, 44, 46, 48, 61, 64, 67, 74, 88, 91, 94, 101, 102, 103, 104, 111};
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
