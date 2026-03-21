package com.zzk.videomodule.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzk.common.model.page.PageResponse;
import com.zzk.videomodule.dto.BindVideoCategoryTagsRequest;
import com.zzk.videomodule.dto.SaveVideoRequest;
import com.zzk.videomodule.dto.UpdateVideoRequest;
import com.zzk.videomodule.dto.UpdateVideoStatusRequest;
import com.zzk.videomodule.dto.VideoQueryRequest;
import com.zzk.videomodule.entity.Video;
import com.zzk.videomodule.vo.VideoCardVO;
import com.zzk.videomodule.vo.VideoCategoryTagVO;
import com.zzk.videomodule.vo.VideoDetailVO;
import java.util.List;

/**
* @author 周振坤
* @description 针对表【video(视频主表)】的数据库操作Service
* @createDate 2026-03-19 23:32:45
*/
public interface VideoService extends IService<Video> {

    VideoDetailVO publishVideo(SaveVideoRequest request);

    VideoDetailVO saveDraft(SaveVideoRequest request);

    VideoDetailVO updateVideo(Long videoId, UpdateVideoRequest request);

    void deleteVideo(Long videoId);

    VideoDetailVO updateVideoStatus(Long videoId, UpdateVideoStatusRequest request);

    VideoDetailVO getVideoDetail(Long videoId);

    PageResponse<VideoCardVO> listHomeVideos(VideoQueryRequest request);

    PageResponse<VideoCardVO> listZoneVideos(Long zoneId, VideoQueryRequest request);

    PageResponse<VideoCardVO> listUserVideos(Long uid, VideoQueryRequest request);

    VideoCategoryTagVO bindCategoryTags(Long videoId, BindVideoCategoryTagsRequest request);

    VideoCategoryTagVO getCategoryTags(Long videoId);

    List<VideoCardVO> listVideoCardsByIds(List<Long> videoIds);
}
