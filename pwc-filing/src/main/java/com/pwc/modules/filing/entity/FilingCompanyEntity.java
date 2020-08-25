package com.pwc.modules.filing.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@TableName("filing_company")
public class FilingCompanyEntity {
    /**
     * id
     */
    private Long companyId;
    /**
     * 关联企业基本信息id
     */
    private Long deptId;
    /**
     * 第三方平台企业ID
     */
    private Long orgId;
    /**
     * 登陆账号
     */
    private String loginAccount;
    /**
     * 登录方式
     */
    private Integer loginMethod;
    /**
     * 登录密码
     */
    private String loginPassword;
    /**
     * 申报地区
     */
    private String region;
    /**
     * 是否可以自动修改 纳税人性质
     */
    private String canChange;
    /**
     * 个税申报密码
     */
    private String filingPassowrd;
    /**
     * 会计准则
     */
    @NotNull(message = "会计准则不能为空")
    private String accountingStandards;
    /**
     * 纳税人身份
     */
    @NotNull(message = "纳税人身份不能为空")
    private String vatTaxpayer;
    /**
     * 启用年
     */
    @NotNull(message = "纳税启用年不能为空")
    private String enabledYear;
    /**
     * 启用月份
     */
    @NotNull(message = "纳税启用月不能为空")
    private String enabledMonth;

    /**
     * 纳税期限代码 06按期 11按次
     */
    @NotNull(message = "纳税期限代码不能为空")
    private String taxPeriodCode;
}
