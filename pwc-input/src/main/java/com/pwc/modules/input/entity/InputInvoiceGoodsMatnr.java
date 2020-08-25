package com.pwc.modules.input.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pwc.common.utils.excel.annotation.ExcelField;

@TableName("input_invoice_goods_matnr")
public class InputInvoiceGoodsMatnr {
    @TableId
    private Integer id;
    @ExcelField(title="商品名称", align=1, sort=2)
    private String goodsName;

    @ExcelField(title="规格", align=1, sort=3)
    private String goodsType;

    @ExcelField(title="物料号", align=1, sort=1)
    private String maktx;

    private String used;

    @ExcelField(title="来源", align=1, sort=4)
    private String type;

    public String getUsed() {
        return used;
    }

    public void setUsed(String used) {
        this.used = used;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getMaktx() {
        return maktx;
    }

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    public void setMaktx(String maktx) {
        this.maktx = maktx;
    }
}
