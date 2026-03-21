package com.zzk.interactionmodule.vo;

import com.zzk.videomodule.vo.VideoCardVO;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "收藏夹详情")
public class FavoriteFolderDetailVO {

    private final FavoriteFolderVO folder;
    private final List<VideoCardVO> videos;
}
