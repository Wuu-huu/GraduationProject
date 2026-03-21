package com.zzk.auditmodule.controller;

import com.zzk.auditmodule.dto.AuditDecisionRequest;
import com.zzk.auditmodule.dto.AuditPageQuery;
import com.zzk.auditmodule.dto.BanUserRequest;
import com.zzk.auditmodule.dto.HandleReportRequest;
import com.zzk.auditmodule.enums.BanTypeEnum;
import com.zzk.auditmodule.service.AuditTaskService;
import com.zzk.auditmodule.service.BanRecordService;
import com.zzk.auditmodule.service.ReportRecordService;
import com.zzk.auditmodule.service.RiskControlLogService;
import com.zzk.auditmodule.vo.AuditTaskVO;
import com.zzk.auditmodule.vo.BanRecordVO;
import com.zzk.auditmodule.vo.ReportRecordVO;
import com.zzk.auditmodule.vo.RiskControlLogVO;
import com.zzk.common.exception.ForbiddenException;
import com.zzk.common.model.page.PageResponse;
import com.zzk.common.model.response.ApiResponse;
import com.zzk.common.security.SecurityContextUtils;
import com.zzk.messagemodule.dto.PageQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "审核管理")
@RestController
@RequestMapping("/api/audit")
public class AuditAdminController {

    private final AuditTaskService auditTaskService;
    private final ReportRecordService reportRecordService;
    private final BanRecordService banRecordService;
    private final RiskControlLogService riskControlLogService;

    public AuditAdminController(AuditTaskService auditTaskService,
                                ReportRecordService reportRecordService,
                                BanRecordService banRecordService,
                                RiskControlLogService riskControlLogService) {
        this.auditTaskService = auditTaskService;
        this.reportRecordService = reportRecordService;
        this.banRecordService = banRecordService;
        this.riskControlLogService = riskControlLogService;
    }

    @Operation(summary = "视频审核列表")
    @GetMapping("/videos")
    public ApiResponse<PageResponse<AuditTaskVO>> listVideoAuditTasks(@Valid AuditPageQuery query) {
        assertAdmin();
        return ApiResponse.success(auditTaskService.listVideoAuditTasks(query));
    }

    @Operation(summary = "视频审核通过")
    @PostMapping("/videos/{auditId}/approve")
    public ApiResponse<AuditTaskVO> approveVideoAudit(@PathVariable Long auditId,
                                                      @Valid @RequestBody(required = false) AuditDecisionRequest request) {
        assertAdmin();
        return ApiResponse.success(auditTaskService.approveAuditTask(auditId, request == null ? new AuditDecisionRequest() : request));
    }

    @Operation(summary = "视频审核驳回")
    @PostMapping("/videos/{auditId}/reject")
    public ApiResponse<AuditTaskVO> rejectVideoAudit(@PathVariable Long auditId,
                                                     @Valid @RequestBody(required = false) AuditDecisionRequest request) {
        assertAdmin();
        return ApiResponse.success(auditTaskService.rejectAuditTask(auditId, request == null ? new AuditDecisionRequest() : request));
    }

    @Operation(summary = "评论审核列表")
    @GetMapping("/comments")
    public ApiResponse<PageResponse<AuditTaskVO>> listCommentAuditTasks(@Valid AuditPageQuery query) {
        assertAdmin();
        return ApiResponse.success(auditTaskService.listCommentAuditTasks(query));
    }

    @Operation(summary = "评论审核通过")
    @PostMapping("/comments/{auditId}/approve")
    public ApiResponse<AuditTaskVO> approveCommentAudit(@PathVariable Long auditId,
                                                        @Valid @RequestBody(required = false) AuditDecisionRequest request) {
        assertAdmin();
        return ApiResponse.success(auditTaskService.approveAuditTask(auditId, request == null ? new AuditDecisionRequest() : request));
    }

    @Operation(summary = "评论审核驳回")
    @PostMapping("/comments/{auditId}/reject")
    public ApiResponse<AuditTaskVO> rejectCommentAudit(@PathVariable Long auditId,
                                                       @Valid @RequestBody(required = false) AuditDecisionRequest request) {
        assertAdmin();
        return ApiResponse.success(auditTaskService.rejectAuditTask(auditId, request == null ? new AuditDecisionRequest() : request));
    }

    @Operation(summary = "举报列表")
    @GetMapping("/reports")
    public ApiResponse<PageResponse<ReportRecordVO>> listReports(@Valid PageQuery query) {
        assertAdmin();
        return ApiResponse.success(reportRecordService.listReports(query));
    }

    @Operation(summary = "处理举报")
    @PostMapping("/reports/{reportId}/handle")
    public ApiResponse<ReportRecordVO> handleReport(@PathVariable Long reportId,
                                                    @Valid @RequestBody HandleReportRequest request) {
        assertAdmin();
        return ApiResponse.success(reportRecordService.handleReport(reportId, request));
    }

    @Operation(summary = "封禁用户")
    @PostMapping("/users/{uid}/ban")
    public ApiResponse<BanRecordVO> banUser(@PathVariable Long uid,
                                            @Valid @RequestBody BanUserRequest request) {
        assertAdmin();
        request.setBanType(BanTypeEnum.BAN.getCode());
        return ApiResponse.success(banRecordService.banUser(uid, request));
    }

    @Operation(summary = "禁言用户")
    @PostMapping("/users/{uid}/mute")
    public ApiResponse<BanRecordVO> muteUser(@PathVariable Long uid,
                                             @Valid @RequestBody BanUserRequest request) {
        assertAdmin();
        request.setBanType(BanTypeEnum.MUTE.getCode());
        return ApiResponse.success(banRecordService.banUser(uid, request));
    }

    @Operation(summary = "风控日志列表")
    @GetMapping("/risk-logs")
    public ApiResponse<PageResponse<RiskControlLogVO>> listRiskLogs(@Valid PageQuery query) {
        assertAdmin();
        return ApiResponse.success(riskControlLogService.listLogs(query));
    }

    private void assertAdmin() {
        if (!SecurityContextUtils.getLoginUser().isAdmin()) {
            throw new ForbiddenException("仅管理员可执行该操作");
        }
    }
}
