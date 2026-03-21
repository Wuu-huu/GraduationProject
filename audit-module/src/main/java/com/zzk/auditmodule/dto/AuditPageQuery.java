package com.zzk.auditmodule.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
@Schema(description = "审核分页查询")
public class AuditPageQuery {

    @Min(1)
    private Long pageNum = 1L;

    @Min(1)
    private Long pageSize = 10L;

    @Schema(description = "审核状态")
    private Integer auditStatus;
}
