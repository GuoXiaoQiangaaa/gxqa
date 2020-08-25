package com.pwc.modules.input.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pwc.common.utils.excel.annotation.ExcelField;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@TableName("input_invoice_material_sap")
public class InputInvoiceMaterialSapEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId
    private Integer id;
    private String bukrs; //公司代码
    private String lifnr; //供应商编号
    private String name1; //供应商全称
    private String sortl; //供应商简称
    @ExcelField(title="采购订单编号", align=1, sort=30)
    private String ebeln; //SAP采购订单编号
    private String bsart; //SAP采购订单类型
    private String ebelp; //SAP采购订单项目
    @ExcelField(title="采购组", align=1, sort=190)
    private String ekgrp; //采购组
    @ExcelField(title="物料凭证号", align=1, sort=10)
    private String mblnr; //物料凭证编号
    @ExcelField(title="序号", align=1, sort=20)
    private String zeile; //物料凭证项目
    private String werks; //工厂
    @ExcelField(title="物料编码", align=1, sort=50)
    private String matnr; //物料编码
    @ExcelField(title="*物料组*物料描述", align=1, sort=40)
    private String maktx; //物料描述
    @ExcelField(title="单位", align=1, sort=60)
    private String meins; //单位
    private String menge; //订单数量(暂不启用)
    private String menge1; //入库数量(暂不启用)
    private String zzresult; //质检结果
    @ExcelField(title="数量", align=1, sort=80)
    private BigDecimal mengeQm; //合格数量
    private String mengeRe; //退货数量(暂不启用)
    @ExcelField(title="交货日期", align=1, sort=170)
    private String prdat; // 交货日期
    @ExcelField(title="扣款金额", align=1, sort=150)
    private Float zkkje; //扣款金额(暂不启用)
    private String qmdate; // 质检入账日期
    @ExcelField(title="扣款比例", align=1, sort=160)
    private String zzkkbl; //扣款比例(暂不启用)
    private String entryYear;  // 入账年份
    private String mwskz; //税码
    @ExcelField(title="税率", align=1, sort=90)
    private String kbetr; //税率
    @ExcelField(title="税额", align=1, sort=100)
    private BigDecimal wmwst; //税额
    @ExcelField(title="不含税金额", align=1, sort=110)
    private BigDecimal zzkkbhs; //扣款后不含税金额
    @ExcelField(title="含税金额", align=1, sort=120)
    private BigDecimal wrbtr; //扣款后含税金额
    @ExcelField(title="合同编号", align=1, sort=180)
    private String zzcontract; //合同编号
    private String budatMkpf; //仓库入账日期
