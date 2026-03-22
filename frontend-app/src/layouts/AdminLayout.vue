<template>
  <div class="layout-shell admin-shell">
    <AppHeader />
    <div class="layout-panel">
      <AppSidebar title="管理后台" :items="sidebarItems" />
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
  { label: '后台首页', to: '/admin' },
  { label: '用户管理', to: '/admin/users' },
  { label: '视频审核', to: '/admin/audit/videos' },
  { label: '评论审核', to: '/admin/audit/comments' },
  { label: '举报处理', to: '/admin/reports' },
  { label: '分类标签管理', to: '/admin/categories' }
]
</script>