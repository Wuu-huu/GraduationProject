package com.zzk.interactionmodule.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzk.interactionmodule.dto.SaveFavoriteFolderRequest;
import com.zzk.interactionmodule.entity.FavoriteFolder;
import com.zzk.interactionmodule.vo.FavoriteFolderDetailVO;
import com.zzk.interactionmodule.vo.FavoriteFolderVO;
import java.util.List;

/**
* @author 周振坤
* @description 针对表【favorite_folder(收藏夹表)】的数据库操作Service
* @createDate 2026-03-19 23:35:04
*/
public interface FavoriteFolderService extends IService<FavoriteFolder> {

    FavoriteFolderVO createFolder(SaveFavoriteFolderRequest request);

    FavoriteFolderVO updateFolder(Long favoriteFolderId, SaveFavoriteFolderRequest request);

    void deleteFolder(Long favoriteFolderId);

    List<FavoriteFolderVO> listCurrentUserFolders();

    FavoriteFolderDetailVO getFolderDetail(Long favoriteFolderId);

    void addVideoToFolder(Long favoriteFolderId, Long videoId);

    void removeVideoFromFolder(Long favoriteFolderId, Long videoId);
}
