export interface CommentItem {
  commentId: number
  uid: number
  nickname: string
  avatarUrl?: string | null
  content: string
  likeCount: number
  createTime: string
}
