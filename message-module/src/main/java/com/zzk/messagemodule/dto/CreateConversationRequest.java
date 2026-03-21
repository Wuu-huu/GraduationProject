package com.zzk.messagemodule.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "创建会话请求")
public class CreateConversationRequest {

    @NotNull
    @Schema(description = "对方用户 ID")
    private Long targetUid;
}
