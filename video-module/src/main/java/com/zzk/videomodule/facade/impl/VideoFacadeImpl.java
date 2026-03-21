package com.zzk.videomodule.facade.impl;

import com.zzk.common.enums.ApiCodeEnum;
import com.zzk.common.exception.BusinessException;
import com.zzk.videomodule.entity.Video;
import com.zzk.videomodule.enums.VideoPublishStatusEnum;
import com.zzk.videomodule.enums.VideoStatusEnum;
import com.zzk.videomodule.enums.VideoVisibilityEnum;
import com.zzk.videomodule.facade.VideoFacade;
import com.zzk.videomodule.service.VideoService;
import com.zzk.videomodule.service.VideoStatService;
import com.zzk.videomodule.vo.VideoCardVO;
import com.zzk.videomodule.vo.VideoSnapshotVO;
import java.util.Collection;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class VideoFacadeImpl implements VideoFacade {

    private final VideoService videoService;
    private final VideoStatService videoStatService;

    public VideoFacadeImpl(VideoService videoService, VideoStatService videoStatService) {
        this.videoService = videoService;
        this.videoStatService = videoStatService;
    }

    @Override
    public VideoSnapshotVO getVideoSnapshot(Long vid) {
        Video video = videoService.getById(vid);
        if (video == null) {
            return null;
        }
        return VideoSnapshotVO.builder()
                .vid(video.getVid())
                .uid(video.getUid())
                .title(video.getTitle())
                .status(video.getStatus())
                .publishStatus(video.getPublishStatus())
                .visibility(video.getVisibility())
                .build();
    }

    @Override
    public void assertVideoAccessible(Long vid) {
        VideoSnapshotVO snapshot = getVideoSnapshot(vid);
        if (snapshot == null
                || value(snapshot.getStatus()) != VideoStatusEnum.NORMAL.getCode()
                || value(snapshot.getPublishStatus()) != VideoPublishStatusEnum.PUBLISHED.getCode()
                || value(snapshot.getVisibility()) != VideoVisibilityEnum.PUBLIC.getCode()) {
            throw new BusinessException(ApiCodeEnum.BAD_REQUEST, "Video is not available");
        }
    }

    @Override
    public void adjustVideoStats(Long vid,
                                 long likeDelta,
                                 long dislikeDelta,
                                 long coinDelta,
                                 long favoriteDelta,
                                 long commentDelta,
                                 long danmuDelta,
                                 long playDelta) {
        videoStatService.adjustVideoStat(vid, likeDelta, dislikeDelta, coinDelta, favoriteDelta, commentDelta, danmuDelta, playDelta);
    }

    @Override
    public List<VideoCardVO> listVideoCards(Collection<Long> videoIds) {
        return videoService.listVideoCardsByIds(videoIds == null ? List.of() : videoIds.stream().toList());
    }

    private int value(Integer value) {
        return value == null ? 0 : value;
    }
}
