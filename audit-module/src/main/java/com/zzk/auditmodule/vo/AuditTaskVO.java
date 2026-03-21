package com.zzk.auditmodule.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "审核任务")
public class AuditTaskVO {

    private final Long auditId;
    private final Integer bizType;
    private final Long bizId;
    private final Integer auditStatus;
    private final String reason;
    private final Long auditorUid;
    private final Date submitTime;
    private final Date auditTime;
}
