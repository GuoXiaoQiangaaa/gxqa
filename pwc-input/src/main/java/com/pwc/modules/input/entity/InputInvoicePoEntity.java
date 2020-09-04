package com.pwc.modules.input.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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

    @TableId
    private Integer poid;  // poid
    private String poNumber; // po号码
    private String invoiceNumber; // 发票号码
    private Date createTime; // 创建日期
    private Date updateTime; // 操作日期
    private String createBy; // 创建人
    private String updateBy; // 操作人
    private String deptId; // 所属公司
}
