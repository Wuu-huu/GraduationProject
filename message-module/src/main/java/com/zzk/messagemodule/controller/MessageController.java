package com.zzk.messagemodule.controller;

import com.zzk.common.model.page.PageResponse;
import com.zzk.common.model.response.ApiResponse;
import com.zzk.messagemodule.dto.CreateConversationRequest;
import com.zzk.messagemodule.dto.MarkNotificationReadRequest;
import com.zzk.messagemodule.dto.PageQuery;
import com.zzk.messagemodule.dto.SendMessageRequest;
import com.zzk.messagemodule.service.ConversationService;
import com.zzk.messagemodule.service.MessageService;
import com.zzk.messagemodule.service.NotificationService;
import com.zzk.messagemodule.vo.ConversationVO;
import com.zzk.messagemodule.vo.MessageVO;
import com.zzk.messagemodule.vo.NotificationVO;
import com.zzk.messagemodule.vo.UnreadCountVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "消息通知")
@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final ConversationService conversationService;
    private final MessageService messageService;
    private final NotificationService notificationService;

    public MessageController(ConversationService conversationService,
                             MessageService messageService,
                             NotificationService notificationService) {
        this.conversationService = conversationService;
        this.messageService = messageService;
        this.notificationService = notificationService;
    }

    @Operation(summary = "创建私信会话")
    @PostMapping("/conversations")
    public ApiResponse<ConversationVO> createConversation(@Valid @RequestBody CreateConversationRequest request) {
        return ApiResponse.success(conversationService.createConversation(request));
    }

    @Operation(summary = "会话列表")
    @GetMapping("/conversations")
    public ApiResponse<PageResponse<ConversationVO>> listConversations(@Valid PageQuery query) {
        return ApiResponse.success(conversationService.listConversations(query));
    }

    @Operation(summary = "会话详情")
    @GetMapping("/conversations/{conversationId}")
    public ApiResponse<PageResponse<MessageVO>> getConversationDetail(@PathVariable Long conversationId,
                                                                      @Valid PageQuery query) {
        return ApiResponse.success(conversationService.getConversationDetail(conversationId, query));
    }

    @Operation(summary = "发送消息")
    @PostMapping("/conversations/{conversationId}/messages")
    public ApiResponse<MessageVO> sendMessage(@PathVariable Long conversationId,
                                              @Valid @RequestBody SendMessageRequest request) {
        return ApiResponse.success(messageService.sendMessage(conversationId, request));
    }

    @Operation(summary = "会话已读")
    @PutMapping("/conversations/{conversationId}/read")
    public ApiResponse<Void> markConversationRead(@PathVariable Long conversationId) {
        conversationService.markConversationRead(conversationId);
        return ApiResponse.success();
    }

    @Operation(summary = "通知列表")
    @GetMapping("/notifications")
    public ApiResponse<PageResponse<NotificationVO>> listNotifications(@Valid PageQuery query) {
        return ApiResponse.success(notificationService.listNotifications(query));
    }

    @Operation(summary = "通知已读")
    @PutMapping("/notifications/read")
    public ApiResponse<Void> markNotificationsRead(@RequestBody(required = false) MarkNotificationReadRequest request) {
        notificationService.markNotificationsRead(request == null ? new MarkNotificationReadRequest() : request);
        return ApiResponse.success();
    }

    @Operation(summary = "未读数")
    @GetMapping("/unread-count")
    public ApiResponse<UnreadCountVO> getUnreadCount() {
        return ApiResponse.success(notificationService.getUnreadCount());
    }
}
