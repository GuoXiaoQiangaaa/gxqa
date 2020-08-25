package com.pwc.modules.input.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

@TableName("input_material_document")
public class InputMaterialDocumentEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId
    private Integer id;
    private String materialDocumentNumber;
    private String materialNumber;
    private String materialDescription;
    private Integer materialCount;
    private Double materialPrice;
    private Double materialTotalPrice;
    private Double materialFreePrice;
    private Double materialTaxRate;
    private Date materialDeadDate;
    private String materialOrderNumber;
    private Integer materialOrderType;
    private String materialCompanyCode;
    private String materialSupplier;


    private Integer invoiceId; //发票表id

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMaterialDocumentNumber() {
        return materialDocumentNumber;
    }

    public void setMaterialDocumentNumber(String materialDocumentNumber) {
        this.materialDocumentNumber = materialDocumentNumber;
    }

    public String getMaterialNumber() {
        return materialNumber;
    }

    public void setMaterialNumber(String materialNumber) {
        this.materialNumber = materialNumber;
    }

    public String getMaterialDescription() {
        return materialDescription;
    }

    public void setMaterialDescription(String materialDescription) {
        this.materialDescription = materialDescription;
    }

    public Integer getMaterialCount() {
        return materialCount;
    }

    public void setMaterialCount(Integer materialCount) {
        this.materialCount = materialCount;
    }

    public Double getMaterialPrice() {
        return materialPrice;
    }

    public void setMaterialPrice(Double materialPrice) {
        this.materialPrice = materialPrice;
    }

    public Double getMaterialTotalPrice() {
        return materialTotalPrice;
    }

    public void setMaterialTotalPrice(Double materialTotalPrice) {
        this.materialTotalPrice = materialTotalPrice;
    }

    public Double getMaterialFreePrice() {
        return materialFreePrice;
    }

    public void setMaterialFreePrice(Double materialFreePrice) {
        this.materialFreePrice = materialFreePrice;
    }

    public Double getMaterialTaxRate() {
        return materialTaxRate;
    }

    public void setMaterialTaxRate(Double materialTaxRate) {
        this.materialTaxRate = materialTaxRate;
    }

    public Date getMaterialDeadDate() {
        return materialDeadDate;
    }

    public void setMaterialDeadDate(Date materialDeadDate) {
        this.materialDeadDate = materialDeadDate;
    }

    public String getMaterialOrderNumber() {
        return materialOrderNumber;
    }

    public void setMaterialOrderNumber(String materialOrderNumber) {
        this.materialOrderNumber = materialOrderNumber;
    }

    public Integer getMaterialOrderType() {
        return materialOrderType;
    }

    public void setMaterialOrderType(Integer materialOrderType) {
        this.materialOrderType = materialOrderType;
    }

    public String getMaterialCompanyCode() {
        return materialCompanyCode;
    }

    public void setMaterialCompanyCode(String materialCompanyCode) {
        this.materialCompanyCode = materialCompanyCode;
    }

    public String getMaterialSupplier() {
        return materialSupplier;
    }

    public void setMaterialSupplier(String materialSupplier) {
        this.materialSupplier = materialSupplier;
    }

    @Override
    public String toString() {
        return "MaterialDocumentEntity{" +
                "id=" + id +
                ", materialDocumentNumber='" + materialDocumentNumber + '\'' +
                ", materialNumber='" + materialNumber + '\'' +
                ", materialDescription='" + materialDescription + '\'' +
                ", materialCount=" + materialCount +
                ", materialPrice=" + materialPrice +
                ", materialTotalPrice=" + materialTotalPrice +
                ", materialFreePrice=" + materialFreePrice +
                ", materialTaxRate=" + materialTaxRate +
                ", materialDeadDate=" + materialDeadDate +
                ", materialOrderNumber='" + materialOrderNumber + '\'' +
                ", materialOrderType=" + materialOrderType +
                ", materialCompanyCode='" + materialCompanyCode + '\'' +
                ", materialSupplier='" + materialSupplier + '\'' +
                ", invoiceId=" + invoiceId +
                '}';
    }
}
