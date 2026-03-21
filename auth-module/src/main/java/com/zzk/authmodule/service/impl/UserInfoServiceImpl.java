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
import com.zzk.common.exception.BusinessException;
import com.zzk.common.model.security.LoginUser;
import com.zzk.common.security.JwtProperties;
import com.zzk.common.security.JwtTokenProvider;
import com.zzk.common.security.SecurityContextUtils;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 认证域核心服务，实现注册、登录和登录态查询。
 */
@Slf4j
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtProperties jwtProperties;

    public UserInfoServiceImpl(PasswordEncoder passwordEncoder,
                               JwtTokenProvider jwtTokenProvider,
                               JwtProperties jwtProperties) {
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtProperties = jwtProperties;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoginVO register(RegisterRequest request) {
        try {
            // 当前阶段注册流程只落 user_info，扩展资料由后续 user-module 补齐。
            validateUniqueFields(request);
            UserInfo userInfo = new UserInfo();
            userInfo.setUsername(request.getUsername());
            userInfo.setPasswordHash(passwordEncoder.encode(request.getPassword()));
            userInfo.setEmail(StringUtils.hasText(request.getEmail()) ? request.getEmail() : null);
            userInfo.setPhone(StringUtils.hasText(request.getPhone()) ? request.getPhone() : null);
            userInfo.setState(UserStateEnum.NORMAL.getCode());
            userInfo.setRole(UserRoleEnum.USER.getCode());
            userInfo.setRegisterTime(LocalDateTime.now());
            userInfo.setLastLoginTime(LocalDateTime.now());
            save(userInfo);
            log.info("Register success, uid={}, username={}", userInfo.getUid(), userInfo.getUsername());
            return buildLoginVO(userInfo);
        } catch (Exception ex) {
            log.error("Register service failed, username={}, email={}, phone={}",
                    request.getUsername(), request.getEmail(), request.getPhone(), ex);
            throw ex;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoginVO login(LoginRequest request) {
        try {
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
        } catch (Exception ex) {
            log.error("Login service failed, username={}", request.getUsername(), ex);
            throw ex;
        }
    }

    @Override
    public CurrentUserVO getCurrentUser() {
        try {
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
        } catch (Exception ex) {
            log.error("Get current user failed", ex);
            throw ex;
        }
    }

    private void validateUniqueFields(RegisterRequest request) {
        try {
            if (lambdaQuery().eq(UserInfo::getUsername, request.getUsername()).exists()) {
                throw new BusinessException(ApiCodeEnum.USERNAME_EXISTS);
            }
            if (StringUtils.hasText(request.getEmail())
                    && lambdaQuery().eq(UserInfo::getEmail, request.getEmail()).exists()) {
                throw new BusinessException(ApiCodeEnum.BAD_REQUEST, "邮箱已被占用");
            }
            if (StringUtils.hasText(request.getPhone())
                    && lambdaQuery().eq(UserInfo::getPhone, request.getPhone()).exists()) {
                throw new BusinessException(ApiCodeEnum.BAD_REQUEST, "手机号已被占用");
            }
        } catch (Exception ex) {
            log.error("Validate unique fields failed, username={}, email={}, phone={}",
                    request.getUsername(), request.getEmail(), request.getPhone(), ex);
            throw ex;
        }
    }

    private LoginVO buildLoginVO(UserInfo userInfo) {
        // 登录成功后统一返回 token 与基础身份信息，便于前端直接建立会话。
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
}
