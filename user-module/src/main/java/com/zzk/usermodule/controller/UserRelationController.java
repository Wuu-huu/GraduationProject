package com.zzk.usermodule.controller;

import com.zzk.common.model.page.PageResponse;
import com.zzk.common.model.response.ApiResponse;
import com.zzk.usermodule.dto.UserFollowPageQuery;
import com.zzk.usermodule.service.UserFollowService;
import com.zzk.usermodule.vo.UserCardVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户关系控制器。
 */
@Slf4j
@Validated
@Tag(name = "用户关系")
@RestController
@RequestMapping("/api/users")
public class UserRelationController {

    private final UserFollowService userFollowService;

    public UserRelationController(UserFollowService userFollowService) {
        this.userFollowService = userFollowService;
    }

    @Operation(summary = "关注用户")
    @PostMapping("/{uid}/follow")
    public ApiResponse<Void> follow(@PathVariable Long uid) {
        log.info("Receive follow request, targetUid={}", uid);
        userFollowService.follow(uid);
        return ApiResponse.success();
    }

    @Operation(summary = "取消关注用户")
    @DeleteMapping("/{uid}/follow")
    public ApiResponse<Void> unfollow(@PathVariable Long uid) {
        log.info("Receive unfollow request, targetUid={}", uid);
        userFollowService.unfollow(uid);
        return ApiResponse.success();
    }

    @Operation(summary = "查询粉丝列表")
    @GetMapping("/{uid}/followers")
    public ApiResponse<PageResponse<UserCardVO>> getFollowers(@PathVariable Long uid, @Valid UserFollowPageQuery query) {
        log.info("Receive followers request, uid={}, pageNum={}, pageSize={}", uid, query.getPageNum(), query.getPageSize());
        return ApiResponse.success(userFollowService.getFollowers(uid, query));
    }

    @Operation(summary = "查询关注列表")
    @GetMapping("/{uid}/following")
    public ApiResponse<PageResponse<UserCardVO>> getFollowing(@PathVariable Long uid, @Valid UserFollowPageQuery query) {
        log.info("Receive following request, uid={}, pageNum={}, pageSize={}", uid, query.getPageNum(), query.getPageSize());
        return ApiResponse.success(userFollowService.getFollowing(uid, query));
    }
}
