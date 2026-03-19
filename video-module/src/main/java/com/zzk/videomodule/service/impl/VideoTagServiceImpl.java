package com.zzk.videomodule.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzk.videomodule.entity.VideoTag;
import com.zzk.videomodule.service.VideoTagService;
import com.zzk.videomodule.mapper.VideoTagMapper;
import org.springframework.stereotype.Service;

/**
* @author 周振坤
* @description 针对表【video_tag(视频标签关系表)】的数据库操作Service实现
* @createDate 2026-03-19 23:33:11
*/
@Service
public class VideoTagServiceImpl extends ServiceImpl<VideoTagMapper, VideoTag>
    implements VideoTagService{

}




