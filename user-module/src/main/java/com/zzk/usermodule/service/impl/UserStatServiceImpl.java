package com.zzk.usermodule.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzk.usermodule.entity.UserStat;
import com.zzk.usermodule.mapper.UserStatMapper;
import com.zzk.usermodule.service.UserStatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @author 周振坤
* @description 针对表【user_stat(用户统计表)】的数据库操作Service实现
* @createDate 2026-03-19 23:29:56
*/
@Slf4j
@Service
public class UserStatServiceImpl extends ServiceImpl<UserStatMapper, UserStat>
        implements UserStatService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void initUserStat(Long uid) {
        if (getById(uid) != null) {
            return;
        }
        UserStat userStat = new UserStat();
        userStat.setUid(uid);
        userStat.setFansCount(0);
        userStat.setFollowingCount(0);
        userStat.setLikeReceivedCount(0L);
        userStat.setVideoCount(0);
        userStat.setPlayReceivedCount(0L);
        save(userStat);
        log.info("Initialized user stat, uid={}", uid);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void increaseFansCount(Long uid, int delta) {
        UserStat stat = getOrCreate(uid);
        stat.setFansCount(Math.max(0, defaultInt(stat.getFansCount()) + delta));
        updateById(stat);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void increaseFollowingCount(Long uid, int delta) {
        UserStat stat = getOrCreate(uid);
        stat.setFollowingCount(Math.max(0, defaultInt(stat.getFollowingCount()) + delta));
        updateById(stat);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void increaseVideoCount(Long uid, int delta) {
        UserStat stat = getOrCreate(uid);
        stat.setVideoCount(Math.max(0, defaultInt(stat.getVideoCount()) + delta));
        updateById(stat);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void increaseLikeReceivedCount(Long uid, long delta) {
        UserStat stat = getOrCreate(uid);
        stat.setLikeReceivedCount(Math.max(0L, defaultLong(stat.getLikeReceivedCount()) + delta));
        updateById(stat);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void increasePlayReceivedCount(Long uid, long delta) {
        UserStat stat = getOrCreate(uid);
        stat.setPlayReceivedCount(Math.max(0L, defaultLong(stat.getPlayReceivedCount()) + delta));
        updateById(stat);
    }

    private UserStat getOrCreate(Long uid) {
        UserStat stat = getById(uid);
        if (stat != null) {
            return stat;
        }
        initUserStat(uid);
        return getById(uid);
    }

    private int defaultInt(Integer value) {
        return value == null ? 0 : value;
    }

    private long defaultLong(Long value) {
        return value == null ? 0L : value;
    }
}




