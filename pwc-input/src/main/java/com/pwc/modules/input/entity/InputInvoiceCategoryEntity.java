package com.pwc.modules.input.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * input_invoice_category
 * @author zk
 */
@Data
@TableName("input_invoice_category")
public class InputInvoiceCategoryEntity implements Serializable {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 分类名
     */
    private String category;

    /**
     * 删除标志
     */
    @TableLogic(value = "0", delval = "-1")
    private Integer delFlag;

    private static final long serialVersionUID = 1L;
}