package com.zzk.interactionmodule.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzk.common.enums.ApiCodeEnum;
import com.zzk.common.exception.BusinessException;
import com.zzk.common.security.SecurityContextUtils;
import com.zzk.interactionmodule.dto.SaveFavoriteFolderRequest;
import com.zzk.interactionmodule.entity.FavoriteFolder;
import com.zzk.interactionmodule.entity.FavoriteItem;
import com.zzk.interactionmodule.entity.UserVideoState;
import com.zzk.interactionmodule.mapper.FavoriteItemMapper;
import com.zzk.interactionmodule.mapper.FavoriteFolderMapper;
import com.zzk.interactionmodule.mapper.UserVideoStateMapper;
import com.zzk.interactionmodule.service.FavoriteFolderService;
import com.zzk.interactionmodule.vo.FavoriteFolderDetailVO;
import com.zzk.interactionmodule.vo.FavoriteFolderVO;
import com.zzk.videomodule.facade.VideoFacade;
import com.zzk.videomodule.vo.VideoCardVO;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @author 周振坤
* @description 针对表【favorite_folder(收藏夹表)】的数据库操作Service实现
* @createDate 2026-03-19 23:35:04
*/
@Service
public class FavoriteFolderServiceImpl extends ServiceImpl<FavoriteFolderMapper, FavoriteFolder>
        implements FavoriteFolderService {

    private final FavoriteItemMapper favoriteItemMapper;
    private final UserVideoStateMapper userVideoStateMapper;
    private final VideoFacade videoFacade;

    public FavoriteFolderServiceImpl(FavoriteItemMapper favoriteItemMapper,
                                     UserVideoStateMapper userVideoStateMapper,
                                     VideoFacade videoFacade) {
        this.favoriteItemMapper = favoriteItemMapper;
        this.userVideoStateMapper = userVideoStateMapper;
        this.videoFacade = videoFacade;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FavoriteFolderVO createFolder(SaveFavoriteFolderRequest request) {
        Long currentUserId = SecurityContextUtils.getCurrentUserId();
        FavoriteFolder folder = new FavoriteFolder();
        fillFolder(folder, request, currentUserId);
        Date now = new Date();
        folder.setItemCount(0);
        folder.setStatus(1);
        folder.setCreateTime(now);
        folder.setUpdateTime(now);
        save(folder);
        return toVO(folder);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FavoriteFolderVO updateFolder(Long favoriteFolderId, SaveFavoriteFolderRequest request) {
        FavoriteFolder folder = getOwnedFolder(favoriteFolderId);
        fillFolder(folder, request, folder.getUid());
        folder.setUpdateTime(new Date());
        updateById(folder);
        return toVO(folder);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFolder(Long favoriteFolderId) {
        FavoriteFolder folder = getOwnedFolder(favoriteFolderId);
        folder.setStatus(0);
        folder.setUpdateTime(new Date());
        updateById(folder);
    }

    @Override
    public List<FavoriteFolderVO> listCurrentUserFolders() {
        return list(Wrappers.<FavoriteFolder>lambdaQuery()
                .eq(FavoriteFolder::getUid, SecurityContextUtils.getCurrentUserId())
                .eq(FavoriteFolder::getStatus, 1)
                .orderByAsc(FavoriteFolder::getSortNo)
                .orderByDesc(FavoriteFolder::getUpdateTime))
                .stream()
                .map(this::toVO)
                .toList();
    }

    @Override
    public FavoriteFolderDetailVO getFolderDetail(Long favoriteFolderId) {
        FavoriteFolder folder = getOwnedFolder(favoriteFolderId);
        List<Long> videoIds = favoriteItemMapper.selectList(Wrappers.<FavoriteItem>lambdaQuery()
                        .eq(FavoriteItem::getFid, favoriteFolderId)
                        .eq(FavoriteItem::getStatus, 1)
                        .orderByDesc(FavoriteItem::getCreateTime))
                .stream()
                .map(FavoriteItem::getVid)
                .toList();
        List<VideoCardVO> videos = videoFacade.listVideoCards(videoIds);
        return FavoriteFolderDetailVO.builder()
                .folder(toVO(folder))
                .videos(videos)
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addVideoToFolder(Long favoriteFolderId, Long videoId) {
        FavoriteFolder folder = getOwnedFolder(favoriteFolderId);
        videoFacade.assertVideoAccessible(videoId);
        FavoriteItem item = favoriteItemMapper.selectOne(Wrappers.<FavoriteItem>lambdaQuery()
                .eq(FavoriteItem::getFid, favoriteFolderId)
                .eq(FavoriteItem::getVid, videoId)
                .eq(FavoriteItem::getUid, folder.getUid())
                .last("limit 1"));
        boolean alreadyFavorited = hasAnyFavorite(folder.getUid(), videoId);
        if (item == null) {
            item = new FavoriteItem();
            item.setFid(favoriteFolderId);
            item.setVid(videoId);
            item.setUid(folder.getUid());
            item.setStatus(1);
            item.setCreateTime(new Date());
            favoriteItemMapper.insert(item);
        } else if (value(item.getStatus()) == 0) {
            item.setStatus(1);
            favoriteItemMapper.updateById(item);
        } else {
            return;
        }
        folder.setItemCount(value(folder.getItemCount()) + 1);
        folder.setUpdateTime(new Date());
        updateById(folder);
        if (!alreadyFavorited) {
            videoFacade.adjustVideoStats(videoId, 0, 0, 0, 1, 0, 0, 0);
        }
        updateFavoriteState(folder.getUid(), videoId, true);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeVideoFromFolder(Long favoriteFolderId, Long videoId) {
        FavoriteFolder folder = getOwnedFolder(favoriteFolderId);
        FavoriteItem item = favoriteItemMapper.selectOne(Wrappers.<FavoriteItem>lambdaQuery()
                .eq(FavoriteItem::getFid, favoriteFolderId)
                .eq(FavoriteItem::getVid, videoId)
                .eq(FavoriteItem::getUid, folder.getUid())
                .eq(FavoriteItem::getStatus, 1)
                .last("limit 1"));
        if (item == null) {
            return;
        }
        item.setStatus(0);
        favoriteItemMapper.updateById(item);
        folder.setItemCount(Math.max(0, value(folder.getItemCount()) - 1));
        folder.setUpdateTime(new Date());
        updateById(folder);
        boolean stillFavorited = hasAnyFavorite(folder.getUid(), videoId);
        if (!stillFavorited) {
            videoFacade.adjustVideoStats(videoId, 0, 0, 0, -1, 0, 0, 0);
        }
        updateFavoriteState(folder.getUid(), videoId, stillFavorited);
    }

    private FavoriteFolder getOwnedFolder(Long favoriteFolderId) {
        FavoriteFolder folder = getById(favoriteFolderId);
        if (folder == null || value(folder.getStatus()) != 1) {
            throw new BusinessException(ApiCodeEnum.BAD_REQUEST, "Favorite folder not found");
        }
        if (!SecurityContextUtils.getCurrentUserId().equals(folder.getUid())) {
            throw new BusinessException(ApiCodeEnum.FORBIDDEN, "You can only modify your own favorite folder");
        }
        return folder;
    }

    private void fillFolder(FavoriteFolder folder, SaveFavoriteFolderRequest request, Long uid) {
        folder.setUid(uid);
        folder.setFolderType(request.getFolderType());
        folder.setTitle(request.getTitle().trim());
        folder.setDescription(blankToNull(request.getDescription()));
        folder.setCoverUrl(blankToNull(request.getCoverUrl()));
        folder.setVisible(request.getVisible());
        folder.setSortNo(request.getSortNo());
    }

    private FavoriteFolderVO toVO(FavoriteFolder folder) {
        return FavoriteFolderVO.builder()
                .fid(folder.getFid())
                .uid(folder.getUid())
                .folderType(folder.getFolderType())
                .title(folder.getTitle())
                .description(folder.getDescription())
                .coverUrl(folder.getCoverUrl())
                .visible(folder.getVisible())
                .sortNo(folder.getSortNo())
                .itemCount(folder.getItemCount())
                .createTime(folder.getCreateTime())
                .updateTime(folder.getUpdateTime())
                .build();
    }

    private boolean hasAnyFavorite(Long uid, Long videoId) {
        return favoriteItemMapper.selectCount(Wrappers.<FavoriteItem>lambdaQuery()
                .eq(FavoriteItem::getUid, uid)
                .eq(FavoriteItem::getVid, videoId)
                .eq(FavoriteItem::getStatus, 1)) > 0;
    }

    private void updateFavoriteState(Long uid, Long videoId, boolean favorited) {
        UserVideoState state = userVideoStateMapper.selectOne(Wrappers.<UserVideoState>lambdaQuery()
                .eq(UserVideoState::getUid, uid)
                .eq(UserVideoState::getVid, videoId)
                .last("limit 1"));
        if (state == null) {
            state = new UserVideoState();
            state.setUid(uid);
            state.setVid(videoId);
            state.setLiked(0);
            state.setDisliked(0);
            state.setCoinCount(0);
            state.setWatchLater(0);
            state.setFavorited(favorited ? 1 : 0);
            state.setTotalWatchSec(0);
            state.setPlayTimes(0);
            state.setLastPlayProgressSec(0);
            userVideoStateMapper.insert(state);
            return;
        }
        state.setFavorited(favorited ? 1 : 0);
        userVideoStateMapper.updateById(state);
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private int value(Integer value) {
        return value == null ? 0 : value;
    }
}




