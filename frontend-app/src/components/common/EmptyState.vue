<template>
  <div class="empty-state">
    <h3>{{ title }}</h3>
    <p>{{ description }}</p>
  </div>
</template>

<script setup lang="ts">
defineProps<{
  title: string
  description?: string
}>()
</script>

<style scoped lang="scss">
.empty-state {
  padding: 48px 24px;
  text-align: center;
  border-radius: 20px;
  background: #fff;
  border: 1px dashed #cbd5e1;
}
</style>
