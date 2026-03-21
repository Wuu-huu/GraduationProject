package com.zzk.interactionmodule.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "收藏视频请求")
public class FavoriteVideoRequest {

    @NotNull
    @Schema(description = "收藏夹 ID")
    private Long favoriteFolderId;
}
