package com.zzk.videomodule.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "作者摘要信息")
public class AuthorSummaryVO {

    @Schema(description = "作者 ID")
    private final Long uid;

    @Schema(description = "用户名")
    private final String username;

    @Schema(description = "昵称")
    private final String nickname;

    @Schema(description = "头像地址")
    private final String avatarUrl;
}
