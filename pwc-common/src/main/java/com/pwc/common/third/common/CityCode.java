package com.pwc.common.third.common;

/**
 * @author zk
 */
public enum CityCode {
    /**
     * 北京市
     */
    BEIJING("11"),
    /**
     * 天津市
     */
    TIANJIN("12"),
    /**
     * 河北省
     */
    HEBEI("13"),
    /**
     * 山西省
     */
    SHANXI("14"),
    /**
     * 内蒙古区
     */
    NEIMENGGU("15"),
    /**
     * 辽宁省
     */
    LIAONING("21"),
    /**
     * 吉林省
     */
    JILIN("22"),
    /**
     * 黑龙江省
     */
    HEILONGJIANG("23"),
    /**
     * 上海市
     */
    SHANGHAI("31"),
    /**
     * 江苏省
     */
    JIANGSU("32"),
    /**
     * 浙江省
     */
    ZHEJIANG("33"),
    /**
     * 安徽省
     */
    ANHUI("34"),
    /**
     * 福建省
     */
    FUJIAN("35"),
    /**
     * 江西省
     */
    JIANGXI("36"),
    /**
     * 山东省
     */
    SHANDONG("37"),
    /**
     * 河南省
     */
    HENAN("41"),
    /**
     * 湖北省
     */
    HUBEI("42"),
    /**
     * 湖南省
     */
    HUNAN("43"),
    /**
     * 广东省
     */
    GUANGDONG("44"),
    /**
     * 广西省
     */
    GUANGXI("45"),
    /**
     * 海南省
     */
    HAINAN("46"),
    /**
     * 重庆市
     */
    CHONGQING("50"),
    /**
     * 四川省
     */
    SICHUAN("51"),
    /**
     * 贵州省
     */
    GUIZHOU("52"),
    /**
     * 云南省
     */
    YUNNAN("53"),
    /**
     * 西藏区
     */
    XIZANG("54"),
    /**
     * 陕西省
     */
    SHANXIS("61"),
    /**
     * 甘肃省
     */
    GANSU("62"),
    /**
     * 青海省
     */
    QINGHAI("63"),
    /**
     * 宁夏区
     */
    NIGNXIA("64"),
    /**
     * 新疆区
     */
    XINJIANG("65"),
    /**
     * 大连市
     */
    DALIAN("2102"),
    /**
     * 宁波市
     */
    NINGBO("3302"),
    /**
     * 厦门市
     */
    XIAMEN("3502"),
    /**
     * 青岛市
     */
    QINGDAO("3702"),
    /**
     * 深圳市
     */
    SHENZHEN("4403"),
    ;
    private String value;

    CityCode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

