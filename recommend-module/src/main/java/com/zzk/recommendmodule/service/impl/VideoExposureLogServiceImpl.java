package com.zzk.recommendmodule.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzk.recommendmodule.entity.VideoExposureLog;
import com.zzk.recommendmodule.mapper.VideoExposureLogMapper;
import com.zzk.recommendmodule.service.VideoExposureLogService;
import com.zzk.recommendmodule.vo.RecommendVideoVO;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Service;

/**
* @author 周振坤
* @description 针对表【video_exposure_log(视频曝光日志表)】的数据库操作Service实现
* @createDate 2026-03-19 23:39:35
*/
@Service
public class VideoExposureLogServiceImpl extends ServiceImpl<VideoExposureLogMapper, VideoExposureLog>
    implements VideoExposureLogService{

    @Override
    public void saveExposureLogs(String requestId, Long uid, String scene, List<RecommendVideoVO> videos, Date exposeTime) {
        if (videos == null || videos.isEmpty()) {
            return;
        }
        Date now = exposeTime == null ? new Date() : exposeTime;
        long persistUid = uid == null ? 0L : uid;
        for (RecommendVideoVO video : videos) {
            VideoExposureLog log = new VideoExposureLog();
            log.setRequestId(requestId);
            log.setUid(persistUid);
            log.setVid(video.getVid());
            log.setScene(scene);
            log.setPositionNo(video.getPositionNo());
            log.setRecallSource(video.getSource());
            log.setScore(video.getScore() == null ? null : BigDecimal.valueOf(video.getScore()));
            log.setIsClicked(0);
            log.setIsPlayed(0);
            log.setExposeTime(now);
            save(log);
        }
    }
}




