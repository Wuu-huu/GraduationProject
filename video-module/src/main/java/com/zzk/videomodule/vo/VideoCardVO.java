package com.zzk.videomodule.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "视频列表卡片")
public class VideoCardVO {

    private final Long vid;
    private final String title;
    private final String subtitle;
    private final String coverUrl;
    private final Integer durationSec;
    private final Long authorUid;
    private final String authorName;
    private final String authorAvatarUrl;
    private final Long playCount;
    private final Long likeCount;
    private final Long commentCount;
    private final Long favoriteCount;
    private final Date publishTime;
}
