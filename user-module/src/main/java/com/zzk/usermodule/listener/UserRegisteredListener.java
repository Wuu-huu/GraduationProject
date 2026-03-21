package com.zzk.usermodule.listener;

import com.zzk.common.event.UserRegisteredEvent;
import com.zzk.usermodule.service.UserProfileService;
import com.zzk.usermodule.service.UserSettingService;
import com.zzk.usermodule.service.UserStatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * 用户注册事件监听器，负责初始化用户域默认数据。
 */
@Slf4j
@Component
public class UserRegisteredListener {

    private final UserProfileService userProfileService;
    private final UserSettingService userSettingService;
    private final UserStatService userStatService;

    public UserRegisteredListener(UserProfileService userProfileService,
                                  UserSettingService userSettingService,
                                  UserStatService userStatService) {
        this.userProfileService = userProfileService;
        this.userSettingService = userSettingService;
        this.userStatService = userStatService;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUserRegistered(UserRegisteredEvent event) {
        userProfileService.initUserProfile(event.uid(), event.username());
        userSettingService.initUserSetting(event.uid());
        userStatService.initUserStat(event.uid());
        log.info("Handled user registered event, uid={}", event.uid());
    }
}
