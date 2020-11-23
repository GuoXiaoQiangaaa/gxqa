package com.pwc.common.annotation;

import java.lang.annotation.*;

/**
 * 接口数据日志表 sys_api_log
 *
 * @author zlb
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiLogFilter {
    /**
     * 分类：1发票传入接口，2发票入账接口，3发票查验接口，4发票勾选接口，5海关缴款书同步，6海关缴款书勾选，7统计接口
     */
    String type() default "";

}

