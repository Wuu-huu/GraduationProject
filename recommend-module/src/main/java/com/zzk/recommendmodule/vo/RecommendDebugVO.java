package com.zzk.recommendmodule.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RecommendDebugVO {

    private final Long vid;
    private final String source;
    private final Double score;
}
