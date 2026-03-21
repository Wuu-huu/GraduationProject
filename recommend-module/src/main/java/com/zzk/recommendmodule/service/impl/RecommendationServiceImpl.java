package com.zzk.recommendmodule.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zzk.common.exception.ForbiddenException;
import com.zzk.common.model.page.PageResponse;
import com.zzk.common.security.SecurityContextUtils;
import com.zzk.interactionmodule.entity.VideoAction;
import com.zzk.interactionmodule.enums.VideoActionTypeEnum;
import com.zzk.recommendmodule.config.RecommendProperties;
import com.zzk.recommendmodule.dto.RecommendQueryRequest;
import com.zzk.recommendmodule.entity.RecommendItemSimilarity;
import com.zzk.recommendmodule.entity.RecommendRequest;
import com.zzk.recommendmodule.enums.RecommendSceneEnum;
import com.zzk.recommendmodule.enums.RecommendSourceEnum;
import com.zzk.recommendmodule.mapper.RecommendVideoActionReadMapper;
import com.zzk.recommendmodule.mapper.RecommendVideoCategoryReadMapper;
import com.zzk.recommendmodule.mapper.RecommendVideoReadMapper;
import com.zzk.recommendmodule.mapper.RecommendVideoStatReadMapper;
import com.zzk.recommendmodule.mapper.RecommendVideoTagReadMapper;
import com.zzk.recommendmodule.service.RecommendItemSimilarityService;
import com.zzk.recommendmodule.service.RecommendRequestService;
import com.zzk.recommendmodule.service.RecommendResultService;
import com.zzk.recommendmodule.service.RecommendationService;
import com.zzk.recommendmodule.service.VideoExposureLogService;
import com.zzk.recommendmodule.vo.RecommendVideoVO;
import com.zzk.videomodule.entity.Video;
import com.zzk.videomodule.entity.VideoCategory;
import com.zzk.videomodule.entity.VideoStat;
import com.zzk.videomodule.entity.VideoTag;
import com.zzk.videomodule.enums.VideoPublishStatusEnum;
import com.zzk.videomodule.enums.VideoStatusEnum;
import com.zzk.videomodule.enums.VideoVisibilityEnum;
import com.zzk.videomodule.facade.VideoFacade;
import com.zzk.videomodule.vo.VideoCardVO;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RecommendationServiceImpl implements RecommendationService {

    private final RecommendProperties recommendProperties;
    private final RecommendRequestService recommendRequestService;
    private final RecommendResultService recommendResultService;
    private final VideoExposureLogService videoExposureLogService;
    private final RecommendItemSimilarityService recommendItemSimilarityService;
    private final RecommendVideoReadMapper videoReadMapper;
    private final RecommendVideoStatReadMapper videoStatReadMapper;
    private final RecommendVideoCategoryReadMapper videoCategoryReadMapper;
    private final RecommendVideoTagReadMapper videoTagReadMapper;
    private final RecommendVideoActionReadMapper videoActionReadMapper;
    private final VideoFacade videoFacade;

    public RecommendationServiceImpl(RecommendProperties recommendProperties,
                                     RecommendRequestService recommendRequestService,
                                     RecommendResultService recommendResultService,
                                     VideoExposureLogService videoExposureLogService,
                                     RecommendItemSimilarityService recommendItemSimilarityService,
                                     RecommendVideoReadMapper videoReadMapper,
                                     RecommendVideoStatReadMapper videoStatReadMapper,
                                     RecommendVideoCategoryReadMapper videoCategoryReadMapper,
                                     RecommendVideoTagReadMapper videoTagReadMapper,
                                     RecommendVideoActionReadMapper videoActionReadMapper,
                                     VideoFacade videoFacade) {
        this.recommendProperties = recommendProperties;
        this.recommendRequestService = recommendRequestService;
        this.recommendResultService = recommendResultService;
        this.videoExposureLogService = videoExposureLogService;
        this.recommendItemSimilarityService = recommendItemSimilarityService;
        this.videoReadMapper = videoReadMapper;
        this.videoStatReadMapper = videoStatReadMapper;
        this.videoCategoryReadMapper = videoCategoryReadMapper;
        this.videoTagReadMapper = videoTagReadMapper;
        this.videoActionReadMapper = videoActionReadMapper;
        this.videoFacade = videoFacade;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PageResponse<RecommendVideoVO> listHomeRecommendations(RecommendQueryRequest request) {
        Long loginUid = currentUserIdOrNull();
        RecommendRequest track = recommendRequestService.createRequest(loginUid,
                RecommendSceneEnum.HOME.getCode(), request.getPageNum(), "PC", new Date());
        List<ScoredCandidate> candidates = new ArrayList<>();
        candidates.addAll(buildHotCandidates(recommendProperties.getHotCandidateSize(), RecommendSourceEnum.HOT));
        candidates.addAll(buildLatestCandidates(recommendProperties.getLatestCandidateSize()));
        if (loginUid != null) {
            candidates.addAll(buildUserPreferenceCandidates(loginUid));
        }
        return finalizePage(track, RecommendSceneEnum.HOME.getCode(), request, candidates, Collections.emptySet());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PageResponse<RecommendVideoVO> listRelatedRecommendations(Long videoId, RecommendQueryRequest request) {
        RecommendRequest track = recommendRequestService.createRequest(currentUserIdOrNull(),
                RecommendSceneEnum.RELATED.getCode(), request.getPageNum(), "PC", new Date());
        List<ScoredCandidate> candidates = new ArrayList<>();
        candidates.addAll(buildItemCfCandidates(List.of(videoId), recommendProperties.getRelatedCandidateSize(), Set.of(videoId)));
        candidates.addAll(buildSameCategoryCandidates(videoId));
        candidates.addAll(buildSameTagCandidates(videoId));
        candidates.addAll(buildHotCandidates(recommendProperties.getHotCandidateSize() / 2, RecommendSourceEnum.FALLBACK));
        return finalizePage(track, RecommendSceneEnum.RELATED.getCode(), request, candidates, Set.of(videoId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PageResponse<RecommendVideoVO> listZoneRecommendations(Long zoneId, RecommendQueryRequest request) {
        RecommendRequest track = recommendRequestService.createRequest(currentUserIdOrNull(),
                RecommendSceneEnum.ZONE.getCode(), request.getPageNum(), "PC", new Date());
        return finalizePage(track, RecommendSceneEnum.ZONE.getCode(), request, buildZoneCandidates(zoneId), Collections.emptySet());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PageResponse<RecommendVideoVO> listHotVideos(RecommendQueryRequest request) {
        RecommendRequest track = recommendRequestService.createRequest(currentUserIdOrNull(),
                RecommendSceneEnum.HOT.getCode(), request.getPageNum(), "PC", new Date());
        return finalizePage(track, RecommendSceneEnum.HOT.getCode(), request,
                buildHotCandidates(recommendProperties.getHotCandidateSize(), RecommendSourceEnum.HOT), Collections.emptySet());
    }

    @Override
    public void rebuildItemSimilarity(Long videoId) {
        if (!SecurityContextUtils.getLoginUser().isAdmin()) {
            throw new ForbiddenException("仅管理员可执行该操作");
        }
        recommendItemSimilarityService.rebuildForVideo(videoId, recommendProperties.getItemCfTopN());
    }

    private PageResponse<RecommendVideoVO> finalizePage(RecommendRequest track,
                                                        String scene,
                                                        RecommendQueryRequest request,
                                                        List<ScoredCandidate> candidates,
                                                        Set<Long> excludeIds) {
        Map<Long, ScoredCandidate> merged = mergeCandidates(candidates, excludeIds);
        List<Long> orderedIds = merged.values().stream()
                .sorted(Comparator.comparing(ScoredCandidate::score).reversed())
                .map(ScoredCandidate::videoId)
                .toList();
        if (orderedIds.isEmpty()) {
            return PageResponse.<RecommendVideoVO>builder()
                    .records(List.of())
                    .total(0L)
                    .pageNum(defaultPageNum(request))
                    .pageSize(defaultPageSize(request))
                    .build();
        }
        long pageNum = defaultPageNum(request);
        long pageSize = defaultPageSize(request);
        int fromIndex = (int) Math.min((pageNum - 1) * pageSize, orderedIds.size());
        int toIndex = (int) Math.min(fromIndex + pageSize, orderedIds.size());
        List<Long> pageIds = orderedIds.subList(fromIndex, toIndex);
        Map<Long, VideoCardVO> cardMap = videoFacade.listVideoCards(pageIds).stream()
                .collect(Collectors.toMap(VideoCardVO::getVid, Function.identity()));
        List<RecommendVideoVO> records = new ArrayList<>();
        for (int index = 0; index < pageIds.size(); index++) {
            Long videoId = pageIds.get(index);
            VideoCardVO card = cardMap.get(videoId);
            ScoredCandidate candidate = merged.get(videoId);
            if (card == null || candidate == null) {
                continue;
            }
            records.add(RecommendVideoVO.builder()
                    .vid(card.getVid())
                    .title(card.getTitle())
                    .subtitle(card.getSubtitle())
                    .coverUrl(card.getCoverUrl())
                    .durationSec(card.getDurationSec())
                    .authorUid(card.getAuthorUid())
                    .authorName(card.getAuthorName())
                    .authorAvatarUrl(card.getAuthorAvatarUrl())
                    .playCount(card.getPlayCount())
                    .likeCount(card.getLikeCount())
                    .commentCount(card.getCommentCount())
                    .favoriteCount(card.getFavoriteCount())
                    .publishTime(card.getPublishTime())
                    .source(candidate.source())
                    .score(scale(candidate.score()))
                    .positionNo(fromIndex + index + 1)
                    .build());
        }
        if (recommendProperties.isTrackingEnabled()) {
            Date now = new Date();
            recommendResultService.saveResults(track.getRequestId(), track.getUid(), records, now);
            videoExposureLogService.saveExposureLogs(track.getRequestId(), track.getUid(), scene, records, now);
        }
        return PageResponse.<RecommendVideoVO>builder()
                .records(records)
                .total(merged.size())
                .pageNum(pageNum)
                .pageSize(pageSize)
                .build();
    }

    private Map<Long, ScoredCandidate> mergeCandidates(List<ScoredCandidate> candidates, Set<Long> excludeIds) {
        Map<Long, ScoredCandidate> merged = new LinkedHashMap<>();
        Set<Long> validVideoIds = listVisibleVideos(candidates.stream().map(ScoredCandidate::videoId).toList())
                .stream()
                .map(Video::getVid)
                .collect(Collectors.toSet());
        for (ScoredCandidate candidate : candidates) {
            if (candidate == null || excludeIds.contains(candidate.videoId()) || !validVideoIds.contains(candidate.videoId())) {
                continue;
            }
            ScoredCandidate existing = merged.get(candidate.videoId());
            if (existing == null) {
                merged.put(candidate.videoId(), candidate);
                continue;
            }
            double combinedScore = existing.score() + candidate.score();
            String source = existing.score() >= candidate.score() ? existing.source() : candidate.source();
            merged.put(candidate.videoId(), new ScoredCandidate(candidate.videoId(), source, combinedScore));
        }
        return merged;
    }

    private List<ScoredCandidate> buildHotCandidates(int limit, RecommendSourceEnum source) {
        List<VideoStat> stats = videoStatReadMapper.selectList(Wrappers.<VideoStat>lambdaQuery()
                .orderByDesc(VideoStat::getHotScore)
                .orderByDesc(VideoStat::getPlayCount)
                .last("limit " + Math.max(limit, 1)));
        return stats.stream()
                .map(stat -> new ScoredCandidate(stat.getVid(), source.getCode(),
                        weight(stat.getHotScore(), recommendProperties.getHotWeight())))
                .toList();
    }

    private List<ScoredCandidate> buildLatestCandidates(int limit) {
        List<Video> videos = videoReadMapper.selectList(Wrappers.<Video>lambdaQuery()
                .eq(Video::getStatus, VideoStatusEnum.NORMAL.getCode())
                .eq(Video::getPublishStatus, VideoPublishStatusEnum.PUBLISHED.getCode())
                .eq(Video::getVisibility, VideoVisibilityEnum.PUBLIC.getCode())
                .orderByDesc(Video::getPublishTime)
                .last("limit " + Math.max(limit, 1)));
        return videos.stream()
                .map(video -> new ScoredCandidate(video.getVid(), RecommendSourceEnum.LATEST.getCode(),
                        freshnessScore(video.getPublishTime()) * recommendProperties.getFreshnessWeight()))
                .toList();
    }

    private List<ScoredCandidate> buildZoneCandidates(Long zoneId) {
        List<VideoCategory> relations = videoCategoryReadMapper.selectList(Wrappers.<VideoCategory>lambdaQuery()
                .eq(VideoCategory::getCategoryId, zoneId));
        if (relations.isEmpty()) {
            return List.of();
        }
        Set<Long> zoneVideoIds = relations.stream().map(VideoCategory::getVid).collect(Collectors.toSet());
        Map<Long, VideoStat> statMap = videoStatReadMapper.selectBatchIds(zoneVideoIds).stream()
                .collect(Collectors.toMap(VideoStat::getVid, Function.identity()));
        List<ScoredCandidate> candidates = new ArrayList<>();
        for (Long videoId : zoneVideoIds) {
            VideoStat stat = statMap.get(videoId);
            double score = weight(stat == null ? null : stat.getHotScore(), recommendProperties.getHotWeight());
            candidates.add(new ScoredCandidate(videoId, RecommendSourceEnum.ZONE_HOT.getCode(), score));
        }
        return candidates;
    }

    private List<ScoredCandidate> buildUserPreferenceCandidates(Long uid) {
        List<Long> recentVideoIds = listRecentPositiveActionVideoIds(uid);
        if (recentVideoIds.isEmpty()) {
            return List.of();
        }
        Map<Long, Double> categoryScores = loadCategoryPreferenceScores(recentVideoIds);
        Map<Long, Double> tagScores = loadTagPreferenceScores(recentVideoIds);
        List<ScoredCandidate> candidates = new ArrayList<>();

        if (!categoryScores.isEmpty()) {
            List<VideoCategory> allCategories = videoCategoryReadMapper.selectList(Wrappers.<VideoCategory>lambdaQuery()
                    .eq(VideoCategory::getIsPrimary, 1));
            for (VideoCategory relation : allCategories) {
                Double score = categoryScores.get(relation.getCategoryId());
                if (score != null) {
                    candidates.add(new ScoredCandidate(relation.getVid(), RecommendSourceEnum.CATEGORY_PREF.getCode(),
                            score * recommendProperties.getCategoryWeight()));
                }
            }
        }

        if (!tagScores.isEmpty()) {
            List<VideoTag> allTags = videoTagReadMapper.selectList(Wrappers.<VideoTag>lambdaQuery());
            for (VideoTag relation : allTags) {
                Double score = tagScores.get(relation.getTagId());
                if (score != null) {
                    candidates.add(new ScoredCandidate(relation.getVid(), RecommendSourceEnum.TAG_PREF.getCode(),
                            score * recommendProperties.getTagWeight()));
                }
            }
        }

        candidates.addAll(buildItemCfCandidates(recentVideoIds, recommendProperties.getItemCfTopN(), new HashSet<>(recentVideoIds)));
        return candidates;
    }

    private List<ScoredCandidate> buildItemCfCandidates(List<Long> seedVideoIds, int limit, Set<Long> excludeIds) {
        List<ScoredCandidate> candidates = new ArrayList<>();
        for (Long seedVideoId : seedVideoIds) {
            List<RecommendItemSimilarity> items = recommendItemSimilarityService.listTopSimilarities(seedVideoId, limit);
            if (items.isEmpty()) {
                recommendItemSimilarityService.rebuildForVideo(seedVideoId, limit);
                items = recommendItemSimilarityService.listTopSimilarities(seedVideoId, limit);
            }
            for (RecommendItemSimilarity item : items) {
                if (!excludeIds.contains(item.getRelatedVid())) {
                    candidates.add(new ScoredCandidate(item.getRelatedVid(), RecommendSourceEnum.ITEM_CF.getCode(),
                            (item.getScore() == null ? 0D : item.getScore().doubleValue()) * recommendProperties.getItemCfWeight()));
                }
            }
        }
        return candidates;
    }

    private List<ScoredCandidate> buildSameCategoryCandidates(Long videoId) {
        VideoCategory category = videoCategoryReadMapper.selectOne(Wrappers.<VideoCategory>lambdaQuery()
                .eq(VideoCategory::getVid, videoId)
                .eq(VideoCategory::getIsPrimary, 1)
                .last("limit 1"));
        if (category == null) {
            return List.of();
        }
        return videoCategoryReadMapper.selectList(Wrappers.<VideoCategory>lambdaQuery()
                        .eq(VideoCategory::getCategoryId, category.getCategoryId()))
                .stream()
                .filter(item -> !Objects.equals(item.getVid(), videoId))
                .map(item -> new ScoredCandidate(item.getVid(), RecommendSourceEnum.CATEGORY_PREF.getCode(),
                        recommendProperties.getCategoryWeight()))
                .toList();
    }

    private List<ScoredCandidate> buildSameTagCandidates(Long videoId) {
        List<VideoTag> tags = videoTagReadMapper.selectList(Wrappers.<VideoTag>lambdaQuery().eq(VideoTag::getVid, videoId));
        if (tags.isEmpty()) {
            return List.of();
        }
        Set<Long> tagIds = tags.stream().map(VideoTag::getTagId).collect(Collectors.toSet());
        List<VideoTag> relatedTags = videoTagReadMapper.selectList(Wrappers.<VideoTag>lambdaQuery().in(VideoTag::getTagId, tagIds));
        Map<Long, Long> matchCount = relatedTags.stream()
                .filter(tag -> !Objects.equals(tag.getVid(), videoId))
                .collect(Collectors.groupingBy(VideoTag::getVid, Collectors.counting()));
        return matchCount.entrySet().stream()
                .map(entry -> new ScoredCandidate(entry.getKey(), RecommendSourceEnum.TAG_PREF.getCode(),
                        entry.getValue() * recommendProperties.getTagWeight()))
                .toList();
    }

    private List<Long> listRecentPositiveActionVideoIds(Long uid) {
        return videoActionReadMapper.selectList(Wrappers.<VideoAction>lambdaQuery()
                        .eq(VideoAction::getUid, uid)
                        .eq(VideoAction::getCancelFlag, 0)
                        .in(VideoAction::getActionType, positiveActionTypes())
                        .orderByDesc(VideoAction::getActionTime)
                        .last("limit 50"))
                .stream()
                .map(VideoAction::getVid)
                .distinct()
                .toList();
    }

    private Map<Long, Double> loadCategoryPreferenceScores(List<Long> videoIds) {
        return videoCategoryReadMapper.selectList(Wrappers.<VideoCategory>lambdaQuery()
                        .in(VideoCategory::getVid, videoIds)
                        .eq(VideoCategory::getIsPrimary, 1))
                .stream()
                .collect(Collectors.groupingBy(VideoCategory::getCategoryId, Collectors.counting()))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().doubleValue()));
    }

    private Map<Long, Double> loadTagPreferenceScores(List<Long> videoIds) {
        return videoTagReadMapper.selectList(Wrappers.<VideoTag>lambdaQuery().in(VideoTag::getVid, videoIds))
                .stream()
                .collect(Collectors.groupingBy(VideoTag::getTagId, Collectors.counting()))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().doubleValue()));
    }

    private List<Video> listVisibleVideos(Collection<Long> videoIds) {
        if (videoIds == null || videoIds.isEmpty()) {
            return List.of();
        }
        return videoReadMapper.selectList(Wrappers.<Video>lambdaQuery()
                .in(Video::getVid, videoIds)
                .eq(Video::getStatus, VideoStatusEnum.NORMAL.getCode())
                .eq(Video::getPublishStatus, VideoPublishStatusEnum.PUBLISHED.getCode())
                .eq(Video::getVisibility, VideoVisibilityEnum.PUBLIC.getCode()));
    }

    private List<Integer> positiveActionTypes() {
        return List.of(
                VideoActionTypeEnum.LIKE.getCode(),
                VideoActionTypeEnum.COIN.getCode(),
                VideoActionTypeEnum.FAVORITE.getCode(),
                VideoActionTypeEnum.WATCH_LATER.getCode(),
                VideoActionTypeEnum.COMMENT.getCode(),
                VideoActionTypeEnum.DANMU.getCode()
        );
    }

    private Long currentUserIdOrNull() {
        try {
            return SecurityContextUtils.getCurrentUserId();
        } catch (Exception ex) {
            return null;
        }
    }

    private long defaultPageNum(RecommendQueryRequest request) {
        return request.getPageNum() == null ? 1L : request.getPageNum();
    }

    private long defaultPageSize(RecommendQueryRequest request) {
        long size = request.getPageSize() == null ? recommendProperties.getDefaultPageSize() : request.getPageSize();
        return Math.max(1, Math.min(size, recommendProperties.getMaxPageSize()));
    }

    private double freshnessScore(Date publishTime) {
        if (publishTime == null) {
            return 0D;
        }
        long hours = Math.max(1L, Duration.between(publishTime.toInstant(), Instant.now()).toHours());
        return 1D / hours;
    }

    private double weight(BigDecimal value, double factor) {
        return value == null ? 0D : value.doubleValue() * factor;
    }

    private double scale(double value) {
        return BigDecimal.valueOf(value).setScale(4, RoundingMode.HALF_UP).doubleValue();
    }

    private record ScoredCandidate(Long videoId, String source, double score) {
    }
}
