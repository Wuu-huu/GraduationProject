package com.zzk.messagemodule.facade.impl;

import com.zzk.messagemodule.enums.NotificationTypeEnum;
import com.zzk.messagemodule.facade.MessageNoticeFacade;
import com.zzk.messagemodule.service.NotificationService;
import org.springframework.stereotype.Component;

@Component
public class MessageNoticeFacadeImpl implements MessageNoticeFacade {

    private final NotificationService notificationService;

    public MessageNoticeFacadeImpl(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public void createAuditNotification(Long uid, Integer bizType, Long bizId, String title, String content) {
        notificationService.createNotification(uid, NotificationTypeEnum.AUDIT.getCode(), bizType, bizId, title, content);
    }
}
