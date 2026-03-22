import { defineStore } from 'pinia'
import { getHomeRecommend, getHotRecommend, getRelatedRecommend, getZoneRecommend } from '@/api/recommend'
import type { RecommendVideo } from '@/types/recommend'

export const useRecommendStore = defineStore('recommendStore', {
  state: () => ({
    homeRecommend: [] as RecommendVideo[],
    hotRecommend: [] as RecommendVideo[],
    relatedRecommend: [] as RecommendVideo[],
    zoneRecommend: [] as RecommendVideo[]
  }),
  actions: {
    async fetchHomeRecommend() {
      const response = await getHomeRecommend({ pageNum: 1, pageSize: 12 })
      this.homeRecommend = response.data.records
    },
    async fetchHotRecommend() {
      const response = await getHotRecommend({ pageNum: 1, pageSize: 10 })
      this.hotRecommend = response.data.records
    },
    async fetchRelatedRecommend(videoId: number) {
      const response = await getRelatedRecommend(videoId, { pageNum: 1, pageSize: 8 })
      this.relatedRecommend = response.data.records
    },
    async fetchZoneRecommend(zoneId: number) {
      const response = await getZoneRecommend(zoneId, { pageNum: 1, pageSize: 12 })
      this.zoneRecommend = response.data.records
    }
  }
})
