package com.zzk.usermodule.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 更新用户设置请求。
 */
@Data
@Schema(description = "更新用户设置请求")
public class UpdateUserSettingRequest {

    @Schema(description = "是否开启推荐", example = "true")
    private Boolean openRecommend;

    @Schema(description = "是否开启推送", example = "true")
    private Boolean openPush;

    @Schema(description = "是否开启私信", example = "true")
    private Boolean openDm;

    @Schema(description = "是否公开关注列表", example = "true")
    private Boolean openFollowVisible;

    @Schema(description = "是否公开收藏夹", example = "true")
    private Boolean openFavoriteVisible;
}
