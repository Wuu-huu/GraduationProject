package com.zzk.usermodule.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzk.usermodule.entity.UserSetting;
import com.zzk.usermodule.service.UserSettingService;
import com.zzk.usermodule.mapper.UserSettingMapper;
import org.springframework.stereotype.Service;

/**
* @author 周振坤
* @description 针对表【user_setting(用户设置表)】的数据库操作Service实现
* @createDate 2026-03-19 23:30:26
*/
@Service
public class UserSettingServiceImpl extends ServiceImpl<UserSettingMapper, UserSetting>
    implements UserSettingService{

}




