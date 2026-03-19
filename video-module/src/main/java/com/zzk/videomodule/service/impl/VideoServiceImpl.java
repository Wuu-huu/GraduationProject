package com.zzk.videomodule.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzk.videomodule.entity.Video;
import com.zzk.videomodule.service.VideoService;
import com.zzk.videomodule.mapper.VideoMapper;
import org.springframework.stereotype.Service;

/**
* @author 周振坤
* @description 针对表【video(视频主表)】的数据库操作Service实现
* @createDate 2026-03-19 23:32:45
*/
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video>
    implements VideoService{

}




