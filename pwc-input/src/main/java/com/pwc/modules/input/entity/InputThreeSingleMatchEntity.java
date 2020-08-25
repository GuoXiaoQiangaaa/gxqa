package com.pwc.modules.input.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 *三单匹配Entity
 *
 * @author cj
 * @version 2018-12-05
 */
@TableName("input_three_single_match")
public class InputThreeSingleMatchEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId
    private Integer id;
    private String fromCompany; // 购方企业名称
    private String fromDutyParagraph;  // 购方税号
    private String toCompany; //销方企业名称
    private String toDutyParagraph; //销方税号
    private String batch; // 批次
    private String batchSerialNumber; // 批次流水号
    private String createTime; // 上传时间
    private String matchStatus; //状态
    private Integer materialDocumentId;
    private String fromCompanyBankAccount; //购方企业银行及账户
    private String toCompanyBankAccount; //销方企业银行及账户

    @TableField(exist = false)
    private String invoiceNumber;

    @TableField(exist = false)
    private String invoiceBatchNumber;

    public String getInvoiceBatchNumber() {
        return invoiceBatchNumber;
    }

    public void setInvoiceBatchNumber(String invoiceBatchNumber) {
        this.invoiceBatchNumber = invoiceBatchNumber;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getFromCompanyBankAccount() {
        return fromCompanyBankAccount;
    }

    public void setFromCompanyBankAccount(String fromCompanyBankAccount) {
        this.fromCompanyBankAccount = fromCompanyBankAccount;
    }

    public String getToCompanyBankAccount() {
        return toCompanyBankAccount;
    }

    public void setToCompanyBankAccount(String toCompanyBankAccount) {
        this.toCompanyBankAccount = toCompanyBankAccount;
    }

    public Integer getMaterialDocumentId() {
        return materialDocumentId;
    }

    public void setMaterialDocumentId(Integer materialDocumentId) {
        this.materialDocumentId = materialDocumentId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFromCompany() {
        return fromCompany;
    }

    public void setFromCompany(String fromCompany) {
        this.fromCompany = fromCompany;
    }

    public String getFromDutyParagraph() {
        return fromDutyParagraph;
    }

    public void setFromDutyParagraph(String fromDutyParagraph) {
        this.fromDutyParagraph = fromDutyParagraph;
    }

    public String getToCompany() {
        return toCompany;
    }

    public void setToCompany(String toCompany) {
        this.toCompany = toCompany;
    }

    public String getToDutyParagraph() {
        return toDutyParagraph;
    }

    public void setToDutyParagraph(String toDutyParagraph) {
        this.toDutyParagraph = toDutyParagraph;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getBatchSerialNumber() {
        return batchSerialNumber;
    }

    public void setBatchSerialNumber(String batchSerialNumber) {
        this.batchSerialNumber = batchSerialNumber;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getMatchStatus() {
        return matchStatus;
    }

    public void setMatchStatus(String matchStatus) {
        this.matchStatus = matchStatus;
    }
}
