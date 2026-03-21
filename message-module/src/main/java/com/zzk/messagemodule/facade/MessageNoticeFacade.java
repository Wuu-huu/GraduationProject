package com.zzk.messagemodule.facade;

public interface MessageNoticeFacade {

    void createAuditNotification(Long uid, Integer bizType, Long bizId, String title, String content);
}
