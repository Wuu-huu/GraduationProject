package com.zzk.videomodule.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
@Schema(description = "合集新增或编辑请求")
public class SaveVideoSeriesRequest {

    @NotBlank
    @Size(max = 100)
    @Schema(description = "合集标题")
    private String title;

    @Size(max = 500)
    @Schema(description = "合集描述")
    private String description;

    @Size(max = 500)
    @Schema(description = "封面地址")
    private String coverUrl;

    @NotNull
    @Schema(description = "是否公开 0私密 1公开", example = "1")
    private Integer visible;

    @Schema(description = "合集内视频 ID 列表")
    private List<Long> videoIds = new ArrayList<>();
}
