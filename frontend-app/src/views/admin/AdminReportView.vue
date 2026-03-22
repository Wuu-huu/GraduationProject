<template>
  <div class="page-shell">
    <section class="panel-card">
      <div class="panel-head">
        <div>
          <p class="section-eyebrow">举报处理</p>
          <h2>处理举报记录</h2>
        </div>
      </div>
      <div class="report-list">
        <article v-for="item in items" :key="item.reportId" class="report-item">
          <div>
            <strong>举报单 #{{ item.reportId }}</strong>
            <p>目标 ID：{{ item.targetId }}</p>
            <p>{{ item.reasonText || '暂无补充说明' }}</p>
          </div>
          <div class="report-actions">
            <el-button @click="handleProcess(item.reportId, 1)">处理通过</el-button>
            <el-button type="danger" @click="handleProcess(item.reportId, 2)">驳回举报</el-button>
          </div>
        </article>
      </div>
      <AppPagination :current-page="pageNum" :page-size="pageSize" :total="total" @change="handlePageChange" />
    </section>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import AppPagination from '@/components/common/AppPagination.vue'
import { getReportList, handleReport } from '@/api/admin'
import type { ReportItem } from '@/types/admin'

const items = ref<ReportItem[]>([])
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)

onMounted(loadItems)

async function loadItems() {
  const response = await getReportList(pageNum.value, pageSize.value)
  items.value = response.data.records
  total.value = response.data.total
  pageNum.value = response.data.pageNum
  pageSize.value = response.data.pageSize
}

async function handlePageChange(page: number) {
  pageNum.value = page
  await loadItems()
}

async function handleProcess(reportId: number, status: number) {
  await handleReport(reportId, status, '后台页面处理')
  ElMessage.success('举报处理成功')
  await loadItems()
}
</script>

<style scoped lang="scss">
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

.report-list {
  display: grid;
}

.report-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  padding: 16px 0;
  border-top: 1px solid #eef2f7;

  p {
    margin: 6px 0 0;
    color: #64748b;
  }
}

.report-actions {
  display: flex;
  gap: 12px;
}
</style>
