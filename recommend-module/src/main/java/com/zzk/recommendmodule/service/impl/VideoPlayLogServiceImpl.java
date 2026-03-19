package com.zzk.recommendmodule.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzk.recommendmodule.entity.VideoPlayLog;
import com.zzk.recommendmodule.service.VideoPlayLogService;
import com.zzk.recommendmodule.mapper.VideoPlayLogMapper;
import org.springframework.stereotype.Service;

/**
* @author 周振坤
* @description 针对表【video_play_log(视频播放日志表)】的数据库操作Service实现
* @createDate 2026-03-19 23:39:44
*/
@Service
public class VideoPlayLogServiceImpl extends ServiceImpl<VideoPlayLogMapper, VideoPlayLog>
    implements VideoPlayLogService{

}




