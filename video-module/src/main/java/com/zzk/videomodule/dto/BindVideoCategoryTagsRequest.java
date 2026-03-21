package com.zzk.videomodule.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
@Schema(description = "视频分类标签绑定请求")
public class BindVideoCategoryTagsRequest {

    @NotNull
    @Schema(description = "主分区 ID")
    private Long primaryCategoryId;

    @Schema(description = "标签 ID 列表")
    private List<Long> tagIds = new ArrayList<>();
}
