package com.pwc.modules.input.entity;

import java.io.Serializable;
import java.math.BigDecimal;

//
public class InputInvoiceDetailsDocumentRpa implements Serializable {
    private Integer Id;
    private String materialCode;
    private BigDecimal materialPrice;
    private String materialAmmount;
    private Integer documentRpaId;

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public BigDecimal getMaterialPrice() {
        return materialPrice;
    }

    public void setMaterialPrice(BigDecimal materialPrice) {
        this.materialPrice = materialPrice;
    }

    public String getMaterialAmmount() {
        return materialAmmount;
    }

    public void setMaterialAmmount(String materialAmmount) {
        this.materialAmmount = materialAmmount;
    }

    public Integer getDocumentRpaId() {
        return documentRpaId;
    }

    public void setDocumentRpaId(Integer documentRpaId) {
        this.documentRpaId = documentRpaId;
    }
}
