package com.zzk.interactionmodule.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "用户对视频的互动状态")
public class UserVideoStateVO {

    private final Long vid;
    private final Boolean liked;
    private final Boolean disliked;
    private final Integer coinCount;
    private final Boolean favorited;
    private final Boolean watchLater;
}
