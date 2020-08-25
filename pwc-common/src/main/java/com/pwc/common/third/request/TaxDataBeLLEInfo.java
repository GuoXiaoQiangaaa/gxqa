package com.pwc.common.third.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Tax Data BeLLE Info
 * @author louxin
 */
@Data
public class TaxDataBeLLEInfo {

    /**
     * 门店税号
     */
    private String storesEin;
    /**
     * 所在省份
     */
    private String province;
    /**
     * 所在城市
     */
    private String city;
    /**
     * 公司名称（简称）
     */
    private String companyName;
    /**
     * 应征增值税不含税销售额（3%征收率）
     * json["xgmData"]["zzssyyxgmnsr"]["zzsxgmGrid"]["zzsxgmGridlb"][0]["yzzzsbhsxse"]
     */
    private String yzzzsbhsxse;
    /**
     * 税务机关代开的增值税专用发票不含税销售额（3%征收率）
     * json["xgmData"]["zzssyyxgmnsr"]["zzsxgmGrid"]["zzsxgmGridlb"][0]["swjgdkdzzszyfpbhsxse"]
     */
    private String swjgdkdzzszyfpbhsxse;
    /**
     * 税控器具开具的普通发票不含税销售额
     * json["xgmData"]["zzssyyxgmnsr"]["zzsxgmGrid"]["zzsxgmGridlb"][0]["skqjkjdptfpbhsxse"]
     */
    private String skqjkjdptfpbhsxse;
    /**
     * 应税增值税不含税销售额（5%征收率)
     * json["xgmData"]["zzssyyxgmnsr"]["zzsxgmGrid"]["zzsxgmGridlb"][0]["xsczbdcbhsxse"]
     */
    private String xsczbdcbhsxse;
    /**
     * 税务机关代开的增值税专用发票不含税销售额（5%征收率）
     * json["xgmData"]["zzssyyxgmnsr"]["zzsxgmGrid"]["zzsxgmGridlb"][0]["swjgdkdzzszyfpbhsxse1"]
     */
    private String swjgdkdzzszyfpbhsxse1;
    /**
     * 税控器具开具的普通发票不含税销售额
     * json["xgmData"]["zzssyyxgmnsr"]["zzsxgmGrid"]["zzsxgmGridlb"][0]["skqjkjdptfpbhsxse2"]
     */
    private String skqjkjdptfpbhsxse2;
    /**
     * 税销售使用过的固定资产不含税销售额
     * json["xgmData"]["zzssyyxgmnsr"]["zzsxgmGrid"]["zzsxgmGridlb"][0]["xssygdysgdzcbhsxse"]
     */
    private String xssygdysgdzcbhsxse;
    /**
     * 其中：税控器具开具的普通发票不含税销售额
     * json["xgmData"]["zzssyyxgmnsr"]["zzsxgmGrid"]["zzsxgmGridlb"][0]["skqjkjdptfpbhsxse1"]
     */
    private String skqjkjdptfpbhsxse1;
    /**
     * 免税销售额
     * json["xgmData"]["zzssyyxgmnsr"]["zzsxgmGrid"]["zzsxgmGridlb"][0]["msxse"]
     */
    private String msxse;
    /**
     * 其中：小微企业免税销售额
     * json["xgmData"]["zzssyyxgmnsr"]["zzsxgmGrid"]["zzsxgmGridlb"][0]["xwqymsxse"]
     */
    private String xwqymsxse;
    /**
     * 未达起征点销售额
     * json["xgmData"]["zzssyyxgmnsr"]["zzsxgmGrid"]["zzsxgmGridlb"][0]["wdqzdxse"]
     */
    private String wdqzdxse;
    /**
     * 其他免税销售额
     * json["xgmData"]["zzssyyxgmnsr"]["zzsxgmGrid"]["zzsxgmGridlb"][0]["qtmsxse"]
     */
    private String qtmsxse;
    /**
     * 出口免税销售额
     * json["xgmData"]["zzssyyxgmnsr"]["zzsxgmGrid"]["zzsxgmGridlb"][0]["ckmsxse"]
     */
    private String ckmsxse;
    /**
     * 其中：税控器具开具的普通发票销售额
     * json["xgmData"]["zzssyyxgmnsr"]["zzsxgmGrid"]["zzsxgmGridlb"][0]["skqjkjdptfpxse1"]
     */
    private String skqjkjdptfpxse1;
    /**
     * 核定销售额
     * json["xgmData"]["zzssyyxgmnsr"]["zzsxgmGrid"]["zzsxgmGridlb"][0]["hdxse"]
     */
    private String hdxse;
    /**
     * 本期应纳税额
     * json["xgmData"]["zzssyyxgmnsr"]["zzsxgmGrid"]["zzsxgmGridlb"][0]["bqynse"]
     * json["xgmData"]["zzssyyxgmnsr"]["zzsxgmGrid"]["zzsxgmGridlb"][1]["bqynse"]
     */
    private String bqynse;
    /**
     * 核定应纳税额
     * json["xgmData"]["zzssyyxgmnsr"]["zzsxgmGrid"]["zzsxgmGridlb"][0]["hdynse"]
     */
    private String hdynse;
    /**
     * 本期应纳税额减征额
     * json["xgmData"]["zzssyyxgmnsr"]["zzsxgmGrid"]["zzsxgmGridlb"][0]["bqynsejze"]
     */
    private String bqynsejze;
    /**
     * 本期免税额
     * json["xgmData"]["zzssyyxgmnsr"]["zzsxgmGrid"]["zzsxgmGridlb"][0]["bqmse"]
     */
    private String bqmse;
    /**
     * 其中：小微企业免税额
     * json["xgmData"]["zzssyyxgmnsr"]["zzsxgmGrid"]["zzsxgmGridlb"][0]["xwqymse"]
     */
    private String xwqymse;
    /**
     * 未达起征点免税额
     * json["xgmData"]["zzssyyxgmnsr"]["zzsxgmGrid"]["zzsxgmGridlb"][0]["wdqzdmse"]
     */
    private String wdqzdmse;
    /**
     * 应纳税额合计
     * json["xgmData"]["zzssyyxgmnsr"]["zzsxgmGrid"]["zzsxgmGridlb"][0]["ynsehj"]
     * json["xgmData"]["zzssyyxgmnsr"]["zzsxgmGrid"]["zzsxgmGridlb"][1]["ynsehj"]
     */
    private String ynsehj;
    /**
     * 本期预缴税额
     * json["xgmData"]["zzssyyxgmnsr"]["zzsxgmGrid"]["zzsxgmGridlb"][0]["bqyjse1"]
     */
    private String bqyjse1;
    /**
     * 本期应补（退）税额
     * json["xgmData"]["zzssyyxgmnsr"]["zzsxgmGrid"]["zzsxgmGridlb"][0]["bqybtse"]
     */
    private String bqybtse;
    /**
     * 本期销售不动产的销售额
     * json["xgmData"]["zzssyyxgmnsr"]["zzsxgmGrid"]["zzsxgmGridlb"][0]["bdcxse"]
     */
    private String bdcxse;
    /**
     * 应征增值税不含税销售额（3%征收率）
     * json["xgmData"]["zzssyyxgmnsr"]["zzsxgmGrid"]["zzsxgmGridlb"][1]["yzzzsbhsxse"]
     */
    private String yzzzsbhsxse_1;
    /**
     * 税务机关代开的增值税专用发票不含税销售额（3%征收率）
     * json["xgmData"]["zzssyyxgmnsr"]["zzsxgmGrid"]["zzsxgmGridlb"][1]["swjgdkdzzszyfpbhsxse"]
     */
    private String swjgdkdzzszyfpbhsxse_1;
    /**
     * 税控器具开具的普通发票不含税销售额
     * json["xgmData"]["zzssyyxgmnsr"]["zzsxgmGrid"]["zzsxgmGridlb"][1]["skqjkjdptfpbhsxse"]
     */
    private String skqjkjdptfpbhsxse_1;
    /**
     * 应税增值税不含税销售额（5%征收率)
     * json["xgmData"]["zzssyyxgmnsr"]["zzsxgmGrid"]["zzsxgmGridlb"][1]["xsczbdcbhsxse"]
     */
    private String xsczbdcbhsxse_1;
    /**
     * 税务机关代开的增值税专用发票不含税销售额（5%征收率）
     * json["xgmData"]["zzssyyxgmnsr"]["zzsxgmGrid"]["zzsxgmGridlb"][1]["swjgdkdzzszyfpbhsxse1"]
     */
    private String swjgdkdzzszyfpbhsxse1_1;
    /**
     * 税控器具开具的普通发票不含税销售额
     * json["xgmData"]["zzssyyxgmnsr"]["zzsxgmGrid"]["zzsxgmGridlb"][1]["skqjkjdptfpbhsxse2"]
     */
    private String skqjkjdptfpbhsxse2_1;
    /**
     * 税销售使用过的固定资产不含税销售额
     * json["xgmData"]["zzssyyxgmnsr"]["zzsxgmGrid"]["zzsxgmGridlb"][1]["xssygdysgdzcbhsxse"]
     */
    private String xssygdysgdzcbhsxse_1;
    /**
     * 其中：税控器具开具的普通发票不含税销售额
     * json["xgmData"]["zzssyyxgmnsr"]["zzsxgmGrid"]["zzsxgmGridlb"][1]["skqjkjdptfpbhsxse1"]
     */
    private String skqjkjdptfpbhsxse1_1;
    /**
     * 免税销售额
     * json["xgmData"]["zzssyyxgmnsr"]["zzsxgmGrid"]["zzsxgmGridlb"][1]["msxse"]
     */
    private String msxse_1;
    /**
     * 其中：小微企业免税销售额
     * json["xgmData"]["zzssyyxgmnsr"]["zzsxgmGrid"]["zzsxgmGridlb"][1]["xwqymsxse"]
     */
    private String xwqymsxse_1;
    /**
     * 未达起征点销售额
     * json["xgmData"]["zzssyyxgmnsr"]["zzsxgmGrid"]["zzsxgmGridlb"][1]["wdqzdxse"]
     */
    private String wdqzdxse_1;
    /**
     * 其他免税销售额
     * json["xgmData"]["zzssyyxgmnsr"]["zzsxgmGrid"]["zzsxgmGridlb"][1]["qtmsxse"]
     */
    private String qtmsxse_1;
    /**
     * 出口免税销售额
     * json["xgmData"]["zzssyyxgmnsr"]["zzsxgmGrid"]["zzsxgmGridlb"][1]["ckmsxse"]
     */
    private String ckmsxse_1;
    /**
     * 其中：税控器具开具的普通发票销售额
     * json["xgmData"]["zzssyyxgmnsr"]["zzsxgmGrid"]["zzsxgmGridlb"][1]["skqjkjdptfpxse1"]
     */
    private String skqjkjdptfpxse1_1;
    /**
     * 核定销售额
     * json["xgmData"]["zzssyyxgmnsr"]["zzsxgmGrid"]["zzsxgmGridlb"][1]["hdxse"]
     */
    private String hdxse_1;
    /**
     * 本期应纳税额
     * json["xgmData"]["zzssyyxgmnsr"]["zzsxgmGrid"]["zzsxgmGridlb"][1]["bqynse"]
     */
    private String bqynse_1;
    /**
     * 核定应纳税额
     * json["xgmData"]["zzssyyxgmnsr"]["zzsxgmGrid"]["zzsxgmGridlb"][1]["hdynse"]
     */
    private String hdynse_1;
    /**
     * 本期应纳税额减征额
     * json["xgmData"]["zzssyyxgmnsr"]["zzsxgmGrid"]["zzsxgmGridlb"][1]["bqynsejze"]
     */
    private String bqynsejze_1;
    /**
     * 本期免税额
     * json["xgmData"]["zzssyyxgmnsr"]["zzsxgmGrid"]["zzsxgmGridlb"][1]["bqmse"]
     */
    private String bqmse_1;
    /**
     * 其中：小微企业免税额
     * json["xgmData"]["zzssyyxgmnsr"]["zzsxgmGrid"]["zzsxgmGridlb"][1]["xwqymse"]
     */
    private String xwqymse_1;
    /**
     * 未达起征点免税额
     * json["xgmData"]["zzssyyxgmnsr"]["zzsxgmGrid"]["zzsxgmGridlb"][1]["wdqzdmse"]
     */
    private String wdqzdmse_1;
    /**
     * 应纳税额合计
     * json["xgmData"]["zzssyyxgmnsr"]["zzsxgmGrid"]["zzsxgmGridlb"][1]["ynsehj"]
     */
    private String ynsehj_1;
    /**
     * 本期预缴税额
     * json["xgmData"]["zzssyyxgmnsr"]["zzsxgmGrid"]["zzsxgmGridlb"][1]["bqyjse1"]
     */
    private String bqyjse1_1;
    /**
     * 本期应补（退）税额
     * json["xgmData"]["zzssyyxgmnsr"]["zzsxgmGrid"]["zzsxgmGridlb"][1]["bqybtse"]
     */
    private String bqybtse_1;
    /**
     * 本期销售不动产的销售额
     * json["xgmData"]["zzssyyxgmnsr"]["zzsxgmGrid"]["zzsxgmGridlb"][1]["bdcxse"]
     */
    private String bdcxse_1;

}
