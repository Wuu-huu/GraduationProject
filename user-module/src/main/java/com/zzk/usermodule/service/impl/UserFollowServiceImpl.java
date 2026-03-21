package com.zzk.usermodule.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzk.authmodule.entity.UserInfo;
import com.zzk.authmodule.service.UserInfoService;
import com.zzk.common.enums.ApiCodeEnum;
import com.zzk.common.exception.BusinessException;
import com.zzk.common.model.page.PageResponse;
import com.zzk.common.security.SecurityContextUtils;
import com.zzk.usermodule.dto.UserFollowPageQuery;
import com.zzk.usermodule.entity.UserFollow;
import com.zzk.usermodule.entity.UserProfile;
import com.zzk.usermodule.entity.UserStat;
import com.zzk.usermodule.enums.UserFollowStateEnum;
import com.zzk.usermodule.mapper.UserFollowMapper;
import com.zzk.usermodule.service.UserFollowService;
import com.zzk.usermodule.service.UserProfileService;
import com.zzk.usermodule.service.UserStatService;
import com.zzk.usermodule.vo.UserCardVO;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户关注领域服务实现。
 */
@Slf4j
@Service
public class UserFollowServiceImpl extends ServiceImpl<UserFollowMapper, UserFollow>
        implements UserFollowService {

    private final UserInfoService userInfoService;
    private final UserProfileService userProfileService;
    private final UserStatService userStatService;

    public UserFollowServiceImpl(UserInfoService userInfoService,
                                 UserProfileService userProfileService,
                                 UserStatService userStatService) {
        this.userInfoService = userInfoService;
        this.userProfileService = userProfileService;
        this.userStatService = userStatService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void follow(Long targetUid) {
        Long currentUserId = SecurityContextUtils.getCurrentUserId();
        if (currentUserId.equals(targetUid)) {
            throw new BusinessException(ApiCodeEnum.BAD_REQUEST, "不能关注自己");
        }
        ensureUserExists(targetUid);

        UserFollow follow = lambdaQuery()
                .eq(UserFollow::getFollowerUid, currentUserId)
                .eq(UserFollow::getFolloweeUid, targetUid)
                .one();
        if (follow == null) {
            follow = new UserFollow();
            follow.setFollowerUid(currentUserId);
            follow.setFolloweeUid(targetUid);
            follow.setState(UserFollowStateEnum.FOLLOWING.getCode());
            follow.setCreateTime(LocalDateTime.now());
            save(follow);
            userStatService.increaseFollowingCount(currentUserId, 1);
            userStatService.increaseFansCount(targetUid, 1);
            log.info("Follow created, uid={}, targetUid={}", currentUserId, targetUid);
            return;
        }
        if (UserFollowStateEnum.FOLLOWING.getCode() == follow.getState()) {
            return;
        }
        follow.setState(UserFollowStateEnum.FOLLOWING.getCode());
        follow.setCreateTime(LocalDateTime.now());
        updateById(follow);
        userStatService.increaseFollowingCount(currentUserId, 1);
        userStatService.increaseFansCount(targetUid, 1);
        log.info("Follow restored, uid={}, targetUid={}", currentUserId, targetUid);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unfollow(Long targetUid) {
        Long currentUserId = SecurityContextUtils.getCurrentUserId();
        UserFollow follow = lambdaQuery()
                .eq(UserFollow::getFollowerUid, currentUserId)
                .eq(UserFollow::getFolloweeUid, targetUid)
                .one();
        if (follow == null || UserFollowStateEnum.UNFOLLOWED.getCode() == follow.getState()) {
            return;
        }
        follow.setState(UserFollowStateEnum.UNFOLLOWED.getCode());
        updateById(follow);
        userStatService.increaseFollowingCount(currentUserId, -1);
        userStatService.increaseFansCount(targetUid, -1);
        log.info("Follow canceled, uid={}, targetUid={}", currentUserId, targetUid);
    }

    @Override
    public boolean hasFollowed(Long followerUid, Long followeeUid) {
        if (followerUid == null || followeeUid == null) {
            return false;
        }
        return lambdaQuery()
                .eq(UserFollow::getFollowerUid, followerUid)
                .eq(UserFollow::getFolloweeUid, followeeUid)
                .eq(UserFollow::getState, UserFollowStateEnum.FOLLOWING.getCode())
                .count() > 0;
    }

    @Override
    public PageResponse<UserCardVO> getFollowers(Long targetUid, UserFollowPageQuery query) {
        ensureUserExists(targetUid);
        Page<UserFollow> page = new Page<>(query.getPageNum(), query.getPageSize());
        IPage<UserFollow> result = page(page, Wrappers.<UserFollow>lambdaQuery()
                .eq(UserFollow::getFolloweeUid, targetUid)
                .eq(UserFollow::getState, UserFollowStateEnum.FOLLOWING.getCode())
                .orderByDesc(UserFollow::getId));
        List<Long> userIds = result.getRecords().stream()
                .map(UserFollow::getFollowerUid)
                .toList();
        return buildUserCardPage(userIds, result.getTotal(), result.getCurrent(), result.getSize());
    }

    @Override
    public PageResponse<UserCardVO> getFollowing(Long targetUid, UserFollowPageQuery query) {
        ensureUserExists(targetUid);
        Page<UserFollow> page = new Page<>(query.getPageNum(), query.getPageSize());
        IPage<UserFollow> result = page(page, Wrappers.<UserFollow>lambdaQuery()
                .eq(UserFollow::getFollowerUid, targetUid)
                .eq(UserFollow::getState, UserFollowStateEnum.FOLLOWING.getCode())
                .orderByDesc(UserFollow::getId));
        List<Long> userIds = result.getRecords().stream()
                .map(UserFollow::getFolloweeUid)
                .toList();
        return buildUserCardPage(userIds, result.getTotal(), result.getCurrent(), result.getSize());
    }

    private PageResponse<UserCardVO> buildUserCardPage(List<Long> userIds, long total, long pageNum, long pageSize) {
        if (userIds.isEmpty()) {
            return PageResponse.<UserCardVO>builder()
                    .records(Collections.emptyList())
                    .total(total)
                    .pageNum(pageNum)
                    .pageSize(pageSize)
                    .build();
        }

        Map<Long, UserProfile> profileMap = userProfileService.listByIds(userIds).stream()
                .collect(Collectors.toMap(UserProfile::getUid, profile -> profile, (a, b) -> a, LinkedHashMap::new));
        Map<Long, UserStat> statMap = userStatService.listByIds(userIds).stream()
                .collect(Collectors.toMap(UserStat::getUid, stat -> stat, (a, b) -> a, LinkedHashMap::new));

        Long currentUserId = getCurrentUserIdSafely();
        Set<Long> followedIds = currentUserId == null ? Collections.emptySet()
                : lambdaQuery()
                .eq(UserFollow::getFollowerUid, currentUserId)
                .eq(UserFollow::getState, UserFollowStateEnum.FOLLOWING.getCode())
                .in(UserFollow::getFolloweeUid, userIds)
                .list()
                .stream()
                .map(UserFollow::getFolloweeUid)
                .collect(Collectors.toSet());

        List<UserCardVO> records = new ArrayList<>(userIds.size());
        for (Long userId : userIds) {
            UserProfile profile = profileMap.get(userId);
            UserStat stat = statMap.get(userId);
            records.add(UserCardVO.builder()
                    .uid(userId)
                    .nickname(profile == null ? null : profile.getNickname())
                    .avatarUrl(profile == null ? null : profile.getAvatarUrl())
                    .signature(profile == null ? null : profile.getSignature())
                    .fansCount(stat == null || stat.getFansCount() == null ? 0 : stat.getFansCount())
                    .followingCount(stat == null || stat.getFollowingCount() == null ? 0 : stat.getFollowingCount())
                    .followed(followedIds.contains(userId))
                    .build());
        }

        return PageResponse.<UserCardVO>builder()
                .records(records)
                .total(total)
                .pageNum(pageNum)
                .pageSize(pageSize)
                .build();
    }

    private void ensureUserExists(Long uid) {
        UserInfo userInfo = userInfoService.getById(uid);
        if (userInfo == null) {
            throw new BusinessException(ApiCodeEnum.ACCOUNT_NOT_FOUND);
        }
    }

    private Long getCurrentUserIdSafely() {
        try {
            return SecurityContextUtils.getCurrentUserId();
        } catch (Exception ex) {
            return null;
        }
    }
}