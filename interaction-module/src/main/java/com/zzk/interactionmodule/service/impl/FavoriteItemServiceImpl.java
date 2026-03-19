package com.zzk.interactionmodule.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzk.interactionmodule.entity.FavoriteItem;
import com.zzk.interactionmodule.service.FavoriteItemService;
import com.zzk.interactionmodule.mapper.FavoriteItemMapper;
import org.springframework.stereotype.Service;

/**
* @author 周振坤
* @description 针对表【favorite_item(收藏夹视频关系表)】的数据库操作Service实现
* @createDate 2026-03-19 23:35:08
*/
@Service
public class FavoriteItemServiceImpl extends ServiceImpl<FavoriteItemMapper, FavoriteItem>
    implements FavoriteItemService{

}




