package com.zzk.messagemodule.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzk.common.model.page.PageResponse;
import com.zzk.messagemodule.dto.MarkNotificationReadRequest;
import com.zzk.messagemodule.dto.PageQuery;
import com.zzk.messagemodule.entity.Notification;
import com.zzk.messagemodule.vo.NotificationVO;
import com.zzk.messagemodule.vo.UnreadCountVO;

/**
* @author 周振坤
* @description 针对表【notification(通知表)】的数据库操作Service
* @createDate 2026-03-19 23:36:55
*/
public interface NotificationService extends IService<Notification> {

    PageResponse<NotificationVO> listNotifications(PageQuery query);

    void markNotificationsRead(MarkNotificationReadRequest request);

    UnreadCountVO getUnreadCount();

    void createNotification(Long uid, Integer noticeType, Integer bizType, Long bizId, String title, String content);
}
