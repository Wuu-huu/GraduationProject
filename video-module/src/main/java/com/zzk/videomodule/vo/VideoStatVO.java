package com.zzk.videomodule.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "视频统计信息")
public class VideoStatVO {

    private final Long vid;
    private final Long exposureCount;
    private final Long playCount;
    private final Long likeCount;
    private final Long dislikeCount;
    private final Long coinCount;
    private final Long favoriteCount;
    private final Long shareCount;
    private final Long commentCount;
    private final Long danmuCount;
    private final BigDecimal avgWatchSec;
    private final BigDecimal avgCompletionRate;
    private final BigDecimal hotScore;
}
