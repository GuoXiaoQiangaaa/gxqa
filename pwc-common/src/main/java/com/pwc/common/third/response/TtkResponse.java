package com.pwc.common.third.response;

import lombok.Data;
import lombok.ToString;

/**
 * @author zk
 */
@Data
@ToString
public class TtkResponse<T> {
    /**
     * 返回头信息
     */
    private ResponseHead head;

    /**
     * 返回请求内容
     */
    private T body;

//    @Override
//    public String toString(){
//        String bodyStr = "null";
//        if (body instanceof String) {
//            if (StringUtils.isNotBlank((String)body)) {
//                bodyStr = body.toString();
//            }
//        } else {
//            bodyStr = null == body ? null :  body.toString();
//        }
//        return "[ head: "+head.toString()+"," +
//                " body: "+ bodyStr +" ]";
//    }
}


