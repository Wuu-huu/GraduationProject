<template>
  <div class="page-shell">
    <section class="panel-card">
      <div class="panel-head">
        <div>
          <p class="section-eyebrow">用户管理</p>
          <h2>用户列表</h2>
        </div>
      </div>
      <EmptyState title="暂无数据" description="" />
    </section>
  </div>
</template>

<script setup lang="ts">
import EmptyState from '@/components/common/EmptyState.vue'
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

.section-eyebrow {
  margin: 0 0 6px;
  color: #94a3b8;
  font-size: 12px;
  letter-spacing: 0.08em;
  text-transform: uppercase;
}
</style>