package com.zzk.common.event;

import java.util.Date;

/**
 * 通用用户行为埋点事件。
 * 当前阶段先在单体内同步发布，由推荐模块监听后落库。
 * 后续可切换为 Kafka 异步消息而不改变业务调用点。
 */
public record UserBehaviorTrackEvent(
        Long uid,
        Integer objType,
        Long objId,
        String eventType,
        String eventValue,
        String scene,
        String pageFrom,
        String deviceType,
        Date clientTime
) {
}
