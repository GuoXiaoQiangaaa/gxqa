package com.pwc.modules.input.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.pwc.common.utils.excel.annotation.ExcelField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@TableName("input_invoice_batch")
public class InputInvoiceBatchEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId
    private Integer id;

    @ExcelField(title="批次号", align=1, sort=10)
    private String invoiceBatchNumber;

    private String invoiceBatchImage;

    private String invoiceBatchFrom;

    @TableField(exist = false)
    private String invoiceStatus;

    private String expressNumber;
    @ExcelField(title="上传日期", align=1, sort=70)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private Integer companyId;

    private Date updateTime;
    @ExcelField(title="上传人", align=1, sort=20)
    private String createBy;

    private String entryMsg; // 入账返回信息

    private String checkWhiteBlackStatus; // 是否通过红黑名单校验 0，未通过 1，通过


    @ExcelField(title="会计凭证编号", align=1, sort=90)
    private String belnr;
    private String uploadAccount;
    @TableField(exist = false)
    @ExcelField(title="购方企业名称", align=1, sort=30)
    private String invoicePurchaserCompany;
    @TableField(exist = false)
    @ExcelField(title="购方税号", align=1, sort=40)
    private String invoicePurchaserParagraph;
    @TableField(exist = false)
    private String ids;
    @TableField(exist = false)
    @ExcelField(title="销方企业名称", align=1, sort=50)
    private String invoiceSellCompany;
    @TableField(exist = false)
    @ExcelField(title="销方税号", align=1, sort=60)
    private String invoiceSellParagraph;
    @ExcelField(title="批次状态", align=1, sort=120)
    private String invoiceBatchStatus;
    @TableField(exist = false)
    private List<String> invoiceIds;
    @ExcelField(title="入账日期", align=1, sort=100)
    private String entryDate;
    @TableField(exist = false)
    private String sapNumber;
    @TableField(exist = false)
    private String invoiceNumber;
    @TableField(exist = false)
    private String status;
    @TableField(exist = false)
    @ExcelField(title="详细错误信息", align=1, sort=130)
    private String threeErrorDescription;
    @TableField(exist = false)
    private String PMNTTRMS; // 付款条件码
    @TableField(exist = false)
    private String PSTNGDATE; // 过账时间
    @ExcelField(title="发票凭证编号", align=1, sort=80)
    private String entrySuccessCode; // 入账凭证编号
    private String errorInfo;
    @ExcelField(title="最近操作人", align=1, sort=110)
    private String updateBy;

    @TableField(exist = false)
    private List<InputInvoiceEntity> invoiceList;


    @TableField(exist = false)
    private List<InputInvoiceVoucherEntity> invoiceVoucherEntityList;
    @TableField(exist = false)
    private Integer state;
    @TableField(exist = false)
    private List<String> idList;
    @TableField(exist = false)
    private String createDate;



    @TableField(exist = false)
    private String invoiceEntryMessage;

    @Override
    public String toString() {
        return "InvoiceBatchEntity{" +
                "id=" + id +
                ", invoiceBatchNumber='" + invoiceBatchNumber + '\'' +
                ", invoiceBatchImage='" + invoiceBatchImage + '\'' +
                ", invoiceBatchFrom='" + invoiceBatchFrom + '\'' +
                ", invoiceStatus='" + invoiceStatus + '\'' +
                ", expressNumber='" + expressNumber + '\'' +
                ", createTime=" + createTime +
                ", companyId=" + companyId +
                ", updateTime=" + updateTime +
                ", createBy='" + createBy + '\'' +
                ", entryMsg='" + entryMsg + '\'' +
                ", checkWhiteBlackStatus='" + checkWhiteBlackStatus + '\'' +
                ", belnr='" + belnr + '\'' +
                ", uploadAccount='" + uploadAccount + '\'' +
                ", invoicePurchaserCompany='" + invoicePurchaserCompany + '\'' +
                ", invoicePurchaserParagraph='" + invoicePurchaserParagraph + '\'' +
                ", invoiceSellCompany='" + invoiceSellCompany + '\'' +
                ", invoiceSellParagraph='" + invoiceSellParagraph + '\'' +
                ", invoiceBatchStatus='" + invoiceBatchStatus + '\'' +
                ", invoiceIds=" + invoiceIds +
                ", entryDate='" + entryDate + '\'' +
                ", sapNumber='" + sapNumber + '\'' +
                ", invoiceNumber='" + invoiceNumber + '\'' +
                ", status='" + status + '\'' +
                ", threeErrorDescription='" + threeErrorDescription + '\'' +
                ", PMNTTRMS='" + PMNTTRMS + '\'' +
                ", PSTNGDATE='" + PSTNGDATE + '\'' +
                ", entrySuccessCode='" + entrySuccessCode + '\'' +
                ", errorInfo='" + errorInfo + '\'' +
                ", invoiceVoucherEntityList=" + invoiceVoucherEntityList +
                ", state=" + state +
                ", idList=" + idList +
                ", createDate='" + createDate + '\'' +
                ", invoiceEntryMessage='" + invoiceEntryMessage + '\'' +
                '}';
    }
}