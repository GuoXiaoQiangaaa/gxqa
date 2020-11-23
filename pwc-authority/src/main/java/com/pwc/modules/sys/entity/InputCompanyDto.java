package com.pwc.modules.sys.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 企业信息(通过添加dept时进行维护)
 *
 * @author fanpf
 * @date 2020/9/9
 */
@Data
public class InputCompanyDto implements Serializable {
    private static final long serialVersionUID = -4650600784821086501L;

    /** 企业主键 */
    private Long id;

    /** 企业编号 */
    private String companyNumber;

    /** 企业名称 */
    private String companyName;
    
    /** 税号 */
    private String companyDutyParagraph;

    /** 地址电话 */
    private String companyAddressPhone;
    
    /** 银行账号 */
    private String companyBankAccount;

    /** 统计状态: 0:未统计; 1:申请统计中; 2:申请统计成功; 3:申请统计失败; 4:确认统计中; 5:确认统计成功; 6:确认统计失败; 7:统计撤销中 */
    private String status;

    /** 关联dept表的主键 */
    private Long deptId;

    /** 创建人 */
    private String createBy;

    /** 创建时间 */
    private Date createTime;

    /** 更新人 */
    private String updateBy;

    /** 更新时间 */
    private Date updateTime;
}
