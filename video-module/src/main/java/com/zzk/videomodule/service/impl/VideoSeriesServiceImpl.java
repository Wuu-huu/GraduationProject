package com.zzk.videomodule.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzk.videomodule.entity.VideoSeries;
import com.zzk.videomodule.service.VideoSeriesService;
import com.zzk.videomodule.mapper.VideoSeriesMapper;
import org.springframework.stereotype.Service;

/**
* @author 周振坤
* @description 针对表【video_series(视频合集表)】的数据库操作Service实现
* @createDate 2026-03-19 23:33:18
*/
@Service
public class VideoSeriesServiceImpl extends ServiceImpl<VideoSeriesMapper, VideoSeries>
    implements VideoSeriesService{

}




