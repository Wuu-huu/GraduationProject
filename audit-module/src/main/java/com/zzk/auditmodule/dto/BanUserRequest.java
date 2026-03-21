package com.zzk.auditmodule.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import java.util.Date;
import lombok.Data;

@Data
@Schema(description = "封禁或禁言请求")
public class BanUserRequest {

    @Schema(description = "处罚类型，由后端接口内部写入")
    private Integer banType;

    @Schema(description = "结束时间")
    private Date endTime;

    @Size(max = 500)
    @Schema(description = "处罚原因")
    private String reason;
}