package com.pwc.common.third.common;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zk
 */
public class TtkConstants {

//    public final static String API_HOST_DEBUG = "http://api.aierp.cn:8089/v1";
//    public final static String API_HOST_DEBUG_DEV = "http://api.aierp.cn:8089/v1";

    public final static String API_HOST_DEBUG_DEV = "http://api.dev.aierp.cn:8089/v1";
    public final static String WEB_HOST_DEV = "http://debug.aierp.cn:8089";

    /**
     * test
     */
    public final static String API_HOST_TEST = "http://api.test.aierp.cn:8089/v1";
    public final static String WEB_HOST_TEST = "http://test.aierp.cn:8089";
    public final static String APP_KEY_TEST = "10005030";
    public final static String APP_SECRET_TEST = "q82lgpwhp2zivas1kmhvhsxytvmlk73aan4ic8ulwqak2sg2ojkof14x5uagnjmm";


    /**
     * erp demo
     */
    public final static String API_HOST_ERP_DEMO = "https://apierpdemo.jchl.com/v1";
    public final static String WEB_HOST_ERP_DEMO = "https://erpdemo.jchl.com";

    /**
     * 正式环境 erp
     */
    public final static String APP_KEY_ERP = "10005030";
    public final static String APP_SECRET_ERP = "1okicbgfyhfynn75qpxipvbaq4pogs6j7p5aj09vj6kavdpcr4ejegofvy24did6";
    public final static String API_HOST_ERP = "https://apierp.jchl.com/v1";
    public final static String WEB_HOST_ERP = "https://erp.jchl.com";


    /**
     * erp demo
     */
//    public final static String API_HOST = API_HOST_ERP_DEMO;
//    public final static String WEB_HOST = WEB_HOST_ERP_DEMO;
//
//    public final static String APP_KEY = APP_KEY_TEST;
//    public final static String APP_SECRET = APP_SECRET_TEST;

    //test
//    public final static String API_HOST = API_HOST_TEST;
//    public final static String WEB_HOST = WEB_HOST_TEST;
//
//    public final static String APP_KEY = APP_KEY_TEST;
//    public final static String APP_SECRET = APP_SECRET_TEST;

    //erp
    public final static String API_HOST = API_HOST_ERP;
    public final static String WEB_HOST = WEB_HOST_ERP;

    public final static String APP_KEY = APP_KEY_ERP;
    public final static String APP_SECRET = APP_SECRET_ERP;


//    public final static String CREATE_ORG = API_HOST_DEBUG_DEV + "/openapi/basicData/createOrg";

//    public final static String GET_TOKEN = API_HOST_DEBUG_DEV + "/edf/oauth2/access_token";

    public enum TtkResCode {
        /**
         * 成功
         */
        SUCCESS("0");

        String value;

