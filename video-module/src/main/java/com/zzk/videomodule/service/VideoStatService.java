package com.zzk.videomodule.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzk.videomodule.entity.VideoStat;
import com.zzk.videomodule.vo.VideoStatVO;

/**
* @author 周振坤
* @description 针对表【video_stat(视频统计表)】的数据库操作Service
* @createDate 2026-03-19 23:33:28
*/
public interface VideoStatService extends IService<VideoStat> {

    void initVideoStat(Long videoId);

    VideoStatVO getVideoStat(Long videoId);

    void adjustVideoStat(Long videoId,
                         long likeDelta,
                         long dislikeDelta,
                         long coinDelta,
                         long favoriteDelta,
                         long commentDelta,
                         long danmuDelta,
                         long playDelta);
}
