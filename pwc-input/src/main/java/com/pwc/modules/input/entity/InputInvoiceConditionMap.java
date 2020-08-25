package com.pwc.modules.input.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("input_invoice_condition_map")
public class InputInvoiceConditionMap {
    private Integer id;

    private Integer conditionDate; //条件天数
    @TableField(exist = false)
    private String conditionDate2; //条件天数

    private String conditionCode; // 条件码

    private String isDelete; // 是否删除（0：否，1：是）

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getConditionDate2() {
        return conditionDate2;
    }

    public void setConditionDate2(String conditionDate2) {
        this.conditionDate2 = conditionDate2;
    }

    public Integer getConditionDate() {
        return conditionDate;
    }

    public void setConditionDate(Integer conditionDate) {
        this.conditionDate = conditionDate;
    }

    public String getConditionCode() {
        return conditionCode;
    }

    public void setConditionCode(String conditionCode) {
        this.conditionCode = conditionCode;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }
}
