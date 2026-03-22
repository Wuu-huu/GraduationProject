import { defineStore } from 'pinia'
import {
  createConversation,
  getConversations,
  getConversationDetail,
  getNotifications,
  getUnreadCount,
  markConversationRead,
  markNotificationsRead,
  sendMessage
} from '@/api/message'
import type { ConversationItem, MessageItem, NotificationItem, UnreadCount } from '@/types/message'

export const useMessageStore = defineStore('messageStore', {
  state: () => ({
    conversations: [] as ConversationItem[],
    notifications: [] as NotificationItem[],
    currentMessages: [] as MessageItem[],
    unread: {
      conversationUnreadCount: 0,
      notificationUnreadCount: 0,
      totalUnreadCount: 0
    } as UnreadCount
  }),
  actions: {
    async fetchOverview() {
      const [conversations, notifications, unread] = await Promise.all([
        getConversations(),
        getNotifications(),
        getUnreadCount()
      ])
      this.conversations = conversations.data.records
      this.notifications = notifications.data.records
      this.unread = unread.data
    },
    async fetchConversationDetail(conversationId: number) {
      const response = await getConversationDetail(conversationId)
      this.currentMessages = response.data.records
    },
    async markRead(conversationId: number) {
      await markConversationRead(conversationId)
      await this.fetchOverview()
    },
    async createConversation(targetUid: number) {
      const response = await createConversation(targetUid)
      await this.fetchOverview()
      return response.data
    },
    async sendMessage(conversationId: number, receiverUid: number, content: string) {
      const response = await sendMessage(conversationId, receiverUid, content)
      this.currentMessages = [...this.currentMessages, response.data]
      await this.fetchOverview()
      return response.data
    },
    async markAllNotificationsRead() {
      await markNotificationsRead()
      await this.fetchOverview()
    }
  }
})
