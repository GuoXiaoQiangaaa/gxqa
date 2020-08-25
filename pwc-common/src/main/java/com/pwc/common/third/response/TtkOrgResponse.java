package com.pwc.common.third.response;

import com.pwc.common.third.request.TaxReportAccessType;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author zk
 */
@Data
@ToString
public class TtkOrgResponse {
    /**
     * 企业id
     */
    private Long orgId;
    /**
     * 企业名称
     */
    private String name;

    /**
     * 会计准则：
     * 企业会计准则 2000020001，
     * 小企业会计准则 2000020002，
     * 企业会计准则（商业银行） 2000020003，
     * 企业会计准则（保险公司） 2000020004，
     * 企业会计准则（证券公司） 2000020005，
     * 企业会计准则（担保企业会计核算办法） 2000020006，
     * 事业单位会计制度 2000020007
     */
    private Long accountingStandards;

    /**
     * 纳税人身份:
     * 一般纳税人 2000010001，
     * 小规模纳税人 2000010002
     */
    private Long vatTaxpayer;

    /**
     * 启用年
     */
    private String enabledYear;

    /**
     * 启用月份
     */
    private String enabledMonth;

    /**
     * 税报取数方式信息设置 (可选参数)
     */
    private List<TaxReportAccessType> taxReportAccessTypeDtoList;

    
}
