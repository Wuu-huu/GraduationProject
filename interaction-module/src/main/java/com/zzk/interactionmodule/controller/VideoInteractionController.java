package com.zzk.interactionmodule.controller;

import com.zzk.common.model.response.ApiResponse;
import com.zzk.interactionmodule.dto.CoinVideoRequest;
import com.zzk.interactionmodule.dto.FavoriteVideoRequest;
import com.zzk.interactionmodule.service.VideoActionService;
import com.zzk.interactionmodule.vo.UserVideoStateVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "视频互动")
@RestController
@RequestMapping("/api/interactions/videos/{videoId}")
public class VideoInteractionController {

    private final VideoActionService videoActionService;

    public VideoInteractionController(VideoActionService videoActionService) {
        this.videoActionService = videoActionService;
    }

    @Operation(summary = "点赞视频")
    @PostMapping("/like")
    public ApiResponse<UserVideoStateVO> likeVideo(@PathVariable Long videoId) {
        return ApiResponse.success(videoActionService.likeVideo(videoId));
    }

    @Operation(summary = "取消点赞")
    @DeleteMapping("/like")
    public ApiResponse<UserVideoStateVO> cancelLikeVideo(@PathVariable Long videoId) {
        return ApiResponse.success(videoActionService.cancelLikeVideo(videoId));
    }

    @Operation(summary = "点踩视频")
    @PostMapping("/dislike")
    public ApiResponse<UserVideoStateVO> dislikeVideo(@PathVariable Long videoId) {
        return ApiResponse.success(videoActionService.dislikeVideo(videoId));
    }

    @Operation(summary = "取消点踩")
    @DeleteMapping("/dislike")
    public ApiResponse<UserVideoStateVO> cancelDislikeVideo(@PathVariable Long videoId) {
        return ApiResponse.success(videoActionService.cancelDislikeVideo(videoId));
    }

    @Operation(summary = "投币")
    @PostMapping("/coin")
    public ApiResponse<UserVideoStateVO> coinVideo(@PathVariable Long videoId,
                                                   @Valid @RequestBody CoinVideoRequest request) {
        return ApiResponse.success(videoActionService.coinVideo(videoId, request));
    }

    @Operation(summary = "加入稍后再看")
    @PostMapping("/watch-later")
    public ApiResponse<UserVideoStateVO> addWatchLater(@PathVariable Long videoId) {
        return ApiResponse.success(videoActionService.addWatchLater(videoId));
    }

    @Operation(summary = "移除稍后再看")
    @DeleteMapping("/watch-later")
    public ApiResponse<UserVideoStateVO> removeWatchLater(@PathVariable Long videoId) {
        return ApiResponse.success(videoActionService.removeWatchLater(videoId));
    }

    @Operation(summary = "收藏视频到收藏夹")
    @PostMapping("/favorite")
    public ApiResponse<UserVideoStateVO> favoriteVideo(@PathVariable Long videoId,
                                                       @Valid @RequestBody FavoriteVideoRequest request) {
        return ApiResponse.success(videoActionService.favoriteVideo(videoId, request));
    }

    @Operation(summary = "查询当前用户对视频的互动状态")
    @GetMapping("/state")
    public ApiResponse<UserVideoStateVO> getCurrentVideoState(@PathVariable Long videoId) {
        return ApiResponse.success(videoActionService.getCurrentVideoState(videoId));
    }
}
