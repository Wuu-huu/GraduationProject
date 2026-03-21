package com.zzk.usermodule.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzk.common.security.SecurityContextUtils;
import com.zzk.usermodule.convert.UserProfileConvert;
import com.zzk.usermodule.dto.UpdateUserSettingRequest;
import com.zzk.usermodule.entity.UserSetting;
import com.zzk.usermodule.mapper.UserSettingMapper;
import com.zzk.usermodule.service.UserSettingService;
import com.zzk.usermodule.vo.UserSettingVO;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @author 周振坤
* @description 针对表【user_setting(用户设置表)】的数据库操作Service实现
* @createDate 2026-03-19 23:30:26
*/
@Slf4j
@Service
public class UserSettingServiceImpl extends ServiceImpl<UserSettingMapper, UserSetting>
        implements UserSettingService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void initUserSetting(Long uid) {
        if (getById(uid) != null) {
            return;
        }
        UserSetting setting = new UserSetting();
        setting.setUid(uid);
        setting.setOpenRecommend(1);
        setting.setOpenPush(1);
        setting.setOpenDm(1);
        setting.setOpenFollowVisible(1);
        setting.setOpenFavoriteVisible(1);
        setting.setUpdateTime(LocalDateTime.now());
        save(setting);
        log.info("Initialized user setting, uid={}", uid);
    }

    @Override
    public UserSettingVO getCurrentUserSetting() {
        Long currentUserId = SecurityContextUtils.getCurrentUserId();
        return UserProfileConvert.toSettingVO(getOrCreateSetting(currentUserId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserSettingVO updateCurrentUserSetting(UpdateUserSettingRequest request) {
        Long currentUserId = SecurityContextUtils.getCurrentUserId();
        UserSetting setting = getOrCreateSetting(currentUserId);
        if (request.getOpenRecommend() != null) {
            setting.setOpenRecommend(UserProfileConvert.booleanToInt(request.getOpenRecommend()));
        }
        if (request.getOpenPush() != null) {
            setting.setOpenPush(UserProfileConvert.booleanToInt(request.getOpenPush()));
        }
        if (request.getOpenDm() != null) {
            setting.setOpenDm(UserProfileConvert.booleanToInt(request.getOpenDm()));
        }
        if (request.getOpenFollowVisible() != null) {
            setting.setOpenFollowVisible(UserProfileConvert.booleanToInt(request.getOpenFollowVisible()));
        }
        if (request.getOpenFavoriteVisible() != null) {
            setting.setOpenFavoriteVisible(UserProfileConvert.booleanToInt(request.getOpenFavoriteVisible()));
        }
        setting.setUpdateTime(LocalDateTime.now());
        updateById(setting);
        log.info("Updated user setting, uid={}", currentUserId);
        return UserProfileConvert.toSettingVO(setting);
    }

    private UserSetting getOrCreateSetting(Long uid) {
        UserSetting setting = getById(uid);
        if (setting != null) {
            return setting;
        }
        initUserSetting(uid);
        return getById(uid);
    }
}




