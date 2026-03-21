package com.zzk.usermodule.controller;

import com.zzk.common.model.response.ApiResponse;
import com.zzk.usermodule.dto.UpdateUserProfileRequest;
import com.zzk.usermodule.service.UserProfileService;
import com.zzk.usermodule.vo.UserHomeVO;
import com.zzk.usermodule.vo.UserProfileVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户资料控制器。
 */
@Slf4j
@Tag(name = "用户资料")
@RestController
@RequestMapping("/api/users")
public class UserProfileController {

    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @Operation(summary = "获取当前用户资料")
    @GetMapping("/me/profile")
    public ApiResponse<UserProfileVO> getCurrentUserProfile() {
        log.info("Receive current user profile request");
        return ApiResponse.success(userProfileService.getCurrentUserProfile());
    }

    @Operation(summary = "更新当前用户资料")
    @PutMapping("/me/profile")
    public ApiResponse<UserProfileVO> updateCurrentUserProfile(@Valid @RequestBody UpdateUserProfileRequest request) {
        log.info("Receive update current user profile request");
        return ApiResponse.success(userProfileService.updateCurrentUserProfile(request));
    }

    @Operation(summary = "查询用户主页")
    @GetMapping("/{uid}/profile")
    public ApiResponse<UserHomeVO> getUserHome(@PathVariable Long uid) {
        log.info("Receive user home request, uid={}", uid);
        return ApiResponse.success(userProfileService.getUserHome(uid));
    }
}
