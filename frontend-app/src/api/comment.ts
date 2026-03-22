import request from './request'
import type { ApiResponse, PageResponse } from '@/types/api'
import type { CommentItem } from '@/types/comment'

export function getVideoComments(videoId: number, pageNum = 1, pageSize = 20): Promise<ApiResponse<PageResponse<CommentItem>>> {
  return request.get(`/api/videos/${videoId}/comments`, { params: { pageNum, pageSize } })
}

export function getCommentReplies(commentId: number, pageNum = 1, pageSize = 20): Promise<ApiResponse<PageResponse<CommentItem>>> {
  return request.get(`/api/comments/${commentId}/replies`, { params: { pageNum, pageSize } })
}

export function createComment(videoId: number, content: string): Promise<ApiResponse<CommentItem>> {
  return request.post('/api/comments', { vid: videoId, content })
}

export function likeComment(commentId: number): Promise<ApiResponse<CommentItem>> {
  return request.post(`/api/comments/${commentId}/like`)
}
