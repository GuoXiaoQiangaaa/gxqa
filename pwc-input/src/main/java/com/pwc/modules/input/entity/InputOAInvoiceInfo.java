package com.pwc.modules.input.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pwc.common.utils.excel.annotation.ExcelField;

import java.math.BigDecimal;
import java.util.Arrays;

@TableName("input_oa_invoice_info")
public class InputOAInvoiceInfo {

    private Integer id;

    @ExcelField(title="报销单号", align=1, sort=1)
    private String barCode;

    private String requestId;

    @ExcelField(title="发票来源", align=1, sort=7)
    private String invoiceUploadType;
    @ExcelField(title="审核备注", align=1, sort=20)
    private String invoiceRemark;

    private String InvoiceUpdatePerson;

    private String invoiceUpdateDate;

    @ExcelField(title="发票号码", align=1, sort=2)
    private String invoiceNumber;

    private String invoicePrintedCode;

    private String invoicePrintedNumber;

    @ExcelField(title="发票代码", align=1, sort=3)
    private String invoiceCode;

    private String invoiceImage;

    @ExcelField(title="开票日期", align=1, sort=17)
    private String invoiceCreateDate;

    @ExcelField(title="校验码", align=1, sort=9)
    private String invoiceCheckCode;

    @ExcelField(title="不含税金额（不含税）", align=1, sort=14)
    private BigDecimal invoiceFreePrice;

    @ExcelField(title="含税金额（含税）", align=1, sort=15)
    private BigDecimal invoiceTotalPrice;

    @ExcelField(title="税额", align=1, sort=16)
    private BigDecimal invoiceTaxPrice;

    private String docId;

    @ExcelField(title="购方企业名称", align=1, sort=10)
    private String invoicePurchaserCompany;

    @ExcelField(title="购方税号", align=1, sort=11)
    private String invoicePurchaserParagraph;

    @ExcelField(title="销方企业名称", align=1, sort=12)
    private String invoiceSellCompany;

    @ExcelField(title="销方税号", align=1, sort=13)
    private String invoiceSellParagraph;

    @ExcelField(title="备注", align=1, sort=14)
    private String invoiceDescription;

    @ExcelField(title="发票状态", align=1, sort=4)
    private String invoiceStatus;

//    private Integer messageId;

    @ExcelField(title="入账时间", align=1, sort=5)
    private String postDate; //入账时间

    @ExcelField(title="上传时间", align=1, sort=6)
    private String invoiceUploadDate;

    @ExcelField(title="发票类型", align=1, sort=8)
    private String invoiceType;

    private String invoicePurchaserBankAccount;

    private String invoiceSellBankAccount;

    private String invoicePurchaserAddress;

    private String invoiceSellAddress;

    @ExcelField(title="详情", align=1, sort=18)
    private String invoiceErrorInfo;

    private String invoiceDelete;

    private String invoiceReturn;
    private String worning;   //购方企业不一致状态  0:不一致  1：一致


    public String getWorning() {
        return worning;
    }

