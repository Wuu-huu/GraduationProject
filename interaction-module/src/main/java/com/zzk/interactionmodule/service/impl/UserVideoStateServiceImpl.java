package com.zzk.interactionmodule.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzk.interactionmodule.entity.UserVideoState;
import com.zzk.interactionmodule.service.UserVideoStateService;
import com.zzk.interactionmodule.mapper.UserVideoStateMapper;
import org.springframework.stereotype.Service;

/**
* @author 周振坤
* @description 针对表【user_video_state(用户视频状态聚合表)】的数据库操作Service实现
* @createDate 2026-03-19 23:35:29
*/
@Service
public class UserVideoStateServiceImpl extends ServiceImpl<UserVideoStateMapper, UserVideoState>
    implements UserVideoStateService{

}




