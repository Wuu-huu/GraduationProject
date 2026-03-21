package com.zzk.messagemodule.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "会话摘要")
public class ConversationVO {

    private final Long conversationId;
    private final Integer conversationType;
    private final Long targetUid;
    private final String targetName;
    private final String targetAvatarUrl;
    private final Long latestMessageId;
    private final String latestContent;
    private final Date latestTime;
    private final Integer unreadCount;
}
