package com.zzk.common.model.security;

import com.zzk.common.enums.UserRoleEnum;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 登录用户上下文对象，存放在 SecurityContext 中。
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginUser implements Serializable {

    private Long uid;
    private String username;
    private Integer role;

    public boolean isAdmin() {
        return UserRoleEnum.fromCode(role).isAdmin();
    }
}
