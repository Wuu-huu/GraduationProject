export interface ConversationItem {
  conversationId: number
  conversationType?: number
  targetUid?: number
  targetName?: string
  targetAvatarUrl?: string | null
  latestMessageId?: number
  latestContent?: string
  latestTime?: string
  unreadCount: number
}

export interface NotificationItem {
  notificationId: number
  noticeType?: number
  bizType?: number
  bizId?: number
  title: string
  content: string
  isRead: number
  createTime: string
}

export interface MessageItem {
  messageId: number
  conversationId: number
  senderUid: number
  receiverUid: number
  senderName?: string
  senderAvatarUrl?: string | null
  msgType?: number
  content: string
  isRecalled?: number
  createTime: string
}

export interface UnreadCount {
  conversationUnreadCount: number
  notificationUnreadCount: number
  totalUnreadCount: number
}
