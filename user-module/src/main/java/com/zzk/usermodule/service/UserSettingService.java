package com.zzk.usermodule.service;

import com.zzk.usermodule.entity.UserSetting;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zzk.usermodule.dto.UpdateUserSettingRequest;
import com.zzk.usermodule.vo.UserSettingVO;

/**
* @author 周振坤
* @description 针对表【user_setting(用户设置表)】的数据库操作Service
* @createDate 2026-03-19 23:30:26
*/
public interface UserSettingService extends IService<UserSetting> {

    void initUserSetting(Long uid);

    UserSettingVO getCurrentUserSetting();

    UserSettingVO updateCurrentUserSetting(UpdateUserSettingRequest request);
}
