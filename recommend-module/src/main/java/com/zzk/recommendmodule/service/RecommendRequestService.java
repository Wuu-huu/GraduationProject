package com.zzk.recommendmodule.service;

import com.zzk.recommendmodule.entity.RecommendRequest;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.Date;

/**
* @author 周振坤
* @description 针对表【recommend_request(推荐请求表)】的数据库操作Service
* @createDate 2026-03-19 23:39:21
*/
public interface RecommendRequestService extends IService<RecommendRequest> {

    RecommendRequest createRequest(Long uid, String scene, Long pageNo, String deviceType, Date requestTime);
}
