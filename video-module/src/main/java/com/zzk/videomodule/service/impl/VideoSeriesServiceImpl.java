package com.zzk.videomodule.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzk.authmodule.entity.UserInfo;
import com.zzk.authmodule.service.UserInfoService;
import com.zzk.common.enums.ApiCodeEnum;
import com.zzk.common.exception.BusinessException;
import com.zzk.common.security.SecurityContextUtils;
import com.zzk.usermodule.entity.UserProfile;
import com.zzk.usermodule.service.UserProfileService;
import com.zzk.videomodule.dto.SaveVideoSeriesRequest;
import com.zzk.videomodule.entity.Video;
import com.zzk.videomodule.entity.VideoSeries;
import com.zzk.videomodule.entity.VideoSeriesItem;
import com.zzk.videomodule.entity.VideoStat;
import com.zzk.videomodule.enums.VideoPublishStatusEnum;
import com.zzk.videomodule.enums.VideoStatusEnum;
import com.zzk.videomodule.enums.VideoVisibilityEnum;
import com.zzk.videomodule.mapper.VideoSeriesMapper;
import com.zzk.videomodule.mapper.VideoSeriesItemMapper;
import com.zzk.videomodule.mapper.VideoStatMapper;
import com.zzk.videomodule.mapper.VideoMapper;
import com.zzk.videomodule.service.VideoSeriesService;
import com.zzk.videomodule.vo.VideoCardVO;
import com.zzk.videomodule.vo.VideoSeriesVO;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @author 周振坤
* @description 针对表【video_series(视频合集表)】的数据库操作Service实现
* @createDate 2026-03-19 23:33:18
*/
@Service
public class VideoSeriesServiceImpl extends ServiceImpl<VideoSeriesMapper, VideoSeries>
        implements VideoSeriesService {

    private final VideoSeriesItemMapper videoSeriesItemMapper;
    private final VideoMapper videoMapper;
    private final VideoStatMapper videoStatMapper;
    private final UserInfoService userInfoService;
    private final UserProfileService userProfileService;

    public VideoSeriesServiceImpl(VideoSeriesItemMapper videoSeriesItemMapper,
                                  VideoMapper videoMapper,
                                  VideoStatMapper videoStatMapper,
                                  UserInfoService userInfoService,
                                  UserProfileService userProfileService) {
        this.videoSeriesItemMapper = videoSeriesItemMapper;
        this.videoMapper = videoMapper;
        this.videoStatMapper = videoStatMapper;
        this.userInfoService = userInfoService;
        this.userProfileService = userProfileService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public VideoSeriesVO createSeries(SaveVideoSeriesRequest request) {
        Long currentUserId = SecurityContextUtils.getCurrentUserId();
        VideoSeries series = new VideoSeries();
        fillSeries(series, request, currentUserId);
        Date now = new Date();
        series.setStatus(VideoStatusEnum.NORMAL.getCode());
        series.setCreateTime(now);
        series.setUpdateTime(now);
        save(series);
        replaceSeriesVideos(series.getSeriesId(), request.getVideoIds(), currentUserId);
        return getSeriesDetail(series.getSeriesId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public VideoSeriesVO updateSeries(Long seriesId, SaveVideoSeriesRequest request) {
        Long currentUserId = SecurityContextUtils.getCurrentUserId();
        VideoSeries series = getOwnedSeries(seriesId, currentUserId);
        fillSeries(series, request, currentUserId);
        series.setUpdateTime(new Date());
        updateById(series);
        replaceSeriesVideos(seriesId, request.getVideoIds(), currentUserId);
        return getSeriesDetail(seriesId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSeries(Long seriesId) {
        Long currentUserId = SecurityContextUtils.getCurrentUserId();
        VideoSeries series = getOwnedSeries(seriesId, currentUserId);
        series.setStatus(VideoStatusEnum.DELETED.getCode());
        series.setUpdateTime(new Date());
        updateById(series);
    }

    @Override
    public VideoSeriesVO getSeriesDetail(Long seriesId) {
        VideoSeries series = getById(seriesId);
        if (series == null || value(series.getStatus()) != VideoStatusEnum.NORMAL.getCode()) {
            throw new BusinessException(ApiCodeEnum.BAD_REQUEST, "Series not found");
        }
        return VideoSeriesVO.builder()
                .seriesId(series.getSeriesId())
                .uid(series.getUid())
                .title(series.getTitle())
                .description(series.getDescription())
                .coverUrl(series.getCoverUrl())
                .visible(series.getVisible())
                .createTime(series.getCreateTime())
                .updateTime(series.getUpdateTime())
                .videos(loadSeriesVideos(seriesId))
                .build();
    }

    private void fillSeries(VideoSeries series, SaveVideoSeriesRequest request, Long currentUserId) {
        series.setUid(currentUserId);
        series.setTitle(request.getTitle().trim());
        series.setDescription(blankToNull(request.getDescription()));
        series.setCoverUrl(blankToNull(request.getCoverUrl()));
        series.setVisible(request.getVisible());
    }

    private VideoSeries getOwnedSeries(Long seriesId, Long currentUserId) {
        VideoSeries series = getById(seriesId);
        if (series == null || value(series.getStatus()) != VideoStatusEnum.NORMAL.getCode()) {
            throw new BusinessException(ApiCodeEnum.BAD_REQUEST, "Series not found");
        }
        if (!currentUserId.equals(series.getUid())) {
            throw new BusinessException(ApiCodeEnum.FORBIDDEN, "You can only modify your own series");
        }
        return series;
    }

    private void replaceSeriesVideos(Long seriesId, List<Long> videoIds, Long currentUserId) {
        videoSeriesItemMapper.delete(Wrappers.<VideoSeriesItem>lambdaQuery()
                .eq(VideoSeriesItem::getSeriesId, seriesId));
        if (videoIds == null || videoIds.isEmpty()) {
            return;
        }
        int sortNo = 1;
        for (Long videoId : videoIds) {
            Video video = videoMapper.selectById(videoId);
            if (video == null || !currentUserId.equals(video.getUid())) {
                throw new BusinessException(ApiCodeEnum.BAD_REQUEST, "Video does not belong to current user");
            }
            VideoSeriesItem item = new VideoSeriesItem();
            item.setSeriesId(seriesId);
            item.setVid(videoId);
            item.setSortNo(sortNo++);
            item.setCreateTime(new Date());
            videoSeriesItemMapper.insert(item);
        }
    }

    private List<VideoCardVO> loadSeriesVideos(Long seriesId) {
        List<VideoSeriesItem> items = videoSeriesItemMapper.selectList(Wrappers.<VideoSeriesItem>lambdaQuery()
                .eq(VideoSeriesItem::getSeriesId, seriesId)
                .orderByAsc(VideoSeriesItem::getSortNo));
        if (items.isEmpty()) {
            return List.of();
        }
        List<Long> videoIds = items.stream().map(VideoSeriesItem::getVid).toList();
        List<Video> videos = videoMapper.selectBatchIds(videoIds);
        Map<Long, Video> videoMap = videos.stream().collect(Collectors.toMap(Video::getVid, Function.identity()));
        Map<Long, VideoStat> statMap = videoStatMapper.selectBatchIds(videoIds).stream()
                .collect(Collectors.toMap(VideoStat::getVid, Function.identity()));
        Map<Long, UserInfo> userInfoMap = videos.stream()
                .map(Video::getUid)
                .distinct()
                .map(userInfoService::getById)
                .filter(user -> user != null)
                .collect(Collectors.toMap(UserInfo::getUid, Function.identity()));
        Map<Long, UserProfile> profileMap = videos.stream()
                .map(Video::getUid)
                .distinct()
                .map(userProfileService::getById)
                .filter(profile -> profile != null)
                .collect(Collectors.toMap(UserProfile::getUid, Function.identity()));

        List<VideoCardVO> result = new ArrayList<>();
        for (VideoSeriesItem item : items) {
            Video video = videoMap.get(item.getVid());
            if (video == null
                    || value(video.getStatus()) != VideoStatusEnum.NORMAL.getCode()
                    || value(video.getPublishStatus()) != VideoPublishStatusEnum.PUBLISHED.getCode()
                    || value(video.getVisibility()) != VideoVisibilityEnum.PUBLIC.getCode()) {
                continue;
            }
            UserInfo userInfo = userInfoMap.get(video.getUid());
            UserProfile profile = profileMap.get(video.getUid());
            VideoStat stat = statMap.get(video.getVid());
            result.add(VideoCardVO.builder()
                    .vid(video.getVid())
                    .title(video.getTitle())
                    .subtitle(video.getSubtitle())
                    .coverUrl(video.getCoverUrl())
                    .durationSec(video.getDurationSec())
                    .authorUid(video.getUid())
                    .authorName(profile != null && profile.getNickname() != null ? profile.getNickname()
                            : userInfo == null ? null : userInfo.getUsername())
                    .authorAvatarUrl(profile == null ? null : profile.getAvatarUrl())
                    .playCount(stat == null ? 0L : safeLong(stat.getPlayCount()))
                    .likeCount(stat == null ? 0L : safeLong(stat.getLikeCount()))
                    .commentCount(stat == null ? 0L : safeLong(stat.getCommentCount()))
                    .favoriteCount(stat == null ? 0L : safeLong(stat.getFavoriteCount()))
                    .publishTime(video.getPublishTime())
                    .build());
        }
        return result;
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private int value(Integer value) {
        return value == null ? 0 : value;
    }

    private long safeLong(Long value) {
        return value == null ? 0L : value;
    }
}




