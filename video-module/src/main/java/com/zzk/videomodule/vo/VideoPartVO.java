package com.zzk.videomodule.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "视频分P")
public class VideoPartVO {

    private final Long partId;
    private final Integer partNo;
    private final String title;
    private final String videoUrl;
    private final Integer durationSec;
    private final Long sizeBytes;
}
