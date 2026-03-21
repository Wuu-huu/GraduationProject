package com.zzk.videomodule.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "合集信息")
public class VideoSeriesVO {

    private final Long seriesId;
    private final Long uid;
    private final String title;
    private final String description;
    private final String coverUrl;
    private final Integer visible;
    private final Date createTime;
    private final Date updateTime;
    private final List<VideoCardVO> videos;
}
