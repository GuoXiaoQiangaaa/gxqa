package com.pwc.modules.input.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pwc.common.utils.excel.annotation.ExcelField;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("input_trans_out_category")
public class InputTansOutCategoryEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId
    private Integer id;
//    private Integer createBy; // 上传人
    @ExcelField(title="转出目录", align=1)
    private String category;
    @ExcelField(title="税收科目", align=1)
    private String item;  // 对应品类
    @TableLogic(value = "0", delval = "-1")
    private Integer status; //状态 0 正常 -1删除


}
