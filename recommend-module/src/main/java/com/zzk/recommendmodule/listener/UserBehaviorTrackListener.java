package com.zzk.recommendmodule.listener;

import com.zzk.common.event.UserBehaviorTrackEvent;
import com.zzk.recommendmodule.service.UserBehaviorEventService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class UserBehaviorTrackListener {

    private final UserBehaviorEventService userBehaviorEventService;

    public UserBehaviorTrackListener(UserBehaviorEventService userBehaviorEventService) {
        this.userBehaviorEventService = userBehaviorEventService;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void onUserBehaviorTrack(UserBehaviorTrackEvent event) {
        userBehaviorEventService.recordBehavior(
                event.uid(),
                event.objType(),
                event.objId(),
                event.eventType(),
                event.eventValue(),
                event.scene(),
                event.pageFrom(),
                event.deviceType(),
                event.clientTime()
        );
    }
}
