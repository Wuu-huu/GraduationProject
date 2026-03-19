package com.zzk.videomodule.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzk.videomodule.entity.VideoStat;
import com.zzk.videomodule.service.VideoStatService;
import com.zzk.videomodule.mapper.VideoStatMapper;
import org.springframework.stereotype.Service;

/**
* @author 周振坤
* @description 针对表【video_stat(视频统计表)】的数据库操作Service实现
* @createDate 2026-03-19 23:33:28
*/
@Service
public class VideoStatServiceImpl extends ServiceImpl<VideoStatMapper, VideoStat>
    implements VideoStatService{

}




