package com.pwc.modules.input.entity;



import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
@Data
@TableName("input_company_task_details")
public class InputCompanyTaskDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *id
     */
    @TableId
    private Integer id;
    /**
     *companyId
     */
    private Integer companyId;
    /**
     *申请统计业务执行状态
     */
    private String type;
    /**
     *统计月份
     */
    private String statisticsMonth;
    /**
     *发票类型
     */
    private String invoiceType;
    /**
     *抵扣发票份数
     */
    private Integer deductInvoiceNum;
    /**
     *抵扣总金额（其中发票类型为17,30时 无金额，返回“-”）
     */
    private String deductAmount;
    /**
     *抵扣总有效税额
     */
    private String deductTax;
    /**
     *不抵扣发票份数
     */
    private Integer unDeductInvoiceNum;
    /**
     *不抵扣总金额（其中发票类型为17,30时 无金额，返回“-”）
     */
    private String unDeductAmount;
    /**
     *不抵扣总有效税额
     */
    private String unDeductTax;

}
