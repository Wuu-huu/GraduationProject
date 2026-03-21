package com.zzk.videomodule.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "视频状态更新请求")
public class UpdateVideoStatusRequest {

    @NotNull
    @Schema(description = "发布状态 0草稿 1已发布 2已下架", example = "2")
    private Integer publishStatus;
}
