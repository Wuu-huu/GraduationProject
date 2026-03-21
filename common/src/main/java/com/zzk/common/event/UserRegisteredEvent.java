package com.zzk.common.event;

import java.time.LocalDateTime;

/**
 * 用户注册完成事件，用于驱动用户域初始化默认资料和设置。
 */
public record UserRegisteredEvent(Long uid, String username, LocalDateTime registerTime) {
}
