package com.zzk.interactionmodule.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "发送弹幕请求")
public class CreateDanmuRequest {

    @Schema(description = "视频 ID")
    private Long vid;

    @Schema(description = "分P ID")
    private Long partId;

    @NotBlank
    @Size(max = 200)
    @Schema(description = "弹幕内容")
    private String content;

    @Size(max = 20)
    @Schema(description = "弹幕颜色", example = "#FFFFFF")
    private String color;

    @Schema(description = "字体大小", example = "25")
    private Integer fontSize;

    @Schema(description = "弹幕模式", example = "1")
    private Integer mode;

    @NotNull
    @Min(0)
    @Schema(description = "时间点，单位毫秒", example = "3000")
    private Integer timePointMs;
}
