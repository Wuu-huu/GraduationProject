package com.zzk.auditmodule.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "封禁记录")
public class BanRecordVO {

    private final Long id;
    private final Long uid;
    private final Integer banType;
    private final Date startTime;
    private final Date endTime;
    private final String reason;
    private final Long operatorUid;
    private final Integer status;
    private final Date createTime;
}
