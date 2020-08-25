package com.pwc.modules.input.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

@TableName("input_sap_invoice_mappingid")
public class InputSapInvoiceMappingIdEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId
    private Integer id;

    private Integer sapId;

    private Integer responsibleId;

    private Integer goodsMatnrId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSapId() {
        return sapId;
    }

    public void setSapId(Integer sapId) {
        this.sapId = sapId;
    }

    public Integer getResponsibleId() {
        return responsibleId;
    }

    public void setResponsibleId(Integer responsibleId) {
        this.responsibleId = responsibleId;
    }

    public Integer getGoodsMatnrId() {
        return goodsMatnrId;
    }

    public void setGoodsMatnrId(Integer goodsMatnrId) {
        this.goodsMatnrId = goodsMatnrId;
    }
}
