package com.pwc.modules.input.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 进项配置关联表
 * @author Gxw
 * @date 2020/7/22 11:32
 */
@Data
@TableName("input_invoice_label_related")
public class InputInvoiceLabelRelatedEntity implements Serializable {
    @TableId
    private Integer id;
    private Integer invoiceId;//发票id
    private Integer levelId;// 标签id
//    private String levelName;//标签名称
    private Integer subjectId;// 科目id
    @TableField(exist = false)
    private String subjectName;//科目名称
    private BigDecimal outRatio;//转出比例
    private String updateBy;// 操作人
    private Integer companyId;//企业id
    @TableField(fill= FieldFill.INSERT)
    private Date createTime;// 插入日期
    @TableField(fill= FieldFill.UPDATE)
    private Date updateTime;// 更新日期
    private Integer outFalg;// 转出标志
    private String reasonNote; // 备注原因
    private BigDecimal amount;//差异金额;
}
