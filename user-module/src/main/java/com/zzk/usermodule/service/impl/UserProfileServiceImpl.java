package com.zzk.usermodule.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzk.usermodule.entity.UserProfile;
import com.zzk.usermodule.service.UserProfileService;
import com.zzk.usermodule.mapper.UserProfileMapper;
import org.springframework.stereotype.Service;

/**
* @author 周振坤
* @description 针对表【user_profile(用户资料表)】的数据库操作Service实现
* @createDate 2026-03-19 23:28:19
*/
@Service
public class UserProfileServiceImpl extends ServiceImpl<UserProfileMapper, UserProfile>
    implements UserProfileService{

}




