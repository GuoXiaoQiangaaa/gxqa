package com.pwc.common.third.request;

import lombok.Data;

/**
 * sd
 */
@Data
public class FilingSD {

    /**
     * 地税计税依据-印花税计税依据（购销合同）金额"
     */
    private String dsjsyjYhsjsyjgxhtje;

    /**
     * 地税计税依据-印花税计税依据（财产租赁合同）金额"
     */
    private String dsjsyjYhsjsyjcczlhtje;

    /**
     * 地税计税依据-工人经费人数
     */
    private String dsjsyjGrjfrs;
    /**
     * 地税计税依据-工会经费（工资）
     */
    private String dsjsyjGhjfgz;
    /**
     * 地税计税依据-"残保金人数"
     */
    private String dsjsyjCbjrs;
    /**
     * 地税计税依据-"残保金计税依据（工资等）"
     */
    private String dsjsyjCbjjsyjgzd;
}
