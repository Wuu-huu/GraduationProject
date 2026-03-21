package com.zzk.authmodule.vo;

import lombok.Builder;
import lombok.Getter;

/**
 * 登录成功后的认证结果。
 */
@Getter
@Builder
public class LoginVO {

    private final String accessToken;
    private final Long expiresIn;
    private final Long uid;
    private final String username;
    private final Integer role;
}
