package com.pwc.modules.input.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class InputInvoiceEntityVo implements Serializable {
    private String vat_type;

    private String number;

    private String code;

    private String date;

    private String check_code;

    private String total_amout_without_tax;


}
