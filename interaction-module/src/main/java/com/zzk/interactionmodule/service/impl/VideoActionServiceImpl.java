package com.zzk.interactionmodule.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzk.common.enums.ApiCodeEnum;
import com.zzk.common.exception.BusinessException;
import com.zzk.common.security.SecurityContextUtils;
import com.zzk.interactionmodule.dto.CoinVideoRequest;
import com.zzk.interactionmodule.dto.FavoriteVideoRequest;
import com.zzk.interactionmodule.entity.UserVideoState;
import com.zzk.interactionmodule.entity.VideoAction;
import com.zzk.interactionmodule.enums.VideoActionTypeEnum;
import com.zzk.interactionmodule.mapper.VideoActionMapper;
import com.zzk.interactionmodule.mapper.UserVideoStateMapper;
import com.zzk.interactionmodule.service.FavoriteFolderService;
import com.zzk.interactionmodule.service.VideoActionService;
import com.zzk.interactionmodule.vo.UserVideoStateVO;
import com.zzk.usermodule.service.UserStatService;
import com.zzk.videomodule.facade.VideoFacade;
import com.zzk.videomodule.vo.VideoSnapshotVO;
import java.util.Date;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @author 周振坤
* @description 针对表【video_action(视频互动行为明细表)】的数据库操作Service实现
* @createDate 2026-03-19 23:35:18
*/
@Service
public class VideoActionServiceImpl extends ServiceImpl<VideoActionMapper, VideoAction>
        implements VideoActionService {

    private final UserVideoStateMapper userVideoStateMapper;
    private final FavoriteFolderService favoriteFolderService;
    private final VideoFacade videoFacade;
    private final UserStatService userStatService;

    public VideoActionServiceImpl(UserVideoStateMapper userVideoStateMapper,
                                  FavoriteFolderService favoriteFolderService,
                                  VideoFacade videoFacade,
                                  UserStatService userStatService) {
        this.userVideoStateMapper = userVideoStateMapper;
        this.favoriteFolderService = favoriteFolderService;
        this.videoFacade = videoFacade;
        this.userStatService = userStatService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserVideoStateVO likeVideo(Long videoId) {
        VideoSnapshotVO snapshot = requireVideo(videoId);
        UserVideoState state = getOrCreateState(videoId);
        if (value(state.getLiked()) == 1) {
            return toVO(state);
        }
        state.setLiked(1);
        long dislikeDelta = 0L;
        if (value(state.getDisliked()) == 1) {
            state.setDisliked(0);
            dislikeDelta = -1L;
            recordAction(videoId, VideoActionTypeEnum.DISLIKE, 1, 1);
        }
        userVideoStateMapper.updateById(state);
        videoFacade.adjustVideoStats(videoId, 1, dislikeDelta, 0, 0, 0, 0, 0);
        userStatService.increaseLikeReceivedCount(snapshot.getUid(), 1);
        recordAction(videoId, VideoActionTypeEnum.LIKE, 1, 0);
        return toVO(state);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserVideoStateVO cancelLikeVideo(Long videoId) {
        VideoSnapshotVO snapshot = requireVideo(videoId);
        UserVideoState state = getOrCreateState(videoId);
        if (value(state.getLiked()) == 0) {
            return toVO(state);
        }
        state.setLiked(0);
        userVideoStateMapper.updateById(state);
        videoFacade.adjustVideoStats(videoId, -1, 0, 0, 0, 0, 0, 0);
        userStatService.increaseLikeReceivedCount(snapshot.getUid(), -1);
        recordAction(videoId, VideoActionTypeEnum.LIKE, 1, 1);
        return toVO(state);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserVideoStateVO dislikeVideo(Long videoId) {
        VideoSnapshotVO snapshot = requireVideo(videoId);
        UserVideoState state = getOrCreateState(videoId);
        if (value(state.getDisliked()) == 1) {
            return toVO(state);
        }
        long likeDelta = 0L;
        if (value(state.getLiked()) == 1) {
            state.setLiked(0);
            likeDelta = -1L;
            recordAction(videoId, VideoActionTypeEnum.LIKE, 1, 1);
        }
        state.setDisliked(1);
        userVideoStateMapper.updateById(state);
        videoFacade.adjustVideoStats(videoId, likeDelta, 1, 0, 0, 0, 0, 0);
        if (likeDelta < 0) {
            userStatService.increaseLikeReceivedCount(snapshot.getUid(), likeDelta);
        }
        recordAction(videoId, VideoActionTypeEnum.DISLIKE, 1, 0);
        return toVO(state);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserVideoStateVO cancelDislikeVideo(Long videoId) {
        requireVideo(videoId);
        UserVideoState state = getOrCreateState(videoId);
        if (value(state.getDisliked()) == 0) {
            return toVO(state);
        }
        state.setDisliked(0);
        userVideoStateMapper.updateById(state);
        videoFacade.adjustVideoStats(videoId, 0, -1, 0, 0, 0, 0, 0);
        recordAction(videoId, VideoActionTypeEnum.DISLIKE, 1, 1);
        return toVO(state);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserVideoStateVO coinVideo(Long videoId, CoinVideoRequest request) {
        requireVideo(videoId);
        UserVideoState state = getOrCreateState(videoId);
        int total = value(state.getCoinCount()) + request.getCoinCount();
        if (total > 2) {
            throw new BusinessException(ApiCodeEnum.BAD_REQUEST, "A video can receive at most 2 coins from one user");
        }
        state.setCoinCount(total);
        userVideoStateMapper.updateById(state);
        videoFacade.adjustVideoStats(videoId, 0, 0, request.getCoinCount(), 0, 0, 0, 0);
        recordAction(videoId, VideoActionTypeEnum.COIN, request.getCoinCount(), 0);
        return toVO(state);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserVideoStateVO addWatchLater(Long videoId) {
        requireVideo(videoId);
        UserVideoState state = getOrCreateState(videoId);
        if (value(state.getWatchLater()) == 0) {
            state.setWatchLater(1);
            userVideoStateMapper.updateById(state);
            recordAction(videoId, VideoActionTypeEnum.WATCH_LATER, 1, 0);
        }
        return toVO(state);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserVideoStateVO removeWatchLater(Long videoId) {
        requireVideo(videoId);
        UserVideoState state = getOrCreateState(videoId);
        if (value(state.getWatchLater()) == 1) {
            state.setWatchLater(0);
            userVideoStateMapper.updateById(state);
            recordAction(videoId, VideoActionTypeEnum.WATCH_LATER, 1, 1);
        }
        return toVO(state);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserVideoStateVO favoriteVideo(Long videoId, FavoriteVideoRequest request) {
        requireVideo(videoId);
        favoriteFolderService.addVideoToFolder(request.getFavoriteFolderId(), videoId);
        UserVideoState state = getOrCreateState(videoId);
        state.setFavorited(1);
        userVideoStateMapper.updateById(state);
        recordAction(videoId, VideoActionTypeEnum.FAVORITE, 1, 0);
        return toVO(state);
    }

    @Override
    public UserVideoStateVO getCurrentVideoState(Long videoId) {
        requireVideo(videoId);
        return toVO(getOrCreateState(videoId));
    }

    private VideoSnapshotVO requireVideo(Long videoId) {
        videoFacade.assertVideoAccessible(videoId);
        return videoFacade.getVideoSnapshot(videoId);
    }

    private UserVideoState getOrCreateState(Long videoId) {
        Long currentUserId = SecurityContextUtils.getCurrentUserId();
        UserVideoState state = userVideoStateMapper.selectOne(Wrappers.<UserVideoState>lambdaQuery()
                .eq(UserVideoState::getUid, currentUserId)
                .eq(UserVideoState::getVid, videoId)
                .last("limit 1"));
        if (state != null) {
            return state;
        }
        state = new UserVideoState();
        state.setUid(currentUserId);
        state.setVid(videoId);
        state.setLiked(0);
        state.setDisliked(0);
        state.setCoinCount(0);
        state.setFavorited(0);
        state.setWatchLater(0);
        state.setTotalWatchSec(0);
        state.setPlayTimes(0);
        state.setLastPlayProgressSec(0);
        userVideoStateMapper.insert(state);
        return state;
    }

    private void recordAction(Long videoId, VideoActionTypeEnum actionType, Integer actionValue, Integer cancelFlag) {
        VideoAction action = new VideoAction();
        action.setUid(SecurityContextUtils.getCurrentUserId());
        action.setVid(videoId);
        action.setActionType(actionType.getCode());
        action.setActionValue(actionValue);
        action.setActionTime(new Date());
        action.setCancelFlag(cancelFlag);
        save(action);
    }

    private UserVideoStateVO toVO(UserVideoState state) {
        return UserVideoStateVO.builder()
                .vid(state.getVid())
                .liked(value(state.getLiked()) == 1)
                .disliked(value(state.getDisliked()) == 1)
                .coinCount(value(state.getCoinCount()))
                .favorited(value(state.getFavorited()) == 1)
                .watchLater(value(state.getWatchLater()) == 1)
                .build();
    }

    private int value(Integer value) {
        return value == null ? 0 : value;
    }
}




