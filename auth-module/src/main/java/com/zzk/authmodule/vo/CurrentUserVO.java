package com.zzk.authmodule.vo;

import lombok.Builder;
import lombok.Getter;

/**
 * 当前登录用户信息响应体。
 */
@Getter
@Builder
public class CurrentUserVO {

    private final Long uid;
    private final String username;
    private final Integer role;
    private final Integer state;
}