    public void setWorning(String worning) {
        this.worning = worning;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getBarCode() {
        return barCode;
    }

    public String getInvoiceRemark() {
        return invoiceRemark;
    }

    public void setInvoiceRemark(String invoiceRemark) {
        this.invoiceRemark = invoiceRemark;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getInvoiceDelete() {
        return invoiceDelete;
    }

    public String getInvoicePrintedCode() {
        return invoicePrintedCode;
    }

    public void setInvoicePrintedCode(String invoicePrintedCode) {
        this.invoicePrintedCode = invoicePrintedCode;
    }

    public String getInvoicePrintedNumber() {
        return invoicePrintedNumber;
    }

    public void setInvoicePrintedNumber(String invoicePrintedNumber) {
        this.invoicePrintedNumber = invoicePrintedNumber;
    }

    public void setInvoiceDelete(String invoiceDelete) {
        this.invoiceDelete = invoiceDelete;
    }

    public String getInvoiceReturn() {
        return invoiceReturn;
    }

    public void setInvoiceReturn(String invoiceReturn) {
        this.invoiceReturn = invoiceReturn;
    }

    @TableField(exist = false)
    private String[] ids;

    @TableField(exist = false)
    private String idsStr;

    @TableField(exist = false)
    private String invoiceTotalPriceBegin;
    @TableField(exist = false)
    private String invoiceTotalPriceEnd;
    @TableField(exist = false)
    private String invoiceUploadDateArray;
    @TableField(exist = false)
    private String invoiceCreateDateArray;

    public String getInvoiceUploadDateArray() {
        return invoiceUploadDateArray;
    }

    public void setInvoiceUploadDateArray(String invoiceUploadDateArray) {
        this.invoiceUploadDateArray = invoiceUploadDateArray;
    }

    public String getInvoiceCreateDateArray() {
        return invoiceCreateDateArray;
    }

    public void setInvoiceCreateDateArray(String invoiceCreateDateArray) {
        this.invoiceCreateDateArray = invoiceCreateDateArray;
    }

    public String getInvoiceTotalPriceBegin() {
        return invoiceTotalPriceBegin;
    }

    public void setInvoiceTotalPriceBegin(String invoiceTotalPriceBegin) {
        this.invoiceTotalPriceBegin = invoiceTotalPriceBegin;
    }

    public String getInvoiceTotalPriceEnd() {
        return invoiceTotalPriceEnd;
    }

    public void setInvoiceTotalPriceEnd(String invoiceTotalPriceEnd) {
        this.invoiceTotalPriceEnd = invoiceTotalPriceEnd;
    }

    public String getIdsStr() {
        return idsStr;
    }

    public String getInvoiceImage() {
        return invoiceImage;
    }

    public void setInvoiceImage(String invoiceImage) {
        this.invoiceImage = invoiceImage;
    }

    public void setIdsStr(String idsStr) {
        this.idsStr = idsStr;
    }

    public String getInvoiceErrorInfo() {
        return invoiceErrorInfo;
    }

    public void setInvoiceErrorInfo(String invoiceErrorInfo) {
        this.invoiceErrorInfo = invoiceErrorInfo;
    }

    public String[] getIds() {
        return ids;
    }

    public void setIds(String[] ids) {
        this.ids = ids;
    }

    public String getInvoicePurchaserBankAccount() {
        return invoicePurchaserBankAccount;
    }

    public void setInvoicePurchaserBankAccount(String invoicePurchaserBankAccount) {
        this.invoicePurchaserBankAccount = invoicePurchaserBankAccount;
    }

    public String getInvoiceSellBankAccount() {
        return invoiceSellBankAccount;
    }

    public void setInvoiceSellBankAccount(String invoiceSellBankAccount) {
        this.invoiceSellBankAccount = invoiceSellBankAccount;
    }

    public String getInvoicePurchaserAddress() {
        return invoicePurchaserAddress;
    }

    public void setInvoicePurchaserAddress(String invoicePurchaserAddress) {
        this.invoicePurchaserAddress = invoicePurchaserAddress;
    }

    public String getInvoiceSellAddress() {
        return invoiceSellAddress;
    }

    public void setInvoiceSellAddress(String invoiceSellAddress) {
        this.invoiceSellAddress = invoiceSellAddress;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInvoiceUploadType() {
        return invoiceUploadType;
    }

    public void setInvoiceUploadType(String invoiceUploadType) {
        this.invoiceUploadType = invoiceUploadType;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getInvoiceCode() {
        return invoiceCode;
    }

    public void setInvoiceCode(String invoiceCode) {
        this.invoiceCode = invoiceCode;
    }

    public String getInvoiceCreateDate() {
        return invoiceCreateDate;
    }

//    public Integer getMessageId() {
//        return messageId;
//    }
//
//    public void setMessageId(Integer messageId) {
//        this.messageId = messageId;
//    }


    public String getInvoiceUpdatePerson() {
        return InvoiceUpdatePerson;
    }

    public void setInvoiceUpdatePerson(String invoiceUpdatePerson) {
        InvoiceUpdatePerson = invoiceUpdatePerson;
    }

    public String getInvoiceUpdateDate() {
        return invoiceUpdateDate;
    }

    public void setInvoiceUpdateDate(String invoiceUpdateDate) {
        this.invoiceUpdateDate = invoiceUpdateDate;
    }

    public String getInvoiceUploadDate() {
        return invoiceUploadDate;
    }

    public void setInvoiceUploadDate(String invoiceUploadDate) {
        this.invoiceUploadDate = invoiceUploadDate;
    }

    public void setInvoiceCreateDate(String invoiceCreateDate) {
        this.invoiceCreateDate = invoiceCreateDate;
    }

    public String getInvoiceCheckCode() {
        return invoiceCheckCode;
    }

    public void setInvoiceCheckCode(String invoiceCheckCode) {
        this.invoiceCheckCode = invoiceCheckCode;
    }

    public BigDecimal getInvocieTotalPrice() {
        return invoiceTotalPrice;
    }

    public void setInvocieTotalPrice(BigDecimal invoiceTotalPrice) {
        this.invoiceTotalPrice = invoiceTotalPrice;
    }

    public BigDecimal getInvoiceFreePrice() {
        return invoiceFreePrice;
    }

    public void setInvoiceFreePrice(BigDecimal invoiceFreePrice) {
        this.invoiceFreePrice = invoiceFreePrice;
    }

    public BigDecimal getInvoiceTaxPrice() {
        return invoiceTaxPrice;
    }

    public void setInvoiceTaxPrice(BigDecimal invoiceTaxPrice) {
        this.invoiceTaxPrice = invoiceTaxPrice;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getInvoicePurchaserCompany() {
        return invoicePurchaserCompany;
    }

    public void setInvoicePurchaserCompany(String invoicePurchaserCompany) {
        this.invoicePurchaserCompany = invoicePurchaserCompany;
    }

    public String getInvoicePurchaserParagraph() {
        return invoicePurchaserParagraph;
    }

    public void setInvoicePurchaserParagraph(String invoicePurchaserParagraph) {
        this.invoicePurchaserParagraph = invoicePurchaserParagraph;
    }

    public String getInvoiceSellCompany() {
        return invoiceSellCompany;
    }

    public void setInvoiceSellCompany(String invoiceSellCompany) {
        this.invoiceSellCompany = invoiceSellCompany;
    }

    public BigDecimal getInvoiceTotalPrice() {
        return invoiceTotalPrice;
    }

    public void setInvoiceTotalPrice(BigDecimal invoiceTotalPrice) {
        this.invoiceTotalPrice = invoiceTotalPrice;
    }

    public String getInvoiceSellParagraph() {
        return invoiceSellParagraph;
    }

    public void setInvoiceSellParagraph(String invoiceSellParagraph) {
        this.invoiceSellParagraph = invoiceSellParagraph;
    }

    public String getInvoiceDescription() {
        return invoiceDescription;
    }

    public void setInvoiceDescription(String invoiceDescription) {
        this.invoiceDescription = invoiceDescription;
    }

    public String getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(String invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @Override
    public String toString() {
        return "OAInvoiceInfo{" +
                "id=" + id +
                ", barCode='" + barCode + '\'' +
                ", requestId='" + requestId + '\'' +
                ", invoiceUploadType='" + invoiceUploadType + '\'' +
                ", invoiceRemark='" + invoiceRemark + '\'' +
                ", InvoiceUpdatePerson='" + InvoiceUpdatePerson + '\'' +
                ", invoiceUpdateDate='" + invoiceUpdateDate + '\'' +
                ", invoiceNumber='" + invoiceNumber + '\'' +
                ", invoicePrintedCode='" + invoicePrintedCode + '\'' +
                ", invoicePrintedNumber='" + invoicePrintedNumber + '\'' +
                ", invoiceCode='" + invoiceCode + '\'' +
                ", invoiceImage='" + invoiceImage + '\'' +
                ", invoiceCreateDate='" + invoiceCreateDate + '\'' +
                ", invoiceCheckCode='" + invoiceCheckCode + '\'' +
                ", invoiceFreePrice=" + invoiceFreePrice +
                ", invoiceTotalPrice=" + invoiceTotalPrice +
                ", invoiceTaxPrice=" + invoiceTaxPrice +
                ", docId='" + docId + '\'' +
                ", invoicePurchaserCompany='" + invoicePurchaserCompany + '\'' +
                ", invoicePurchaserParagraph='" + invoicePurchaserParagraph + '\'' +
                ", invoiceSellCompany='" + invoiceSellCompany + '\'' +
                ", invoiceSellParagraph='" + invoiceSellParagraph + '\'' +
                ", invoiceDescription='" + invoiceDescription + '\'' +
                ", invoiceStatus='" + invoiceStatus + '\'' +
                ", postDate='" + postDate + '\'' +
                ", invoiceUploadDate='" + invoiceUploadDate + '\'' +
                ", invoiceType='" + invoiceType + '\'' +
                ", invoicePurchaserBankAccount='" + invoicePurchaserBankAccount + '\'' +
                ", invoiceSellBankAccount='" + invoiceSellBankAccount + '\'' +
                ", invoicePurchaserAddress='" + invoicePurchaserAddress + '\'' +
                ", invoiceSellAddress='" + invoiceSellAddress + '\'' +
                ", invoiceErrorInfo='" + invoiceErrorInfo + '\'' +
                ", invoiceDelete='" + invoiceDelete + '\'' +
                ", invoiceReturn='" + invoiceReturn + '\'' +
                ", ids=" + Arrays.toString(ids) +
                ", idsStr='" + idsStr + '\'' +
                ", invoiceTotalPriceBegin='" + invoiceTotalPriceBegin + '\'' +
                ", invoiceTotalPriceEnd='" + invoiceTotalPriceEnd + '\'' +
                ", invoiceUploadDateArray='" + invoiceUploadDateArray + '\'' +
                ", invoiceCreateDateArray='" + invoiceCreateDateArray + '\'' +
                '}';
    }
}
