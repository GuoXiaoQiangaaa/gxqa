package com.pwc.modules.input.entity.vo;



import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.pwc.common.utils.excel.annotation.ExcelField;
import com.pwc.modules.input.entity.InputInvoiceMaterialEntity;
import com.pwc.modules.input.entity.InputMaterialDocumentEntity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 票据上传Entity
 *
 * @author cj
 * @version 2018-12-05
 */
@TableName("invoice")
public class InputInvoiceAuthEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId
    private Integer id;
    @ExcelField(title="发票代码", align=1, sort=2)
    private String invoiceCode;        // 发票代码
    @ExcelField(title="发票号码", align=1, sort=3)
    private String invoiceNumber;        // 发票号
    private String printedNumber;// 右上角机打发票号码
    @ExcelField(title="票面金额（含税）", align=1, sort=9)
    private BigDecimal invoiceTotalPrice;        // 界面金额（含税）
    @ExcelField(title="票面金额（不含税）", align=1, sort=10)
    private BigDecimal invoiceFreePrice;        // 界面金额（不含税）
    @ExcelField(title="税额", align=1, sort=11)
    private BigDecimal invoiceTaxPrice;        // 税额
    @ExcelField(title="校验码", align=1, sort=12)
    private String invoiceCheckCode;        // 校验码
    @ExcelField(title="开票日期", align=1, sort=13)
    private String invoiceCreateDate;        // 开票日期
    private String invoiceType;        // 发票类型
    private String invoiceEntity;        // 发票实体
    @ExcelField(title="业务类型", align=1, sort=16)
    private String invoiceFromto;        // 业务类型
    @ExcelField(title="上传方式", align=1, sort=21,dictType ="invoiceUploadType")
    private String invoiceUploadType;        // 上传方式
    private String invoiceImage;        // 票据图
    @ExcelField(title="发票状态", align=1, sort=17)
    private String invoiceStatus;        // 发票状态
    @TableField(exist = false)
    private String invoiceStatus2;  // 发票状态（excel）
    private String invoiceBatchNumber;        // 批次号
    private String invoiceRecognition;        // 是否已经识别
    private String invoiceReturn;        // 是否退票
    private String invoiceDelete;        // 是否失效
    @ExcelField(title="上传日期", align=1, sort=14)
    private String invoiceUploadDate;   //上传日期
    private String invoiceVerifyTruth; //验真分类
    @TableField(exist = false)
    private String invoiceVerifyTruth2;
    @TableField(exist = false)
    private List<String> fileList;
    @ExcelField(title="购方企业名称", align=1, sort=5)
    private String invoicePurchaserCompany; //购方企业名称
    @ExcelField(title="购方税号", align=1, sort=6)
    private String invoicePurchaserParagraph;//购方税号
    @ExcelField(title="销方企业名称", align=1, sort=7)
    private String invoiceSellCompany; //销方企业名称
    @ExcelField(title="销方税号", align=1, sort=8)
    private String invoiceSellParagraph; //销方税号
    @ExcelField(title="条码号", align=1, sort=1)
    private String invoiceBarCode; // 条码号
    private String invoiceSerialNumber;  // 序列号
    private String invoiceAuthentication; // 认证状态
    private String invoiceExamineStatus; // 查验状态
    private Date invoiceErrorDate; // 验真失败时间
    private String invoiceAuthType; // 认证方式 1勾选 2 扫描
    private String invoiceAuthPattern; // 认证模式1抵扣 2退税
    private String invoiceNsrsbh;  // 纳税人识别号
    private Integer companyId; // 所属公司Id
    private String invoiceAuthPeriod; // 税款所属期
    @ExcelField(title="认证日期", align=1, sort=18)
    private String invoiceAuthDate; //认证日期
    @ExcelField(title="认证结果", align=1, sort=19)
    private String invoiceDeductibleResult; //认证结果
    @TableField(exist = false)
    private String[] companyIds;
    private Date authDate; // 认证时间(new)
    @ExcelField(title="税款所属期", align=1, sort=23)
    private String invoiceDeductiblePeriod; // 税款所属期
    @ExcelField(title="退税认证", align=1, sort=20)
    private String isExportTaxRebate;
    @JsonFormat(pattern="yyyy-MM-dd hh:mm:ss")
    public Date getAuthDate() {
        return authDate;
    }

    @ExcelField(title="确认状态", align=1, sort=22)
    private String applyStatus;

    public String getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(String applyStatus) {
        this.applyStatus = applyStatus;
    }

    public String getInvoiceDeductiblePeriod() {
        return invoiceDeductiblePeriod;
    }


    public String getIsExportTaxRebate() {
        return isExportTaxRebate;
    }

    public void setIsExportTaxRebate(String isExportTaxRebate) {
        this.isExportTaxRebate = isExportTaxRebate;
    }

    public void setInvoiceDeductiblePeriod(String invoiceDeductiblePeriod) {
        this.invoiceDeductiblePeriod = invoiceDeductiblePeriod;
    }

    public void setAuthDate(Date authDate) {
        this.authDate = authDate;
    }

    private Integer invoiceCheckCount;  // 验真次数

    @TableField(exist = false)
    private String printedCode;// 右上角机打发票代码

    @TableField(exist = false)
    private List<InputInvoiceMaterialEntity> invoiceMaterialEntityList;//发票明细list

    @TableField(exist = false)
    private Integer type;

    @TableField(exist = false)
    private List<String> invoiceImageList;

    public List<String> getInvoiceImageList() {
        return invoiceImageList;
    }

    public void setInvoiceImageList(List<String> invoiceImageList) {
        this.invoiceImageList = invoiceImageList;
    }

    public String getInvoiceDeductibleResult() {
        return invoiceDeductibleResult;
    }

    public void setInvoiceDeductibleResult(String invoiceDeductibleResult) {
        this.invoiceDeductibleResult = invoiceDeductibleResult;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public List<InputInvoiceMaterialEntity> getInvoiceMaterialEntityList() {
        return invoiceMaterialEntityList;
    }

    public void setInvoiceMaterialEntityList(List<InputInvoiceMaterialEntity> invoiceMaterialEntityList) {
        this.invoiceMaterialEntityList = invoiceMaterialEntityList;
    }

    public String getInvoiceAuthPeriod() {
        return invoiceAuthPeriod;
    }

    public void setInvoiceAuthPeriod(String invoiceAuthPeriod) {
        this.invoiceAuthPeriod = invoiceAuthPeriod;
    }

    public String getPrintedNumber() {
        return printedNumber;
    }

    public void setPrintedNumber(String printedNumber) {
        this.printedNumber = printedNumber;
    }

    public String getPrintedCode() {
        return printedCode;
    }

    public void setPrintedCode(String printedCode) {
        this.printedCode = printedCode;
    }

    public String getInvoiceAuthDate() {
        return invoiceAuthDate;
    }

    public void setInvoiceAuthDate(String invoiceAuthDate) {
        this.invoiceAuthDate = invoiceAuthDate;
    }

    public String getInvoiceAuthPattern() {
        return invoiceAuthPattern;
    }

    public void setInvoiceAuthPattern(String invoiceAuthPattern) {
        this.invoiceAuthPattern = invoiceAuthPattern;
    }

    public String getInvoiceVerifyTruth2() {
        return invoiceVerifyTruth2;
    }

    public void setInvoiceVerifyTruth2(String invoiceVerifyTruth2) {
        this.invoiceVerifyTruth2 = invoiceVerifyTruth2;
    }

    public String getInvoiceStatus2() {
        return invoiceStatus2;
    }

    public void setInvoiceStatus2(String invoiceStatus2) {
        this.invoiceStatus2 = invoiceStatus2;
    }

    public String[] getCompanyIds() {
        return companyIds;
    }

    public void setCompanyIds(String[] companyIds) {
        this.companyIds = companyIds;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getInvoiceNsrsbh() {
        return invoiceNsrsbh;
    }

    public void setInvoiceNsrsbh(String invoiceNsrsbh) {
        this.invoiceNsrsbh = invoiceNsrsbh;
    }

    public String getInvoiceAuthType() {
        return invoiceAuthType;
    }

    public void setInvoiceAuthType(String invoiceAuthType) {
        this.invoiceAuthType = invoiceAuthType;
    }

    public String getInvoiceExamineStatus() {
        return invoiceExamineStatus;
    }

    public void setInvoiceExamineStatus(String invoiceExamineStatus) {
        this.invoiceExamineStatus = invoiceExamineStatus;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getInvoiceErrorDate() {
        return invoiceErrorDate;
    }

    public void setInvoiceErrorDate(Date invoiceErrorDate) {
        this.invoiceErrorDate = invoiceErrorDate;
    }

    public String getInvoiceAuthentication() {
        return invoiceAuthentication;
    }

    public void setInvoiceAuthentication(String invoiceAuthentication) {
        this.invoiceAuthentication = invoiceAuthentication;
    }

    public String getInvoiceSerialNumber() {
        return invoiceSerialNumber;
    }

    public void setInvoiceSerialNumber(String invoiceSerialNumber) {
        this.invoiceSerialNumber = invoiceSerialNumber;
    }

    public String getInvoiceBarCode() {
        return invoiceBarCode;
    }

    public void setInvoiceBarCode(String invoiceBarCode) {
        this.invoiceBarCode = invoiceBarCode;
    }

    private String invoicePurchaserBankAccount; //购方银行及账号

    private String invoicePurchaserAddress; //购方地址电话

    private String invoiceSellBankAccount; //销方银行及账号

    private String invoiceSellAddress;  //销方地址电话

    private String invoiceDescription; //发票验真备注

    private Date invoiceExceptionDate; // 异常发票时间

    private String invoiceErrorDescription; //验真失败原因
    private String invoiceDeleteReason;     //失效原因

    private Integer invoiceFoundCount; // 查无此票时的验真次数
    private Date invoiceNofoundTime;

    public Date getInvoiceNofoundTime() {
        return invoiceNofoundTime;
    }

    public void setInvoiceNofoundTime(Date invoiceNofoundTime) {
        this.invoiceNofoundTime = invoiceNofoundTime;
    }

    public Integer getInvoiceFoundCount() {
        return invoiceFoundCount;
    }

    public void setInvoiceFoundCount(Integer invoiceFoundCount) {
        this.invoiceFoundCount = invoiceFoundCount;
    }

    public String getInvoiceDeleteReason() {
        return invoiceDeleteReason;
    }

    public void setInvoiceDeleteReason(String invoiceDeleteReason) {
        this.invoiceDeleteReason = invoiceDeleteReason;
    }

    @TableField(exist = false)
    private List<InputMaterialDocumentEntity> materialDocumentEntityList; //关联物料表


    @TableField(exist = false)
    private String invoiceUploadDateArray;
    @TableField(exist = false)
    private String invoiceCreateDateArray;
    @TableField(exist = false)
    private String invoiceTotalPriceBegin;
    @TableField(exist = false)
    private String invoiceTotalPriceEnd;
    @TableField(exist = false)
    private String invoiceIds;

    public List<InputMaterialDocumentEntity> getMaterialDocumentEntityList() {
        return materialDocumentEntityList;
    }

    public void setMaterialDocumentEntityList(List<InputMaterialDocumentEntity> materialDocumentEntityList) {
        this.materialDocumentEntityList = materialDocumentEntityList;
    }

    public String getInvoiceErrorDescription() {
        return invoiceErrorDescription;
    }

    public void setInvoiceErrorDescription(String invoiceErrorDescription) {
        this.invoiceErrorDescription = invoiceErrorDescription;
    }

    public Integer getInvoiceCheckCount() {
        return invoiceCheckCount;
    }

    public void setInvoiceCheckCount(Integer invoiceCheckCount) {
        this.invoiceCheckCount = invoiceCheckCount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInvoiceCode() {
        return invoiceCode;
    }

    public void setInvoiceCode(String invoiceCode) {
        this.invoiceCode = invoiceCode;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public BigDecimal getInvoiceTotalPrice() {
        return invoiceTotalPrice;
    }

    public void setInvoiceTotalPrice(BigDecimal invoiceTotalPrice) {
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

    public String getInvoiceCheckCode() {
        return invoiceCheckCode;
    }

    public void setInvoiceCheckCode(String invoiceCheckCode) {
        this.invoiceCheckCode = invoiceCheckCode;
    }

    public String getInvoiceCreateDate() {
        return invoiceCreateDate;
    }

    public void setInvoiceCreateDate(String invoiceCreateDate) {
        this.invoiceCreateDate = invoiceCreateDate;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getInvoiceEntity() {
        return invoiceEntity;
    }

    public void setInvoiceEntity(String invoiceEntity) {
        this.invoiceEntity = invoiceEntity;
    }

    public String getInvoiceFromto() {
        return invoiceFromto;
    }

    public void setInvoiceFromto(String invoiceFromto) {
        this.invoiceFromto = invoiceFromto;
    }

    public String getInvoiceUploadType() {
        return invoiceUploadType;
    }

    public Date getInvoiceExceptionDate() {
        return invoiceExceptionDate;
    }

    public void setInvoiceExceptionDate(Date invoiceExceptionDate) {
        this.invoiceExceptionDate = invoiceExceptionDate;
    }

    public void setInvoiceUploadType(String invoiceUploadType) {
        this.invoiceUploadType = invoiceUploadType;
    }

    public String getInvoiceImage() {
        return invoiceImage;
    }

    public void setInvoiceImage(String invoiceImage) {
        this.invoiceImage = invoiceImage;
    }

    public String getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(String invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public String getInvoiceBatchNumber() {
        return invoiceBatchNumber;
    }

    public void setInvoiceBatchNumber(String invoiceBatchNumber) {
        this.invoiceBatchNumber = invoiceBatchNumber;
    }

    public String getInvoiceRecognition() {
        return invoiceRecognition;
    }

    public void setInvoiceRecognition(String invoiceRecognition) {
        this.invoiceRecognition = invoiceRecognition;
    }

    public String getInvoiceReturn() {
        return invoiceReturn;
    }

    public void setInvoiceReturn(String invoiceReturn) {
        this.invoiceReturn = invoiceReturn;
    }

    public String getInvoiceDelete() {
        return invoiceDelete;
    }

    public void setInvoiceDelete(String invoiceDelete) {
        this.invoiceDelete = invoiceDelete;
    }

    public String getInvoiceUploadDate() {
        return invoiceUploadDate;
    }

    public void setInvoiceUploadDate(String invoiceUploadDate) {
        this.invoiceUploadDate = invoiceUploadDate;
    }

    public String getInvoiceVerifyTruth() {
        return invoiceVerifyTruth;
    }

    public void setInvoiceVerifyTruth(String invoiceVerifyTruth) {
        this.invoiceVerifyTruth = invoiceVerifyTruth;
    }

    public List<String> getFileList() {
        return fileList;
    }

    public void setFileList(List<String> fileList) {
        this.fileList = fileList;
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

    public String getInvoiceSellParagraph() {
        return invoiceSellParagraph;
    }

    public void setInvoiceSellParagraph(String invoiceSellParagraph) {
        this.invoiceSellParagraph = invoiceSellParagraph;
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

    public String getInvoiceIds() {
        return invoiceIds;
    }

    public void setInvoiceIds(String invoiceIds) {
        this.invoiceIds = invoiceIds;
    }

    public String getInvoiceDescription() {
        return invoiceDescription;
    }

    public void setInvoiceDescription(String invoiceDescription) {
        this.invoiceDescription = invoiceDescription;
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

    @Override
    public String toString() {
        return "InvoiceEntity{" +
                "id=" + id +
                ", invoiceCode='" + invoiceCode + '\'' +
                ", invoiceNumber='" + invoiceNumber + '\'' +
                ", invoiceTotalPrice='" + invoiceTotalPrice + '\'' +
                ", invoiceFreePrice='" + invoiceFreePrice + '\'' +
                ", invoiceTaxPrice='" + invoiceTaxPrice + '\'' +
                ", invoiceCheckCode='" + invoiceCheckCode + '\'' +
                ", invoiceCreateDate='" + invoiceCreateDate + '\'' +
                ", invoiceType='" + invoiceType + '\'' +
                ", invoiceEntity='" + invoiceEntity + '\'' +
                ", invoiceFromto='" + invoiceFromto + '\'' +
                ", invoiceUploadType='" + invoiceUploadType + '\'' +
                ", invoiceImage='" + invoiceImage + '\'' +
                ", invoiceStatus='" + invoiceStatus + '\'' +
                ", invoiceBatchNumber=" + invoiceBatchNumber +
                ", invoiceRecognition=" + invoiceRecognition +
                ", invoiceReturn='" + invoiceReturn + '\'' +
                ", invoiceDelete='" + invoiceDelete + '\'' +
                ", invoiceUploadDate='" + invoiceUploadDate + '\'' +
                ", invoiceVerifyTruth='" + invoiceVerifyTruth + '\'' +
                ", fileList=" + fileList +
                ", invoicePurchaserCompany='" + invoicePurchaserCompany + '\'' +
                ", invoicePurchaserParagraph='" + invoicePurchaserParagraph + '\'' +
                ", invoiceSellCompany='" + invoiceSellCompany + '\'' +
                ", invoiceSellParagraph='" + invoiceSellParagraph + '\'' +
                ", invoicePurchaserBankAccount='" + invoicePurchaserBankAccount + '\'' +
                ", invoiceSellBankAccount='" + invoiceSellBankAccount + '\'' +
                ", materialDocumentEntityList=" + materialDocumentEntityList +
                ", invoiceUploadDateArray='" + invoiceUploadDateArray + '\'' +
                ", invoiceCreateDateArray='" + invoiceCreateDateArray + '\'' +
                ", invoiceTotalPriceBegin='" + invoiceTotalPriceBegin + '\'' +
                ", invoiceTotalPriceEnd='" + invoiceTotalPriceEnd + '\'' +
                ", invoiceIds='" + invoiceIds + '\'' +
                '}';
    }
}
