package com.zzk.usermodule.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzk.usermodule.entity.UserStat;
import com.zzk.usermodule.service.UserStatService;
import com.zzk.usermodule.mapper.UserStatMapper;
import org.springframework.stereotype.Service;

/**
* @author 周振坤
* @description 针对表【user_stat(用户统计表)】的数据库操作Service实现
* @createDate 2026-03-19 23:29:56
*/
@Service
public class UserStatServiceImpl extends ServiceImpl<UserStatMapper, UserStat>
    implements UserStatService{

}




