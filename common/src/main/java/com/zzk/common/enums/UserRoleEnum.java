package com.zzk.common.enums;

import lombok.Getter;

/**
 * 用户角色枚举，当前阶段直接映射 user_info.role。
 */
@Getter
public enum UserRoleEnum {

    USER(0, "普通用户"),
    ADMIN(1, "管理员"),
    SUPER_ADMIN(2, "超级管理员");

    private final int code;
    private final String desc;

    UserRoleEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static UserRoleEnum fromCode(Integer code) {
        if (code == null) {
            return USER;
        }
        for (UserRoleEnum value : values()) {
            if (value.code == code) {
                return value;
            }
        }
        return USER;
    }

    public boolean isAdmin() {
        return this == ADMIN || this == SUPER_ADMIN;
    }
}
