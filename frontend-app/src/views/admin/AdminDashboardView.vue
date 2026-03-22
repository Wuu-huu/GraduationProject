<template>
  <div class="page-shell">
    <section class="admin-overview">
      <article>
        <strong>{{ videoAuditCount }}</strong>
        <span>视频审核</span>
      </article>
      <article>
        <strong>{{ commentAuditCount }}</strong>
        <span>评论审核</span>
      </article>
      <article>
        <strong>{{ reportCount }}</strong>
        <span>举报处理</span>
      </article>
    </section>

    <section class="panel-card">
      <div class="panel-head">
        <div>
          <p class="section-eyebrow">待办概览</p>
          <h2>最近审核队列</h2>
        </div>
      </div>
      <div class="queue-grid">
        <div class="queue-card">
          <h3>视频审核</h3>
          <p>当前已从后端加载 {{ videoAuditCount }} 条待处理任务。</p>
        </div>
        <div class="queue-card">
          <h3>评论审核</h3>
          <p>当前已从后端加载 {{ commentAuditCount }} 条待处理任务。</p>
        </div>
        <div class="queue-card">
          <h3>举报处理</h3>
          <p>当前已从后端加载 {{ reportCount }} 条举报记录。</p>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { getCommentAuditList, getReportList, getVideoAuditList } from '@/api/admin'
import type { AuditItem, ReportItem } from '@/types/admin'

const videoAudits = ref<AuditItem[]>([])
const commentAudits = ref<AuditItem[]>([])
const reports = ref<ReportItem[]>([])

const videoAuditCount = computed(() => videoAudits.value.length)
const commentAuditCount = computed(() => commentAudits.value.length)
const reportCount = computed(() => reports.value.length)

onMounted(async () => {
  const [videoResponse, commentResponse, reportResponse] = await Promise.all([
    getVideoAuditList(1, 10),
    getCommentAuditList(1, 10),
    getReportList(1, 10)
  ])

  videoAudits.value = videoResponse.data.records
  commentAudits.value = commentResponse.data.records
  reports.value = reportResponse.data.records
})
</script>

<style scoped lang="scss">
.admin-overview {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 16px;

  article {
    padding: 20px;
    border-radius: 22px;
    border: 1px solid #e7ebf3;
    background: #fff;
    display: grid;
    gap: 6px;
  }
}

.panel-card {
  padding: 22px;
  border-radius: 24px;
  border: 1px solid #e7ebf3;
  background: #fff;
  display: grid;
  gap: 16px;
}

.panel-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.section-eyebrow {
  margin: 0 0 6px;
  color: #94a3b8;
  font-size: 12px;
  letter-spacing: 0.08em;
  text-transform: uppercase;
}

.queue-grid {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 16px;
}

.queue-card {
  padding: 18px;
  border-radius: 18px;
  background: #f8fafc;
}
</style>
