package com.pwc.modules.input.entity.vo;

import lombok.Data;

/**
 * @description: 匹配结果列表
 * @author: Gxw
 * @create: 2020-09-28 13:32
 **/
@Data
public class MatchResultVo {
    /**
     * 月份
     */
    private String month;
    /**
     * 分类
     */
    private String sort;
    /**
     * 入账税额
     */
    private String creditTax;
    /**
     *认证税额
     */
    private String certificationTax;
    /**
     * 本月入账未认证
     */
    private String monthCredTax;
    /**
     * 本月认证未入账
     */
    private String monthCertTax;
    /**
     *前期认证本月入账
     */
    private String monthCredBeforeTax;
    /**
     *前期入账本月认证
     */
    private String monthCertBeforeTax;
    /**
     *税会差异
     */
    private String differenceTax;
    /**
     * 差异调整合计
     */
    private String adjustmentTax;
    /**
     * 核对
     */
    private String checkTax;
}
