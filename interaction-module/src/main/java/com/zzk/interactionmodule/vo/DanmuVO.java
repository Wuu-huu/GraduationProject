package com.zzk.interactionmodule.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "弹幕信息")
public class DanmuVO {

    private final Long danmuId;
    private final Long vid;
    private final Long partId;
    private final Long uid;
    private final String content;
    private final String color;
    private final Integer fontSize;
    private final Integer mode;
    private final Integer timePointMs;
    private final Date createTime;
}
