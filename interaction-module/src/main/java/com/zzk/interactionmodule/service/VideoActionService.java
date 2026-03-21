package com.zzk.interactionmodule.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzk.interactionmodule.dto.CoinVideoRequest;
import com.zzk.interactionmodule.dto.FavoriteVideoRequest;
import com.zzk.interactionmodule.entity.VideoAction;
import com.zzk.interactionmodule.vo.UserVideoStateVO;

/**
* @author 周振坤
* @description 针对表【video_action(视频互动行为明细表)】的数据库操作Service
* @createDate 2026-03-19 23:35:18
*/
public interface VideoActionService extends IService<VideoAction> {

    UserVideoStateVO likeVideo(Long videoId);

    UserVideoStateVO cancelLikeVideo(Long videoId);

    UserVideoStateVO dislikeVideo(Long videoId);

    UserVideoStateVO cancelDislikeVideo(Long videoId);

    UserVideoStateVO coinVideo(Long videoId, CoinVideoRequest request);

    UserVideoStateVO addWatchLater(Long videoId);

    UserVideoStateVO removeWatchLater(Long videoId);

    UserVideoStateVO favoriteVideo(Long videoId, FavoriteVideoRequest request);

    UserVideoStateVO getCurrentVideoState(Long videoId);
}
