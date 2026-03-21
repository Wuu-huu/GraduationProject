package com.zzk.videomodule.enums;

import lombok.Getter;

@Getter
public enum VideoVisibilityEnum {

    PRIVATE(0),
    PUBLIC(1);

    private final int code;

    VideoVisibilityEnum(int code) {
        this.code = code;
    }
}
