package com.zzk.interactionmodule.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzk.common.security.SecurityContextUtils;
import com.zzk.interactionmodule.dto.CreateDanmuRequest;
import com.zzk.interactionmodule.entity.Danmu;
import com.zzk.interactionmodule.mapper.DanmuMapper;
import com.zzk.interactionmodule.service.DanmuService;
import com.zzk.interactionmodule.vo.DanmuVO;
import com.zzk.videomodule.facade.VideoFacade;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DanmuServiceImpl extends ServiceImpl<DanmuMapper, Danmu>
        implements DanmuService {

    private final VideoFacade videoFacade;

    public DanmuServiceImpl(VideoFacade videoFacade) {
        this.videoFacade = videoFacade;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DanmuVO createDanmu(CreateDanmuRequest request) {
        videoFacade.assertVideoAccessible(request.getVid());
        Danmu danmu = new Danmu();
        danmu.setVid(request.getVid());
        danmu.setPartId(request.getPartId());
        danmu.setUid(SecurityContextUtils.getCurrentUserId());
        danmu.setContent(request.getContent().trim());
        danmu.setColor(normalizeColor(request.getColor()));
        danmu.setFontSize(request.getFontSize() == null ? 25 : request.getFontSize());
        danmu.setMode(request.getMode() == null ? 1 : request.getMode());
        danmu.setTimePointMs(request.getTimePointMs());
        danmu.setStatus(1);
        danmu.setCreateTime(new Date());
        save(danmu);
        videoFacade.adjustVideoStats(request.getVid(), 0, 0, 0, 0, 0, 1, 0);
        return toVO(danmu);
    }

    @Override
    public List<DanmuVO> listDanmuByVideo(Long videoId, Long partId) {
        videoFacade.assertVideoAccessible(videoId);
        return list(Wrappers.<Danmu>lambdaQuery()
                .eq(Danmu::getVid, videoId)
                .eq(partId != null, Danmu::getPartId, partId)
                .eq(Danmu::getStatus, 1)
                .orderByAsc(Danmu::getTimePointMs))
                .stream()
                .map(this::toVO)
                .toList();
    }

    private DanmuVO toVO(Danmu danmu) {
        return DanmuVO.builder()
                .danmuId(danmu.getDanmuId())
                .vid(danmu.getVid())
                .partId(danmu.getPartId())
                .uid(danmu.getUid())
                .content(danmu.getContent())
                .color(danmu.getColor())
                .fontSize(danmu.getFontSize())
                .mode(danmu.getMode())
                .timePointMs(danmu.getTimePointMs())
                .createTime(danmu.getCreateTime())
                .build();
    }

    private String normalizeColor(String color) {
        if (color == null || color.isBlank()) {
            return "#FFFFFF";
        }
        String value = color.trim();
        if (value.matches("^#[0-9A-Fa-f]{6}$")) {
            return value.toUpperCase();
        }
        if (value.matches("^[0-9]{1,8}$")) {
            int rgb = Integer.parseInt(value);
            return String.format("#%06X", rgb & 0xFFFFFF);
        }
        return "#FFFFFF";
    }
}