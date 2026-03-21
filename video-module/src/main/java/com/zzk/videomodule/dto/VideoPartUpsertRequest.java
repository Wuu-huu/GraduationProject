package com.zzk.videomodule.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "分P新增或编辑请求")
public class VideoPartUpsertRequest {

    @Schema(description = "分P ID，新增时为空")
    private Long partId;

    @NotNull
    @Min(1)
    @JsonAlias("sortNo")
    @Schema(description = "分P序号", example = "1")
    private Integer partNo;

    @NotBlank
    @Size(max = 100)
    @Schema(description = "分P标题")
    private String title;

    @NotBlank
    @Size(max = 500)
    @Schema(description = "视频资源地址")
    private String videoUrl;

    @NotNull
    @Min(0)
    @Schema(description = "时长，单位秒", example = "300")
    private Integer durationSec;

    @Min(0)
    @Schema(description = "文件大小，单位字节", example = "1024")
    private Long sizeBytes;
}