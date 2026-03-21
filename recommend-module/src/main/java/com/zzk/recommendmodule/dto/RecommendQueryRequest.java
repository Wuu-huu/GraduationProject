package com.zzk.recommendmodule.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
@Schema(description = "推荐分页查询参数")
public class RecommendQueryRequest {

    @Min(1)
    @Schema(description = "页码", example = "1")
    private Long pageNum = 1L;

    @Min(1)
    @Max(30)
    @Schema(description = "每页数量", example = "12")
    private Long pageSize = 12L;
}