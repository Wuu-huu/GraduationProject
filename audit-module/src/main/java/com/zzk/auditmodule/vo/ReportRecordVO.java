package com.zzk.auditmodule.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "举报记录")
public class ReportRecordVO {

    private final Long reportId;
    private final Long reporterUid;
    private final Integer targetType;
    private final Long targetId;
    private final Integer reasonType;
    private final String reasonText;
    private final Integer status;
    private final Date createTime;
    private final Date handleTime;
}
