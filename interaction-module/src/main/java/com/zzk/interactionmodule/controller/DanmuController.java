package com.zzk.interactionmodule.controller;

import com.zzk.common.model.response.ApiResponse;
import com.zzk.interactionmodule.dto.CreateDanmuRequest;
import com.zzk.interactionmodule.service.DanmuService;
import com.zzk.interactionmodule.vo.DanmuVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "弹幕互动")
@RestController
@RequestMapping("/api")
public class DanmuController {

    private final DanmuService danmuService;

    public DanmuController(DanmuService danmuService) {
        this.danmuService = danmuService;
    }

    @Operation(summary = "发送弹幕")
    @PostMapping("/videos/{videoId}/danmakus")
    public ApiResponse<DanmuVO> createDanmu(@PathVariable Long videoId,
                                            @Valid @RequestBody CreateDanmuRequest request) {
        request.setVid(videoId);
        return ApiResponse.success(danmuService.createDanmu(request));
    }

    @Operation(summary = "查询弹幕")
    @GetMapping("/videos/{videoId}/danmakus")
    public ApiResponse<List<DanmuVO>> listDanmus(@PathVariable Long videoId,
                                                 @RequestParam(required = false) Long partId) {
        return ApiResponse.success(danmuService.listDanmuByVideo(videoId, partId));
    }
}
