package com.zzk.messagemodule.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzk.messagemodule.entity.Notification;
import com.zzk.messagemodule.service.NotificationService;
import com.zzk.messagemodule.mapper.NotificationMapper;
import org.springframework.stereotype.Service;

/**
* @author 周振坤
* @description 针对表【notification(通知表)】的数据库操作Service实现
* @createDate 2026-03-19 23:36:55
*/
@Service
public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, Notification>
    implements NotificationService{

}




