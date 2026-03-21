package com.zzk.videomodule.controller;

import com.zzk.common.model.page.PageResponse;
import com.zzk.common.model.response.ApiResponse;
import com.zzk.videomodule.dto.BindVideoCategoryTagsRequest;
import com.zzk.videomodule.dto.SaveVideoRequest;
import com.zzk.videomodule.dto.UpdateVideoRequest;
import com.zzk.videomodule.dto.UpdateVideoStatusRequest;
import com.zzk.videomodule.dto.VideoQueryRequest;
import com.zzk.videomodule.service.VideoService;
import com.zzk.videomodule.service.VideoStatService;
import com.zzk.videomodule.vo.VideoCardVO;
import com.zzk.videomodule.vo.VideoCategoryTagVO;
import com.zzk.videomodule.vo.VideoDetailVO;
import com.zzk.videomodule.vo.VideoStatVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "视频内容")
@RestController
@RequestMapping("/api")
public class VideoController {

    private final VideoService videoService;
    private final VideoStatService videoStatService;

    public VideoController(VideoService videoService, VideoStatService videoStatService) {
        this.videoService = videoService;
        this.videoStatService = videoStatService;
    }

    @Operation(summary = "发布视频")
    @PostMapping("/videos")
    public ApiResponse<VideoDetailVO> publishVideo(@Valid @RequestBody SaveVideoRequest request) {
        log.info("Receive publish video request");
        return ApiResponse.success(videoService.publishVideo(request));
    }

    @Operation(summary = "保存草稿")
    @PostMapping("/videos/drafts")
    public ApiResponse<VideoDetailVO> saveDraft(@Valid @RequestBody SaveVideoRequest request) {
        log.info("Receive save draft request");
        return ApiResponse.success(videoService.saveDraft(request));
    }

    @Operation(summary = "编辑视频")
    @PutMapping("/videos/{videoId}")
    public ApiResponse<VideoDetailVO> updateVideo(@PathVariable Long videoId,
                                                  @Valid @RequestBody UpdateVideoRequest request) {
        log.info("Receive update video request, videoId={}", videoId);
        return ApiResponse.success(videoService.updateVideo(videoId, request));
    }

    @Operation(summary = "删除视频")
    @DeleteMapping("/videos/{videoId}")
    public ApiResponse<Void> deleteVideo(@PathVariable Long videoId) {
        log.info("Receive delete video request, videoId={}", videoId);
        videoService.deleteVideo(videoId);
        return ApiResponse.success();
    }

    @Operation(summary = "更新视频发布状态")
    @PutMapping("/videos/{videoId}/status")
    public ApiResponse<VideoDetailVO> updateVideoStatus(@PathVariable Long videoId,
                                                        @Valid @RequestBody UpdateVideoStatusRequest request) {
        log.info("Receive update video status request, videoId={}", videoId);
        return ApiResponse.success(videoService.updateVideoStatus(videoId, request));
    }

    @Operation(summary = "查询视频详情")
    @GetMapping("/videos/{videoId}")
    public ApiResponse<VideoDetailVO> getVideoDetail(@PathVariable Long videoId) {
        return ApiResponse.success(videoService.getVideoDetail(videoId));
    }

    @Operation(summary = "首页视频列表")
    @GetMapping("/videos")
    public ApiResponse<PageResponse<VideoCardVO>> listVideos(@Valid VideoQueryRequest request) {
        return ApiResponse.success(videoService.listHomeVideos(request));
    }

    @Operation(summary = "分区视频列表")
    @GetMapping("/videos/zone/{zoneId}")
    public ApiResponse<PageResponse<VideoCardVO>> listZoneVideos(@PathVariable Long zoneId,
                                                                 @Valid VideoQueryRequest request) {
        return ApiResponse.success(videoService.listZoneVideos(zoneId, request));
    }

    @Operation(summary = "用户投稿列表")
    @GetMapping("/users/{uid}/videos")
    public ApiResponse<PageResponse<VideoCardVO>> listUserVideos(@PathVariable Long uid,
                                                                 @Valid VideoQueryRequest request) {
        return ApiResponse.success(videoService.listUserVideos(uid, request));
    }

    @Operation(summary = "绑定视频分类与标签")
    @PutMapping("/videos/{videoId}/category-tags")
    public ApiResponse<VideoCategoryTagVO> bindCategoryTags(@PathVariable Long videoId,
                                                            @Valid @RequestBody BindVideoCategoryTagsRequest request) {
        return ApiResponse.success(videoService.bindCategoryTags(videoId, request));
    }

    @Operation(summary = "获取视频分类与标签")
    @GetMapping("/videos/{videoId}/category-tags")
    public ApiResponse<VideoCategoryTagVO> getCategoryTags(@PathVariable Long videoId) {
        return ApiResponse.success(videoService.getCategoryTags(videoId));
    }

    @Operation(summary = "查询视频统计")
    @GetMapping("/videos/{videoId}/stats")
    public ApiResponse<VideoStatVO> getVideoStats(@PathVariable Long videoId) {
        return ApiResponse.success(videoStatService.getVideoStat(videoId));
    }
}
