package com.zzk.common.config;

import org.mybatis.spring.annotation.MapperScan;
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
}
