package com.zzk.interactionmodule.controller;

import com.zzk.common.model.page.PageResponse;
import com.zzk.common.model.response.ApiResponse;
import com.zzk.interactionmodule.dto.CommentPageQuery;
import com.zzk.interactionmodule.dto.CreateCommentRequest;
import com.zzk.interactionmodule.service.CommentService;
import com.zzk.interactionmodule.vo.CommentVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "评论互动")
@RestController
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @Operation(summary = "发表评论")
    @PostMapping("/comments")
    public ApiResponse<CommentVO> createComment(@Valid @RequestBody CreateCommentRequest request) {
        return ApiResponse.success(commentService.createComment(request));
    }

    @Operation(summary = "回复评论")
    @PostMapping("/comments/{commentId}/reply")
    public ApiResponse<CommentVO> replyComment(@PathVariable Long commentId,
                                               @Valid @RequestBody CreateCommentRequest request) {
        return ApiResponse.success(commentService.replyComment(commentId, request));
    }

    @Operation(summary = "查询视频评论列表")
    @GetMapping("/videos/{videoId}/comments")
    public ApiResponse<PageResponse<CommentVO>> listVideoComments(@PathVariable Long videoId,
                                                                  @Valid CommentPageQuery query) {
        return ApiResponse.success(commentService.listVideoComments(videoId, query));
    }

    @Operation(summary = "查询评论回复列表")
    @GetMapping("/comments/{commentId}/replies")
    public ApiResponse<PageResponse<CommentVO>> listReplies(@PathVariable Long commentId,
                                                            @Valid CommentPageQuery query) {
        return ApiResponse.success(commentService.listReplies(commentId, query));
    }

    @Operation(summary = "点赞评论")
    @PostMapping("/comments/{commentId}/like")
    public ApiResponse<CommentVO> likeComment(@PathVariable Long commentId) {
        return ApiResponse.success(commentService.likeComment(commentId));
    }

    @Operation(summary = "取消评论点赞")
    @DeleteMapping("/comments/{commentId}/like")
    public ApiResponse<CommentVO> cancelLikeComment(@PathVariable Long commentId) {
        return ApiResponse.success(commentService.cancelLikeComment(commentId));
    }
}
