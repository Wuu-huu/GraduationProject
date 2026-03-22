<template>
  <div class="layout-shell creator-shell">
    <AppHeader />
    <div class="layout-panel">
      <AppSidebar title="创作中心" :items="sidebarItems" />
      <main class="layout-content">
        <RouterView />
      </main>
    </div>
  </div>
</template>

<script setup lang="ts">
import AppHeader from '@/components/layout/AppHeader.vue'
import AppSidebar from '@/components/layout/AppSidebar.vue'

const sidebarItems = [
  { label: '创作首页', to: '/creator' },
  { label: '投稿管理', to: '/creator/videos' },
  { label: '视频投稿', to: '/creator/upload' },
  { label: '合集管理', to: '/creator/series' }
]
</script>