package com.zzk.videomodule.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
@Schema(description = "视频列表分页查询")
public class VideoQueryRequest {

    @Min(1)
    @Schema(description = "页码", example = "1")
    private Long pageNum = 1L;

    @Min(1)
    @Schema(description = "每页数量", example = "10")
    private Long pageSize = 10L;

    @Schema(description = "排序类型 latest/hot", example = "latest")
    private String orderType;
}
