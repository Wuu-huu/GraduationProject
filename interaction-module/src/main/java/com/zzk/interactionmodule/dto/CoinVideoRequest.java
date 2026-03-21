package com.zzk.interactionmodule.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "视频投币请求")
public class CoinVideoRequest {

    @NotNull
    @Min(1)
    @Max(2)
    @Schema(description = "投币数量", example = "1")
    private Integer coinCount;
}
