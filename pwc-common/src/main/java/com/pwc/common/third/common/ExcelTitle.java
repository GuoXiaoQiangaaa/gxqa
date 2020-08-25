package com.pwc.common.third.common;

/**
 * 导入Excel表头对应
 * @author zk
 */
public class ExcelTitle {

    /**
     * 增值税对应 读取
     */
    public enum VatRead {
        JXMX_JXZYFPFS("进项明细-进项专用发票份数"),
        JXMX_JXXSE("进项明细-进项销售额"),
        JXMX_JXSE("进项明细-进项税额"),
        JXMX_JXZCSE_HZZYFP("进项明细-进项转出税额（红字专用发票）"),
        JXMX_JXZCSE_FZCSS("进项明细-进项转出税额（非正常损失）"),
        JXMX_JXZCSE_JTFLGRXF("进项明细-进项转出税额（集体福利个人消费）"),
        JXMX_JXZCSE_QT("进项明细-进项转出税额（其他，如有请注明项目）"),
        JXMX_ZZSSKXTZYSBFJJSWHF("进项明细-增值税税控系统专用设备费及技术维护费"),
        JXMX_FHBDQXGMJZ("进项明细-非湖北地区小规模减征"),
        JXMX_LKYSSL("进项明细-旅客运输数量"),
        JXMX_LKYSJE("进项明细-旅客运输金额"),
        JXMX_LKYSSE("进项明细-旅客运输税额"),
        JXHJ_JXXSE("进项合计-进项销售额"),
        JXHJ_JXSE("进项合计-进项税额"),
        XXMX_ZYFPFS("销项明细-专用发票份数"),
        XXMX_PTFPFS("销项明细-普通发票份数"),
        XXMX_QTFPFS("销项明细-其他发票份数"),
        XXMX_XSJE_ZP("销项明细-销售金额（专票）"),
        XXMX_ZZSSE_ZP("销项明细-增值税税额（专票）"),
        // 不同模版对应同一个值
        XXMX_XSJE_PP("销项明细-销售金额（普票）"),
        XXMX_XSJE_PP_13("销项明细-13%销售金额（普票）"),
        // 同上
        XXMX_ZZSSE_PP("销项明细-增值税税额（普票）"),
        XXMX_ZZSSE_PP_13("销项明细-13%增值税税额（普票）"),

        XXMX_XSJE16_QTFP("销项明细-16%销售金额（其他发票）"),
        XXMX_ZZSSE16_QTFP("销项明细-16%增值税税额（其他发票）"),
        XXMX_XSJE17_ZP("销项明细-17%销售金额（专票）"),
        XXMX_ZZSSE17_ZP("销项明细-17%增值税税额（专票）"),
        XXMX_XSJE17_PP("销项明细-17%销售金额（普票）"),
        XXMX_ZZSSE17_PP("销项明细-17%增值税税额（普票）"),
        XXMX_XSJE17_QTFP("销项明细-17%销售金额（其他发票）"),
        XXMX_ZZSSE17_QTFP("销项明细-17%增值税税额（其他发票）"),
        XXMX_ZZSPTFPJE0_CK("销项明细-0%增值税普通发票金额（出口）"),
        XXMX_ZZSPTFPSE("销项明细-0%增值税普通发票税额"),
        XXMX_WKPJE3("销项明细-未开票金额3%"),
        XXMX_WKPSE3("销项明细-未开票税额3%"),
        XXMX_WKPJE("销项明细-未开票金额"),
        XXMX_WKPSE("销项明细-未开票税额"),
        XXHJ_XXXSE("销项合计-销项销售额"),
        XXHJ_XXSE("销项合计-销项税额"),
        BYYJZZS_BYYJZZS("本月应交增值税-本月应交增值税"),
        BYYJZZS_BYZMYJZZS("本月应交增值税-本月账面应交增值税"),
        BYYJZZS_SQYNSE_LDWFS("本月应交增值税-上期应纳税额（留抵为负数）"),
        ;
        private String value;

