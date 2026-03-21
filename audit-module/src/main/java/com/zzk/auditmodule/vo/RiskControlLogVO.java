package com.zzk.auditmodule.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "风控日志")
public class RiskControlLogVO {

    private final Long id;
    private final Long uid;
    private final String riskType;
    private final Integer bizType;
    private final Long bizId;
    private final String detail;
    private final Integer riskLevel;
    private final Date createTime;
}
