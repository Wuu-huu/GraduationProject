<template>
  <div class="page-shell">
    <section class="conversation-shell">
      <aside class="conversation-list-card">
        <div class="panel-head">
          <p class="section-eyebrow">会话列表</p>
          <h2>我的私信</h2>
        </div>
        <RouterLink
          v-for="item in messageStore.conversations"
          :key="item.conversationId"
          :to="`/messages/conversations/${item.conversationId}`"
          class="conversation-link"
        >
          {{ item.targetName || `用户 ${item.targetUid}` }}
        </RouterLink>
      </aside>
      <section class="conversation-detail-card">
        <div class="panel-head">
          <p class="section-eyebrow">消息内容</p>
          <h2>会话详情</h2>
        </div>
        <div class="message-stream">
          <article v-for="item in messageStore.currentMessages" :key="item.messageId" class="message-bubble">
            <strong>{{ item.senderName || `用户 ${item.senderUid}` }}</strong>
            <p>{{ item.content }}</p>
          </article>
        </div>
        <div class="message-composer">
          <el-input v-model="messageContent" type="textarea" :rows="3" placeholder="输入想发送的消息内容" />
          <div class="composer-footer">
            <el-button type="primary" :loading="sending" @click="handleSendMessage">发送消息</el-button>
          </div>
        </div>
      </section>
    </section>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useMessageStore } from '@/stores/messageStore'

const route = useRoute()
const messageStore = useMessageStore()
const messageContent = ref('')
const sending = ref(false)

const conversationId = computed(() => Number(route.params.conversationId))
const currentConversation = computed(() =>
  messageStore.conversations.find((item) => item.conversationId === conversationId.value) ?? null
)

async function loadConversation() {
  if (!Number.isNaN(conversationId.value)) {
    await Promise.all([messageStore.fetchOverview(), messageStore.fetchConversationDetail(conversationId.value)])
    await messageStore.markRead(conversationId.value)
  }
}

onMounted(loadConversation)
watch(() => route.params.conversationId, loadConversation)

async function handleSendMessage() {
  const content = messageContent.value.trim()
  if (!content) {
    ElMessage.warning('请输入消息内容')
    return
  }

  if (!currentConversation.value?.targetUid) {
    ElMessage.warning('未找到接收方')
    return
  }

  sending.value = true
  try {
    await messageStore.sendMessage(conversationId.value, currentConversation.value.targetUid, content)
    messageContent.value = ''
    ElMessage.success('消息发送成功')
  } finally {
    sending.value = false
  }
}
</script>

<style scoped lang="scss">
.conversation-shell {
  display: grid;
  grid-template-columns: 300px 1fr;
  gap: 20px;
}

.conversation-list-card,
.conversation-detail-card {
  padding: 22px;
  border-radius: 24px;
  border: 1px solid #e7ebf3;
  background: #fff;
  display: grid;
  gap: 14px;
}

.section-eyebrow {
  margin: 0 0 6px;
  color: #94a3b8;
  font-size: 12px;
  letter-spacing: 0.08em;
  text-transform: uppercase;
}

.conversation-link {
  padding: 12px 14px;
  border-radius: 14px;
  background: #f8fafc;
}

.message-stream {
  display: grid;
  gap: 12px;
}

.message-bubble {
  padding: 14px;
  border-radius: 16px;
  background: #f8fafc;

  p {
    margin: 6px 0 0;
  }
}

.message-composer {
  display: grid;
  gap: 12px;
  margin-top: 8px;
}

.composer-footer {
  display: flex;
  justify-content: flex-end;
}
</style>