        VatRead(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 增值税对应 写
     */
    public enum VatWrite {
        JXMX_JXZYFPFS("jxmxJxzyfpfs", "进项专用发票份数"),
        JXMX_JXXSE("jxmxJxxse", "进项销售额"),
        JXMX_JXSE("jxmxJxse", "进项税额"),
        JXMX_JXZCSE_HZZYFP("jxmxJxzcseHzzyfp", "进项转出税额（红字专用发票）"),
        JXMX_JXZCSE_FZCSS("jxmxJxzcseFzcss", "进项转出税额（非正常损失）"),
        JXMX_JXZCSE_JTFLGRXF("jxmxJxzcseJtflgrxf", "进项转出税额（集体福利个人消费）"),
        JXMX_JXZCSE_QT("jxmxJxzcseQt", "进项转出税额（其他）"),
        JXMX_ZZSSKXTZYSBFJJSWHF("jxmxZzsskxtzysbfjjswhf", "增值税税控系统专用设备费及技术维护费"),
        JXMX_LKYSSL("jxmxLkyssl", "旅客运输数量"),
        JXMX_LKYSJE("jxmxLkysje", "旅客运输金额"),
        JXMX_LKYSSE("jxmxLkysse", "旅客运输税额"),
        JXHJ_JXXSE("jxhjJxxse", "进项销售额"),
        JXHJ_JXSE("jxhjJxse", "进项税额"),
        XXMX_ZYFPFS("xxmxZyfpfs", "专用发票分数"),
        XXMX_PTFPFS("xxmxPtfpfs", "普通发票分数"),
        XXMX_QTFPFS("xxmxQtfpfs", "其他发票分数"),
        XXMX_XSJE_ZP("xxmxXsjeZp", "销售金额（专票）"),
        XXMX_ZZSSE_ZP("xxmxZzsseZp", "增值税税额（专票）"),
        XXMX_XSJE_PP("xxmxXsjePp", "销售金额（普票）"),
        XXMX_ZZSSE_PP("xxmxZzssePp", "增值税税额（普票）"),
        XXMX_XSJE16_QTFP("xxmxXsje16Qtfp", "16%销售金额（其他发票）"),
        XXMX_ZZSSE16_QTFP("xxmxZzsse16Qtfp", "16%增值税税额（其他发票）"),
        XXMX_XSJE17_ZP("xxmxXsje17Zp", "17%销售金额（专票）"),
        XXMX_ZZSSE17_ZP("xxmxZzsse17Zp", "17%增值税税额（专票）"),
        XXMX_XSJE17_PP("xxmxXsje17Pp", "17%销售金额（普票）"),
        XXMX_ZZSSE17_PP("xxmxZzsse17Pp", "17%增值税税额（普票）"),
        XXMX_XSJE17_QTFP("xxmxXsje17Qtfp", "17%销售金额（其他发票）"),
        XXMX_ZZSSE17_QTFP("xxmxZzsse17Qtfp", "17%增值税税额（其他发票）"),
        XXMX_ZZSPTFPJE0_CK("xxmxZzsptfpje0Ck", "0%增值税普通发票金额（出口）"),
        XXMX_ZZSPTFPSE("xxmxZzsptfpse0", "0%增值税普通发票税额"),
        XXMX_WKPJE3("xxmxWkpje3", "未开票金额3%"),
        XXMX_WKPSE3("xxmxWkpse3", "未开票税额3%"),
        XXMX_WKPJE("xxmxWkpje", "未开票金额"),
        XXMX_WKPSE("xxmxWkpse", "未开票税额"),
        XXHJ_XXXSE("xxhjXxxse", "销项销售额"),
        XXHJ_XXSE("xxhjXxse", "销项税额"),
        BYYJZZS_BYYJZZS("byyjzzsByyjzzs", "本月应交增值税"),
        BYYJZZS_BYZMYJZZS("byyjzzsByzmyjzzs", "本月账面应交增值税"),
        BYYJZZS_SQYNSE_LDWFS("byyjzzsSqynseLdwfs","上期应纳税额（留底为负数）"),
        STORES_EIN("storesEin", "门店税号"),
        STORES_NO("storesNo", "门店号"),
        PROVINCE("province", "所在省份"),
        CITY("city", "所在城市"),
        COMPANY_NAME("companyName", "公司店名（简称）"),
        TAX_RATE("taxRate", "税率"),
        ;
        private String value;
        private String name;

        VatWrite(String name ,String value) {
            this.value = value;
            this.name = name;
        }
        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }
    }


    /**
     * 印花税对应 读取
     */
    public enum SDRead {
        DSJSYJ_YHSJSYJGXHTJE("地税计税依据-印花税计税依据（购销合同）金额"),
        DSJSYJ_YHSJSYJCCZLHTJE("地税计税依据-印花税计税依据（财产租赁合同）金额"),
        DSJSYJ_GRJFRS("地税计税依据-工人经费人数"),
        DSJSYJ_GHJFGZ("地税计税依据-工会经费（工资）"),
        DSJSYJ_CBJRS("地税计税依据-残保金人数"),
        DSJSYJ_CBJJSYJGZD("地税计税依据-残保金计税依据（工资等）"),
        ;
        private String value;

        SDRead(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 企业所得税
     */
    public enum CITRead {
        QYSDS_YYLFESJZZCDZMYSBSJDCY("企业所得税-由于六费二税减征造成的账面与申报数据的差异"),
        QYSDS_SNDZGXC("企业所得税-上年度职工薪酬"),
        QYSDS_SNDZCZE("企业所得税-上年度资产总额"),
        QYSDS_CYRS("企业所得税-从业人数"),
        QYSDS_FTBL("企业所得税-分摊比例"),
        QYSDS_BJDFTSE("企业所得税-本季度分摊税额"),
        ;

        private String value;

        CITRead(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 门店基础信息
     */
    public enum StoresInformation {

        STORES_EIN("基础信息-门店税号"),
        STORES_NO("基础信息-门店号"),
        PROVINCE("基础信息-所在省份"),
        CITY("基础信息-所在城市"),
        COMPANY_NAME("基础信息-公司店名（简称）"),
        TAX_RATE("基础信息-税率"),
        ;
        private String value;

        StoresInformation(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

}