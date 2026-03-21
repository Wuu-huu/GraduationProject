package com.zzk.messagemodule.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "发送消息请求")
public class SendMessageRequest {

    @NotNull
    @Schema(description = "接收方用户 ID")
    private Long receiverUid;

    @NotNull
    @Schema(description = "消息类型", example = "1")
    private Integer msgType;

    @NotBlank
    @Size(max = 2000)
    @Schema(description = "消息内容")
    private String content;
}
