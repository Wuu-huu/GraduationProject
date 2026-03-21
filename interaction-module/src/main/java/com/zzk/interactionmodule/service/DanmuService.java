package com.zzk.interactionmodule.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzk.interactionmodule.dto.CreateDanmuRequest;
import com.zzk.interactionmodule.entity.Danmu;
import com.zzk.interactionmodule.vo.DanmuVO;
import java.util.List;

/**
* @author 周振坤
* @description 针对表【danmu(弹幕表)】的数据库操作Service
* @createDate 2026-03-19 23:34:53
*/
public interface DanmuService extends IService<Danmu> {

    DanmuVO createDanmu(CreateDanmuRequest request);

    List<DanmuVO> listDanmuByVideo(Long videoId, Long partId);
}
