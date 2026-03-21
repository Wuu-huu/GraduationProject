package com.zzk.auditmodule.enums;

import lombok.Getter;

@Getter
public enum AuditBizTypeEnum {

    VIDEO(1),
    COMMENT(2);

    private final int code;

    AuditBizTypeEnum(int code) {
        this.code = code;
    }
}
