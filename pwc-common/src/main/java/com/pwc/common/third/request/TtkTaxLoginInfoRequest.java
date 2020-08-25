package com.pwc.common.third.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * 上传网报账号信息请求体
 * @author zk
 */
@Data
public class TtkTaxLoginInfoRequest {

    /**
     * 企业ID（通过创建企业接口createOrg获得）
     */
    private Long id;

    /**
     * 登录信息结构体
     */
    private TaxLoginInfo dlxxDto;

    /**
     * true
     */
    @JSONField(name = "isReturnValue")
    private boolean isReturnValue;


}

