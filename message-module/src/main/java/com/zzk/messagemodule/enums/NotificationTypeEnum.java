package com.zzk.messagemodule.enums;

import lombok.Getter;

@Getter
public enum NotificationTypeEnum {

    SYSTEM(1),
    INTERACTION(2),
    AUDIT(3);

    private final int code;

    NotificationTypeEnum(int code) {
        this.code = code;
    }
}
