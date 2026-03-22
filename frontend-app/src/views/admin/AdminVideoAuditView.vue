<template>
  <div class="page-shell">
    <section class="panel-card">
      <div class="panel-head">
        <div>
          <p class="section-eyebrow">��Ƶ���</p>
          <h2>����������Ƶ</h2>
        </div>
      </div>
      <div class="audit-list">
        <article v-for="item in items" :key="item.auditId" class="audit-item">
          <div>
            <strong>��˵� #{{ item.auditId }}</strong>
            <p>��Ƶ ID��{{ item.bizId }}</p>
          </div>
          <div class="audit-actions">
            <el-button @click="handleApprove(item.auditId)">ͨ��</el-button>
            <el-button type="danger" @click="handleReject(item.auditId)">����</el-button>
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
import { approveVideoAudit, getVideoAuditList, rejectVideoAudit } from '@/api/admin'
import type { AuditItem } from '@/types/admin'

const items = ref<AuditItem[]>([])
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)

onMounted(loadItems)

async function loadItems() {
  const response = await getVideoAuditList(pageNum.value, pageSize.value)
  items.value = response.data.records
  total.value = response.data.total
  pageNum.value = response.data.pageNum
  pageSize.value = response.data.pageSize
}

async function handlePageChange(page: number) {
  pageNum.value = page
  await loadItems()
}

async function handleApprove(auditId: number) {
  await approveVideoAudit(auditId)
  ElMessage.success('��Ƶ�����ͨ��')
  await loadItems()
}

async function handleReject(auditId: number) {
  await rejectVideoAudit(auditId, '��̨ҳ�沵��')
  ElMessage.success('��Ƶ����Ѳ���')
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

.audit-list {
  display: grid;
}

.audit-item {
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

.audit-actions {
  display: flex;
  gap: 12px;
}
</style>
