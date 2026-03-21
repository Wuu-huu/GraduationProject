package com.zzk.usermodule.service;

import com.zzk.usermodule.entity.UserStat;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 周振坤
* @description 针对表【user_stat(用户统计表)】的数据库操作Service
* @createDate 2026-03-19 23:29:56
*/
public interface UserStatService extends IService<UserStat> {

    void initUserStat(Long uid);

    void increaseFansCount(Long uid, int delta);

    void increaseFollowingCount(Long uid, int delta);
}
