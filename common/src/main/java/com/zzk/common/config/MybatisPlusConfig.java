package com.zzk.common.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 统一 Mapper 扫描入口，避免每个模块重复配置。
 */
@Configuration
@MapperScan(basePackages = {
        "com.zzk.authmodule.mapper",
        "com.zzk.usermodule.mapper",
        "com.zzk.videomodule.mapper",
        "com.zzk.interactionmodule.mapper",
        "com.zzk.messagemodule.mapper",
        "com.zzk.auditmodule.mapper",
        "com.zzk.recommendmodule.mapper",
        "com.zzk.adminmodule.mapper"
})
public class MybatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return interceptor;
    }
}
