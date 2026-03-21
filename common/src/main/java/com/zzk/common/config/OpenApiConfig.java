package com.zzk.common.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI 文档基础信息配置。
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI graduationProjectOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("类哔哩哔哩视频平台 API")
                        .description("模块化单体后端接口文档")
                        .version("v1.0.0")
                        .license(new License().name("Internal Use")))
                .externalDocs(new ExternalDocumentation().description("Project README"));
    }
}
