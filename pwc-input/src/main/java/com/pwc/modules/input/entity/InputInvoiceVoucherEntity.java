package com.pwc.modules.input.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2019-01-17.
 */
@TableName("input_invoice_voucher")
public class InputInvoiceVoucherEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId
    private Integer id;
    private Integer invoiceBatchNumber;
    private String invoiceNumber;
    private String duplicate;
    private String voucherNumber;
    @TableField(exist = false)
    private List<String> invoiceIds;
    private Integer status;

    public String getDuplicate() {
        return duplicate;
    }

    public void setDuplicate(String duplicate) {
        this.duplicate = duplicate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<String> getInvoiceIds() {
        return invoiceIds;
    }

    public void setInvoiceIds(List<String> invoiceIds) {
        this.invoiceIds = invoiceIds;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getVoucherNumber() {
        return voucherNumber;
    }

    public void setVoucherNumber(String voucherNumber) {
        this.voucherNumber = voucherNumber;
    }

    public Integer getInvoiceBatchNumber() {
        return invoiceBatchNumber;
    }

    public void setInvoiceBatchNumber(Integer invoiceBatchNumber) {
        this.invoiceBatchNumber = invoiceBatchNumber;
    }

    @Override
    public String toString() {
        return "InvoiceVoucherEntity{" +
                "id=" + id +
                ", invoiceBatchNumber=" + invoiceBatchNumber +
                ", invoiceNumber='" + invoiceNumber + '\'' +
                ", voucherNumber='" + voucherNumber + '\'' +
                ", invoiceIds=" + invoiceIds +
                ", status=" + status +
                '}';
    }
}
