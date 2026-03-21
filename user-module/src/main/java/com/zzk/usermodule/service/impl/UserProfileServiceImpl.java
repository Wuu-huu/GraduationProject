package com.zzk.usermodule.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzk.authmodule.entity.UserInfo;
import com.zzk.authmodule.service.UserInfoService;
import com.zzk.common.enums.ApiCodeEnum;
import com.zzk.common.exception.BusinessException;
import com.zzk.common.security.SecurityContextUtils;
import com.zzk.usermodule.convert.UserProfileConvert;
import com.zzk.usermodule.dto.UpdateUserProfileRequest;
import com.zzk.usermodule.entity.UserFollow;
import com.zzk.usermodule.entity.UserProfile;
import com.zzk.usermodule.entity.UserStat;
import com.zzk.usermodule.enums.UserFollowStateEnum;
import com.zzk.usermodule.mapper.UserFollowMapper;
import com.zzk.usermodule.mapper.UserProfileMapper;
import com.zzk.usermodule.service.UserProfileService;
import com.zzk.usermodule.service.UserStatService;
import com.zzk.usermodule.vo.UserHomeVO;
import com.zzk.usermodule.vo.UserProfileVO;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
* @author 周振坤
* @description 针对表【user_profile(用户资料表)】的数据库操作Service实现
* @createDate 2026-03-19 23:28:19
*/
@Slf4j
@Service
public class UserProfileServiceImpl extends ServiceImpl<UserProfileMapper, UserProfile>
        implements UserProfileService {

    private final UserInfoService userInfoService;
    private final UserStatService userStatService;
    private final UserFollowMapper userFollowMapper;

    public UserProfileServiceImpl(UserInfoService userInfoService,
                                  UserStatService userStatService,
                                  UserFollowMapper userFollowMapper) {
        this.userInfoService = userInfoService;
        this.userStatService = userStatService;
        this.userFollowMapper = userFollowMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void initUserProfile(Long uid, String username) {
        if (getById(uid) != null) {
            return;
        }
        UserProfile profile = new UserProfile();
        profile.setUid(uid);
        profile.setNickname(username);
        profile.setUpdateTime(LocalDateTime.now());
        save(profile);
        log.info("Initialized user profile, uid={}", uid);
    }

    @Override
    public UserProfileVO getCurrentUserProfile() {
        Long currentUserId = SecurityContextUtils.getCurrentUserId();
        return UserProfileConvert.toProfileVO(getOrCreateProfile(currentUserId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserProfileVO updateCurrentUserProfile(UpdateUserProfileRequest request) {
        Long currentUserId = SecurityContextUtils.getCurrentUserId();
        UserProfile profile = getOrCreateProfile(currentUserId);
        profile.setNickname(request.getNickname().trim());
        profile.setAvatarUrl(trimToNull(request.getAvatarUrl()));
        profile.setBackgroundUrl(trimToNull(request.getBackgroundUrl()));
        profile.setGender(request.getGender());
        profile.setBirthday(request.getBirthday());
        profile.setSignature(trimToNull(request.getSignature()));
        profile.setProvince(trimToNull(request.getProvince()));
        profile.setCity(trimToNull(request.getCity()));
        profile.setUpdateTime(LocalDateTime.now());
        updateById(profile);
        log.info("Updated user profile, uid={}", currentUserId);
        return UserProfileConvert.toProfileVO(profile);
    }

    @Override
    public UserHomeVO getUserHome(Long targetUid) {
        UserInfo userInfo = userInfoService.getById(targetUid);
        if (userInfo == null) {
            throw new BusinessException(ApiCodeEnum.ACCOUNT_NOT_FOUND);
        }

        UserProfile profile = getOrCreateProfile(targetUid, userInfo.getUsername());
        UserStat stat = userStatService.getById(targetUid);
        Long currentUserId = getCurrentUserIdSafely();
        boolean followed = currentUserId != null && currentUserId > 0
                && userFollowMapper.selectCount(Wrappers.<UserFollow>lambdaQuery()
                .eq(UserFollow::getFollowerUid, currentUserId)
                .eq(UserFollow::getFolloweeUid, targetUid)
                .eq(UserFollow::getState, UserFollowStateEnum.FOLLOWING.getCode())) > 0;

        return UserHomeVO.builder()
                .uid(targetUid)
                .username(userInfo.getUsername())
                .profile(UserProfileConvert.toProfileVO(profile))
                .fansCount(stat == null ? 0 : defaultInt(stat.getFansCount()))
                .followingCount(stat == null ? 0 : defaultInt(stat.getFollowingCount()))
                .likeReceivedCount(stat == null ? 0L : defaultLong(stat.getLikeReceivedCount()))
                .videoCount(stat == null ? 0 : defaultInt(stat.getVideoCount()))
                .playReceivedCount(stat == null ? 0L : defaultLong(stat.getPlayReceivedCount()))
                .followed(followed)
                .build();
    }

    private UserProfile getOrCreateProfile(Long uid) {
        UserInfo userInfo = userInfoService.getById(uid);
        if (userInfo == null) {
            throw new BusinessException(ApiCodeEnum.ACCOUNT_NOT_FOUND);
        }
        return getOrCreateProfile(uid, userInfo.getUsername());
    }

    private UserProfile getOrCreateProfile(Long uid, String username) {
        UserProfile profile = getById(uid);
        if (profile != null) {
            return profile;
        }
        initUserProfile(uid, username);
        return getById(uid);
    }

    private Long getCurrentUserIdSafely() {
        try {
            return SecurityContextUtils.getCurrentUserId();
        } catch (Exception ex) {
            return null;
        }
    }

    private String trimToNull(String value) {
        return StringUtils.hasText(value) ? value.trim() : null;
    }

    private int defaultInt(Integer value) {
        return value == null ? 0 : value;
    }

    private long defaultLong(Long value) {
        return value == null ? 0L : value;
    }
}




