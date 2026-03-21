package com.zzk.auditmodule.enums;

import lombok.Getter;

@Getter
public enum AuditStatusEnum {

    PENDING(0),
    APPROVED(1),
    REJECTED(2);

    private final int code;

    AuditStatusEnum(int code) {
        this.code = code;
    }
}
