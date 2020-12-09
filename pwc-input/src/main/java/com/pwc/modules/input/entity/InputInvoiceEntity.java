package com.pwc.modules.input.entity;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pwc.common.utils.excel.annotation.ExcelField;
import lombok.Data;

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
@Data
@TableName("input_invoice")
public class InputInvoiceEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    @TableId
    private Integer id;
    //    @ExcelField(title="发票凭证编号", align=1, sort=170)
    private String entrySuccessCode; // 入账凭证编号
    //    @ExcelField(title="会计凭证编号", align=1, sort=180)
    private String belnr;
    private String invoiceRemarks; // 备注
    private Integer createBy; // 上传人
    @ExcelField(title = "发票代码", align = 1, sort = 10)
    private String invoiceCode;        // 发票代码
    private String invoicePrintedCode;  // 代码小号
    @ExcelField(title = "发票号码", align = 1, sort = 20)
    private String invoiceNumber;        // 发票号
    private String invoicePrintedNumber;  // 号码小号
    @ExcelField(title = "界面金额（含税）", align = 1, sort = 80)
    private BigDecimal invoiceTotalPrice;        // 界面金额（含税）
    @ExcelField(title = "界面金额（不含税）", align = 1, sort = 90)
    private BigDecimal invoiceFreePrice;        // 界面金额（不含税）
    @ExcelField(title = "税额", align = 1, sort = 110)
    private BigDecimal invoiceTaxPrice;        // 税额
    private String invoiceCheckCode;        // 校验码
    @ExcelField(title = "开票日期", align = 1, sort = 150)
    private String invoiceCreateDate;        // 开票日期
    private String invoiceType;        // 发票类型
    @ExcelField(title = "发票类型", align = 1, sort = 120, dictType = "invoiceEntity")
    private String invoiceEntity;        // 发票实体
    private String invoiceFromto;        // 发票来源
    //    @ExcelField(title="上传方式", align=1, sort=,dictType ="invoiceUploadType")
    private String invoiceUploadType;        // 上传方式 上传方式 0 抵账库上传 1：启动扫描仪上传 2： 手工上传电子发票
    private String invoiceImage;        // 票据图
    @TableField(exist = false)
    private String imageName;        // 票据名称
    @ExcelField(title = "发票状态", align = 1, sort = 200)
    private String invoiceStatus;        // 发票状态
    @ExcelField(title = "分组号", align = 1, sort = 30)
    private String invoiceBatchNumber;        // 批次号
    @ExcelField(title = "金税发票状态", align = 1, sort = 140, dictType = "goldenTaxStatus")
    //（0 正常，1 失控，2 作废，3 红冲)
    private String invoiceRecognition;
    //    @ExcelField(title="快递单号", align=1, sort=160)
    private String expressNumber; // 寄件码
    private String invoiceReturn;        // 是否退票
    private String invoiceDelete;        // 是否失效
    @ExcelField(title = "上传日期", align = 1, sort = 160)
    private String invoiceUploadDate;   //上传日期
    //    @ExcelField(title="验真分类", align=1, sort=240)
    private String invoiceVerifyTruth; //验真分类
    //    @ExcelField(title="invoice_transout",align=1, sort=240)
    private String invoiceTransOut; //进项转出标记
    private String invoiceDeductiblePeriod; // 税款所属期
    @TableField(exist = false)
    private String invoiceAuthPeriod;
    private String invoiceDeductibleResult; //认证结果
    private Date authDate; // 认证时间(new)
    private String invoiceAuthDate; //认证日期
    private Boolean verfy; //认证
    @TableField(exist = false)
    private List<String> fileList;
    @TableField(exist = false)
    private String uploadType; // 上传方式 1：启动扫描仪上传 2： 上传电子发票
    @TableField(exist = false)
    private String[] ids3;
    @ExcelField(title = "购方企业名称", align = 1, sort = 40)
    private String invoicePurchaserCompany; //购方企业名称
    @ExcelField(title = "购方税号", align = 1, sort = 50)
    private String invoicePurchaserParagraph;//购方税号
    @ExcelField(title = "销方企业名称", align = 1, sort = 60)
    private String invoiceSellCompany; //销方企业名称
    @ExcelField(title = "销方税号", align = 1, sort = 70)
    private String invoiceSellParagraph; //销方税号
    @TableField(exist = false)
    private String batchNumber;
    @TableField(exist = false)
    private List<String> InvoiceBatchNumberList;
    @TableField(exist = false)
    @ExcelField(title = "上传人", align = 1, sort = 170)
    private String createUserName;
    @TableField(exist = false)
    private String priceStr;
    @TableField(exist = false)
    private String pitch;
    @TableField(exist = false)
    private String pitch2;
    @TableField(exist = false)
    List<InputInvoiceMaterialEntity> InvoiceMaterialEntityList;  //所属发票的明细集合
    @TableField(exist = false)
    private List<String> picUrlList;
    //商品名称
    @TableField(exist = false)
    private String sphSpmc;
    private String invoiceAuthType; // 认证方式 1勾选 2 扫描
    private String invoiceAuthPattern; // 认证模式1抵扣 2退税
    private String invoiceElectronicType;  // 电子发票类型：0：普通,1:通行费
    private String[] companyIds;
    private String invoiceAuthentication;
    private String invoiceExceptionDate;
    private String invoiceExamineStatus;
    /**
     * Report ID
     */
    private String invoiceExpense;
    private String invoiceTransOutType;


    private String applyStatus; // 统计状态 1:已撤消，2:已确认

    private String entryMessage;

    private Integer companyId; // 所属公司Id

    private String repeatBill;//重复发票状态 0：不重复 1：重复

    private String invoiceEntryMessage; // 入账结果

    private String invoicePurchaserBankAccount; //购方银行及账号

    private String invoicePurchaserAddress; //购方地址电话

    private String invoiceSellBankAccount; //销方银行及账号

    private String invoiceSellAddress;  //销方地址电话

    private String invoiceDescription; // 二次验证失败
    @ExcelField(title = "异常原因", align = 1, sort = 190)
    private String invoiceErrorDescription; //验真失败原因

    @TableField(exist = false)
    private List<InputMaterialDocumentEntity> materialDocumentEntityList; //关联物料表
    @TableField(exist = false)
    private String deductiblePeriodBegin;
    @TableField(exist = false)
    private String deductiblePeriodEnd;


    @TableField(exist = false)
    private String invoiceUploadDateArray;
    @TableField(exist = false)
    private String invoiceCreateDateArray;
    @TableField(exist = false)
    private String entryDateArray;
    @TableField(exist = false)
    private String invoiceTotalPriceBegin;
    @TableField(exist = false)
    private String invoiceTotalPriceEnd;
    @TableField(exist = false)
    private String invoiceIds;
    @TableField(exist = false)
    private List<String> imagesList;
    @TableField(exist = false)
    private List<String> imagesNameList;
    @TableField(exist = false)
    private List<String> ids;
    @TableField(exist = false)
    private List<String> ids2;
    private Integer invoiceGroup;
    @TableField(exist = false)
    private List<String> invoices;
    @TableField(exist = false)
    private String idsap;
    @TableField(exist = false)
    private String idma;
    private String invoiceCheckCodeDetails;

    @TableField(exist = false)
    private List<String> invoiceBatchNumbers;
    // @ExcelField(title="订单类型", align=1, sort=210)
    private String invoiceSapType;
    // 转出类型
    @TableField(exist = false)
    private String objectName;
    // 转出状态
    @TableField(exist = false)
    private Integer outFlag;
    // 转出比例
    @TableField(exist = false)
    private BigDecimal outRatio;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ExcelField(title = "更新日期", align = 1, sort = 180)
    private Date updateTime;
    @ExcelField(title = "上传时间", align = 1, sort = 180)
    private Date uploadCreateTime;
    // 公司编码
    private String legalEntity;
    /**
     * 请求id来请求勾选结果
     */
    private String requestId;

    /**
     * 发票序号 YYYY_mm_dd: HH_mm_{{index}}
     */
    private String invoiceSeq;

    /**
     * 入账税额 (第三方回写)
     */


    /**
     * 5AP发票 4 Te发票 3 po 2 红 1 蓝 0 其他
     */
    private int invoiceStyle;
    /**
     * 发票分类(0:NonPo Related; 1:MRKO; 2:DFU; 3:EDI; 4:R&D_外部; 5:IC_R&D; 6:IC_RRB; 7:IC_非R&D; 8:Red-letter VAT; 9:General)
     *
     * @param Related
     */
    @ExcelField(title = "发票分类", align = 1, sort = 120)
    private String invoiceClass;
    /**
     * 修改发票分类原因
     */
    private String classReason;

    @ExcelField(title = "税率", align = 1, sort = 100)
    private String tax;
    @TableField(exist = false)
    /**是否多税率 0 否 1 是 */
    private String manyTax;
    /**
     * po号码
     *
     * @return
     */
    private String poNumber;
    /**
     * 关联上传id
     */
    private Integer uploadId;

    /**
     * 红字通知单编号
     *
     * @return
     */
    private String redNoticeNumber;

    /**
     * 进度
     *
     * @return
     */
    private String progressRate;

    /**
     * 类型 1 蓝票 2 红票
     *
     * @return
     */
    private Integer sourceStyle;
    /**
     * 账票匹配标志
     */
    private String invoiceMatch;
    /**
     * 账票匹配日期
     */
    private String matchDate;
    /**
     * 入账日期
     */
    private String entryDate;
    /**
     * 年月
     */
    private String yearAndMonth;
    /**
     * SAP差异原因
     */
    private String sapReason;
    /**
     * SAP税额
     */
    private String sapTax;
    /**
     * SAP差异额
     */
    private String sapCheckTax;

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
                ", verfy='" + verfy + '\'' +
                ",  invoiceTransOut='" + invoiceTransOut + '\'' +
                ", invoiceAuthType='" + invoiceAuthType + '\'' +
                ", invoiceAuthPattern='" + invoiceAuthPattern + '\'' +
                ", invoiceElectronicType='" + invoiceElectronicType + '\'' +
                ", invoiceDeductiblePeriod=" + invoiceDeductiblePeriod + '\'' +
                ", invoiceDeductibleResult=" + invoiceDeductibleResult + '\'' +
                ", authDate=" + authDate + '\'' +
                ", invoiceAuthDate=" + invoiceAuthDate + '\'' +
                ", deductiblePeriodBegin=" + deductiblePeriodBegin + '\'' +
                ", deductiblePeriodEnd=" + deductiblePeriodEnd + '\'' +
                ", invoiceExpense=" + invoiceExpense + '\'' +

                '}';


    }


}