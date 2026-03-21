package com.zzk.recommendmodule.enums;

import lombok.Getter;

@Getter
public enum BehaviorObjectTypeEnum {

    VIDEO(1),
    COMMENT(2),
    SEARCH(3);

    private final int code;

    BehaviorObjectTypeEnum(int code) {
        this.code = code;
    }
}
