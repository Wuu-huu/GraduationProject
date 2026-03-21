package com.zzk.interactionmodule.enums;

import lombok.Getter;

@Getter
public enum VideoActionTypeEnum {

    LIKE(1),
    DISLIKE(2),
    COIN(3),
    FAVORITE(4),
    WATCH_LATER(5),
    COMMENT(6),
    COMMENT_LIKE(7),
    DANMU(8);

    private final int code;

    VideoActionTypeEnum(int code) {
        this.code = code;
    }
}
