package com.zzk.interactionmodule.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "收藏夹信息")
public class FavoriteFolderVO {

    private final Long fid;
    private final Long uid;
    private final Integer folderType;
    private final String title;
    private final String description;
    private final String coverUrl;
    private final Integer visible;
    private final Integer sortNo;
    private final Integer itemCount;
    private final Date createTime;
    private final Date updateTime;
}
