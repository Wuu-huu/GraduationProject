package com.zzk.videomodule.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzk.videomodule.dto.VideoPartUpsertRequest;
import com.zzk.videomodule.entity.VideoPart;
import com.zzk.videomodule.vo.VideoPartVO;
import java.util.List;

/**
* @author 周振坤
* @description 针对表【video_part(视频分P表)】的数据库操作Service
* @createDate 2026-03-19 23:32:53
*/
public interface VideoPartService extends IService<VideoPart> {

    List<VideoPartVO> listVideoParts(Long videoId);

    VideoPartVO addVideoPart(Long videoId, VideoPartUpsertRequest request);

    VideoPartVO updateVideoPart(Long videoId, Long partId, VideoPartUpsertRequest request);

    void deleteVideoPart(Long videoId, Long partId);

    void replaceVideoParts(Long videoId, List<VideoPartUpsertRequest> requests);
}
