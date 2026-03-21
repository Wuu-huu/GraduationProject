package com.zzk.recommendmodule.service;

import com.zzk.recommendmodule.entity.VideoExposureLog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zzk.recommendmodule.vo.RecommendVideoVO;
import java.util.Date;
import java.util.List;

/**
* @author 周振坤
* @description 针对表【video_exposure_log(视频曝光日志表)】的数据库操作Service
* @createDate 2026-03-19 23:39:35
*/
public interface VideoExposureLogService extends IService<VideoExposureLog> {

    void saveExposureLogs(String requestId, Long uid, String scene, List<RecommendVideoVO> videos, Date exposeTime);
}
