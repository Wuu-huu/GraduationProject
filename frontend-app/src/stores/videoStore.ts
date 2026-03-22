import { defineStore } from 'pinia'
import { getHomeVideos, getVideoDetail } from '@/api/video'
import type { VideoCard, VideoDetail } from '@/types/video'

export const useVideoStore = defineStore('videoStore', {
  state: () => ({
    homeTotal: 0,
    homePageNum: 1,
    homePageSize: 12,
    zoneTotal: 0,
    zonePageNum: 1,
    zonePageSize: 12,
    homeVideos: [] as VideoCard[],
    zoneVideos: [] as VideoCard[],
    currentVideo: null as VideoDetail | null,
    loading: false
  }),
  actions: {
    async fetchHomeVideos(pageNum = 1, pageSize = 12) {
      this.loading = true
      const response = await getHomeVideos(pageNum, pageSize)
      this.homeVideos = response.data.records
      this.homeTotal = response.data.total
      this.homePageNum = response.data.pageNum
      this.homePageSize = response.data.pageSize
      this.loading = false
    },
    async fetchVideoDetail(videoId: number) {
      this.loading = true
      const response = await getVideoDetail(videoId)
      this.currentVideo = response.data
      this.loading = false
    },
    async fetchZoneVideos(zoneId: number, pageNum = 1, pageSize = 12) {
      this.loading = true
      const response = await import('@/api/video').then(({ getZoneVideos }) => getZoneVideos(zoneId, pageNum, pageSize))
      this.zoneVideos = response.data.records
      this.zoneTotal = response.data.total
      this.zonePageNum = response.data.pageNum
      this.zonePageSize = response.data.pageSize
      this.loading = false
    }
  }
})
