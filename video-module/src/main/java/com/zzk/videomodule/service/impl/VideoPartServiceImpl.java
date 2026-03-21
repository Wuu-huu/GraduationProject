package com.zzk.videomodule.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzk.common.enums.ApiCodeEnum;
import com.zzk.common.exception.BusinessException;
import com.zzk.common.security.SecurityContextUtils;
import com.zzk.videomodule.dto.VideoPartUpsertRequest;
import com.zzk.videomodule.entity.Video;
import com.zzk.videomodule.entity.VideoPart;
import com.zzk.videomodule.enums.VideoStatusEnum;
import com.zzk.videomodule.mapper.VideoMapper;
import com.zzk.videomodule.mapper.VideoPartMapper;
import com.zzk.videomodule.service.VideoPartService;
import com.zzk.videomodule.vo.VideoPartVO;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @author 周振坤
* @description 针对表【video_part(视频分P表)】的数据库操作Service实现
* @createDate 2026-03-19 23:32:53
*/
@Service
public class VideoPartServiceImpl extends ServiceImpl<VideoPartMapper, VideoPart>
        implements VideoPartService {

    private final VideoMapper videoMapper;

    public VideoPartServiceImpl(VideoMapper videoMapper) {
        this.videoMapper = videoMapper;
    }

    @Override
    public List<VideoPartVO> listVideoParts(Long videoId) {
        ensureVideoAccessible(videoId, false);
        return list(Wrappers.<VideoPart>lambdaQuery()
                .eq(VideoPart::getVid, videoId)
                .eq(VideoPart::getStatus, VideoStatusEnum.NORMAL.getCode())
                .orderByAsc(VideoPart::getPartNo))
                .stream()
                .map(this::toVO)
                .toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public VideoPartVO addVideoPart(Long videoId, VideoPartUpsertRequest request) {
        ensureVideoAccessible(videoId, true);
        VideoPart entity = new VideoPart();
        entity.setVid(videoId);
        applyRequest(entity, request);
        entity.setCreateTime(new Date());
        save(entity);
        return toVO(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public VideoPartVO updateVideoPart(Long videoId, Long partId, VideoPartUpsertRequest request) {
        ensureVideoAccessible(videoId, true);
        VideoPart entity = getById(partId);
        if (entity == null || !videoId.equals(entity.getVid())) {
            throw new BusinessException(ApiCodeEnum.BAD_REQUEST, "Video part not found");
        }
        applyRequest(entity, request);
        updateById(entity);
        return toVO(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteVideoPart(Long videoId, Long partId) {
        ensureVideoAccessible(videoId, true);
        VideoPart entity = getById(partId);
        if (entity == null || !videoId.equals(entity.getVid())) {
            throw new BusinessException(ApiCodeEnum.BAD_REQUEST, "Video part not found");
        }
        entity.setStatus(VideoStatusEnum.DELETED.getCode());
        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void replaceVideoParts(Long videoId, List<VideoPartUpsertRequest> requests) {
        if (requests == null || requests.isEmpty()) {
            return;
        }
        ensureVideoAccessible(videoId, true);
        remove(Wrappers.<VideoPart>lambdaQuery().eq(VideoPart::getVid, videoId));
        requests.stream()
                .sorted(Comparator.comparing(VideoPartUpsertRequest::getPartNo))
                .forEach(request -> addVideoPart(videoId, request));
    }

    private void ensureVideoAccessible(Long videoId, boolean ownerOnly) {
        Video video = videoMapper.selectById(videoId);
        if (video == null || VideoStatusEnum.DELETED.getCode() == value(video.getStatus())) {
            throw new BusinessException(ApiCodeEnum.BAD_REQUEST, "Video not found");
        }
        if (ownerOnly && !SecurityContextUtils.getCurrentUserId().equals(video.getUid())) {
            throw new BusinessException(ApiCodeEnum.FORBIDDEN, "You can only modify your own video");
        }
    }

    private void applyRequest(VideoPart entity, VideoPartUpsertRequest request) {
        entity.setPartNo(request.getPartNo());
        entity.setTitle(request.getTitle().trim());
        entity.setVideoUrl(request.getVideoUrl().trim());
        entity.setDurationSec(request.getDurationSec());
        entity.setSizeBytes(request.getSizeBytes());
        entity.setStatus(VideoStatusEnum.NORMAL.getCode());
    }

    private VideoPartVO toVO(VideoPart entity) {
        return VideoPartVO.builder()
                .partId(entity.getPartId())
                .partNo(entity.getPartNo())
                .title(entity.getTitle())
                .videoUrl(entity.getVideoUrl())
                .durationSec(entity.getDurationSec())
                .sizeBytes(entity.getSizeBytes())
                .build();
    }

    private int value(Integer value) {
        return value == null ? 0 : value;
    }
}




