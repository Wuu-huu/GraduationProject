<template>
  <div class="page-shell message-page">
    <section class="message-hero">
      <article>
        <strong>{{ messageStore.unread.totalUnreadCount }}</strong>
        <span>未读总数</span>
      </article>
      <article>
        <strong>{{ messageStore.unread.conversationUnreadCount }}</strong>
        <span>私信未读</span>
      </article>
      <article>
        <strong>{{ messageStore.unread.notificationUnreadCount }}</strong>
        <span>通知未读</span>
      </article>
    </section>

    <section class="message-board">
      <div class="panel-card">
        <div class="panel-head">
          <div class="section-title-block">
            <p class="section-eyebrow">私信会话</p>
            <h2>最近聊天</h2>
          </div>
        </div>
        <RouterLink
          v-for="item in messageStore.conversations"
          :key="item.conversationId"
          :to="`/messages/conversations/${item.conversationId}`"
          class="conversation-item"
        >
          <strong>{{ item.targetName || `用户 ${item.targetUid}` }}</strong>
          <p>{{ item.latestContent || '暂时还没有消息' }}</p>
          <span>{{ item.unreadCount }} 条未读</span>
        </RouterLink>
        <EmptyState v-if="messageStore.conversations.length === 0" title="暂无会话" description="你的私信会话会显示在这里。" />
      </div>

      <div class="panel-card">
        <div class="panel-head">
          <div class="section-title-block">
            <p class="section-eyebrow">系统通知</p>
            <h2>通知列表</h2>
          </div>
          <el-button text type="primary" @click="handleMarkAllRead">全部标记已读</el-button>
        </div>
        <div v-for="item in messageStore.notifications" :key="item.notificationId" class="notification-item">
          <strong>{{ item.title }}</strong>
          <p>{{ item.content }}</p>
        </div>
        <EmptyState v-if="messageStore.notifications.length === 0" title="暂无通知" description="系统通知会显示在这里。" />
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import EmptyState from '@/components/common/EmptyState.vue'
import { useMessageStore } from '@/stores/messageStore'

const messageStore = useMessageStore()

onMounted(async () => {
  await messageStore.fetchOverview()
})

async function handleMarkAllRead() {
  await messageStore.markAllNotificationsRead()
  ElMessage.success('通知已全部标记为已读')
}
</script>

<style scoped lang="scss">
.message-page,
.message-board {
  display: grid;
  gap: 22px;
}

.message-hero {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 16px;
}

.message-hero article {
  padding: 22px;
  border-radius: 24px;
  background: linear-gradient(135deg, #eff6ff, #fff7ed 58%, #fae8ff);
  display: grid;
  gap: 6px;
}

.message-hero strong {
  font-size: 28px;
}

.message-board {
  grid-template-columns: 1.1fr 1fr;
}

.panel-card {
  padding: 22px;
  border-radius: 24px;
  border: 1px solid #e7ebf3;
  background: #fff;
  display: grid;
  gap: 14px;
}

.panel-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
}

.conversation-item,
.notification-item {
  padding: 14px 0;
  border-top: 1px solid #eef2f7;
  display: grid;
  gap: 6px;
}

.conversation-item p,
.notification-item p,
.conversation-item span {
  margin: 0;
  color: #64748b;
}

.conversation-item span {
  font-size: 12px;
}
</style>