package com.pwc.modules.input.entity;

import java.io.Serializable;

//
public class InputInvoiceMaterialDocumentRpa implements Serializable {

    private Integer id;
    private String materialDocumetNo;
    private String poType;
    private Integer ponoDocumentId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getPonoDocumentId() {
        return ponoDocumentId;
    }

    public void setPonoDocumentId(Integer ponoDocumentId) {
        this.ponoDocumentId = ponoDocumentId;
    }
}
