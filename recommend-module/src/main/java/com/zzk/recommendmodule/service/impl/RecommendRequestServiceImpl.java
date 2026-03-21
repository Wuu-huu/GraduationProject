package com.zzk.recommendmodule.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzk.recommendmodule.entity.RecommendRequest;
import com.zzk.recommendmodule.mapper.RecommendRequestMapper;
import com.zzk.recommendmodule.service.RecommendRequestService;
import java.util.Date;
import java.util.UUID;
import org.springframework.stereotype.Service;

/**
* @author 周振坤
* @description 针对表【recommend_request(推荐请求表)】的数据库操作Service实现
* @createDate 2026-03-19 23:39:21
*/
@Service
public class RecommendRequestServiceImpl extends ServiceImpl<RecommendRequestMapper, RecommendRequest>
    implements RecommendRequestService{

    @Override
    public RecommendRequest createRequest(Long uid, String scene, Long pageNo, String deviceType, Date requestTime) {
        RecommendRequest request = new RecommendRequest();
        request.setRequestId(UUID.randomUUID().toString().replace("-", ""));
        request.setUid(uid == null ? 0L : uid);
        request.setScene(scene);
        request.setPageNo(pageNo == null ? 1 : Math.toIntExact(pageNo));
        request.setDeviceType(deviceType);
        request.setRequestTime(requestTime == null ? new Date() : requestTime);
        save(request);
        return request;
    }
}




