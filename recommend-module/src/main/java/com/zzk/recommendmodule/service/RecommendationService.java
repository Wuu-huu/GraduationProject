package com.zzk.recommendmodule.service;

import com.zzk.common.model.page.PageResponse;
import com.zzk.recommendmodule.dto.RecommendQueryRequest;
import com.zzk.recommendmodule.vo.RecommendVideoVO;

public interface RecommendationService {

    PageResponse<RecommendVideoVO> listHomeRecommendations(RecommendQueryRequest request);

    PageResponse<RecommendVideoVO> listRelatedRecommendations(Long videoId, RecommendQueryRequest request);

    PageResponse<RecommendVideoVO> listZoneRecommendations(Long zoneId, RecommendQueryRequest request);

    PageResponse<RecommendVideoVO> listHotVideos(RecommendQueryRequest request);

    void rebuildItemSimilarity(Long videoId);
}
