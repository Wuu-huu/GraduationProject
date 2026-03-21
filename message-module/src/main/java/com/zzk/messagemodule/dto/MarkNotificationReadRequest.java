package com.zzk.messagemodule.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
@Schema(description = "通知已读请求")
public class MarkNotificationReadRequest {

    @Schema(description = "通知 ID 列表，为空表示全部已读")
    private List<Long> notificationIds = new ArrayList<>();
}
