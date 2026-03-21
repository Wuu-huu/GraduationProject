package com.zzk.messagemodule.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzk.common.model.page.PageResponse;
import com.zzk.common.security.SecurityContextUtils;
import com.zzk.messagemodule.dto.MarkNotificationReadRequest;
import com.zzk.messagemodule.dto.PageQuery;
import com.zzk.messagemodule.entity.ConversationRead;
import com.zzk.messagemodule.entity.Notification;
import com.zzk.messagemodule.mapper.ConversationReadMapper;
import com.zzk.messagemodule.mapper.NotificationMapper;
import com.zzk.messagemodule.service.NotificationService;
import com.zzk.messagemodule.vo.NotificationVO;
import com.zzk.messagemodule.vo.UnreadCountVO;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @author 周振坤
* @description 针对表【notification(通知表)】的数据库操作Service实现
* @createDate 2026-03-19 23:36:55
*/
@Service
public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, Notification>
        implements NotificationService {

    private final ConversationReadMapper conversationReadMapper;

    public NotificationServiceImpl(ConversationReadMapper conversationReadMapper) {
        this.conversationReadMapper = conversationReadMapper;
    }

    @Override
    public PageResponse<NotificationVO> listNotifications(PageQuery query) {
        Long currentUserId = SecurityContextUtils.getCurrentUserId();
        Page<Notification> page = page(new Page<>(query.getPageNum(), query.getPageSize()),
                Wrappers.<Notification>lambdaQuery()
                        .eq(Notification::getUid, currentUserId)
                        .orderByDesc(Notification::getCreateTime));
        return PageResponse.<NotificationVO>builder()
                .records(page.getRecords().stream().map(this::toVO).toList())
                .total(page.getTotal())
                .pageNum(page.getCurrent())
                .pageSize(page.getSize())
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markNotificationsRead(MarkNotificationReadRequest request) {
        Long currentUserId = SecurityContextUtils.getCurrentUserId();
        List<Notification> notifications = request.getNotificationIds() == null || request.getNotificationIds().isEmpty()
                ? list(Wrappers.<Notification>lambdaQuery().eq(Notification::getUid, currentUserId).eq(Notification::getIsRead, 0))
                : list(Wrappers.<Notification>lambdaQuery()
                        .eq(Notification::getUid, currentUserId)
                        .in(Notification::getNotificationId, request.getNotificationIds()));
        for (Notification notification : notifications) {
            notification.setIsRead(1);
            updateById(notification);
        }
    }

    @Override
    public UnreadCountVO getUnreadCount() {
        Long currentUserId = SecurityContextUtils.getCurrentUserId();
        int notificationUnreadCount = Math.toIntExact(count(Wrappers.<Notification>lambdaQuery()
                .eq(Notification::getUid, currentUserId)
                .eq(Notification::getIsRead, 0)));
        int conversationUnreadCount = conversationReadMapper.selectList(Wrappers.<ConversationRead>lambdaQuery()
                        .eq(ConversationRead::getUid, currentUserId))
                .stream()
                .mapToInt(item -> item.getUnreadCount() == null ? 0 : item.getUnreadCount())
                .sum();
        return UnreadCountVO.builder()
                .conversationUnreadCount(conversationUnreadCount)
                .notificationUnreadCount(notificationUnreadCount)
                .totalUnreadCount(conversationUnreadCount + notificationUnreadCount)
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createNotification(Long uid, Integer noticeType, Integer bizType, Long bizId, String title, String content) {
        Notification notification = new Notification();
        notification.setUid(uid);
        notification.setNoticeType(noticeType);
        notification.setBizType(bizType);
        notification.setBizId(bizId);
        notification.setTitle(title);
        notification.setContent(content);
        notification.setIsRead(0);
        notification.setCreateTime(new Date());
        save(notification);
    }

    private NotificationVO toVO(Notification notification) {
        return NotificationVO.builder()
                .notificationId(notification.getNotificationId())
                .noticeType(notification.getNoticeType())
                .bizType(notification.getBizType())
                .bizId(notification.getBizId())
                .title(notification.getTitle())
                .content(notification.getContent())
                .isRead(notification.getIsRead())
                .createTime(notification.getCreateTime())
                .build();
    }
}




