package com.pwc.modules.input.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pwc.common.excel.annotation.ExcelField;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @description: Sap导入进项税明细
 * @author: Gxw
 * @create: 2020-09-23 14:57
 **/
@Data
@TableName("input_invoice_sap")
public class InputInvoiceSapEntity implements Serializable {
    @TableId
    private Integer sapId;
    /**公司代码*/
    private String companyCode;
    /**科目*/
    private String account;
    /**参考*/
    private String reference;
    /**凭证编号*/
    private String documentNo;
    /**凭证类型*/
    private String documentType;
    /**记账日期*/
    private String docDate;
    /**入账日期*/
    private String pstngDate;
    /**当地金额*/
    private BigDecimal amountInLocal;
    /**当地币种*/
    private String lcurr;
    /**金额*/
    private BigDecimal amountInDoc;
    /**币种*/
    private String curr;
    /**用户名*/
    private String userName;
    /**分配*/
    private String assignment;
    /**行文本*/
    private String text;
    /**税码*/
    private String tx;
    /**合作伙伴*/
    private String tradingPartner;
    /**
     * 记账码
     */
    private String postingKey;
    /**
     * 年月
     */
    private String yearAndMonth;
    /**
     * 摘要
     */
    private String headerText;
    /**匹配状态 0 未匹配 1 匹配 2 匹配差异*/
    private String sapMatch;
    /**匹配类型 1 发票 2 海关 3 红字通知单*/
    private String matchType;
    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private int createBy;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    /**
     * 更新人
     */
    @TableField(fill = FieldFill.UPDATE)
    private int updateBy;
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;


}