//    @ExcelField(title="付款条件", align=1, sort=100)
//    private String zterm;  //付款条件
    private String matkl;  //物料组
    @ExcelField(title="付款条件(供应商主数据)", align=1, sort=140)
    private String cmzterm; //付款条件(供应商主数据)
    @ExcelField(title="付款条件(采购信息记录)", align=1, sort=130)
    private String irzterm; //付款条件(采购信息记录)
    private String wgbez;  //物料组明细
    private BigDecimal itemAmount;

    public String getPrdat() {
        return prdat;
    }

    public void setPrdat(String prdat) {
        this.prdat = prdat;
    }

    public String getCmzterm() {
        return cmzterm;
    }

    public void setCmzterm(String cmzterm) {
        this.cmzterm = cmzterm;
    }

    public String getIrzterm() {
        return irzterm;
    }

    public void setIrzterm(String irzterm) {
        this.irzterm = irzterm;
    }

    public String getQmdate() {
        return qmdate;
    }

    public void setQmdate(String qmdate) {
        this.qmdate = qmdate;
    }

    public BigDecimal getItemAmount() {
        return itemAmount;
    }

    public void setItemAmount(BigDecimal itemAmount) {
        this.itemAmount = itemAmount;
    }

    public String getEntryYear() {
        return entryYear;
    }

    public void setEntryYear(String entryYear) {
        this.entryYear = entryYear;
    }

    @TableField(exist = false)
    private List<String> mblnrs;
    @TableField(exist = false)
    private String mblnrArray;
    @TableField(exist = false)
    private List<String> invoiceIds;
    @TableField(exist = false)
    @ExcelField(title="不含税单价", align=1, sort=50)
    private BigDecimal bhsdj; //不含税单价
    @TableField(exist = false)
    private String status;  //是否已匹配
    private String mate;  //是否匹配
    private String batchId;  //批次id
    private String materialIds; //
    @ExcelField(title="返回说明", align=1, sort=200)
    private String description;  //sap返回的原因
    private Date createTime;
    private String rcode;
    private String materialLineId;
    @TableField(exist = false)
    private String isLineId;
    @TableField(exist = false)
    private String[] pitch;
    @ExcelField(title="状态", align=1, sort=210)
    private String matchStatus; //校验记录

    private BigDecimal postQm;

    private BigDecimal postUnitprice;

    public String[] getPitch() {
        return pitch;
    }

    public void setPitch(String[] pitch) {
        this.pitch = pitch;
    }

    public BigDecimal getPostUnitprice() {
        return postUnitprice;
    }

    public void setPostUnitprice(BigDecimal postUnitprice) {
        this.postUnitprice = postUnitprice;
    }

    public BigDecimal getPostQm() {
        return postQm;
    }

    public void setPostQm(BigDecimal postQm) {
        this.postQm = postQm;
    }

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String isAccount;  //是否入账

    private List<Integer> sapIds;

    public List<Integer> getSapIds() {
        return sapIds;
    }

    public void setSapIds(List<Integer> sapIds) {
        this.sapIds = sapIds;
    }

    public String getIsAccount() {
        return isAccount;
    }

    public void setIsAccount(String isAccount) {
        this.isAccount = isAccount;
    }

    public String getWgbez() {
        return wgbez;
    }

    public void setWgbez(String wgbez) {
        this.wgbez = wgbez;
    }

    public String getMatchStatus() {
        return matchStatus;
    }

    public void setMatchStatus(String matchStatus) {
        this.matchStatus = matchStatus;
    }

    public String getIsLineId() {
        return isLineId;
    }

    public void setIsLineId(String isLineId) {
        this.isLineId = isLineId;
    }

    public String getMaterialLineId() {
        return materialLineId;
    }

    public void setMaterialLineId(String materialLineId) {
        this.materialLineId = materialLineId;
    }

    public String getRcode() {
        return rcode;
    }

    public void setRcode(String rcode) {
        this.rcode = rcode;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMate() {
        return mate;
    }

    public void setMate(String mate) {
        this.mate = mate;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getMaterialIds() {
        return materialIds;
    }

    public void setMaterialIds(String materialIds) {
        this.materialIds = materialIds;
    }

    public String getMatkl() {
        return matkl;
    }

    public void setMatkl(String matkl) {
        this.matkl = matkl;
    }

    public String getMblnrArray() {
        return mblnrArray;
    }

    public void setMblnrArray(String mblnrArray) {
        this.mblnrArray = mblnrArray;
    }

//    public String getZterm() {
//        return zterm;
//    }
//
//    public void setZterm(String zterm) {
//        this.zterm = zterm;
//    }

    public List<String> getInvoiceIds() {
        return invoiceIds;
    }

    public void setInvoiceIds(List<String> invoiceIds) {
        this.invoiceIds = invoiceIds;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getBhsdj() {
        return bhsdj;
    }

    public void setBhsdj(BigDecimal bhsdj) {
        this.bhsdj = bhsdj;
    }

    public List<String> getMblnrs() {
        return mblnrs;
    }

    public void setMblnrs(List<String> mblnrs) {
        this.mblnrs = mblnrs;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBukrs() {
        return bukrs;
    }

    public void setBukrs(String bukrs) {
        this.bukrs = bukrs;
    }

    public String getLifnr() {
        return lifnr;
    }

    public void setLifnr(String lifnr) {
        this.lifnr = lifnr;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getSortl() {
        return sortl;
    }

    public void setSortl(String sortl) {
        this.sortl = sortl;
    }

    public String getEbeln() {
        return ebeln;
    }

    public void setEbeln(String ebeln) {
        this.ebeln = ebeln;
    }

    public String getBsart() {
        return bsart;
    }

    public void setBsart(String bsart) {
        this.bsart = bsart;
    }

    public String getEbelp() {
        return ebelp;
    }

    public void setEbelp(String ebelp) {
        this.ebelp = ebelp;
    }

    public String getEkgrp() {
        return ekgrp;
    }

    public void setEkgrp(String ekgrp) {
        this.ekgrp = ekgrp;
    }

    public String getMblnr() {
        return mblnr;
    }

    public void setMblnr(String mblnr) {
        this.mblnr = mblnr;
    }

    public String getZeile() {
        return zeile;
    }

    public void setZeile(String zeile) {
        this.zeile = zeile;
    }

    public String getWerks() {
        return werks;
    }

    public void setWerks(String werks) {
        this.werks = werks;
    }

    public String getMatnr() {
        return matnr;
    }

    public void setMatnr(String matnr) {
        this.matnr = matnr;
    }

    public String getMaktx() {
        return maktx;
    }

    public void setMaktx(String maktx) {
        this.maktx = maktx;
    }

    public String getMeins() {
        return meins;
    }

    public void setMeins(String meins) {
        this.meins = meins;
    }

    public String getMenge() {
        return menge;
    }

    public void setMenge(String menge) {
        this.menge = menge;
    }

    public String getMenge1() {
        return menge1;
    }

    public void setMenge1(String menge1) {
        this.menge1 = menge1;
    }

    public String getZzresult() {
        return zzresult;
    }

    public void setZzresult(String zzresult) {
        this.zzresult = zzresult;
    }

    public BigDecimal getMengeQm() {
        return mengeQm;
    }

    public void setMengeQm(BigDecimal mengeQm) {
        this.mengeQm = mengeQm;
    }

    public String getMengeRe() {
        return mengeRe;
    }

    public void setMengeRe(String mengeRe) {
        this.mengeRe = mengeRe;
    }

    public Float getZkkje() {
        return zkkje;
    }

    public void setZkkje(Float zkkje) {
        this.zkkje = zkkje;
    }

    public String getZzkkbl() {
        return zzkkbl;
    }

    public void setZzkkbl(String zzkkbl) {
        this.zzkkbl = zzkkbl;
    }

    public String getMwskz() {
        return mwskz;
    }

    public void setMwskz(String mwskz) {
        this.mwskz = mwskz;
    }

    public String getKbetr() {
        return kbetr;
    }

    public void setKbetr(String kbetr) {
        this.kbetr = kbetr;
    }

    public BigDecimal getWmwst() {
        return wmwst;
    }

    public void setWmwst(BigDecimal wmwst) {
        this.wmwst = wmwst;
    }

    public BigDecimal getZzkkbhs() {
        return zzkkbhs;
    }

    public void setZzkkbhs(BigDecimal zzkkbhs) {
        this.zzkkbhs = zzkkbhs;
    }

    public BigDecimal getWrbtr() {
        return wrbtr;
    }

    public void setWrbtr(BigDecimal wrbtr) {
        this.wrbtr = wrbtr;
    }

    public String getZzcontract() {
        return zzcontract;
    }

    public void setZzcontract(String zzcontract) {
        this.zzcontract = zzcontract;
    }

    public String getBudatMkpf() {
        return budatMkpf;
    }

    public void setBudatMkpf(String budatMkpf) {
        this.budatMkpf = budatMkpf;
    }
}
