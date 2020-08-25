package com.pwc.modules.input.entity;

public class InputInvoiceMaterial {

    private Integer id;
    private String companyCode;
    private String vendorCode;
    private String vendorName;
    private String status;

    private Integer invoiceBatchId;

    public Integer getInvoiceBatchId() {
        return invoiceBatchId;
    }

    public void setInvoiceBatchId(Integer invoiceBatchId) {
        this.invoiceBatchId = invoiceBatchId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getVendorCode() {
        return vendorCode;
    }

    public void setVendorCode(String vendorCode) {
        this.vendorCode = vendorCode;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

}
