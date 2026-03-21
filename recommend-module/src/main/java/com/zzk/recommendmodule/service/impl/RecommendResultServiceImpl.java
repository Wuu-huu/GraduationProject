package com.zzk.recommendmodule.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzk.recommendmodule.entity.RecommendResult;
import com.zzk.recommendmodule.mapper.RecommendResultMapper;
import com.zzk.recommendmodule.service.RecommendResultService;
import com.zzk.recommendmodule.vo.RecommendVideoVO;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Service;

/**
* @author 周振坤
* @description 针对表【recommend_result(推荐结果表)】的数据库操作Service实现
* @createDate 2026-03-19 23:39:25
*/
@Service
public class RecommendResultServiceImpl extends ServiceImpl<RecommendResultMapper, RecommendResult>
    implements RecommendResultService{

    @Override
    public void saveResults(String requestId, Long uid, List<RecommendVideoVO> videos, Date createTime) {
        if (videos == null || videos.isEmpty()) {
            return;
        }
        Date now = createTime == null ? new Date() : createTime;
        long persistUid = uid == null ? 0L : uid;
        for (RecommendVideoVO video : videos) {
            RecommendResult result = new RecommendResult();
            result.setRequestId(requestId);
            result.setUid(persistUid);
            result.setVid(video.getVid());
            result.setPositionNo(video.getPositionNo());
            result.setRecallSource(video.getSource());
            result.setRankScore(decimal(video.getScore()));
            result.setRerankScore(decimal(video.getScore()));
            result.setFinalScore(decimal(video.getScore()));
            result.setCreateTime(now);
            save(result);
        }
    }

    private BigDecimal decimal(Double value) {
        return value == null ? null : BigDecimal.valueOf(value);
    }
}




