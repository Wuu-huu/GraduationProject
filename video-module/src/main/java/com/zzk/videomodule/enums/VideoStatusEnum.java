package com.zzk.videomodule.enums;

import lombok.Getter;

@Getter
public enum VideoStatusEnum {

    DELETED(0),
    NORMAL(1);

    private final int code;

    VideoStatusEnum(int code) {
        this.code = code;
    }
}
