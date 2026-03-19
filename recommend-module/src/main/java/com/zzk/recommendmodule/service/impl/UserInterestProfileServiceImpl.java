package com.zzk.recommendmodule.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzk.recommendmodule.entity.UserInterestProfile;
import com.zzk.recommendmodule.service.UserInterestProfileService;
import com.zzk.recommendmodule.mapper.UserInterestProfileMapper;
import org.springframework.stereotype.Service;

/**
* @author 周振坤
* @description 针对表【user_interest_profile(用户兴趣画像表)】的数据库操作Service实现
* @createDate 2026-03-19 23:40:05
*/
@Service
public class UserInterestProfileServiceImpl extends ServiceImpl<UserInterestProfileMapper, UserInterestProfile>
    implements UserInterestProfileService{

}




