package com.pwc.modules.input.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pwc.common.utils.excel.annotation.ExcelField;

import java.io.Serializable;

@TableName("input_invoice_responsible")
public class InputInvoiceResponsibleEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId
    private Integer id;
    @ExcelField(title="SAP物料组描述", align=1, sort=1)
    private String invoiceResponsible;
    @ExcelField(title="发票税收分类", align=1, sort=1)
    private String goodsCategory;
    @ExcelField(title="状态", align=1, sort=1)
    private String responsibleDelete;
    private String isAdd; //1手动维护，2匹配记录自动插入

    @TableField(exist = false)
    private String ids;

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getIsAdd() {
        return isAdd;
    }

    public void setIsAdd(String isAdd) {
        this.isAdd = isAdd;
    }

    public String getResponsibleDelete() {
        return responsibleDelete;
    }

    public void setResponsibleDelete(String responsibleDelete) {
        this.responsibleDelete = responsibleDelete;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInvoiceResponsible() {
        return invoiceResponsible;
    }

    public void setInvoiceResponsible(String invoiceResponsible) {
        this.invoiceResponsible = invoiceResponsible;
    }

    public String getGoodsCategory() {
        return goodsCategory;
    }

    public void setGoodsCategory(String goodsCategory) {
        this.goodsCategory = goodsCategory;
    }
}
