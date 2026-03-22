import type { Router } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/userStore'
import { hasRole } from '@/utils/permission'

export function setupRouterGuard(router: Router): void {
  router.beforeEach(async (to) => {
    const userStore = useUserStore()

    if (to.meta.title) {
      document.title = `${String(to.meta.title)} - ${import.meta.env.VITE_APP_TITLE}`
    }

    if (userStore.isLoggedIn && !userStore.currentUser) {
      try {
        await userStore.fetchCurrentUser()
      } catch {
        userStore.logout()
      }
    }

    if (userStore.isLoggedIn && (to.name === 'login' || to.name === 'register')) {
      return { name: 'home' }
    }

    if (to.meta.requiresAuth && !userStore.isLoggedIn) {
      return { name: 'login', query: { redirect: to.fullPath } }
    }

    if (!hasRole(userStore.roleCode, to.meta.roles as never)) {
      ElMessage.warning('当前账号没有访问该页面的权限')
      return { name: 'home' }
    }

    return true
  })
}