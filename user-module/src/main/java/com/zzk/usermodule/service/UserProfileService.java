package com.zzk.usermodule.service;

import com.zzk.usermodule.entity.UserProfile;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zzk.usermodule.dto.UpdateUserProfileRequest;
import com.zzk.usermodule.vo.UserHomeVO;
import com.zzk.usermodule.vo.UserProfileVO;

/**
* @author 周振坤
* @description 针对表【user_profile(用户资料表)】的数据库操作Service
* @createDate 2026-03-19 23:28:19
*/
public interface UserProfileService extends IService<UserProfile> {

    void initUserProfile(Long uid, String username);

    UserProfileVO getCurrentUserProfile();

    UserProfileVO updateCurrentUserProfile(UpdateUserProfileRequest request);

    UserHomeVO getUserHome(Long targetUid);
}
