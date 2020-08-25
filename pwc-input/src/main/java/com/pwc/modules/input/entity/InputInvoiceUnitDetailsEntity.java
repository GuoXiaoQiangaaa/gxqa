package com.pwc.modules.input.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pwc.common.utils.excel.annotation.ExcelField;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@TableName("input_invoice_unit_details")
public class InputInvoiceUnitDetailsEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId
    private Integer id;
    private Integer invoiceUnit;
    @ExcelField(title="数量", align=1, sort=4)
    private BigDecimal detailsVal;
    @ExcelField(title="单位", align=1, sort=5)
    private String detailsName;
    private Integer detailsDelete;
    @TableField(exist = false)
    @ExcelField(title="基本单位", align=1, sort=2)
    private String unitName;
    @TableField(exist = false)
    private List<String> invoiceIds;
    @TableField(exist = false)
    private InputInvoiceUnitEntity invoiceUnitEntity;
    @TableField(exist = false)
    @ExcelField(title="基本单位数量", align=1, sort=1)
    private Integer detailsNumber = 1;
    @TableField(exist = false)
    @ExcelField(title="", align=1, sort=3)
    private String detailsMark = "=";
    @TableField(exist = false)
    @ExcelField(title="状态", align=1, sort=6)
    private String status;
    @TableField(exist = false)
    private List<String> detailsNames;


    public List<String> getDetailsNames() {
        return detailsNames;
    }

    public void setDetailsNames(List<String> detailsNames) {
        this.detailsNames = detailsNames;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public Integer getDetailsDelete() {
        return detailsDelete;
    }

    public void setDetailsDelete(Integer detailsDelete) {
        this.detailsDelete = detailsDelete;
    }

    public Integer getDetailsNumber() {
        return detailsNumber;
    }

    public void setDetailsNumber(Integer detailsNumber) {
        this.detailsNumber = detailsNumber;
    }

    public String getDetailsMark() {
        return detailsMark;
    }

    public void setDetailsMark(String detailsMark) {
        this.detailsMark = detailsMark;
    }

    public InputInvoiceUnitEntity getInvoiceUnitEntity() {
        return invoiceUnitEntity;
    }

    public void setInvoiceUnitEntity(InputInvoiceUnitEntity invoiceUnitEntity) {
        this.invoiceUnitEntity = invoiceUnitEntity;
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

    public Integer getInvoiceUnit() {
        return invoiceUnit;
    }

    public void setInvoiceUnit(Integer invoiceUnit) {
        this.invoiceUnit = invoiceUnit;
    }

    public BigDecimal getDetailsVal() {
        return detailsVal;
    }

    public void setDetailsVal(BigDecimal detailsVal) {
        this.detailsVal = detailsVal;
    }

    public String getDetailsName() {
        return detailsName;
    }

    public void setDetailsName(String detailsName) {
        this.detailsName = detailsName;
    }
}
