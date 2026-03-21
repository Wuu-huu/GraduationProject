package com.zzk.interactionmodule.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "收藏夹新增或编辑请求")
public class SaveFavoriteFolderRequest {

    @NotNull
    @Schema(description = "收藏夹类型")
    private Integer folderType;

    @NotBlank
    @Size(max = 100)
    @Schema(description = "收藏夹标题")
    private String title;

    @Size(max = 500)
    @Schema(description = "收藏夹描述")
    private String description;

    @Size(max = 500)
    @Schema(description = "封面地址")
    private String coverUrl;

    @NotNull
    @Schema(description = "可见性 0私密 1公开")
    private Integer visible;

    @Schema(description = "排序号")
    private Integer sortNo;
}
