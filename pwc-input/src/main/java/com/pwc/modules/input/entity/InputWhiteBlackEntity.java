package com.pwc.modules.input.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

@TableName("input_white_black")
public class InputWhiteBlackEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    @TableId
    private Integer id;
    private String name;
    private String approve;
    @TableField(exist = false)
    private InputWhiteBlackListEntity whiteBlackListEntity;

    public InputWhiteBlackListEntity getWhiteBlackListEntity() {
        return whiteBlackListEntity;
    }

    public void setWhiteBlackListEntity(InputWhiteBlackListEntity whiteBlackListEntity) {
        this.whiteBlackListEntity = whiteBlackListEntity;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApprove() {
        return approve;
    }

    public void setApprove(String approve) {
        this.approve = approve;
    }
}
