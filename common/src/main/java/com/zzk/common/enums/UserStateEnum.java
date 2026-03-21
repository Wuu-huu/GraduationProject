package com.zzk.common.enums;

import lombok.Getter;

/**
 * 用户状态枚举，当前阶段直接映射 user_info.state。
 */
@Getter
public enum UserStateEnum {

    NORMAL(0, "正常"),
    BANNED(1, "封禁"),
    CANCELED(2, "注销");

    private final int code;
    private final String desc;

    UserStateEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static UserStateEnum fromCode(Integer code) {
        if (code == null) {
            return NORMAL;
        }
        for (UserStateEnum value : values()) {
            if (value.code == code) {
                return value;
            }
        }
        return NORMAL;
    }
}
