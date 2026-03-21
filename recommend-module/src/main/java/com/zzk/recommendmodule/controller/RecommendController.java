package com.zzk.recommendmodule.controller;

import com.zzk.common.model.page.PageResponse;
import com.zzk.common.model.response.ApiResponse;
import com.zzk.recommendmodule.dto.RecommendQueryRequest;
import com.zzk.recommendmodule.service.RecommendationService;
import com.zzk.recommendmodule.vo.RecommendVideoVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "推荐服务")
@RestController
@RequestMapping("/api/recommend")
public class RecommendController {

    private final RecommendationService recommendationService;

    public RecommendController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @Operation(summary = "首页推荐")
    @GetMapping("/home")
    public ApiResponse<PageResponse<RecommendVideoVO>> listHomeRecommendations(@Valid RecommendQueryRequest request) {
        return ApiResponse.success(recommendationService.listHomeRecommendations(request));
    }

    @Operation(summary = "相关推荐")
    @GetMapping("/videos/{videoId}/related")
    public ApiResponse<PageResponse<RecommendVideoVO>> listRelatedRecommendations(@PathVariable Long videoId,
                                                                                  @Valid RecommendQueryRequest request) {
        return ApiResponse.success(recommendationService.listRelatedRecommendations(videoId, request));
    }

    @Operation(summary = "分区推荐")
    @GetMapping("/zones/{zoneId}")
    public ApiResponse<PageResponse<RecommendVideoVO>> listZoneRecommendations(@PathVariable Long zoneId,
                                                                               @Valid RecommendQueryRequest request) {
        return ApiResponse.success(recommendationService.listZoneRecommendations(zoneId, request));
    }

    @Operation(summary = "热门榜")
    @GetMapping("/hot")
    public ApiResponse<PageResponse<RecommendVideoVO>> listHotVideos(@Valid RecommendQueryRequest request) {
        return ApiResponse.success(recommendationService.listHotVideos(request));
    }

    @Operation(summary = "重建单视频 ItemCF 相似度")
    @PostMapping("/admin/itemcf/{videoId}/rebuild")
    public ApiResponse<Void> rebuildItemSimilarity(@PathVariable Long videoId) {
        recommendationService.rebuildItemSimilarity(videoId);
        return ApiResponse.success();
    }
}