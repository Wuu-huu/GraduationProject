package com.zzk.messagemodule.enums;

import lombok.Getter;

@Getter
public enum ConversationTypeEnum {

    PRIVATE(1);

    private final int code;

    ConversationTypeEnum(int code) {
        this.code = code;
    }
}
