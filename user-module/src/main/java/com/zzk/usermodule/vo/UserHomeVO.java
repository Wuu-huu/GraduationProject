package com.zzk.usermodule.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

/**
 * 用户主页响应。
 */
@Getter
@Builder
@Schema(description = "用户主页")
public class UserHomeVO {

    @Schema(description = "用户ID")
    private final Long uid;

    @Schema(description = "用户名")
    private final String username;

    @Schema(description = "资料信息")
    private final UserProfileVO profile;

    @Schema(description = "粉丝数")
    private final Integer fansCount;

    @Schema(description = "关注数")
    private final Integer followingCount;

    @Schema(description = "获赞数")
    private final Long likeReceivedCount;

    @Schema(description = "投稿数")
    private final Integer videoCount;

    @Schema(description = "播放数")
    private final Long playReceivedCount;

    @Schema(description = "当前登录用户是否已关注")
    private final Boolean followed;
}
