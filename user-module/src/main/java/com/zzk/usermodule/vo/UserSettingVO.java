package com.zzk.usermodule.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

/**
 * 用户设置响应。
 */
@Getter
@Builder
@Schema(description = "用户设置")
public class UserSettingVO {

    @Schema(description = "用户ID")
    private final Long uid;

    @Schema(description = "是否开启推荐")
    private final Boolean openRecommend;

    @Schema(description = "是否开启推送")
    private final Boolean openPush;

    @Schema(description = "是否开启私信")
    private final Boolean openDm;

    @Schema(description = "是否公开关注列表")
    private final Boolean openFollowVisible;

    @Schema(description = "是否公开收藏夹")
    private final Boolean openFavoriteVisible;
}
