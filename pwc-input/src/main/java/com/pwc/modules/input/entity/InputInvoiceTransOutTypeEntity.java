package com.pwc.modules.input.entity;


import com.baomidou.mybatisplus.annotation.TableName;

import java.util.List;

@TableName("input_invoice_trans_out_type")
public class InputInvoiceTransOutTypeEntity {
    List<String> invoiceIds;
    String invoiceTransOutType;

    String createPerson;

    String createTime;

    String invoiceId;
    Integer id;

    public String getCreatePerson() {
        return createPerson;
    }

    public void setCreatePerson(String createPerson) {
        this.createPerson = createPerson;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }



    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getInvoiceTransOutType() {
        return invoiceTransOutType;
    }

    public void setInvoiceTransOutType(String invoiceTransOutType) {
        this.invoiceTransOutType = invoiceTransOutType;
    }

    public List<String> getInvoiceIds() {
        return invoiceIds;
    }

    public void setInvoiceIds(List<String> invoiceIds) {
        this.invoiceIds = invoiceIds;
    }
}
