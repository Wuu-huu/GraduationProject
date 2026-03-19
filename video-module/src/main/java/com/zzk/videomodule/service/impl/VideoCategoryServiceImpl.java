package com.zzk.videomodule.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzk.videomodule.entity.VideoCategory;
import com.zzk.videomodule.service.VideoCategoryService;
import com.zzk.videomodule.mapper.VideoCategoryMapper;
import org.springframework.stereotype.Service;

/**
* @author 周振坤
* @description 针对表【video_category(视频分类关系表)】的数据库操作Service实现
* @createDate 2026-03-19 23:33:02
*/
@Service
public class VideoCategoryServiceImpl extends ServiceImpl<VideoCategoryMapper, VideoCategory>
    implements VideoCategoryService{

}




