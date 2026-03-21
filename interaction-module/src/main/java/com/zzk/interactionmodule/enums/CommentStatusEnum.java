package com.zzk.interactionmodule.enums;

import lombok.Getter;

@Getter
public enum CommentStatusEnum {

    DELETED(0),
    NORMAL(1);

    private final int code;

    CommentStatusEnum(int code) {
        this.code = code;
    }
}
