package com.zzk.usermodule.enums;

import lombok.Getter;

/**
 * 关注关系状态枚举，映射 user_follow.state。
 */
@Getter
public enum UserFollowStateEnum {

    UNFOLLOWED(0, "未关注"),
    FOLLOWING(1, "已关注");

    private final int code;
    private final String desc;

    UserFollowStateEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
