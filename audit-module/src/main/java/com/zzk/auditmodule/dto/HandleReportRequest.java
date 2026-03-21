package com.zzk.auditmodule.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "举报处理请求")
public class HandleReportRequest {

    @NotNull
    @Schema(description = "处理状态", example = "1")
    private Integer status;

    @Size(max = 255)
    private String reason;
}
