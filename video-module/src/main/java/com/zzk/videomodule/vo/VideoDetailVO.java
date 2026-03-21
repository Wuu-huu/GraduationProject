package com.zzk.videomodule.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "视频详情")
public class VideoDetailVO {

    private final Long vid;
    private final String title;
    private final String subtitle;
    private final String coverUrl;
    private final String description;
    private final Integer visibility;
    private final Integer publishStatus;
    private final Integer durationSec;
    private final Date publishTime;
    private final AuthorSummaryVO author;
    private final VideoCategoryTagVO categoryTag;
    private final List<VideoPartVO> parts;
    private final List<VideoSeriesVO> seriesList;
    private final VideoStatVO stats;
}
