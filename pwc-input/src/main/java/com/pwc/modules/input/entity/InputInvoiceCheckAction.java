package com.pwc.modules.input.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

@TableName("input_check_action")
public class InputInvoiceCheckAction implements Serializable {
    @TableId
    private Integer id; //

    private Integer coId; // SAP接口BUKRS

    private String items; // sap返回代码

    private String action; // 权限码

    private String description; // 详细

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCoId() {
        return coId;
    }

    public void setCoId(Integer coId) {
        this.coId = coId;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
