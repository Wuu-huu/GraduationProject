package com.zzk.recommendmodule.service;

import com.zzk.recommendmodule.entity.RecommendItemSimilarity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
* @author 周振坤
* @description 针对表【recommend_item_similarity(ItemCF 相似视频中间表)】的数据库操作Service
* @createDate 2026-03-22 02:59:38
*/
public interface RecommendItemSimilarityService extends IService<RecommendItemSimilarity> {

    List<RecommendItemSimilarity> listTopSimilarities(Long videoId, int limit);

    void rebuildForVideo(Long videoId, int limit);
}
