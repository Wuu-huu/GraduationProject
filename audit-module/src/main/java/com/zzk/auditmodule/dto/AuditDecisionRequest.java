package com.zzk.auditmodule.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "审核决定请求")
public class AuditDecisionRequest {

    @Size(max = 255)
    private String reason;
}
