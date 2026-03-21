package com.zzk.videomodule.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzk.videomodule.entity.VideoStat;
import com.zzk.videomodule.mapper.VideoStatMapper;
import com.zzk.videomodule.service.VideoStatService;
import com.zzk.videomodule.vo.VideoStatVO;
import java.math.BigDecimal;
import java.util.Date;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @author 周振坤
* @description 针对表【video_stat(视频统计表)】的数据库操作Service实现
* @createDate 2026-03-19 23:33:28
*/
@Service
public class VideoStatServiceImpl extends ServiceImpl<VideoStatMapper, VideoStat>
        implements VideoStatService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void initVideoStat(Long videoId) {
        if (getById(videoId) != null) {
            return;
        }
        VideoStat stat = new VideoStat();
        stat.setVid(videoId);
        stat.setExposureCount(0L);
        stat.setPlayCount(0L);
        stat.setLikeCount(0L);
        stat.setDislikeCount(0L);
        stat.setCoinCount(0L);
        stat.setFavoriteCount(0L);
        stat.setShareCount(0L);
        stat.setCommentCount(0L);
        stat.setDanmuCount(0L);
        stat.setAvgWatchSec(BigDecimal.ZERO);
        stat.setAvgCompletionRate(BigDecimal.ZERO);
        stat.setHotScore(BigDecimal.ZERO);
        stat.setUpdateTime(new Date());
        save(stat);
    }

    @Override
    public VideoStatVO getVideoStat(Long videoId) {
        VideoStat stat = getOrCreate(videoId);
        return VideoStatVO.builder()
                .vid(stat.getVid())
                .exposureCount(safeLong(stat.getExposureCount()))
                .playCount(safeLong(stat.getPlayCount()))
                .likeCount(safeLong(stat.getLikeCount()))
                .dislikeCount(safeLong(stat.getDislikeCount()))
                .coinCount(safeLong(stat.getCoinCount()))
                .favoriteCount(safeLong(stat.getFavoriteCount()))
                .shareCount(safeLong(stat.getShareCount()))
                .commentCount(safeLong(stat.getCommentCount()))
                .danmuCount(safeLong(stat.getDanmuCount()))
                .avgWatchSec(defaultDecimal(stat.getAvgWatchSec()))
                .avgCompletionRate(defaultDecimal(stat.getAvgCompletionRate()))
                .hotScore(defaultDecimal(stat.getHotScore()))
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void adjustVideoStat(Long videoId,
                                long likeDelta,
                                long dislikeDelta,
                                long coinDelta,
                                long favoriteDelta,
                                long commentDelta,
                                long danmuDelta,
                                long playDelta) {
        VideoStat stat = getOrCreate(videoId);
        stat.setLikeCount(safeLong(stat.getLikeCount()) + likeDelta);
        stat.setDislikeCount(safeLong(stat.getDislikeCount()) + dislikeDelta);
        stat.setCoinCount(safeLong(stat.getCoinCount()) + coinDelta);
        stat.setFavoriteCount(safeLong(stat.getFavoriteCount()) + favoriteDelta);
        stat.setCommentCount(safeLong(stat.getCommentCount()) + commentDelta);
        stat.setDanmuCount(safeLong(stat.getDanmuCount()) + danmuDelta);
        stat.setPlayCount(safeLong(stat.getPlayCount()) + playDelta);
        stat.setHotScore(BigDecimal.valueOf(
                safeLong(stat.getPlayCount()) * 0.2
                        + safeLong(stat.getLikeCount()) * 1.0
                        + safeLong(stat.getCoinCount()) * 1.5
                        + safeLong(stat.getFavoriteCount()) * 1.2
                        + safeLong(stat.getCommentCount()) * 0.8
                        + safeLong(stat.getDanmuCount()) * 0.3
                        - safeLong(stat.getDislikeCount()) * 0.5
        ));
        stat.setUpdateTime(new Date());
        updateById(stat);
    }

    private VideoStat getOrCreate(Long videoId) {
        VideoStat stat = getById(videoId);
        if (stat != null) {
            return stat;
        }
        initVideoStat(videoId);
        return getById(videoId);
    }

    private long safeLong(Long value) {
        return Math.max(0L, value == null ? 0L : value);
    }

    private BigDecimal defaultDecimal(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}




