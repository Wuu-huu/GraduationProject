import request from './request'
import type { ApiResponse, PageResponse } from '@/types/api'
import type { ConversationItem, MessageItem, NotificationItem, UnreadCount } from '@/types/message'

export function createConversation(targetUid: number): Promise<ApiResponse<ConversationItem>> {
  return request.post('/api/messages/conversations', { targetUid })
}

export function getConversations(pageNum = 1, pageSize = 20): Promise<ApiResponse<PageResponse<ConversationItem>>> {
  return request.get('/api/messages/conversations', { params: { pageNum, pageSize } })
}

export function getConversationDetail(conversationId: number, pageNum = 1, pageSize = 20): Promise<ApiResponse<PageResponse<MessageItem>>> {
  return request.get(`/api/messages/conversations/${conversationId}`, { params: { pageNum, pageSize } })
}

export function getNotifications(pageNum = 1, pageSize = 20): Promise<ApiResponse<PageResponse<NotificationItem>>> {
  return request.get('/api/messages/notifications', { params: { pageNum, pageSize } })
}

export function markConversationRead(conversationId: number): Promise<ApiResponse<void>> {
  return request.put(`/api/messages/conversations/${conversationId}/read`)
}

export function getUnreadCount(): Promise<ApiResponse<UnreadCount>> {
  return request.get('/api/messages/unread-count')
}

export function sendMessage(conversationId: number, receiverUid: number, content: string): Promise<ApiResponse<MessageItem>> {
  return request.post(`/api/messages/conversations/${conversationId}/messages`, {
    receiverUid,
    msgType: 1,
    content
  })
}

export function markNotificationsRead(notificationIds: number[] = []): Promise<ApiResponse<void>> {
  return request.put('/api/messages/notifications/read', { notificationIds })
}
