package com.zzk.videomodule.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "视频轻量信息")
public class VideoSnapshotVO {

    private final Long vid;
    private final Long uid;
    private final String title;
    private final Integer status;
    private final Integer publishStatus;
    private final Integer visibility;
}