        TtkResCode(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 地区对照
     */
    public final static Map<String, String> DISTRICT_MAP = new HashMap<String, String>() {{
        put("310000", CityCode.SHANGHAI.getValue());
        put("110000", CityCode.BEIJING.getValue());
        put("120000", CityCode.TIANJIN.getValue());
        put("130000", CityCode.HEBEI.getValue());
        put("140000", CityCode.SHANXI.getValue());
        put("150000", CityCode.NEIMENGGU.getValue());
        put("210000", CityCode.LIAONING.getValue());
        put("220000", CityCode.JILIN.getValue());
        put("230000", CityCode.HEILONGJIANG.getValue());
        put("320000", CityCode.JIANGSU.getValue());
        //镇江
        put("321100", CityCode.JIANGSU.getValue());
        put("340000", CityCode.ANHUI.getValue());
        put("350000", CityCode.FUJIAN.getValue());
        //福州
        put("350100", CityCode.FUJIAN.getValue());
        put("360000", CityCode.JIANGXI.getValue());
        put("370000", CityCode.SHANDONG.getValue());
        put("370300", CityCode.SHANDONG.getValue());
        put("410000", CityCode.HENAN.getValue());
        put("420000", CityCode.HUBEI.getValue());
        put("430000", CityCode.HUNAN.getValue());
        //广东
        put("440000", CityCode.GUANGDONG.getValue());
        //汕头
        put("440500", CityCode.GUANGDONG.getValue());
        put("450000", CityCode.GUANGXI.getValue());
        put("460000", CityCode.HAINAN.getValue());
        put("500000", CityCode.CHONGQING.getValue());
        put("510000", CityCode.SICHUAN.getValue());
        put("520000", CityCode.GUIZHOU.getValue());
        //贵阳
        put("520100", CityCode.GUIZHOU.getValue());
        put("530000", CityCode.YUNNAN.getValue());
        put("540000", CityCode.XIZANG.getValue());
        //陕西省
        put("610000", CityCode.SHANXIS.getValue());
        //西安市
        put("610100", CityCode.SHANXIS.getValue());
        put("620000", CityCode.GANSU.getValue());
        put("630000", CityCode.QINGHAI.getValue());
        put("640000", CityCode.NIGNXIA.getValue());
        put("650000", CityCode.XINJIANG.getValue());
        put("210200", CityCode.DALIAN.getValue());
        put("330200", CityCode.NINGBO.getValue());
        put("350200", CityCode.XIAMEN.getValue());
        put("370200", CityCode.QINGDAO.getValue());
        put("440300", CityCode.SHENZHEN.getValue());

    }};

    /**
     * 资产负债表对应 -- 企业会计准则
     */
    public final static Map<String, String> BANLANCE_SHEET_MAP = new HashMap<String, String>() {{
        put("货币资金", "hbzj");
        put("短期借款", "dqjk");
        put("以公允价值计量且其变动计入当期损益的金融资产", "ygyjzjlqqbdjrdqsydjrzc");
        put("以公允价值计量且其变动计入当期损益的金融负债", "ygyjzjlqqbdjrdqsydjrfz");
//        put("衍生金融资产", "ysjrzc");
//        put("衍生金融负债", "ysjrfz");
        put("应收票据", "yspj");
        put("应付票据", "yfpj");
        put("应收账款", "yszk");
        put("应付账款", "yfzk");
        put("预付款项", "yfkx");
        put("预收款项", "yskx");
        put("应收利息", "yslx");
        put("应付职工薪酬", "yfzgxc");
        put("应收股利", "ysgl");
        put("应交税费", "yjsf");
        put("其他应收款", "qtysk");
        put("应付利息", "yflx");
        put("存货", "ch");
        put("应付股利", "yfgl");
        put("持有待售资产", "cydszc");
        put("其他应付款", "qtyfk");
        put("一年内到期的非流动资产", "ynndqdfldzc");
        put("持有待售负债", "cydsfz");
        put("其他流动资产", "qtldzc");
        put("一年内到期的非流动负债", "ynndqdfldfz");
        put("流动资产合计", "ldzchj");
        put("其他流动负债", "qtldfz");
        put("流动负债合计", "ldfzhj");
        put("可供出售金融资产", "kgcsjrzc");
        put("持有至到期投资", "cyzdqtz");
        put("长期借款", "zqjk");
        put("长期应收款", "zqysk");
        put("应付债券", "yfzq");
        put("长期股权投资", "zqgqtz");
        put("投资性房地产", "tzxfdc");
        put("固定资产", "gdzc");
        put("长期应付款", "zqyfk");
        put("在建工程", "zjgc");
        put("专项应付款", "zxyfk");
        put("工程物资", "gcwz");
        put("预计负债", "yjfz");
        put("递延收益", "dysy");
        put("生产性生物资产", "scxswzc");
        put("递延所得税负债", "dysdsfz");
        put("油气资产", "yqzc");
        put("其他非流动负债", "qtfldfz");
        put("开发支出", "kfzc");
        put("负债合计", "fzhj");
        put("商誉", "sy");
        put("长期待摊费用", "zqdtfy");
        put("实收资本（或股本）", "sszbhgb");
        put("递延所得税资产", "dysdszc");
        put("其他权益工具", "qtqygj");
        put("其他非流动资产", "qtfldzc");
        put("非流动资产合计", "fldzchj");
        put("资本公积", "zbgj");
        put("减：库存股", "jkcg");
        put("其他综合收益", "qtzhsy");
        put("盈余公积", "yygj");
        put("未分配利润", "wfplr");
        put("所有者权益（或股东权益）合计", "syzqyhgdqyhj");
        put("资产总计", "zczj");
        put("负债和所有者权益（或股东权益）总计", "fzhsyzqyhgdqyzj");
        // 重复
        put("其中：优先股", "yfzqqzyxg");
        put("永续债", "yfzqyxz");
        put("其中：优先股", "qtqygjqzyxg");
        put("永续债", "qtqygjyxz");
    }};

    /**
     * BS ZL1001002 企业会计制度财务报表报送与信息采集
     */
    public final static Map<String, String> BS_ZL1001002 = new HashMap<String, String>() {
        {
            put("","");
        }
    };

    /**
     * 资产负债表对应 -- 企业会计准则 -- apple
     */
    public final static Map<String, String> APPLE_BS_MAP = new HashMap<String, String>() {{
        put("以公允价值计量且变动计入当期损益的金融", "以公允价值计量且其变动计入当期损益的金融资产");
        put("应收票据及应收账款", "应收账款");
//        put("套期工具", "衍生金融资产");  //对应D151 BS Sep sheet中D11单元格套期工具
//        put("套期工具", "衍生金融负债");  //#对应D151 BS Sep sheet中N10单元格套期工具
        put("以公允价值计量且其变动计入当期损益的金", "以公允价值计量且其变动计入当期损益的金融负债");
        put("应付票据及应付账款", "应付账款");
        put("实收资本", "实收资本（或股本）");
        put("所有者权益合计", "所有者权益（或股东权益）合计");
        put("负债和所有者权益总计", "负债和所有者权益（或股东权益）总计");
    }};

    /**
     * 利润表对应 -- 企业会计准则 -- apple
     */
    public final static Map<String, String> APPLE_PL_MAP = new HashMap<String, String>() {{
        put("营业收入", "一、营业收入");
        put("减：  营业成本", "减：营业成本");
        put("税金及附加", "减：营业成本");
        put("财务（收入）／费用", "财务费用");
        put("资产减值损失／（转回）", "资产减值损失");
        put(" 加：  公允价值变动收益／（损失）", "加：公允价值变动收益（损失以“-”号填列）");
        put(" 加：  投资（损失）／收益", "投资收益（损失以“-”号填列）");
        put("加：  资产处置（损失）／收益", "资产处置收益（损失以“-”号填列）");
        put("加：  其他收益", "其他收益");
        put("营业利润／（亏损）", "二、营业利润（亏损以“-”号填列）");
        put("利润／（亏损）总额", "三、利润总额（亏损总额以“-”号填列）");
        put("净利润／（亏损）", "四、净利润（净亏损以“-”号填列）");
        put("其他综合收益／（亏损）的税后净额", "五、其他综合收益的税后净额");
        put("以后将重分类进损益的其他综合收益／（亏损）现金流量套期工具", "4.现金流经套期损益的有效部分");
        put("综合收益／（亏损）总额", "六、综合收益总额");
    }};

    /**
     * 资产负债表对应 -- 企业会计准则 -- apple
     */
    public final static Map<String, String> ZARA_BS_MAP = new HashMap<String, String>() {{
        put("应收票据及应收账款", "应收账款");
        put("应付票据及应付账款", "应付账款");
        put("所有者权益合计", "所有者权益（或股东权益）合计");
        put("负债和所有者权益总计", "负债和所有者权益（或股东权益）总计");
    }};

    /**
     * 利润表对应 -- 企业会计准则 -- zara
     */
    public final static Map<String, String> ZARA_PL_MAP = new HashMap<String, String>() {{
        put("一、营业总收入", "一、营业收入");
        put("加：其他收益", "其他收益");
        put("加：公允价值变动收益（损失以“-”号填列）", "公允价值变动收益（损失以“-”号填列）");
        put("投资收益（损失以“-”号填列）", "投资收益（损失以“-”号填列）");
        put("（一）不能重分类进损益的其他综合收益", "（一）以后不能重分类进损益的其他综合收益");
        put("（二）将重分类进损益的其他综合收益", "（二）以后将重分类进损益的其他综合收益");
        put("七、每股收益：", "七、每股收益");
        put("（一）基本每股收益", "（一）基本每股收益");
        put("（二）稀释每股收益", "（二）稀释每股收益");
    }};


    /**
     * 资产负债表对应 -- 企业会计准则 小规模-- apple
     */
    public final static Map<String, String> ZARA_BS_SMALL_MAP = new HashMap<String, String>() {{
        put("应收票据及应收账款", "应收账款");
        put("预付款项", "预付账款");
        put("固定资产", "固定资产原价");
        put("应付票据及应付账款", "应付账款");
        put("所有者权益（或股东权益）", "所有者权益（或股东权益）合计");
    }};

    /**
     * 利润表对应 -- 企业会计准则 小规模-- zara
     */
    public final static Map<String, String> ZARA_PL_SMALL_MAP = new HashMap<String, String>() {{
        put("一、营业总收入", "一、营业收入");
    }};

    /**
     * 利润表对应 -- 企业会计准则
     */
    public final static Map<String, String> PL_MAP = new HashMap<String, String>() {{
        put("一、营业收入", "yyysr");
        put("减：营业成本", "jyycb");
        put("税金及附加", "sjjfj");
        put("销售费用", "xsfy");
        put("管理费用", "glfy");
        put("财务费用", "cwfy");
        put("资产减值损失", "zcjzss");
        put("加：公允价值变动收益（损失以“-”号填列）", "jgyjzbdsyssyhtl");
        put("投资收益（损失以“-”号填列）", "tzsyssyhtl");
        put("其中：对联营企业和合营企业的投资收益", "qzdlyqyhhyqydtzsy");
        put("资产处置收益（损失以“-”号填列）", "zcczsyssyhtl");
        put("二、营业利润（亏损以“-”号填列）", "eyylrksyhtl");
        put("加：营业外收入", "jyywsr");
        put("减：营业外支出", "jyywzc");
        put("三、利润总额（亏损总额以“-”号填列）", "slrzekszeyhtl");
        put("减：所得税费用", "jsdsfy");
        put("四、净利润（净亏损以“-”号填列）", "sjlrjksyhtl");
        put("（一）持续经营净利润（净亏损以“-”号填列）", "ycxjyjlrjksyhtl");
        put("（二）终止经营净利润（净亏损以“-”号填列）", "ezzjyjlrjksyhtl");
        put("五、其他综合收益的税后净额", "wqtzhsydshje");
        put("（一）以后不能重分类进损益的其他综合收益", "yyhbnzfljsydqtzhsy");
        put("1.重新计量设定收益计划净负债或净资产的变动", "zxjlsdsyjhjfzhjzcdbd");
        put("2.权益法下在被投资单位不能重分类进损益的其他综合收益中享有的份额", "qyfxzbtzdwbnzfljsydqtzhsyzxydfe");
        put("（二）以后将重分类进损益的其他综合收益", "eyhjzfljsydqtzhsy");
        put("1.权益法下在被投资单位以后将重分类进损益的其他综合收益中享有的份额", "qyfxzbtzdwyhjzfljsydqtzhsyzxydfe");
        put("2.可供出售金融资产公允价值变动损益", "kgcsjrzcgyjzbdsy");
        put("3.将有至到期投资重分类可供出售金融资产损益", "jyzdqtzzflkgcsjrzcsy");
        put("4.现金流经套期损益的有效部分", "xjljtqsydyxbf");
        put("5.外币财务报表折算差额", "wbcwbbzsce");
        put("六、综合收益总额", "lzhsyze");
        put("（一）基本每股收益", "yjbmgsy");
        put("（二）稀释每股收益", "exsmgsy");
    }};



    /**
     * 资产负债表对应 -- 企业会计准则 小规模
     */
    public final static Map<String, String> BANLANCE_SHEET_SMALL_MAP = new HashMap<String, String>() {{
        put("货币资金", "hbzj");
        put("短期借款", "dqjk");
        put("短期投资", "dqtz");
        put("应收票据", "yspj");
        put("应付票据", "yfpj");
        put("应付职工薪酬", "yfzgxc");
        put("应收股利", "ysgl");
        put("应交税费", "yjsf");
        put("应收利息", "yslx");
        put("应付利息", "yflx");
        put("其他应收款", "qtysk");
        put("应付利润", "yflr");
        put("存货", "ch");
        put("其他应付款", "qtyfk");
        put("其中：原材料", "qzycl");
        put("其他流动负债", "qtldfz");
        put("在产品", "zcp");
        put("流动负债合计", "ldfzhj");
        put("库存商品", "kcsp");
        put("周转材料", "zzcl");
        put("长期借款", "zqjk");
        put("其他流动资产", "qtldzc");
        put("长期应付款", "zqyfk");
        put("流动资产合计", "ldzchj");
        put("递延收益", "dysy");
        put("其他非流动负债", "qtfldfz");
        put("长期债券投资", "zqzqtz");
        put("非流动负债合计", "fldfzhj");
        put("长期股权投资", "zqgqtz");
        put("负债合计", "fzhj");
        put("固定资产原价", "gdzcyj");
        put("减：累计折旧", "jljzj");
        put("固定资产账面价值", "gdzczmjz");
        put("在建工程", "zjgc");
        put("工程物资", "gcwz");
        put("固定资产清理", "gdzcql");
        put("生产性生物资产", "scxswzc");
        put("无形资产", "wxzc");
        put("实收资本（或股本）", "sszbhgb");
        put("开发支出", "kfzc");
        put("资本公积", "zbgj");
        put("长期待摊费用", "zqdtfy");
        put("盈余公积", "yygj");
        put("其他非流动资产", "qtfldzc");
        put("未分配利润", "wfplr");
        put("非流动资产合计", "fldzchj");
        put("所有者权益（或股东权益）合计", "syzqyhgdqyhj");
        put("资产合计", "zchj");
        put("负债和所有者权益（或股东权益）总计", "fzhsyzqyhgdqyzj");

        // 重复
        put("预收账款", "ldfzyszk");
        put("应付账款", "ldfzyfzk");
        put("应收账款", "ldzcyszk");
        put("预付账款", "ldzcyfzk");
    }};


    /**
     * 利润表对应 -- 企业会计准则 小规模
     */
    public final static Map<String, String> PL_SMALL_MAP = new HashMap<String, String>() {{
        put("一、营业收入", "yyysr");
        put("减：营业成本", "jyycb");
        put("税金及附加", "sjjfj");
        put("其中：消费税", "qzxfs");
        put("营业税", "yys");
        put("城市维护建设税", "cswhjss");
        put("资源税", "zys");
        put("土地增值税", "tdzzs");
        put("城镇土地使用税、房产税、车船税、印花税", "cztdsysfcsccsyhs");
        put("教育费附加、矿产资源补偿费、排污费", "jyffjkczybcfpwf");
        put("销售费用", "xsfy");
        put("其中：商品维修费", "qzspwxf");
        put("广告费和业务宣传费", "ggfhywxcf");
        put("管理费用", "glfy");
        put("其中：开办费", "qzkbf");
        put("业务招待费", "ywzdf");
        put("研究费用", "yjfy");
        put("财务费用", "cwfy");
        put("其中：利息费用（收入以“-”号填列）", "qzlxfysryhtl");
        put("加：投资收益（损失以“-”号填列）", "jtzsyssyhtl");
        put("二、营业利润（亏损以“-”号填列）", "eyylrksyhtl");
        put("加：营业外收入", "jyywsr");
        put("其中：政府补助", "qzzfbz");
        put("减：营业外支出", "jyywzc");
        put("其中：坏账损失", "qzhzss");
        put("无法收回的长期债券投资损失", "wfshdzqzqtzss");
        put("无法收回的长期股权投资损失", "wfshdzqgqtzss");
        put("自然灾害等不可抗力因素造成的损失", "zrzhdbkklyszcdss");
        put("税收滞纳金", "ssznj");
        put("三、利润总额（亏损总额以“-”号填列）", "slrzekszeyhtl");
        put("减：所得税费用", "jsdsfy");
        put("四、净利润（净亏损以“-”号填列）", "sjlrjksyhtl");

    }};


    /**
     * 印花税合同类型
     */
    public final static List<String> SD_AGREEMENTS = new ArrayList<String>() {{
        // 经济合同
        add("101110100");
        // 购销合同
        add("101110101");
        // 建设工程勘察设计合同
        add("101110103");
        // 建筑安装工程承包合同
        add("101110104");
        // 财产租赁合同
        add("101110105");
        // 货物运输合同(按运输费用万分之五贴花)
        add("101110106");
        // 仓储保管合同
        add("101110107");
        // 借款合同
        add("101110108");
        // 财产保险合同
        add("101110109");
        // 技术合同
        add("101110110");
        // 股权转移书据（沪深交易）
        add("101110300");
        // A种股票
        add("101110301");
        // B种股票
        add("101110302");
        // 非交易转让股票A种股票
        add("101110303");
        // 无偿划拨国有股权的股数
        add("101110305");
        // 权利、许可证照
        add("101110400");
        // 营业帐簿
        add("101110500");
        // 其他营业账簿
        add("101110599");
        // 其他凭证
        add("101119800");
        // 滞纳金、罚款
        add("101119900");
        // 滞纳金
        add("101119901");
        // 罚款
        add("101119902");
        // 加工承揽合同
        add("101110102");
        // 产权转移书据
        add("101110200");
        // 资金账簿
        add("101110501");
        // 非交易转让股票B种股票
        add("101110304");
        // 社保基金证券交易A种股票印花税
        add("101110306");
        // 社保基金证券交易B种股票印花税
        add("101110307");
        // 利息收入
        add("101119903");
    }};

}
