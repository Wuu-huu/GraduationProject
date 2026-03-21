package com.zzk.common.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * JWT 配置项，对应 application.yml 中的 app.jwt 前缀。
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "app.jwt")
public class JwtProperties {

    private String secret = "change-me-to-a-long-secret-key-for-jwt-token";
    private long accessTokenExpireSeconds = 86400;
    private String issuer = "graduation-project";
}
