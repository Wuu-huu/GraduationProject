package com.zzk.videomodule.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzk.videomodule.dto.SaveVideoSeriesRequest;
import com.zzk.videomodule.entity.VideoSeries;
import com.zzk.videomodule.vo.VideoSeriesVO;

/**
* @author 周振坤
* @description 针对表【video_series(视频合集表)】的数据库操作Service
* @createDate 2026-03-19 23:33:18
*/
public interface VideoSeriesService extends IService<VideoSeries> {

    VideoSeriesVO createSeries(SaveVideoSeriesRequest request);

    VideoSeriesVO updateSeries(Long seriesId, SaveVideoSeriesRequest request);

    void deleteSeries(Long seriesId);

    VideoSeriesVO getSeriesDetail(Long seriesId);
}
