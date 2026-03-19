package com.zzk.usermodule.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzk.usermodule.entity.UserFollow;
import com.zzk.usermodule.service.UserFollowService;
import com.zzk.usermodule.mapper.UserFollowMapper;
import org.springframework.stereotype.Service;

/**
* @author 周振坤
* @description 针对表【user_follow(用户关注关系表)】的数据库操作Service实现
* @createDate 2026-03-19 23:30:17
*/
@Service
public class UserFollowServiceImpl extends ServiceImpl<UserFollowMapper, UserFollow>
    implements UserFollowService{

}




