package com.zzk.interactionmodule.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzk.authmodule.entity.UserInfo;
import com.zzk.authmodule.service.UserInfoService;
import com.zzk.common.enums.ApiCodeEnum;
import com.zzk.common.exception.BusinessException;
import com.zzk.common.model.page.PageResponse;
import com.zzk.common.security.SecurityContextUtils;
import com.zzk.interactionmodule.dto.CommentPageQuery;
import com.zzk.interactionmodule.dto.CreateCommentRequest;
import com.zzk.interactionmodule.entity.Comment;
import com.zzk.interactionmodule.entity.CommentAction;
import com.zzk.interactionmodule.entity.VideoAction;
import com.zzk.interactionmodule.enums.CommentStatusEnum;
import com.zzk.interactionmodule.enums.VideoActionTypeEnum;
import com.zzk.interactionmodule.mapper.CommentMapper;
import com.zzk.interactionmodule.mapper.CommentActionMapper;
import com.zzk.interactionmodule.mapper.VideoActionMapper;
import com.zzk.interactionmodule.service.CommentService;
import com.zzk.interactionmodule.vo.CommentVO;
import com.zzk.usermodule.entity.UserProfile;
import com.zzk.usermodule.service.UserProfileService;
import com.zzk.videomodule.facade.VideoFacade;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @author 周振坤
* @description 针对表【comment(评论表)】的数据库操作Service实现
* @createDate 2026-03-19 23:34:29
*/
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
        implements CommentService {

    private final CommentActionMapper commentActionMapper;
    private final VideoActionMapper videoActionMapper;
    private final UserInfoService userInfoService;
    private final UserProfileService userProfileService;
    private final VideoFacade videoFacade;

    public CommentServiceImpl(CommentActionMapper commentActionMapper,
                              VideoActionMapper videoActionMapper,
                              UserInfoService userInfoService,
                              UserProfileService userProfileService,
                              VideoFacade videoFacade) {
        this.commentActionMapper = commentActionMapper;
        this.videoActionMapper = videoActionMapper;
        this.userInfoService = userInfoService;
        this.userProfileService = userProfileService;
        this.videoFacade = videoFacade;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommentVO createComment(CreateCommentRequest request) {
        if (request.getVid() == null) {
            throw new BusinessException(ApiCodeEnum.BAD_REQUEST, "Video id is required");
        }
        videoFacade.assertVideoAccessible(request.getVid());
        Long currentUserId = SecurityContextUtils.getCurrentUserId();
        Comment comment = new Comment();
        comment.setVid(request.getVid());
        comment.setUid(currentUserId);
        comment.setRootId(0L);
        comment.setParentId(0L);
        comment.setReplyToUid(0L);
        comment.setContent(request.getContent().trim());
        comment.setStatus(CommentStatusEnum.NORMAL.getCode());
        comment.setIsTop(0);
        comment.setLikeCount(0);
        comment.setReplyCount(0);
        comment.setCreateTime(new Date());
        comment.setUpdateTime(new Date());
        save(comment);
        videoFacade.adjustVideoStats(request.getVid(), 0, 0, 0, 0, 1, 0, 0);
        recordVideoAction(request.getVid(), VideoActionTypeEnum.COMMENT, 1, 0);
        return toVO(comment, currentUserId, false);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommentVO replyComment(Long commentId, CreateCommentRequest request) {
        Comment parent = requireComment(commentId);
        videoFacade.assertVideoAccessible(parent.getVid());
        Long currentUserId = SecurityContextUtils.getCurrentUserId();
        Comment comment = new Comment();
        comment.setVid(parent.getVid());
        comment.setUid(currentUserId);
        comment.setRootId(value(parent.getRootId()) == 0L ? parent.getCommentId() : parent.getRootId());
        comment.setParentId(parent.getCommentId());
        comment.setReplyToUid(parent.getUid() == null ? 0L : parent.getUid());
        comment.setContent(request.getContent().trim());
        comment.setStatus(CommentStatusEnum.NORMAL.getCode());
        comment.setIsTop(0);
        comment.setLikeCount(0);
        comment.setReplyCount(0);
        comment.setCreateTime(new Date());
        comment.setUpdateTime(new Date());
        save(comment);
        parent.setReplyCount(value(parent.getReplyCount()) + 1);
        parent.setUpdateTime(new Date());
        updateById(parent);
        videoFacade.adjustVideoStats(parent.getVid(), 0, 0, 0, 0, 1, 0, 0);
        recordVideoAction(parent.getVid(), VideoActionTypeEnum.COMMENT, 1, 0);
        return toVO(comment, currentUserId, false);
    }

    @Override
    public PageResponse<CommentVO> listVideoComments(Long videoId, CommentPageQuery query) {
        videoFacade.assertVideoAccessible(videoId);
        Page<Comment> page = page(new Page<>(query.getPageNum(), query.getPageSize()), Wrappers.<Comment>lambdaQuery()
                .eq(Comment::getVid, videoId)
                .eq(Comment::getParentId, 0L)
                .eq(Comment::getStatus, CommentStatusEnum.NORMAL.getCode())
                .orderByDesc(Comment::getCreateTime));
        Long currentUserId = currentUserIdOrNull();
        return PageResponse.<CommentVO>builder()
                .records(toVOs(page.getRecords(), currentUserId))
                .total(page.getTotal())
                .pageNum(page.getCurrent())
                .pageSize(page.getSize())
                .build();
    }

    @Override
    public PageResponse<CommentVO> listReplies(Long commentId, CommentPageQuery query) {
        Comment root = requireComment(commentId);
        Long rootId = value(root.getRootId()) == 0L ? root.getCommentId() : root.getRootId();
        Page<Comment> page = page(new Page<>(query.getPageNum(), query.getPageSize()), Wrappers.<Comment>lambdaQuery()
                .eq(Comment::getRootId, rootId)
                .ne(Comment::getParentId, 0L)
                .eq(Comment::getStatus, CommentStatusEnum.NORMAL.getCode())
                .orderByAsc(Comment::getCreateTime));
        Long currentUserId = currentUserIdOrNull();
        return PageResponse.<CommentVO>builder()
                .records(toVOs(page.getRecords(), currentUserId))
                .total(page.getTotal())
                .pageNum(page.getCurrent())
                .pageSize(page.getSize())
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommentVO likeComment(Long commentId) {
        Comment comment = requireComment(commentId);
        Long currentUserId = SecurityContextUtils.getCurrentUserId();
        CommentAction action = commentActionMapper.selectOne(Wrappers.<CommentAction>lambdaQuery()
                .eq(CommentAction::getUid, currentUserId)
                .eq(CommentAction::getCommentId, commentId)
                .eq(CommentAction::getActionType, 1)
                .last("limit 1"));
        if (action == null) {
            action = new CommentAction();
            action.setUid(currentUserId);
            action.setCommentId(commentId);
            action.setActionType(1);
            action.setActionTime(new Date());
            action.setCancelFlag(0);
            commentActionMapper.insert(action);
            comment.setLikeCount(value(comment.getLikeCount()) + 1);
            updateById(comment);
        } else if (value(action.getCancelFlag()) == 1) {
            action.setCancelFlag(0);
            action.setActionTime(new Date());
            commentActionMapper.updateById(action);
            comment.setLikeCount(value(comment.getLikeCount()) + 1);
            updateById(comment);
        }
        return toVO(comment, currentUserId, true);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommentVO cancelLikeComment(Long commentId) {
        Comment comment = requireComment(commentId);
        Long currentUserId = SecurityContextUtils.getCurrentUserId();
        CommentAction action = commentActionMapper.selectOne(Wrappers.<CommentAction>lambdaQuery()
                .eq(CommentAction::getUid, currentUserId)
                .eq(CommentAction::getCommentId, commentId)
                .eq(CommentAction::getActionType, 1)
                .eq(CommentAction::getCancelFlag, 0)
                .last("limit 1"));
        if (action != null) {
            action.setCancelFlag(1);
            action.setActionTime(new Date());
            commentActionMapper.updateById(action);
            comment.setLikeCount(Math.max(0, value(comment.getLikeCount()) - 1));
            updateById(comment);
        }
        return toVO(comment, currentUserId, false);
    }

    private Comment requireComment(Long commentId) {
        Comment comment = getById(commentId);
        if (comment == null || value(comment.getStatus()) != CommentStatusEnum.NORMAL.getCode()) {
            throw new BusinessException(ApiCodeEnum.BAD_REQUEST, "Comment not found");
        }
        return comment;
    }

    private List<CommentVO> toVOs(List<Comment> comments, Long currentUserId) {
        if (comments.isEmpty()) {
            return List.of();
        }
        List<Long> userIds = comments.stream().map(Comment::getUid).distinct().toList();
        Map<Long, UserInfo> userInfoMap = userIds.stream()
                .map(userInfoService::getById)
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(UserInfo::getUid, Function.identity()));
        Map<Long, UserProfile> profileMap = userIds.stream()
                .map(userProfileService::getById)
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(UserProfile::getUid, Function.identity()));
        Map<Long, Boolean> likedMap = currentUserId == null ? Map.of() : commentActionMapper.selectList(Wrappers.<CommentAction>lambdaQuery()
                        .eq(CommentAction::getUid, currentUserId)
                        .eq(CommentAction::getActionType, 1)
                        .eq(CommentAction::getCancelFlag, 0)
                        .in(CommentAction::getCommentId, comments.stream().map(Comment::getCommentId).toList()))
                .stream()
                .collect(Collectors.toMap(CommentAction::getCommentId, action -> Boolean.TRUE));
        return comments.stream()
                .map(comment -> {
                    UserInfo userInfo = userInfoMap.get(comment.getUid());
                    UserProfile profile = profileMap.get(comment.getUid());
                    return CommentVO.builder()
                            .commentId(comment.getCommentId())
                            .vid(comment.getVid())
                            .uid(comment.getUid())
                            .username(userInfo == null ? null : userInfo.getUsername())
                            .nickname(profile == null ? null : profile.getNickname())
                            .avatarUrl(profile == null ? null : profile.getAvatarUrl())
                            .rootId(comment.getRootId())
                            .parentId(comment.getParentId())
                            .replyToUid(comment.getReplyToUid())
                            .content(comment.getContent())
                            .likeCount(value(comment.getLikeCount()))
                            .replyCount(value(comment.getReplyCount()))
                            .liked(Boolean.TRUE.equals(likedMap.get(comment.getCommentId())))
                            .createTime(comment.getCreateTime())
                            .build();
                })
                .toList();
    }

    private CommentVO toVO(Comment comment, Long currentUserId, boolean liked) {
        UserInfo userInfo = userInfoService.getById(comment.getUid());
        UserProfile profile = userProfileService.getById(comment.getUid());
        return CommentVO.builder()
                .commentId(comment.getCommentId())
                .vid(comment.getVid())
                .uid(comment.getUid())
                .username(userInfo == null ? null : userInfo.getUsername())
                .nickname(profile == null ? null : profile.getNickname())
                .avatarUrl(profile == null ? null : profile.getAvatarUrl())
                .rootId(comment.getRootId())
                .parentId(comment.getParentId())
                .replyToUid(comment.getReplyToUid())
                .content(comment.getContent())
                .likeCount(value(comment.getLikeCount()))
                .replyCount(value(comment.getReplyCount()))
                .liked(liked)
                .createTime(comment.getCreateTime())
                .build();
    }

    private void recordVideoAction(Long videoId, VideoActionTypeEnum actionType, Integer actionValue, Integer cancelFlag) {
        VideoAction action = new VideoAction();
        action.setUid(SecurityContextUtils.getCurrentUserId());
        action.setVid(videoId);
        action.setActionType(actionType.getCode());
        action.setActionValue(actionValue);
        action.setActionTime(new Date());
        action.setCancelFlag(cancelFlag);
        videoActionMapper.insert(action);
    }

    private Long currentUserIdOrNull() {
        try {
            return SecurityContextUtils.getCurrentUserId();
        } catch (Exception ex) {
            return null;
        }
    }

    private int value(Integer value) {
        return value == null ? 0 : value;
    }

    private long value(Long value) {
        return value == null ? 0L : value;
    }
}




