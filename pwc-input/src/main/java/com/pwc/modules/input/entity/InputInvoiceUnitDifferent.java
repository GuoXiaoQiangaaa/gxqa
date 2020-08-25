package com.pwc.modules.input.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pwc.common.utils.excel.annotation.ExcelField;

import java.math.BigDecimal;

@TableName("input_invoice_unit_different")
public class InputInvoiceUnitDifferent {

    private Integer id;

    @ExcelField(title="物料编码", align=1, sort=10)
    private String sapMblnr; // 物料号

    @ExcelField(title="物料编码描述", align=1, sort=20)
    private String sapDetails; // 物料号

    @ExcelField(title="单位1", align=1, sort=40)
    private String unitClassification1; // 单位1

    @ExcelField(title="单位2", align=1, sort=70)
    private String unitClassification2; // 单位2

    @TableField(exist = false)
    @ExcelField(title="转换", align=1, sort=50)
    private String symbol = "=";

    @ExcelField(title="数量1", align=1, sort=30)
    private BigDecimal quantityUnit1; // 单位数量1

    @ExcelField(title="数量2", align=1, sort=60)
    private BigDecimal quantityUnit2; // 单位数量2

    @ExcelField(title="状态", align=1, sort=80)
    private String isDelete; // 是否删除

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSapMblnr() {
        return sapMblnr;
    }

    public void setSapMblnr(String sapMblnr) {
        this.sapMblnr = sapMblnr;
    }

    public String getUnitClassification1() {
        return unitClassification1;
    }

    public void setUnitClassification1(String unitClassification1) {
        this.unitClassification1 = unitClassification1;
    }

    public String getUnitClassification2() {
        return unitClassification2;
    }

    public void setUnitClassification2(String unit_classification2) {
        this.unitClassification2 = unit_classification2;
    }

    public BigDecimal getQuantityUnit1() {
        return quantityUnit1;
    }

    public void setQuantityUnit1(BigDecimal quantityUnit1) {
        this.quantityUnit1 = quantityUnit1;
    }

    public BigDecimal getQuantityUnit2() {
        return quantityUnit2;
    }

    public void setQuantityUnit2(BigDecimal quantityUnit2) {
        this.quantityUnit2 = quantityUnit2;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public String getSapDetails() {
        return sapDetails;
    }

    public void setSapDetails(String sapDetails) {
        this.sapDetails = sapDetails;
    }
}
