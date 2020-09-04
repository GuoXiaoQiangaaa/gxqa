package com.pwc.modules.input.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.omg.CORBA.PRIVATE_MEMBER;

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
    private String uploadName; // 文件名称
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;//上传日期
    @TableField(fill = FieldFill.INSERT)
    private Integer createBy;//上传人
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;//操作日期
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateBy;//操作人
    @TableField(fill = FieldFill.INSERT)
    private Integer deptId;
    private Integer uploadType; // 类型 0 ap 1 po
    private Integer uploadSource; // 来源
    private String uploadImage; // 上传路径
}
