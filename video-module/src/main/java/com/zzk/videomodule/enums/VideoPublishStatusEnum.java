package com.zzk.videomodule.enums;

import lombok.Getter;

@Getter
public enum VideoPublishStatusEnum {

    DRAFT(0),
    PUBLISHED(1),
    OFFLINE(2);

    private final int code;

    VideoPublishStatusEnum(int code) {
        this.code = code;
    }
}
