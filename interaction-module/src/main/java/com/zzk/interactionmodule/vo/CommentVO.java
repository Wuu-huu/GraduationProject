package com.zzk.interactionmodule.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "评论信息")
public class CommentVO {

    private final Long commentId;
    private final Long vid;
    private final Long uid;
    private final String username;
    private final String nickname;
    private final String avatarUrl;
    private final Long rootId;
    private final Long parentId;
    private final Long replyToUid;
    private final String content;
    private final Integer likeCount;
    private final Integer replyCount;
    private final Boolean liked;
    private final Date createTime;
}
