package com.zzk.recommendmodule.service;

import com.zzk.recommendmodule.entity.UserBehaviorEvent;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.Date;

/**
* @author 周振坤
* @description 针对表【user_behavior_event(用户通用行为事件表)】的数据库操作Service
* @createDate 2026-03-19 23:39:58
*/
public interface UserBehaviorEventService extends IService<UserBehaviorEvent> {

    void recordBehavior(Long uid,
                        Integer objType,
                        Long objId,
                        String eventType,
                        String eventValue,
                        String scene,
                        String pageFrom,
                        String deviceType,
                        Date clientTime);
}
