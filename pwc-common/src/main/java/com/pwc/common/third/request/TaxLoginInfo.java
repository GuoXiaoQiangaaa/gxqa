package com.pwc.common.third.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * 登录信息结构体
 * @author zk
 */
@Data
public class TaxLoginInfo {
    /**
     * 登录方式(1代表CA证书登录 2代表用户名密码登录 3代表实名制登录 4代表证件登录 5代表手机号密码登录 6代表手机号验证码登录)
     */
    @JSONField(name = "DLFS")
    private String DLFS;

    /**
     * 纳税人识别号（社会信用代码）
     */
    @JSONField(name = "NSRSBH")
    private String NSRSBH;

    /**
     * 企业名称
     */
    @JSONField(name = "QYMC")
    private String QYMC;

    /**
     * 省市代码参考
     */
    @JSONField(name = "SS")
    private String SS;

    /**
     * 是否可以自动修改 纳税人性质
     */
    @JSONField(name = "canChange")
    private boolean canChange;

    /**
     * 网报账号
     */
    @JSONField(name = "DLZH")
    private String DLZH;

    /**
     * 网报密码 （sdk会自动加密传输）
     */
    @JSONField(name = "DLMM")
    private String DLMM;

    /**
     * 个税申报密码
     */
    @JSONField(name = "gssbmm")
    private String gssbmm;
}
