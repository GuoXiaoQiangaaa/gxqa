package com.pwc.modules.input.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @description: PoList表
 * @author: Gxw
 * @create: 2020-09-11 16:53
 **/
@Data
@TableName("input_po_list")
public class InputPoListEntity implements Serializable {
    @TableId
    private int polistId;
    private String companyCode;//  PO所属公司
    private String vendorName;//  供应商名称
    private String purchasingDocument;// 购方凭证
    private String poItem;//PO号码
    private String materialNo;// 货号
    private String shortText;// 行文本
    private String qtyOrdered;// 订单数量
    private String netPrice;// 净价
    private String createDate;// PO创建日期
    private String vendorNo;// 供应商号码
}
