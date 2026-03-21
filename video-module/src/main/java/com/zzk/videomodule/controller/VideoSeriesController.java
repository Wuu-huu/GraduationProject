package com.zzk.videomodule.controller;

import com.zzk.common.model.response.ApiResponse;
import com.zzk.videomodule.dto.SaveVideoSeriesRequest;
import com.zzk.videomodule.service.VideoSeriesService;
import com.zzk.videomodule.vo.VideoCardVO;
import com.zzk.videomodule.vo.VideoSeriesVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "视频合集")
@RestController
@RequestMapping("/api/video-series")
public class VideoSeriesController {

    private final VideoSeriesService videoSeriesService;

    public VideoSeriesController(VideoSeriesService videoSeriesService) {
        this.videoSeriesService = videoSeriesService;
    }

    @Operation(summary = "创建合集")
    @PostMapping
    public ApiResponse<VideoSeriesVO> createSeries(@Valid @RequestBody SaveVideoSeriesRequest request) {
        return ApiResponse.success(videoSeriesService.createSeries(request));
    }

    @Operation(summary = "编辑合集")
    @PutMapping("/{seriesId}")
    public ApiResponse<VideoSeriesVO> updateSeries(@PathVariable Long seriesId,
                                                   @Valid @RequestBody SaveVideoSeriesRequest request) {
        return ApiResponse.success(videoSeriesService.updateSeries(seriesId, request));
    }

    @Operation(summary = "删除合集")
    @DeleteMapping("/{seriesId}")
    public ApiResponse<Void> deleteSeries(@PathVariable Long seriesId) {
        videoSeriesService.deleteSeries(seriesId);
        return ApiResponse.success();
    }

    @Operation(summary = "合集详情")
    @GetMapping("/{seriesId}")
    public ApiResponse<VideoSeriesVO> getSeriesDetail(@PathVariable Long seriesId) {
        return ApiResponse.success(videoSeriesService.getSeriesDetail(seriesId));
    }

    @Operation(summary = "合集视频列表")
    @GetMapping("/{seriesId}/videos")
    public ApiResponse<List<VideoCardVO>> listSeriesVideos(@PathVariable Long seriesId) {
        return ApiResponse.success(videoSeriesService.getSeriesDetail(seriesId).getVideos());
    }
}
