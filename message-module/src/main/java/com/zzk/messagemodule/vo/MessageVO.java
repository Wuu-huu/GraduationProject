package com.zzk.messagemodule.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "消息详情")
public class MessageVO {

    private final Long messageId;
    private final Long conversationId;
    private final Long senderUid;
    private final Long receiverUid;
    private final String senderName;
    private final String senderAvatarUrl;
    private final Integer msgType;
    private final String content;
    private final Integer isRecalled;
    private final Date createTime;
}
