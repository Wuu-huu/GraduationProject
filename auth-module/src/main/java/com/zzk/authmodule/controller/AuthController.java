package com.zzk.authmodule.controller;

import com.zzk.authmodule.dto.LoginRequest;
import com.zzk.authmodule.dto.RegisterRequest;
import com.zzk.authmodule.service.UserInfoService;
import com.zzk.authmodule.vo.CurrentUserVO;
import com.zzk.authmodule.vo.LoginVO;
import com.zzk.common.model.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 认证模块对外入口，当前阶段提供注册、登录和当前用户信息接口。
 */
@Slf4j
@Tag(name = "认证模块")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserInfoService userInfoService;

    public AuthController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @Operation(summary = "注册")
    @PostMapping("/register")
    public ApiResponse<LoginVO> register(@Valid @RequestBody RegisterRequest request) {
        try {
            log.info("Receive register request, username={}", request.getUsername());
            return ApiResponse.success(userInfoService.register(request));
        } catch (Exception ex) {
            log.error("Register request failed, username={}", request.getUsername(), ex);
            throw ex;
        }
    }

    @Operation(summary = "登录")
    @PostMapping("/login")
    public ApiResponse<LoginVO> login(@Valid @RequestBody LoginRequest request) {
        try {
            log.info("Receive login request, username={}", request.getUsername());
            return ApiResponse.success(userInfoService.login(request));
        } catch (Exception ex) {
            log.error("Login request failed, username={}", request.getUsername(), ex);
            throw ex;
        }
    }

    @Operation(summary = "获取当前用户")
    @GetMapping("/me")
    public ApiResponse<CurrentUserVO> currentUser() {
        try {
            log.info("Receive current user request");
            return ApiResponse.success(userInfoService.getCurrentUser());
        } catch (Exception ex) {
            log.error("Current user request failed", ex);
            throw ex;
        }
    }
}
