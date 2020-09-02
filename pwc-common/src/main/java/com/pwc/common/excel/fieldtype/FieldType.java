package com.pwc.common.excel.fieldtype;

import cn.hutool.core.util.StrUtil;

public class FieldType {
    /**
     * 获取对象值（导入）
     */
    public static Object getValue(String val) {
        if (StrUtil.isNotBlank(val)) {
            return val;
        }
        return null;
    }

    /**
     * 获取对象值（导出）
     */
    public static String setValue(Object val) {
        if (val != null){
            return val.toString();
        }
        return "";
    }
}
