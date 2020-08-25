package com.pwc.modules.input.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

@TableName("input_invoice_material_rpa")
public class InputInvoiceMaterialRpaEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    private Integer id;
    private String groupId; //发票号（日期最新的那张发票号）
    private Date gruopDate; //为组名的那张发票的日期
    private String taxCode; //税码
    private String invoiceQuantity; //发票数量
    private Float totalTax; //组内所有发票税额总计
    private Float taxInludedPrice; //组内所有发票价税合计
    private String invoiceNo; //发票号
    private Float tax; //发票的税额

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Date getGruopDate() {
        return gruopDate;
    }

    public void setGruopDate(Date gruopDate) {
        this.gruopDate = gruopDate;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getInvoiceQuantity() {
        return invoiceQuantity;
    }

    public void setInvoiceQuantity(String invoiceQuantity) {
        this.invoiceQuantity = invoiceQuantity;
    }

    public Float getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(Float totalTax) {
        this.totalTax = totalTax;
    }

    public Float getTaxInludedPrice() {
        return taxInludedPrice;
    }

    public void setTaxInludedPrice(Float taxInludedPrice) {
        this.taxInludedPrice = taxInludedPrice;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public Float getTax() {
        return tax;
    }

    public void setTax(Float tax) {
        this.tax = tax;
    }
}
