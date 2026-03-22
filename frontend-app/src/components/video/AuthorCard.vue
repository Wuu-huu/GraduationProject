<template>
  <aside class="author-card">
    <div class="author-card__avatar"></div>
    <div>
      <h4>{{ name }}</h4>
      <p>{{ description }}</p>
      <div class="author-card__actions">
        <el-button type="primary">关注</el-button>
        <el-button plain>私信</el-button>
      </div>
    </div>
  </aside>
</template>

<script setup lang="ts">
defineProps<{
  name: string
  description?: string
}>()
</script>

<style scoped lang="scss">
.author-card {
  display: flex;
  gap: 16px;
  padding: 16px;
  background: #fff;
  border: 1px solid #e7ebf3;
  border-radius: 20px;

  &__avatar {
    width: 56px;
    height: 56px;
    border-radius: 50%;
    background: linear-gradient(135deg, #93c5fd, #f9a8d4);
  }

  &__actions {
    margin-top: 14px;
    display: flex;
    gap: 10px;
  }
}
</style>