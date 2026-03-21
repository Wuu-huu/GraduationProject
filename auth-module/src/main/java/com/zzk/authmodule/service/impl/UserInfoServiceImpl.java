package com.zzk.authmodule.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzk.authmodule.dto.LoginRequest;
import com.zzk.authmodule.dto.RegisterRequest;
import com.zzk.authmodule.entity.UserInfo;
import com.zzk.authmodule.mapper.UserInfoMapper;
import com.zzk.authmodule.service.UserInfoService;
import com.zzk.authmodule.vo.CurrentUserVO;
import com.zzk.authmodule.vo.LoginVO;
import com.zzk.common.enums.ApiCodeEnum;
import com.zzk.common.enums.UserRoleEnum;
import com.zzk.common.enums.UserStateEnum;
import com.zzk.common.event.UserRegisteredEvent;
import com.zzk.common.exception.BusinessException;
import com.zzk.common.model.security.LoginUser;
import com.zzk.common.security.JwtProperties;
import com.zzk.common.security.JwtTokenProvider;
import com.zzk.common.security.SecurityContextUtils;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 认证核心服务，实现注册、登录和当前登录态查询。
 */
@Slf4j
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtProperties jwtProperties;
    private final ApplicationEventPublisher applicationEventPublisher;

    public UserInfoServiceImpl(PasswordEncoder passwordEncoder,
                               JwtTokenProvider jwtTokenProvider,
                               JwtProperties jwtProperties,
                               ApplicationEventPublisher applicationEventPublisher) {
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtProperties = jwtProperties;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoginVO register(RegisterRequest request) {
        validateUniqueFields(request);

        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(request.getUsername().trim());
        userInfo.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        userInfo.setEmail(normalizeOptional(request.getEmail()));
        userInfo.setPhone(normalizeOptional(request.getPhone()));
        userInfo.setState(UserStateEnum.NORMAL.getCode());
        userInfo.setRole(UserRoleEnum.USER.getCode());
        userInfo.setRegisterTime(LocalDateTime.now());
        userInfo.setLastLoginTime(LocalDateTime.now());

        save(userInfo);
        applicationEventPublisher.publishEvent(
                new UserRegisteredEvent(userInfo.getUid(), userInfo.getUsername(), userInfo.getRegisterTime()));
        log.info("Register success, uid={}, username={}", userInfo.getUid(), userInfo.getUsername());
        return buildLoginVO(userInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoginVO login(LoginRequest request) {
        UserInfo userInfo = lambdaQuery().eq(UserInfo::getUsername, request.getUsername()).one();
        if (userInfo == null) {
            throw new BusinessException(ApiCodeEnum.ACCOUNT_NOT_FOUND);
        }
        if (UserStateEnum.fromCode(userInfo.getState()) == UserStateEnum.BANNED) {
            throw new BusinessException(ApiCodeEnum.ACCOUNT_BANNED);
        }
        if (!passwordEncoder.matches(request.getPassword(), userInfo.getPasswordHash())) {
            throw new BusinessException(ApiCodeEnum.PASSWORD_ERROR);
        }

        userInfo.setLastLoginTime(LocalDateTime.now());
        updateById(userInfo);
        log.info("Login success, uid={}, username={}", userInfo.getUid(), userInfo.getUsername());
        return buildLoginVO(userInfo);
    }

    @Override
    public CurrentUserVO getCurrentUser() {
        Long currentUserId = SecurityContextUtils.getCurrentUserId();
        UserInfo userInfo = getById(currentUserId);
        if (userInfo == null) {
            throw new BusinessException(ApiCodeEnum.ACCOUNT_NOT_FOUND);
        }
        return CurrentUserVO.builder()
                .uid(userInfo.getUid())
                .username(userInfo.getUsername())
                .role(userInfo.getRole())
                .state(userInfo.getState())
                .build();
    }

    private void validateUniqueFields(RegisterRequest request) {
        String username = request.getUsername().trim();
        String email = normalizeOptional(request.getEmail());
        String phone = normalizeOptional(request.getPhone());

        if (lambdaQuery().eq(UserInfo::getUsername, username).exists()) {
            throw new BusinessException(ApiCodeEnum.USERNAME_EXISTS);
        }
        if (email != null && lambdaQuery().eq(UserInfo::getEmail, email).exists()) {
            throw new BusinessException(ApiCodeEnum.BAD_REQUEST, "邮箱已被占用");
        }
        if (phone != null && lambdaQuery().eq(UserInfo::getPhone, phone).exists()) {
            throw new BusinessException(ApiCodeEnum.BAD_REQUEST, "手机号已被占用");
        }
    }

    private LoginVO buildLoginVO(UserInfo userInfo) {
        LoginUser loginUser = LoginUser.builder()
                .uid(userInfo.getUid())
                .username(userInfo.getUsername())
                .role(userInfo.getRole())
                .build();
        return LoginVO.builder()
                .accessToken(jwtTokenProvider.createToken(loginUser))
                .expiresIn(jwtProperties.getAccessTokenExpireSeconds())
                .uid(userInfo.getUid())
                .username(userInfo.getUsername())
                .role(userInfo.getRole())
                .build();
    }

    private String normalizeOptional(String value) {
        return StringUtils.hasText(value) ? value.trim() : null;
    }
}