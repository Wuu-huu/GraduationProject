package com.zzk.videomodule.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzk.authmodule.entity.UserInfo;
import com.zzk.authmodule.service.UserInfoService;
import com.zzk.common.enums.ApiCodeEnum;
import com.zzk.common.exception.BusinessException;
import com.zzk.common.model.page.PageResponse;
import com.zzk.common.security.SecurityContextUtils;
import com.zzk.usermodule.entity.UserProfile;
import com.zzk.usermodule.service.UserProfileService;
import com.zzk.usermodule.service.UserStatService;
import com.zzk.videomodule.dto.BindVideoCategoryTagsRequest;
import com.zzk.videomodule.dto.SaveVideoRequest;
import com.zzk.videomodule.dto.UpdateVideoRequest;
import com.zzk.videomodule.dto.UpdateVideoStatusRequest;
import com.zzk.videomodule.dto.VideoQueryRequest;
import com.zzk.videomodule.entity.Category;
import com.zzk.videomodule.entity.Tag;
import com.zzk.videomodule.entity.Video;
import com.zzk.videomodule.entity.VideoCategory;
import com.zzk.videomodule.entity.VideoPart;
import com.zzk.videomodule.entity.VideoSeries;
import com.zzk.videomodule.entity.VideoSeriesItem;
import com.zzk.videomodule.entity.VideoStat;
import com.zzk.videomodule.entity.VideoTag;
import com.zzk.videomodule.enums.VideoPublishStatusEnum;
import com.zzk.videomodule.enums.VideoStatusEnum;
import com.zzk.videomodule.enums.VideoVisibilityEnum;
import com.zzk.videomodule.mapper.CategoryMapper;
import com.zzk.videomodule.mapper.TagMapper;
import com.zzk.videomodule.mapper.VideoMapper;
import com.zzk.videomodule.mapper.VideoCategoryMapper;
import com.zzk.videomodule.mapper.VideoSeriesItemMapper;
import com.zzk.videomodule.mapper.VideoSeriesMapper;
import com.zzk.videomodule.mapper.VideoStatMapper;
import com.zzk.videomodule.mapper.VideoTagMapper;
import com.zzk.videomodule.service.VideoPartService;
import com.zzk.videomodule.service.VideoService;
import com.zzk.videomodule.service.VideoStatService;
import com.zzk.videomodule.vo.AuthorSummaryVO;
import com.zzk.videomodule.vo.VideoCardVO;
import com.zzk.videomodule.vo.VideoCategoryTagVO;
import com.zzk.videomodule.vo.VideoDetailVO;
import com.zzk.videomodule.vo.VideoPartVO;
import com.zzk.videomodule.vo.VideoSeriesVO;
import com.zzk.videomodule.vo.VideoStatVO;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @author 周振坤
* @description 针对表【video(视频主表)】的数据库操作Service实现
* @createDate 2026-03-19 23:32:45
*/
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video>
        implements VideoService {

    private final VideoPartService videoPartService;
    private final VideoStatService videoStatService;
    private final VideoCategoryMapper videoCategoryMapper;
    private final VideoTagMapper videoTagMapper;
    private final CategoryMapper categoryMapper;
    private final TagMapper tagMapper;
    private final VideoSeriesMapper videoSeriesMapper;
    private final VideoSeriesItemMapper videoSeriesItemMapper;
    private final VideoStatMapper videoStatMapper;
    private final UserInfoService userInfoService;
    private final UserProfileService userProfileService;
    private final UserStatService userStatService;

    public VideoServiceImpl(VideoPartService videoPartService,
                            VideoStatService videoStatService,
                            VideoCategoryMapper videoCategoryMapper,
                            VideoTagMapper videoTagMapper,
                            CategoryMapper categoryMapper,
                            TagMapper tagMapper,
                            VideoSeriesMapper videoSeriesMapper,
                            VideoSeriesItemMapper videoSeriesItemMapper,
                            VideoStatMapper videoStatMapper,
                            UserInfoService userInfoService,
                            UserProfileService userProfileService,
                            UserStatService userStatService) {
        this.videoPartService = videoPartService;
        this.videoStatService = videoStatService;
        this.videoCategoryMapper = videoCategoryMapper;
        this.videoTagMapper = videoTagMapper;
        this.categoryMapper = categoryMapper;
        this.tagMapper = tagMapper;
        this.videoSeriesMapper = videoSeriesMapper;
        this.videoSeriesItemMapper = videoSeriesItemMapper;
        this.videoStatMapper = videoStatMapper;
        this.userInfoService = userInfoService;
        this.userProfileService = userProfileService;
        this.userStatService = userStatService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public VideoDetailVO publishVideo(SaveVideoRequest request) {
        Long currentUserId = SecurityContextUtils.getCurrentUserId();
        Video video = buildVideo(request, currentUserId, VideoPublishStatusEnum.PUBLISHED.getCode());
        Date now = new Date();
        video.setPublishTime(now);
        video.setUploadTime(now);
        save(video);
        videoStatService.initVideoStat(video.getVid());
        syncCategoryTags(video.getVid(), request.getPrimaryCategoryId(), request.getTagIds());
        videoPartService.replaceVideoParts(video.getVid(), request.getParts());
        userStatService.increaseVideoCount(currentUserId, 1);
        return getVideoDetail(video.getVid());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public VideoDetailVO saveDraft(SaveVideoRequest request) {
        Long currentUserId = SecurityContextUtils.getCurrentUserId();
        Video video = buildVideo(request, currentUserId, VideoPublishStatusEnum.DRAFT.getCode());
        video.setUploadTime(new Date());
        save(video);
        videoStatService.initVideoStat(video.getVid());
        syncCategoryTags(video.getVid(), request.getPrimaryCategoryId(), request.getTagIds());
        videoPartService.replaceVideoParts(video.getVid(), request.getParts());
        return getVideoDetail(video.getVid());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public VideoDetailVO updateVideo(Long videoId, UpdateVideoRequest request) {
        Video video = getOwnedVideo(videoId);
        applyVideoRequest(video, request);
        video.setUploadTime(new Date());
        updateById(video);
        syncCategoryTags(videoId, request.getPrimaryCategoryId(), request.getTagIds());
        videoPartService.replaceVideoParts(videoId, request.getParts());
        return getVideoDetail(videoId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteVideo(Long videoId) {
        Video video = getOwnedVideo(videoId);
        if (value(video.getStatus()) == VideoStatusEnum.DELETED.getCode()) {
            return;
        }
        boolean counted = isPublishedForPublic(video);
        video.setStatus(VideoStatusEnum.DELETED.getCode());
        video.setDeleteTime(new Date());
        updateById(video);
        if (counted) {
            userStatService.increaseVideoCount(video.getUid(), -1);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public VideoDetailVO updateVideoStatus(Long videoId, UpdateVideoStatusRequest request) {
        Video video = getOwnedVideo(videoId);
        boolean before = isPublishedForPublic(video);
        video.setPublishStatus(request.getPublishStatus());
        if (value(request.getPublishStatus()) == VideoPublishStatusEnum.PUBLISHED.getCode() && video.getPublishTime() == null) {
            video.setPublishTime(new Date());
        }
        updateById(video);
        boolean after = isPublishedForPublic(video);
        if (!before && after) {
            userStatService.increaseVideoCount(video.getUid(), 1);
        } else if (before && !after) {
            userStatService.increaseVideoCount(video.getUid(), -1);
        }
        return getVideoDetail(videoId);
    }

    @Override
    public VideoDetailVO getVideoDetail(Long videoId) {
        Video video = getById(videoId);
        if (video == null || value(video.getStatus()) == VideoStatusEnum.DELETED.getCode()) {
            throw new BusinessException(ApiCodeEnum.BAD_REQUEST, "Video not found");
        }
        Long currentUserId = currentUserIdOrNull();
        if (!Objects.equals(currentUserId, video.getUid()) && !isPublishedForPublic(video)) {
            throw new BusinessException(ApiCodeEnum.FORBIDDEN, "Video is not visible");
        }
        UserInfo userInfo = userInfoService.getById(video.getUid());
        UserProfile profile = userProfileService.getById(video.getUid());
        return VideoDetailVO.builder()
                .vid(video.getVid())
                .title(video.getTitle())
                .subtitle(video.getSubtitle())
                .coverUrl(video.getCoverUrl())
                .description(video.getDescription())
                .visibility(video.getVisibility())
                .publishStatus(video.getPublishStatus())
                .durationSec(video.getDurationSec())
                .publishTime(video.getPublishTime())
                .author(buildAuthor(userInfo, profile, video.getUid()))
                .categoryTag(buildCategoryTag(videoId))
                .parts(videoPartService.listVideoParts(videoId))
                .seriesList(loadSeriesList(videoId, currentUserId))
                .stats(videoStatService.getVideoStat(videoId))
                .build();
    }

    @Override
    public PageResponse<VideoCardVO> listHomeVideos(VideoQueryRequest request) {
        Page<Video> page = page(buildPage(request), Wrappers.<Video>lambdaQuery()
                .eq(Video::getStatus, VideoStatusEnum.NORMAL.getCode())
                .eq(Video::getPublishStatus, VideoPublishStatusEnum.PUBLISHED.getCode())
                .eq(Video::getVisibility, VideoVisibilityEnum.PUBLIC.getCode())
                .orderByDesc(isHotOrder(request), Video::getPublishTime)
                .orderByDesc(!isHotOrder(request), Video::getPublishTime));
        return PageResponse.<VideoCardVO>builder()
                .records(toVideoCards(page.getRecords()))
                .total(page.getTotal())
                .pageNum(page.getCurrent())
                .pageSize(page.getSize())
                .build();
    }

    @Override
    public PageResponse<VideoCardVO> listZoneVideos(Long zoneId, VideoQueryRequest request) {
        List<Long> videoIds = videoCategoryMapper.selectList(Wrappers.<VideoCategory>lambdaQuery()
                        .eq(VideoCategory::getCategoryId, zoneId)
                        .orderByDesc(VideoCategory::getVid))
                .stream()
                .map(VideoCategory::getVid)
                .distinct()
                .toList();
        if (videoIds.isEmpty()) {
            return emptyPage(request);
        }
        Page<Video> page = page(buildPage(request), Wrappers.<Video>lambdaQuery()
                .in(Video::getVid, videoIds)
                .eq(Video::getStatus, VideoStatusEnum.NORMAL.getCode())
                .eq(Video::getPublishStatus, VideoPublishStatusEnum.PUBLISHED.getCode())
                .eq(Video::getVisibility, VideoVisibilityEnum.PUBLIC.getCode())
                .orderByDesc(Video::getPublishTime));
        return PageResponse.<VideoCardVO>builder()
                .records(toVideoCards(page.getRecords()))
                .total(page.getTotal())
                .pageNum(page.getCurrent())
                .pageSize(page.getSize())
                .build();
    }

    @Override
    public PageResponse<VideoCardVO> listUserVideos(Long uid, VideoQueryRequest request) {
        Long currentUserId = currentUserIdOrNull();
        boolean own = Objects.equals(currentUserId, uid);
        Page<Video> page = page(buildPage(request), Wrappers.<Video>lambdaQuery()
                .eq(Video::getUid, uid)
                .eq(Video::getStatus, VideoStatusEnum.NORMAL.getCode())
                .eq(!own, Video::getPublishStatus, VideoPublishStatusEnum.PUBLISHED.getCode())
                .eq(!own, Video::getVisibility, VideoVisibilityEnum.PUBLIC.getCode())
                .orderByDesc(Video::getPublishTime)
                .orderByDesc(Video::getUploadTime));
        return PageResponse.<VideoCardVO>builder()
                .records(toVideoCards(page.getRecords()))
                .total(page.getTotal())
                .pageNum(page.getCurrent())
                .pageSize(page.getSize())
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public VideoCategoryTagVO bindCategoryTags(Long videoId, BindVideoCategoryTagsRequest request) {
        getOwnedVideo(videoId);
        syncCategoryTags(videoId, request.getPrimaryCategoryId(), request.getTagIds());
        return getCategoryTags(videoId);
    }

    @Override
    public VideoCategoryTagVO getCategoryTags(Long videoId) {
        return buildCategoryTag(videoId);
    }

    @Override
    public List<VideoCardVO> listVideoCardsByIds(List<Long> videoIds) {
        if (videoIds == null || videoIds.isEmpty()) {
            return List.of();
        }
        List<Video> videos = baseMapper.selectBatchIds(videoIds);
        Map<Long, Video> videoMap = videos.stream().collect(Collectors.toMap(Video::getVid, Function.identity()));
        List<Video> ordered = videoIds.stream()
                .map(videoMap::get)
                .filter(Objects::nonNull)
                .toList();
        return toVideoCards(ordered);
    }

    private Video buildVideo(SaveVideoRequest request, Long currentUserId, Integer publishStatus) {
        Video video = new Video();
        video.setUid(currentUserId);
        video.setStatus(VideoStatusEnum.NORMAL.getCode());
        video.setPublishStatus(publishStatus);
        applyVideoRequest(video, request);
        return video;
    }

    private void applyVideoRequest(Video video, SaveVideoRequest request) {
        video.setTitle(request.getTitle().trim());
        video.setSubtitle(blankToNull(request.getSubtitle()));
        video.setSourceType(request.getSourceType());
        video.setCopyrightType(request.getCopyrightType());
        video.setCoverUrl(defaultString(request.getCoverUrl()));
        video.setDescription(blankToNull(request.getDescription()));
        video.setVisibility(request.getVisibility());
        video.setDurationSec(request.getDurationSec());
        video.setQualityScore(request.getQualityScore());
    }

    private Video getOwnedVideo(Long videoId) {
        Video video = getById(videoId);
        if (video == null || value(video.getStatus()) == VideoStatusEnum.DELETED.getCode()) {
            throw new BusinessException(ApiCodeEnum.BAD_REQUEST, "Video not found");
        }
        if (!SecurityContextUtils.getCurrentUserId().equals(video.getUid())) {
            throw new BusinessException(ApiCodeEnum.FORBIDDEN, "You can only modify your own video");
        }
        return video;
    }

    private void syncCategoryTags(Long videoId, Long categoryId, List<Long> tagIds) {
        if (categoryId != null && categoryMapper.selectById(categoryId) == null) {
            throw new BusinessException(ApiCodeEnum.BAD_REQUEST, "Category not found");
        }
        videoCategoryMapper.delete(Wrappers.<VideoCategory>lambdaQuery().eq(VideoCategory::getVid, videoId));
        if (categoryId != null) {
            VideoCategory category = new VideoCategory();
            category.setVid(videoId);
            category.setCategoryId(categoryId);
            category.setIsPrimary(1);
            videoCategoryMapper.insert(category);
        }

        videoTagMapper.delete(Wrappers.<VideoTag>lambdaQuery().eq(VideoTag::getVid, videoId));
        if (tagIds == null || tagIds.isEmpty()) {
            return;
        }
        List<Tag> tags = tagMapper.selectBatchIds(tagIds);
        Set<Long> validTagIds = tags.stream().map(Tag::getTagId).collect(Collectors.toSet());
        for (Long tagId : tagIds.stream().distinct().toList()) {
            if (!validTagIds.contains(tagId)) {
                throw new BusinessException(ApiCodeEnum.BAD_REQUEST, "Tag not found");
            }
            VideoTag videoTag = new VideoTag();
            videoTag.setVid(videoId);
            videoTag.setTagId(tagId);
            videoTag.setSource(0);
            videoTag.setWeight(java.math.BigDecimal.ONE);
            videoTagMapper.insert(videoTag);
        }
    }

    private VideoCategoryTagVO buildCategoryTag(Long videoId) {
        VideoCategory primaryCategory = videoCategoryMapper.selectOne(Wrappers.<VideoCategory>lambdaQuery()
                .eq(VideoCategory::getVid, videoId)
                .eq(VideoCategory::getIsPrimary, 1)
                .last("limit 1"));
        Category category = primaryCategory == null ? null : categoryMapper.selectById(primaryCategory.getCategoryId());
        List<VideoTag> relations = videoTagMapper.selectList(Wrappers.<VideoTag>lambdaQuery().eq(VideoTag::getVid, videoId));
        List<Long> tagIds = relations.stream().map(VideoTag::getTagId).toList();
        Map<Long, Tag> tagMap = tagIds.isEmpty()
                ? Collections.emptyMap()
                : tagMapper.selectBatchIds(tagIds).stream().collect(Collectors.toMap(Tag::getTagId, Function.identity()));
        List<String> tagNames = tagIds.stream()
                .map(tagMap::get)
                .filter(Objects::nonNull)
                .map(Tag::getTagName)
                .toList();
        return VideoCategoryTagVO.builder()
                .categoryId(category == null ? null : category.getCategoryId())
                .categoryName(category == null ? null : category.getCategoryName())
                .tagIds(tagIds)
                .tagNames(tagNames)
                .build();
    }

    private List<VideoSeriesVO> loadSeriesList(Long videoId, Long currentUserId) {
        List<VideoSeriesItem> items = videoSeriesItemMapper.selectList(Wrappers.<VideoSeriesItem>lambdaQuery()
                .eq(VideoSeriesItem::getVid, videoId));
        if (items.isEmpty()) {
            return List.of();
        }
        List<Long> seriesIds = items.stream().map(VideoSeriesItem::getSeriesId).distinct().toList();
        List<VideoSeries> seriesList = videoSeriesMapper.selectBatchIds(seriesIds);
        return seriesList.stream()
                .filter(series -> value(series.getStatus()) == VideoStatusEnum.NORMAL.getCode())
                .filter(series -> Objects.equals(currentUserId, series.getUid())
                        || value(series.getVisible()) == VideoVisibilityEnum.PUBLIC.getCode())
                .map(series -> VideoSeriesVO.builder()
                        .seriesId(series.getSeriesId())
                        .uid(series.getUid())
                        .title(series.getTitle())
                        .description(series.getDescription())
                        .coverUrl(series.getCoverUrl())
                        .visible(series.getVisible())
                        .createTime(series.getCreateTime())
                        .updateTime(series.getUpdateTime())
                        .videos(List.of())
                        .build())
                .toList();
    }

    private List<VideoCardVO> toVideoCards(Collection<Video> videos) {
        if (videos == null || videos.isEmpty()) {
            return List.of();
        }
        List<Video> videoList = new ArrayList<>(videos);
        List<Long> userIds = videoList.stream().map(Video::getUid).distinct().toList();
        List<Long> videoIds = videoList.stream().map(Video::getVid).toList();
        Map<Long, UserInfo> userInfoMap = userIds.stream()
                .map(userInfoService::getById)
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(UserInfo::getUid, Function.identity()));
        Map<Long, UserProfile> userProfileMap = userIds.stream()
                .map(userProfileService::getById)
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(UserProfile::getUid, Function.identity()));
        Map<Long, VideoStat> statMap = videoStatMapper.selectBatchIds(videoIds).stream()
                .collect(Collectors.toMap(VideoStat::getVid, Function.identity()));

        List<VideoCardVO> result = new ArrayList<>();
        for (Video video : videoList) {
            UserInfo userInfo = userInfoMap.get(video.getUid());
            UserProfile profile = userProfileMap.get(video.getUid());
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

    private AuthorSummaryVO buildAuthor(UserInfo userInfo, UserProfile profile, Long uid) {
        return AuthorSummaryVO.builder()
                .uid(uid)
                .username(userInfo == null ? null : userInfo.getUsername())
                .nickname(profile == null ? null : profile.getNickname())
                .avatarUrl(profile == null ? null : profile.getAvatarUrl())
                .build();
    }

    private Page<Video> buildPage(VideoQueryRequest request) {
        return new Page<>(request.getPageNum(), request.getPageSize());
    }

    private boolean isPublishedForPublic(Video video) {
        return value(video.getStatus()) == VideoStatusEnum.NORMAL.getCode()
                && value(video.getPublishStatus()) == VideoPublishStatusEnum.PUBLISHED.getCode()
                && value(video.getVisibility()) == VideoVisibilityEnum.PUBLIC.getCode();
    }

    private boolean isHotOrder(VideoQueryRequest request) {
        return request != null && "hot".equalsIgnoreCase(request.getOrderType());
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private String defaultString(String value) {
        return value == null || value.isBlank() ? "" : value.trim();
    }

    private int value(Integer value) {
        return value == null ? 0 : value;
    }

    private long safeLong(Long value) {
        return value == null ? 0L : value;
    }

    private PageResponse<VideoCardVO> emptyPage(VideoQueryRequest request) {
        return PageResponse.<VideoCardVO>builder()
                .records(List.of())
                .total(0L)
                .pageNum(request.getPageNum())
                .pageSize(request.getPageSize())
                .build();
    }

    private Long currentUserIdOrNull() {
        try {
            return SecurityContextUtils.getCurrentUserId();
        } catch (Exception ex) {
            return null;
        }
    }
}




