import { defineStore } from 'pinia'
import type { OptionItem } from '@/types/api'

export const useCategoryStore = defineStore('categoryStore', {
  state: () => ({
    categories: [
      { label: '动画', value: 1 },
      { label: '音乐', value: 2 },
      { label: '游戏', value: 3 },
      { label: '知识', value: 4 }
    ] as OptionItem[],
    activeCategory: 1
  }),
  actions: {
    setActiveCategory(value: number) {
      this.activeCategory = value
    }
  }
})