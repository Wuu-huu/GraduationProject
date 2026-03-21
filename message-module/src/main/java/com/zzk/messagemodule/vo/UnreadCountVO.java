package com.zzk.messagemodule.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "未读数")
public class UnreadCountVO {

    private final Integer conversationUnreadCount;
    private final Integer notificationUnreadCount;
    private final Integer totalUnreadCount;
}
