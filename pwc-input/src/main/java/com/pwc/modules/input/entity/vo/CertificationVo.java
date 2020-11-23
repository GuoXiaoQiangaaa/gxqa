package com.pwc.modules.input.entity.vo;


import com.pwc.common.utils.excel.annotation.ExcelField;
import lombok.Data;

/**
 * @description: 勾选页面上传模版
 * @author: Gxw
 * @create: 2020-09-21 09:54
 **/
@Data
public class CertificationVo {


    @ExcelField(title="NO.", align=1, sort=10)
    private String No;
    @ExcelField(title = "发票代码",align = 1,sort = 20)
    private String invoiceCode;
    @ExcelField(title = "发票号码",align = 1,sort = 30)
    private String invoiceNumber;
    @ExcelField(title = "所属公司",align = 1,sort = 40)
    private String company;
}
