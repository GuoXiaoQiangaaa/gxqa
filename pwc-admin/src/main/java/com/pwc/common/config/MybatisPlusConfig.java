

package com.pwc.common.config;

import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.pwc.MyMetaObjectHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mybatis-plus配置
 *
 * @author
 */
@Configuration
public class MybatisPlusConfig {

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    @Bean
    public ISqlInjector sqlInjector() {
        return new LogicSqlInjector();
    }

    @Bean
    public MyMetaObjectHandler metaObjectHandler() {return new MyMetaObjectHandler();}
}
