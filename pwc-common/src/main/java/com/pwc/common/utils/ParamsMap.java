package com.pwc.common.utils;

import java.util.Map;

/**
 * @description: 获取map里面的字段
 * @author: Gxw
 * @create: 2020-09-02 16:09
 **/
public class ParamsMap {

    /**
     * 防止获取字段值是不在map中，报空指针
     * @param map
     * @param str
     * @return
     */
    public static String findMap(Map map, String str ){
        String val =null;
        if(map.containsKey(str)){
            val =map.get(str).toString();
        }
        return val;
    }
}
