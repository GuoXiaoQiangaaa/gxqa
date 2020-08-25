package com.pwc.modules.input.entity;

import java.math.BigDecimal;
import java.util.List;

public class InputInvoiceVo {

    private Integer materId; // materId

    private Integer sapId; // sapId

    private String zeile; // 序号

    private String mblnr; // sap物料凭证号

    private String sphXh; // mater发票号+序号

    private String maktx; // sap物料货物描述

    private String matkl; // sap物料组

    private String sphGgxh; // mater规格型号

    private String sphJldw; // mater sap单位

    private BigDecimal sphDj; // mater sap不含税单价

    private BigDecimal sphSl; // mater sap数量

    private BigDecimal sphSe; // mater sap税额

    private BigDecimal sphJe; // mater sap不含税金额

    private BigDecimal sphHsje; // mater sap含税金额

    private String matnr;

    private String ebeln; // sap订单编号

    private String sphSlv; // mater sap税率

    private String status; // 状态

    private List<Integer> materialIds;

    private List<Integer> sapIds;

    private String cmzterm; //付款条件(供应商主数据)
    private String irzterm; //付款条件(采购信息记录)

    public String getMatnr() {
        return matnr;
    }

    public void setMatnr(String matnr) {
        this.matnr = matnr;
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

    public String getZeile() {
        return zeile;
    }

    public void setZeile(String zeile) {
        this.zeile = zeile;
    }

    public List<Integer> getMaterialIds() {
        return materialIds;
    }

    public void setMaterialIds(List<Integer> materialIds) {
        this.materialIds = materialIds;
    }

    public List<Integer> getSapIds() {
        return sapIds;
    }

    public void setSapIds(List<Integer> sapIds) {
        this.sapIds = sapIds;
    }

    public Integer getMaterId() {
        return materId;
    }

    public void setMaterId(Integer materId) {
        this.materId = materId;
    }

    public Integer getSapId() {
        return sapId;
    }

    public void setSapId(Integer sapId) {
        this.sapId = sapId;
    }

    public String getMblnr() {
        return mblnr;
    }

    public void setMblnr(String mblnr) {
        this.mblnr = mblnr;
    }

    public String getSphXh() {
        return sphXh;
    }

    public void setSphXh(String sphXh) {
        this.sphXh = sphXh;
    }

    public String getMaktx() {
        return maktx;
    }

    public void setMaktx(String maktx) {
        this.maktx = maktx;
    }

    public String getMatkl() {
        return matkl;
    }

    public void setMatkl(String matkl) {
        this.matkl = matkl;
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

    public BigDecimal getSphDj() {
        return sphDj;
    }

    public void setSphDj(BigDecimal sphDj) {
        this.sphDj = sphDj;
    }

    public BigDecimal getSphSl() {
        return sphSl;
    }

    public void setSphSl(BigDecimal sphSl) {
        this.sphSl = sphSl;
    }

    public BigDecimal getSphSe() {
        return sphSe;
    }

    public void setSphSe(BigDecimal sphSe) {
        this.sphSe = sphSe;
    }

    public BigDecimal getSphJe() {
        return sphJe;
    }

    public void setSphJe(BigDecimal sphJe) {
        this.sphJe = sphJe;
    }

    public BigDecimal getSphHsje() {
        return sphHsje;
    }

    public void setSphHsje(BigDecimal sphHsje) {
        this.sphHsje = sphHsje;
    }

    public String getEbeln() {
        return ebeln;
    }

    public void setEbeln(String ebeln) {
        this.ebeln = ebeln;
    }

    public String getSphSlv() {
        return sphSlv;
    }

    public void setSphSlv(String sphSlv) {
        this.sphSlv = sphSlv;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
