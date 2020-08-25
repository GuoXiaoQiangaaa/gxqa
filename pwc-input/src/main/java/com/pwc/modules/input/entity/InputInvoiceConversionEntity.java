package com.pwc.modules.input.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

@TableName("input_invoice_conversion")
public class InputInvoiceConversionEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId
    private Integer id;
    private String materialUnit;
    private String materialQuantity;
    private String invoiceUnit;
    private String invoiceQuantity;
    private String conversionDelete;


    public String getConversionDelete() {
        return conversionDelete;
    }

    public void setConversionDelete(String conversionDelete) {
        this.conversionDelete = conversionDelete;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMaterialUnit() {
        return materialUnit;
    }

    public void setMaterialUnit(String materialUnit) {
        this.materialUnit = materialUnit;
    }

    public String getMaterialQuantity() {
        return materialQuantity;
    }

    public void setMaterialQuantity(String materialQuantity) {
        this.materialQuantity = materialQuantity;
    }

    public String getInvoiceUnit() {
        return invoiceUnit;
    }

    public void setInvoiceUnit(String invoiceUnit) {
        this.invoiceUnit = invoiceUnit;
    }

    public String getInvoiceQuantity() {
        return invoiceQuantity;
    }

    public void setInvoiceQuantity(String invoiceQuantity) {
        this.invoiceQuantity = invoiceQuantity;
    }
}
