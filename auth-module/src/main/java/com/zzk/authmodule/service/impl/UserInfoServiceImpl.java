package com.zzk.authmodule.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzk.authmodule.entity.UserInfo;
import com.zzk.authmodule.service.UserInfoService;
import com.zzk.authmodule.mapper.UserInfoMapper;
import org.springframework.stereotype.Service;

/**
* @author 周振坤
* @description 针对表【user_info(用户主表)】的数据库操作Service实现
* @createDate 2026-03-19 23:18:11
*/
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
    implements UserInfoService{

}




