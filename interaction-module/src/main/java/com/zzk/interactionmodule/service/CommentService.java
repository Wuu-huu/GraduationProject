package com.zzk.interactionmodule.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzk.common.model.page.PageResponse;
import com.zzk.interactionmodule.dto.CommentPageQuery;
import com.zzk.interactionmodule.dto.CreateCommentRequest;
import com.zzk.interactionmodule.entity.Comment;
import com.zzk.interactionmodule.vo.CommentVO;

/**
* @author 周振坤
* @description 针对表【comment(评论表)】的数据库操作Service
* @createDate 2026-03-19 23:34:29
*/
public interface CommentService extends IService<Comment> {

    CommentVO createComment(CreateCommentRequest request);

    CommentVO replyComment(Long commentId, CreateCommentRequest request);

    PageResponse<CommentVO> listVideoComments(Long videoId, CommentPageQuery query);

    PageResponse<CommentVO> listReplies(Long commentId, CommentPageQuery query);

    CommentVO likeComment(Long commentId);

    CommentVO cancelLikeComment(Long commentId);
}
