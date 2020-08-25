package com.pwc.modules.input.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

@TableName("input_fault_tolerant")
public class InputInvoiceFaultTolerantEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    private Integer id;
    private String fault;
    private String unit;

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFault() {
        return fault;
    }

    public void setFault(String fault) {
        this.fault = fault;
    }

}
