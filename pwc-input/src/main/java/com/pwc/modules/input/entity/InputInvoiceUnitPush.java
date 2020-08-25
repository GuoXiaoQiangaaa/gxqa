package com.pwc.modules.input.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;

@TableName("input_invoice_unit_push")
public class InputInvoiceUnitPush {

    private Integer id;

    @TableField(exist = false)
    private String symbol = "=";

    private String basicUnit1;

    private String basicUnit2;

    private BigDecimal quantityUnit1;

    private BigDecimal quantityUnit2;

    private String isDelete; // 是否删除

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBasicUnit1() {
        return basicUnit1;
    }

    public void setBasicUnit1(String basicUnit1) {
        this.basicUnit1 = basicUnit1;
    }

    public String getBasicUnit2() {
        return basicUnit2;
    }

    public void setBasicUnit2(String basicUnit2) {
        this.basicUnit2 = basicUnit2;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
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
}
