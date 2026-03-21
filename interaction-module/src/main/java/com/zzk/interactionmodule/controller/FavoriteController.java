package com.zzk.interactionmodule.controller;

import com.zzk.common.model.response.ApiResponse;
import com.zzk.interactionmodule.dto.SaveFavoriteFolderRequest;
import com.zzk.interactionmodule.service.FavoriteFolderService;
import com.zzk.interactionmodule.vo.FavoriteFolderDetailVO;
import com.zzk.interactionmodule.vo.FavoriteFolderVO;
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

@Tag(name = "收藏夹")
@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    private final FavoriteFolderService favoriteFolderService;

    public FavoriteController(FavoriteFolderService favoriteFolderService) {
        this.favoriteFolderService = favoriteFolderService;
    }

    @Operation(summary = "创建收藏夹")
    @PostMapping
    public ApiResponse<FavoriteFolderVO> createFolder(@Valid @RequestBody SaveFavoriteFolderRequest request) {
        return ApiResponse.success(favoriteFolderService.createFolder(request));
    }

    @Operation(summary = "编辑收藏夹")
    @PutMapping("/{favoriteId}")
    public ApiResponse<FavoriteFolderVO> updateFolder(@PathVariable("favoriteId") Long favoriteId,
                                                      @Valid @RequestBody SaveFavoriteFolderRequest request) {
        return ApiResponse.success(favoriteFolderService.updateFolder(favoriteId, request));
    }

    @Operation(summary = "删除收藏夹")
    @DeleteMapping("/{favoriteId}")
    public ApiResponse<Void> deleteFolder(@PathVariable("favoriteId") Long favoriteId) {
        favoriteFolderService.deleteFolder(favoriteId);
        return ApiResponse.success();
    }

    @Operation(summary = "收藏夹列表")
    @GetMapping
    public ApiResponse<List<FavoriteFolderVO>> listFolders() {
        return ApiResponse.success(favoriteFolderService.listCurrentUserFolders());
    }

    @Operation(summary = "收藏夹详情")
    @GetMapping("/{favoriteId}")
    public ApiResponse<FavoriteFolderDetailVO> getFolderDetail(@PathVariable("favoriteId") Long favoriteId) {
        return ApiResponse.success(favoriteFolderService.getFolderDetail(favoriteId));
    }

    @Operation(summary = "加入收藏夹")
    @PostMapping("/{favoriteId}/videos/{videoId}")
    public ApiResponse<Void> addVideoToFolder(@PathVariable("favoriteId") Long favoriteId,
                                              @PathVariable Long videoId) {
        favoriteFolderService.addVideoToFolder(favoriteId, videoId);
        return ApiResponse.success();
    }

    @Operation(summary = "移出收藏夹")
    @DeleteMapping("/{favoriteId}/videos/{videoId}")
    public ApiResponse<Void> removeVideoFromFolder(@PathVariable("favoriteId") Long favoriteId,
                                                   @PathVariable Long videoId) {
        favoriteFolderService.removeVideoFromFolder(favoriteId, videoId);
        return ApiResponse.success();
    }
}
