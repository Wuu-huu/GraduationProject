package com.zzk.videomodule.controller;

import com.zzk.common.model.response.ApiResponse;
import com.zzk.videomodule.dto.VideoPartUpsertRequest;
import com.zzk.videomodule.service.VideoPartService;
import com.zzk.videomodule.vo.VideoPartVO;
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

@Tag(name = "视频分P")
@RestController
@RequestMapping("/api/videos/{videoId}/parts")
public class VideoPartController {

    private final VideoPartService videoPartService;

    public VideoPartController(VideoPartService videoPartService) {
        this.videoPartService = videoPartService;
    }

    @Operation(summary = "查询分P列表")
    @GetMapping
    public ApiResponse<List<VideoPartVO>> listVideoParts(@PathVariable Long videoId) {
        return ApiResponse.success(videoPartService.listVideoParts(videoId));
    }

    @Operation(summary = "新增分P")
    @PostMapping
    public ApiResponse<VideoPartVO> addVideoPart(@PathVariable Long videoId,
                                                 @Valid @RequestBody VideoPartUpsertRequest request) {
        return ApiResponse.success(videoPartService.addVideoPart(videoId, request));
    }

    @Operation(summary = "编辑分P")
    @PutMapping("/{partId}")
    public ApiResponse<VideoPartVO> updateVideoPart(@PathVariable Long videoId,
                                                    @PathVariable Long partId,
                                                    @Valid @RequestBody VideoPartUpsertRequest request) {
        return ApiResponse.success(videoPartService.updateVideoPart(videoId, partId, request));
    }

    @Operation(summary = "删除分P")
    @DeleteMapping("/{partId}")
    public ApiResponse<Void> deleteVideoPart(@PathVariable Long videoId, @PathVariable Long partId) {
        videoPartService.deleteVideoPart(videoId, partId);
        return ApiResponse.success();
    }
}
