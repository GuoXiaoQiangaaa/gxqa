package com.pwc.modules.input.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pwc.common.utils.excel.annotation.ExcelField;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@TableName("input_invoice_material")
public class InputInvoiceMaterialEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    private Integer id;
    @TableField(exist = false)
    private String[] pitch;
    private Integer invoiceId;   //发票号
    @ExcelField(title="序号", align=1, sort=10)
    private String sphXh;          //序号
    @ExcelField(title="货物描述", align=1, sort=15)
    private String sphSpmc;         //商品名称
    @ExcelField(title="规格型号", align=1, sort=20)
    private String sphGgxh;         //规格序号
    @ExcelField(title="单位", align=1, sort=25)
    private String sphJldw;         //计量单位
    @ExcelField(title="数量", align=1, sort=40)
    private BigDecimal sphSl;          //数量
    @ExcelField(title="不含税单价", align=1, sort=30)
    private BigDecimal sphDj;            //不含税单价
    @ExcelField(title="不含税金额", align=1, sort=60)
    private BigDecimal sphJe;            //金额
    @ExcelField(title="税率", align=1, sort=50)
    private String sphSlv;          //税率
    @ExcelField(title="税额", align=1, sort=55)
    private BigDecimal sphSe;
    @TableField(exist = false)
    @ExcelField(title="含税金额", align=1, sort=70)
    private BigDecimal materialTotalPrice; //物料含税金额
    @TableField(exist = false)
    private List<Integer> ids;
    @TableField(exist = false)
    private BigDecimal totalPrice;
    @ExcelField(title="状态", align=1, sort=80)
    private String matchStatus;


    public String getSpecialPolicySign() {
        return specialPolicySign;
    }

    public void setSpecialPolicySign(String specialPolicySign) {
        this.specialPolicySign = specialPolicySign;
    }

    public String getRealTaxRate() {
        return realTaxRate;
    }

    public void setRealTaxRate(String realTaxRate) {
        this.realTaxRate = realTaxRate;
    }

    public String getRealTax() {
        return realTax;
    }

    public void setRealTax(String realTax) {
        this.realTax = realTax;
    }

    /** 特殊政策标识*/
    private String specialPolicySign;
    /** 实际税率*/
    private String realTaxRate;
    /** 实际税额*/
    private String realTax;

    public String getTaxClassyCode() {
        return taxClassyCode;
    }

    public void setTaxClassyCode(String taxClassyCode) {
        this.taxClassyCode = taxClassyCode;
    }

    private String taxClassyCode;




    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BigDecimal getMaterialTotalPrice() {
        return materialTotalPrice;
    }

    public void setMaterialTotalPrice(BigDecimal materialTotalPrice) {
        this.materialTotalPrice = materialTotalPrice;
    }

    public String getMatchStatus() {
        return matchStatus;
    }

    public void setMatchStatus(String matchStatus) {
        this.matchStatus = matchStatus;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public String[] getPitch() {
        return pitch;
    }

    public void setPitch(String[] pitch) {
        this.pitch = pitch;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    @TableField(exist = false)
    private List<String> invoiceIds;
    @TableField(exist = false)
    @ExcelField(title="发票号", align=1, sort=5)
    private String invoiceNumber;
    @TableField(exist = false)
    private String invoiceCode;
    @TableField(exist = false)
    private String invoiceStatus;
    @TableField(exist = false)
    private String invoiceType;
    @TableField(exist = false)
    private String invoiceCreateDate;
    @TableField(exist = false)
    private String invoiceGroup;
    private String status;
    @TableField(exist = false)
    private String batchId;

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getSphXh() {
        return sphXh;
    }

    public void setSphXh(String sphXh) {
        this.sphXh = sphXh;
    }

    public String getSphSpmc() {
        return sphSpmc;
    }

    public void setSphSpmc(String sphSpmc) {
        this.sphSpmc = sphSpmc;
    }

    public String getSphGgxh() {
        return sphGgxh;
    }

    public void setSphGgxh(String sphGgxh) {
        this.sphGgxh = sphGgxh;
    }

    public String getSphJldw() {
        return sphJldw;
    }

    public void setSphJldw(String sphJldw) {
        this.sphJldw = sphJldw;
    }

    public BigDecimal getSphSl() {
        return sphSl;
    }

    public void setSphSl(BigDecimal sphSl) {
        this.sphSl = sphSl;
    }

    public BigDecimal getSphDj() {
        return sphDj;
    }

    public void setSphDj(BigDecimal sphDj) {
        this.sphDj = sphDj;
    }

    public BigDecimal getSphJe() {
        return sphJe;
    }

    public void setSphJe(BigDecimal sphJe) {
        this.sphJe = sphJe;
    }

    public String getSphSlv() {
        return sphSlv;
    }

    public void setSphSlv(String sphSlv) {
        this.sphSlv = sphSlv;
    }

    public BigDecimal getSphSe() {
        return sphSe;
    }

    public void setSphSe(BigDecimal sphSe) {
        this.sphSe = sphSe;
    }

    public List<String> getInvoiceIds() {
        return invoiceIds;
    }

    public void setInvoiceIds(List<String> invoiceIds) {
        this.invoiceIds = invoiceIds;
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

    public String getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(String invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public String getInvoiceCreateDate() {
        return invoiceCreateDate;
    }

    public void setInvoiceCreateDate(String invoiceCreateDate) {
        this.invoiceCreateDate = invoiceCreateDate;
    }

    public String getInvoiceGroup() {
        return invoiceGroup;
    }

    public void setInvoiceGroup(String invoiceGroup) {
        this.invoiceGroup = invoiceGroup;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
