<template>
  <section class="video-grid">
    <VideoCard v-for="item in videos" :key="item.vid" :video="item" />
  </section>
</template>

<script setup lang="ts">
import VideoCard from './VideoCard.vue'
import type { RecommendVideo } from '@/types/recommend'
import type { VideoCard as VideoCardType } from '@/types/video'

defineProps<{
  videos: Array<VideoCardType | RecommendVideo>
}>()
</script>

<style scoped lang="scss">
.video-grid {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 22px 18px;
}
</style>