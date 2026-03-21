package com.zzk.common.constant;

import java.util.List;

/**
 * 安全模块使用到的请求头、JWT claim 和白名单常量。
 */
public final class SecurityConstants {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String CLAIM_UID = "uid";
    public static final String CLAIM_ROLE = "role";
    public static final String CLAIM_USERNAME = "username";
    public static final List<String> DEFAULT_WHITE_LIST = List.of(
            "/api/auth/register",
            "/api/auth/login",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/error"
    );

    private SecurityConstants() {
    }
}
