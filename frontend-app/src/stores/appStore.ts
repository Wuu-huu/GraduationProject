import { defineStore } from 'pinia'

export const useAppStore = defineStore('appStore', {
  state: () => ({
    loading: false,
    sidebarCollapsed: false
  }),
  actions: {
    setLoading(loading: boolean) {
      this.loading = loading
    },
    toggleSidebar() {
      this.sidebarCollapsed = !this.sidebarCollapsed
    }
  }
})
