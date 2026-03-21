package com.zzk.recommendmodule.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzk.recommendmodule.entity.RecommendItemSimilarity;
import com.zzk.recommendmodule.mapper.RecommendItemSimilarityMapper;
import com.zzk.recommendmodule.mapper.RecommendVideoActionReadMapper;
import com.zzk.recommendmodule.service.RecommendItemSimilarityService;
import com.zzk.interactionmodule.entity.VideoAction;
import com.zzk.interactionmodule.enums.VideoActionTypeEnum;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @author 周振坤
* @description 针对表【recommend_item_similarity(ItemCF 相似视频中间表)】的数据库操作Service实现
* @createDate 2026-03-22 02:59:38
*/
@Service
public class RecommendItemSimilarityServiceImpl extends ServiceImpl<RecommendItemSimilarityMapper, RecommendItemSimilarity>
    implements RecommendItemSimilarityService{

    private final RecommendVideoActionReadMapper videoActionReadMapper;

    public RecommendItemSimilarityServiceImpl(RecommendVideoActionReadMapper videoActionReadMapper) {
        this.videoActionReadMapper = videoActionReadMapper;
    }

    @Override
    public List<RecommendItemSimilarity> listTopSimilarities(Long videoId, int limit) {
        return list(Wrappers.<RecommendItemSimilarity>lambdaQuery()
                .eq(RecommendItemSimilarity::getVid, videoId)
                .orderByDesc(RecommendItemSimilarity::getScore)
                .last("limit " + Math.max(1, limit)));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rebuildForVideo(Long videoId, int limit) {
        List<VideoAction> seedActions = videoActionReadMapper.selectList(Wrappers.<VideoAction>lambdaQuery()
                .eq(VideoAction::getVid, videoId)
                .eq(VideoAction::getCancelFlag, 0)
                .in(VideoAction::getActionType, positiveActionTypes()));
        Set<Long> userIds = seedActions.stream().map(VideoAction::getUid).collect(Collectors.toSet());
        remove(Wrappers.<RecommendItemSimilarity>lambdaQuery().eq(RecommendItemSimilarity::getVid, videoId));
        if (userIds.isEmpty()) {
            return;
        }

        List<VideoAction> relatedActions = videoActionReadMapper.selectList(Wrappers.<VideoAction>lambdaQuery()
                .in(VideoAction::getUid, userIds)
                .eq(VideoAction::getCancelFlag, 0)
                .in(VideoAction::getActionType, positiveActionTypes())
                .ne(VideoAction::getVid, videoId));
        Map<Long, Double> scoreMap = new HashMap<>();
        for (VideoAction action : relatedActions) {
            scoreMap.merge(action.getVid(), actionWeight(action.getActionType()), Double::sum);
        }
        List<Map.Entry<Long, Double>> topEntries = scoreMap.entrySet().stream()
                .sorted((left, right) -> Double.compare(right.getValue(), left.getValue()))
                .limit(Math.max(1, limit))
                .toList();
        Date now = new Date();
        List<RecommendItemSimilarity> items = new ArrayList<>();
        for (Map.Entry<Long, Double> entry : topEntries) {
            RecommendItemSimilarity item = new RecommendItemSimilarity();
            item.setVid(videoId);
            item.setRelatedVid(entry.getKey());
            item.setScore(BigDecimal.valueOf(entry.getValue()).setScale(6, RoundingMode.HALF_UP));
            item.setSource("ITEM_CF");
            item.setUpdateTime(now);
            items.add(item);
        }
        saveBatch(items);
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

    private double actionWeight(Integer actionType) {
        if (actionType == null) {
            return 1D;
        }
        if (actionType == VideoActionTypeEnum.COIN.getCode()) {
            return 4D;
        }
        if (actionType == VideoActionTypeEnum.FAVORITE.getCode()) {
            return 3.5D;
        }
        if (actionType == VideoActionTypeEnum.LIKE.getCode()) {
            return 3D;
        }
        if (actionType == VideoActionTypeEnum.COMMENT.getCode()) {
            return 2.5D;
        }
        if (actionType == VideoActionTypeEnum.WATCH_LATER.getCode()) {
            return 2D;
        }
        if (actionType == VideoActionTypeEnum.DANMU.getCode()) {
            return 1.5D;
        }
        return 1D;
    }
}




