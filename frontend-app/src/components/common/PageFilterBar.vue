<template>
  <div class="page-filter-bar">
    <slot />
  </div>
</template>

<script setup lang="ts">
</script>

<style scoped lang="scss">
.page-filter-bar {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
  padding: 16px;
  background: #fff;
  border: 1px solid #e7ebf3;
  border-radius: 18px;
}
</style>
