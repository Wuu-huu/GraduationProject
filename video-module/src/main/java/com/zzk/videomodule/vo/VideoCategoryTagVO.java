package com.zzk.videomodule.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "视频分类标签信息")
public class VideoCategoryTagVO {

    private final Long categoryId;
    private final String categoryName;
    private final List<Long> tagIds;
    private final List<String> tagNames;
}
