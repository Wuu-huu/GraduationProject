package com.zzk.usermodule.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

/**
 * 用户卡片响应。
 */
@Getter
@Builder
@Schema(description = "用户卡片")
public class UserCardVO {

    @Schema(description = "用户ID")
    private final Long uid;

    @Schema(description = "昵称")
    private final String nickname;

    @Schema(description = "头像地址")
    private final String avatarUrl;

    @Schema(description = "个性签名")
    private final String signature;

    @Schema(description = "粉丝数")
    private final Integer fansCount;

    @Schema(description = "关注数")
    private final Integer followingCount;

    @Schema(description = "是否已关注")
    private final Boolean followed;
}
