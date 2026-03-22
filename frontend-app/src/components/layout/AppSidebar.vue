<template>
  <aside class="app-sidebar">
    <h3>{{ title }}</h3>
    <RouterLink v-for="item in items" :key="item.to" :to="item.to" class="sidebar-link">
      {{ item.label }}
    </RouterLink>
  </aside>
</template>

<script setup lang="ts">
interface SidebarItem {
  label: string
  to: string
}

defineProps<{
  title: string
  items: SidebarItem[]
}>()
</script>

<style scoped lang="scss">
.app-sidebar {
  position: sticky;
  top: 96px;
  background: rgba(255, 255, 255, 0.92);
  border: 1px solid #e7ebf3;
  border-radius: 24px;
  padding: 22px;
  display: flex;
  flex-direction: column;
  gap: 12px;
  backdrop-filter: blur(10px);
  box-shadow: 0 16px 40px rgba(15, 23, 42, 0.04);
}

.app-sidebar h3 {
  margin: 0 0 6px;
  font-size: 18px;
}

.sidebar-link {
  padding: 12px 14px;
  border-radius: 14px;
  color: #475569;
  transition: all 0.2s ease;
}

.sidebar-link:hover {
  background: #eef4ff;
  color: #0f172a;
}

.sidebar-link.router-link-active {
  background: #0f172a;
  color: #fff;
}
</style>