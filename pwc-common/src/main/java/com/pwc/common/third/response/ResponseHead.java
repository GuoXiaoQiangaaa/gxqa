package com.pwc.common.third.response;

import lombok.Data;
import lombok.ToString;

/**
 * @author zk
 */
@Data
@ToString
public class ResponseHead {

    /**
     * 错误码 0成功
     */
    private String errorCode;

    /**
     * 返回描述信息
     */
    private String errorMsg;

    /**
     * 时间戳
     */
    private Long timestamp;

    /**
     * time
     */
    private Long time;
}
