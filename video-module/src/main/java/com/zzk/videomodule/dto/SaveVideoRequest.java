package com.zzk.videomodule.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
@Schema(description = "视频发布或草稿保存请求")
public class SaveVideoRequest {

    @NotBlank
    @Size(max = 200)
    @Schema(description = "视频标题")
    private String title;

    @Size(max = 200)
    @Schema(description = "副标题")
    private String subtitle;

    @NotNull
    @Schema(description = "来源类型")
    private Integer sourceType;

    @NotNull
    @Schema(description = "版权类型")
    private Integer copyrightType;

    @Size(max = 500)
    @Schema(description = "封面地址")
    private String coverUrl;

    @Size(max = 5000)
    @Schema(description = "视频简介")
    private String description;

    @NotNull
    @Schema(description = "可见性，0私密 1公开", example = "1")
    private Integer visibility;

    @Min(0)
    @Schema(description = "总时长，单位秒", example = "300")
    private Integer durationSec;

    @Schema(description = "质量评分")
    private BigDecimal qualityScore;

    @Schema(description = "主分区 ID")
    private Long primaryCategoryId;

    @Schema(description = "标签 ID 列表")
    private List<Long> tagIds = new ArrayList<>();

    @Valid
    @Schema(description = "分P列表")
    private List<VideoPartUpsertRequest> parts = new ArrayList<>();
}
