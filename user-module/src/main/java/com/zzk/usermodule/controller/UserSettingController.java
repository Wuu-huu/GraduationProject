package com.zzk.usermodule.controller;

import com.zzk.common.model.response.ApiResponse;
import com.zzk.usermodule.dto.UpdateUserSettingRequest;
import com.zzk.usermodule.service.UserSettingService;
import com.zzk.usermodule.vo.UserSettingVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户设置控制器。
 */
@Slf4j
@Tag(name = "用户设置")
@RestController
@RequestMapping("/api/users/me/settings")
public class UserSettingController {

    private final UserSettingService userSettingService;

    public UserSettingController(UserSettingService userSettingService) {
        this.userSettingService = userSettingService;
    }

    @Operation(summary = "获取当前用户设置")
    @GetMapping
    public ApiResponse<UserSettingVO> getCurrentUserSetting() {
        log.info("Receive current user setting request");
        return ApiResponse.success(userSettingService.getCurrentUserSetting());
    }

    @Operation(summary = "更新当前用户设置")
    @PutMapping
    public ApiResponse<UserSettingVO> updateCurrentUserSetting(@RequestBody UpdateUserSettingRequest request) {
        log.info("Receive update current user setting request");
        return ApiResponse.success(userSettingService.updateCurrentUserSetting(request));
    }
}
