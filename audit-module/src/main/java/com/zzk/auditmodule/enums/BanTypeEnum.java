package com.zzk.auditmodule.enums;

import lombok.Getter;

@Getter
public enum BanTypeEnum {

    BAN(1),
    MUTE(2);

    private final int code;

    BanTypeEnum(int code) {
        this.code = code;
    }
}
