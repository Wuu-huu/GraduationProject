package com.zzk.usermodule.service;

import com.zzk.usermodule.entity.UserFollow;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zzk.common.model.page.PageResponse;
import com.zzk.usermodule.dto.UserFollowPageQuery;
import com.zzk.usermodule.vo.UserCardVO;

/**
* @author 周振坤
* @description 针对表【user_follow(用户关注关系表)】的数据库操作Service
* @createDate 2026-03-19 23:30:17
*/
public interface UserFollowService extends IService<UserFollow> {

    void follow(Long targetUid);

    void unfollow(Long targetUid);

    boolean hasFollowed(Long followerUid, Long followeeUid);

    PageResponse<UserCardVO> getFollowers(Long targetUid, UserFollowPageQuery query);

    PageResponse<UserCardVO> getFollowing(Long targetUid, UserFollowPageQuery query);
}
