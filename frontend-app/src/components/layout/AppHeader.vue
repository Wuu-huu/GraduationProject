<template>
  <header class="app-header">
    <div class="app-header__inner">
      <div class="header-left">
        <RouterLink class="brand" to="/">毕业设计</RouterLink>
        <nav class="nav-links">
          <RouterLink to="/">首页</RouterLink>
          <RouterLink to="/zones/1">分区</RouterLink>
          <RouterLink to="/creator">创作中心</RouterLink>
          <RouterLink to="/admin">管理后台</RouterLink>
        </nav>
      </div>
      <div class="header-actions">
        <RouterLink to="/search?keyword=动画">找内容</RouterLink>
        <RouterLink v-if="userStore.isLoggedIn && userStore.currentUser?.uid" :to="`/users/${userStore.currentUser.uid}`">个人中心</RouterLink>
        <RouterLink v-if="userStore.isLoggedIn" to="/messages">消息</RouterLink>
        <RouterLink v-if="userStore.isLoggedIn" to="/settings">设置</RouterLink>
        <template v-if="!userStore.isLoggedIn">
          <RouterLink class="header-auth" to="/login">登录</RouterLink>
          <RouterLink class="header-auth header-auth--primary" to="/register">注册</RouterLink>
        </template>
      </div>
    </div>
  </header>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { useUserStore } from '@/stores/userStore'

const userStore = useUserStore()

onMounted(async () => {
  if (userStore.isLoggedIn && !userStore.currentUser) {
    await userStore.fetchCurrentUser()
  }
})
</script>

<style scoped lang="scss">
.app-header {
  position: sticky;
  top: 0;
  z-index: 20;
  background: rgba(255, 255, 255, 0.95);
  border-bottom: 1px solid #e7ebf3;
  backdrop-filter: blur(12px);

  &__inner {
    width: min(1320px, calc(100% - 32px));
    height: 68px;
    margin: 0 auto;
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 24px;
  }
}

.header-left {
  display: flex;
  align-items: center;
  gap: 28px;
}

.brand {
  color: #0f172a;
  font-size: 24px;
  font-weight: 800;
  letter-spacing: 0.04em;
}

.nav-links,
.header-actions {
  display: flex;
  align-items: center;
  gap: 18px;
}

.nav-links a,
.header-actions a {
  color: #475569;
  font-size: 14px;
  transition: color 0.2s ease;
}

.nav-links a:hover,
.header-actions a:hover,
.router-link-active {
  color: #0f172a;
}

.header-auth {
  padding: 8px 14px;
  border-radius: 999px;
  background: #f8fafc;
}

.header-auth--primary {
  background: #0f172a;
  color: #fff !important;
}
</style>
