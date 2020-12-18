package com.pwc.modules.input.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pwc.common.utils.excel.annotation.ExcelField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @description: PO数据
 * @author: Gxw
 * @create: 2020-09-02 18:10
 **/
@Data
@TableName("input_invoice_po")
public class InputInvoicePoEntity implements Serializable {
    /**
     *  poid
     */
    @TableId
    private Integer poId;
    /**
     * po号码
     */
    @ExcelField(title="po号码", align=1, sort=20)
    private String poNumber;
    /**
     * 发票号码
     */
    @ExcelField(title="发票号码", align=1, sort=30)
    private String invoiceNumber;
    /**
     * 购方名称
     */
    @ExcelField(title="购方名称", align=1, sort=10)
    private String  companyName;
    /**
     * 票据地址
     */
    private String  invoiceImage;
    /**
     * 状态 -1识别失败 0识别重复  1识别成功 2匹配成功
     */
    @ExcelField(title="状态", align=1, sort=40,dictType = "UpdoldState")
    private String status;
    /**
     * 创建日期
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    /**
     *  操作日期
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    /**
     *   创建人
      */
    @TableField(fill = FieldFill.INSERT)
    private Integer createBy;
    /**
     * 操作人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateBy;
    /**
     * 所属公司
     */
    private Integer deptId;
    /**
     * 关联上传id
     */
    private Integer uploadId;
    /**类型*/
    @TableField(exist = false)
    private Integer sourceStyle;
}
