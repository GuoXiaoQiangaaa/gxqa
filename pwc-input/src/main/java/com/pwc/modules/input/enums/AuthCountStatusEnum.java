package com.pwc.modules.input.enums;

/**
 * 认证统计状态枚举类
 * 0:未统计; 1:申请统计中; 2:申请统计成功; 3:申请统计失败; 4:确认统计中; 5:确认统计成功; 6:确认统计失败; 7:统计撤销中
 * @author fanpf
 * @date 2020/9/10
 */
public enum AuthCountStatusEnum {
    NOTCOUNT("0", "未统计"),
    APPLYING("1", "申请统计中"),
    APPLYSUCCESS("2", "申请统计成功"),
    APPLYFAILED("3", "申请统计失败"),
    CONFIRMING("4", "确认统计中"),
    CONFIRMSUCCESS("5", "确认统计成功"),
    CONFIRMFAILED("6", "确认统计失败"),
    CANCELLING("7", "统计撤销中"),
    ;

    private String key;
    private String value;

    AuthCountStatusEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }}
