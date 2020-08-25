package com.pwc.modules.input.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

@TableName("input_invoice_material_pogroup_rpa")
public class InputInvoiceMaterialPogroupRpaEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    private Integer id;
    private String poNo; //订单编号
    private String materialDocumetNo; //材料类物料凭证编码
    private String poType; //材料采购订单
    private String materialCode; //材料类物料编码
    private String materialPrice; //材料类物料的不含税金额(不是单价)
    private String materialAmmount; //材料类物料的数量
    private String materialDocumetNo2; //费用类物料凭证编码
    private String poType2; //费用类采购订单
    private String materialCode2; //费用类物料编码
    private String materialPrice2; //费用类物料的不含税金额(不是单价)
    private String materialAmmount2; //费用类物料的数量

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPoNo() {
        return poNo;
    }

    public void setPoNo(String poNo) {
        this.poNo = poNo;
    }

    public String getMaterialDocumetNo() {
        return materialDocumetNo;
    }

    public void setMaterialDocumetNo(String materialDocumetNo) {
        this.materialDocumetNo = materialDocumetNo;
    }

    public String getPoType() {
        return poType;
    }

    public void setPoType(String poType) {
        this.poType = poType;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getMaterialPrice() {
        return materialPrice;
    }

    public void setMaterialPrice(String materialPrice) {
        this.materialPrice = materialPrice;
    }

    public String getMaterialAmmount() {
        return materialAmmount;
    }

    public void setMaterialAmmount(String materialAmmount) {
        this.materialAmmount = materialAmmount;
    }

    public String getMaterialDocumetNo2() {
        return materialDocumetNo2;
    }

    public void setMaterialDocumetNo2(String materialDocumetNo2) {
        this.materialDocumetNo2 = materialDocumetNo2;
    }

    public String getPoType2() {
        return poType2;
    }

    public void setPoType2(String poType2) {
        this.poType2 = poType2;
    }

    public String getMaterialCode2() {
        return materialCode2;
    }

    public void setMaterialCode2(String materialCode2) {
        this.materialCode2 = materialCode2;
    }

    public String getMaterialPrice2() {
        return materialPrice2;
    }

    public void setMaterialPrice2(String materialPrice2) {
        this.materialPrice2 = materialPrice2;
    }

    public String getMaterialAmmount2() {
        return materialAmmount2;
    }

    public void setMaterialAmmount2(String materialAmmount2) {
        this.materialAmmount2 = materialAmmount2;
    }
}
