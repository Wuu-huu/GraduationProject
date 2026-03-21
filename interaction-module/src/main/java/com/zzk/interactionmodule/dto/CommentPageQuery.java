package com.zzk.interactionmodule.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
@Schema(description = "评论分页查询")
public class CommentPageQuery {

    @Min(1)
    @Schema(description = "页码", example = "1")
    private Long pageNum = 1L;

    @Min(1)
    @Schema(description = "每页数量", example = "10")
    private Long pageSize = 10L;
}
