<template>
  <div class="pagination-wrap">
    <el-pagination
      background
      layout="prev, pager, next"
      :current-page="currentPage"
      :page-size="pageSize"
      :total="total"
      @current-change="handleCurrentChange"
    />
  </div>
</template>

<script setup lang="ts">
const props = defineProps<{
  currentPage: number
  pageSize: number
  total: number
}>()

const emit = defineEmits<{
  (e: 'change', page: number): void
}>()

function handleCurrentChange(page: number) {
  if (page !== props.currentPage) {
    emit('change', page)
  }
}
</script>

<style scoped lang="scss">
.pagination-wrap {
  display: flex;
  justify-content: center;
  padding: 12px 0;
}
</style>
