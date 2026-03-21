package com.zzk.authmodule.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzk.authmodule.dto.LoginRequest;
import com.zzk.authmodule.dto.RegisterRequest;
import com.zzk.authmodule.entity.UserInfo;
import com.zzk.authmodule.vo.CurrentUserVO;
import com.zzk.authmodule.vo.LoginVO;

/**
 * 认证域服务接口。
 */
public interface UserInfoService extends IService<UserInfo> {

    LoginVO register(RegisterRequest request);

    LoginVO login(LoginRequest request);

    CurrentUserVO getCurrentUser();
}
