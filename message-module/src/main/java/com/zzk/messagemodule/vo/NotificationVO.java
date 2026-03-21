package com.zzk.messagemodule.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "通知信息")
public class NotificationVO {

    private final Long notificationId;
    private final Integer noticeType;
    private final Integer bizType;
    private final Long bizId;
    private final String title;
    private final String content;
    private final Integer isRead;
    private final Date createTime;
}
