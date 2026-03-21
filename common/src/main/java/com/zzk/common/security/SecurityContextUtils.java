package com.zzk.common.security;

import com.zzk.common.exception.UnauthorizedException;
import com.zzk.common.model.security.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 安全上下文工具，屏蔽从 SecurityContext 取当前用户的细节。
 */
public final class SecurityContextUtils {

    private SecurityContextUtils() {
    }

    public static LoginUser getLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof LoginUser loginUser)) {
            throw new UnauthorizedException("当前用户未登录");
        }
        return loginUser;
    }

    public static Long getCurrentUserId() {
        return getLoginUser().getUid();
    }
}
