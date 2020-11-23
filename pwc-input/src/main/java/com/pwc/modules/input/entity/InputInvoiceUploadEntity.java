package com.pwc.modules.input.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @description: 票据上传
 * @author: Gxw
 * @create: 2020-09-03 16:19
 **/
@Data
@TableName("input_invoice_upload")
public class InputInvoiceUploadEntity implements Serializable {
    @TableId
    private Integer uploadId;
    /** 文件名称*/
    private String uploadName;
    /** 上传日期*/
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    /** 上传人*/
    @TableField(fill = FieldFill.INSERT)
    private Integer createBy;
    /** 操作日期*/
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    /** 操作人*/
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateBy;
    @TableField(fill = FieldFill.INSERT)
    private Integer deptId;
    /** 类型 0  其他 1蓝字发票 2 红字发票 3 Po list 4 TE发票 5 ap发票
      */
    private Integer uploadType;
    /** 来源 1 扫描上传 2 手动上传*/
    private Integer uploadSource;
    /** 图片路径*/
    private String uploadImage;
    /**状态 0 未识别 1 识别失败 2 识别成功 3重复发票*/
    private String status;
    /**上传人 */
    @TableField(exist = false)
    private String createUserName;
    /**批次号*/
    private String invoiceBatchNumber;
    /***/
    private String id; // 关联id
    /***/
    @TableField(exist = false)
    private InputInvoiceEntity invoiceEntity;
    /***/
    @TableField(exist = false)
    private InputInvoicePoEntity poEntity;
}
