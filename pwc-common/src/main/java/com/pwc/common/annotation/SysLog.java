package com.pwc.common.annotation;

import java.lang.annotation.*;

/**
 * 系统日志注解
 *
 * @author
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {

	String value() default "";
}
