package com.zzk.recommendmodule.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "推荐视频卡片")
public class RecommendVideoVO {

    @Schema(description = "视频 ID")
    private final Long vid;

    @Schema(description = "标题")
    private final String title;

    @Schema(description = "副标题")
    private final String subtitle;

    @Schema(description = "封面地址")
    private final String coverUrl;

    @Schema(description = "视频时长，秒")
    private final Integer durationSec;

    @Schema(description = "作者 ID")
    private final Long authorUid;

    @Schema(description = "作者昵称")
    private final String authorName;

    @Schema(description = "作者头像")
    private final String authorAvatarUrl;

    @Schema(description = "播放数")
    private final Long playCount;

    @Schema(description = "点赞数")
    private final Long likeCount;

    @Schema(description = "评论数")
    private final Long commentCount;

    @Schema(description = "收藏数")
    private final Long favoriteCount;

    @Schema(description = "发布时间")
    private final Date publishTime;

    @Schema(description = "推荐来源")
    private final String source;

    @Schema(description = "推荐分数")
    private final Double score;

    @Schema(description = "当前位置")
    private final Integer positionNo;
}