package com.zzk.interactionmodule.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "评论或回复请求")
public class CreateCommentRequest {

    @Schema(description = "视频 ID")
    private Long vid;

    @Schema(description = "根评论 ID，一级评论为空")
    private Long rootId;

    @Schema(description = "父评论 ID，一级评论为空")
    private Long parentId;

    @Schema(description = "被回复用户 ID")
    private Long replyToUid;

    @NotBlank
    @Size(max = 1000)
    @Schema(description = "评论内容")
    private String content;
}
