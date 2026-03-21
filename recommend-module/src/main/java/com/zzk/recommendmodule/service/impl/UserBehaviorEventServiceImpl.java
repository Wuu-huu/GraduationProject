package com.zzk.recommendmodule.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzk.recommendmodule.entity.UserBehaviorEvent;
import com.zzk.recommendmodule.mapper.UserBehaviorEventMapper;
import com.zzk.recommendmodule.service.UserBehaviorEventService;
import java.util.Date;
import org.springframework.stereotype.Service;

/**
* @author 周振坤
* @description 针对表【user_behavior_event(用户通用行为事件表)】的数据库操作Service实现
* @createDate 2026-03-19 23:39:58
*/
@Service
public class UserBehaviorEventServiceImpl extends ServiceImpl<UserBehaviorEventMapper, UserBehaviorEvent>
    implements UserBehaviorEventService{

    @Override
    public void recordBehavior(Long uid,
                               Integer objType,
                               Long objId,
                               String eventType,
                               String eventValue,
                               String scene,
                               String pageFrom,
                               String deviceType,
                               Date clientTime) {
        UserBehaviorEvent event = new UserBehaviorEvent();
        event.setUid(uid);
        event.setObjType(objType);
        event.setObjId(objId);
        event.setEventType(eventType);
        event.setEventValue(eventValue);
        event.setScene(scene);
        event.setPageFrom(pageFrom);
        event.setDeviceType(deviceType);
        event.setClientTime(clientTime);
        event.setCreateTime(new Date());
        save(event);
    }
}




