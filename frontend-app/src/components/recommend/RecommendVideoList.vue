<template>
  <section class="recommend-section">
    <div class="section-header">
      <h2>{{ title }}</h2>
      <span class="section-count">{{ videos.length }} 条内容</span>
    </div>
    <VideoList :videos="videos" />
  </section>
</template>

<script setup lang="ts">
import VideoList from '@/components/video/VideoList.vue'
import type { RecommendVideo } from '@/types/recommend'

defineProps<{
  title: string
  videos: RecommendVideo[]
}>()
</script>

<style scoped lang="scss">
.recommend-section {
  display: grid;
  gap: 16px;
}

.section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;

  h2 {
    margin: 0;
  }
}

.section-count {
  color: #94a3b8;
  font-size: 13px;
}
</style>