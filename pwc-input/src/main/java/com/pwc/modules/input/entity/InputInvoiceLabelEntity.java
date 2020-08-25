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
 * 进项配置表
 *
 * @author Gxw
 * @date 2020/7/15 16:41
 */
@Data
@TableName("input_invoice_label")
public class InputInvoiceLabelEntity implements Serializable {
    @TableId
    private Integer id;
    @ExcelField(title = "转出目录",align =1,sort = 10)
    private String labelName;//名称
    @ExcelField(title = "税收科目",align =1,sort = 20)
    private String subjectName;//税收科目
    private Integer labelLevel;//层级
    private Integer labelAttribution;// 归属标签
    @TableField(exist = false)
    @ExcelField(title = "标签类型",align =1,sort = 30)
    private String attributionName; //标签名字
    private String numbering;// 编号
    private Integer rangeId; // 范围id
    @TableField(exist = false)
    private String labelRange; // 范围
    @TableField(exist = false)
    private String deptName;// 公司名称
    //    private String taxCode;// 税号
    private Integer deptId;// 公司id
    private Integer labelFalg;// 状态
    private String inquireFalg;//查询标志 查询标志 0是标签 1 免税 2 礼品 3 福利 4科目，5是范围
    private Integer deleteFalg;// 删除状态
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;// 插入日期
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;// 更新日期


}
