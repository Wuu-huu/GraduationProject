<template>
  <RouterLink :to="`/videos/${video.vid}`" class="video-card">
    <div class="video-card__cover">
      <img :src="video.coverUrl || fallbackCover" :alt="video.title" />
      <div class="video-card__overlay">
        <span class="video-card__duration">{{ durationText }}</span>
      </div>
    </div>
    <div class="video-card__body">
      <h3 class="video-card__title">{{ video.title }}</h3>
      <p class="video-card__author">{{ video.authorName }}</p>
      <div class="video-card__meta">
        <span>{{ playText }} 播放</span>
        <span>{{ likeText }} 点赞</span>
      </div>
    </div>
  </RouterLink>
</template>

<script setup lang="ts">
import type { RecommendVideo } from '@/types/recommend'
import type { VideoCard as VideoCardType } from '@/types/video'
import { computed } from 'vue'
import { formatCount } from '@/utils/format'

const props = defineProps<{
  video: VideoCardType | RecommendVideo
}>()

const fallbackCover = 'https://dummyimage.com/480x270/e2e8f0/475569&text=%E8%A7%86%E9%A2%91'

const durationText = computed(() => {
  const sec = props.video.durationSec ?? 0
  const minutes = Math.floor(sec / 60)
  const seconds = String(sec % 60).padStart(2, '0')
  return `${minutes}:${seconds}`
})

const playText = computed(() => formatCount(props.video.playCount))
const likeText = computed(() => formatCount(props.video.likeCount))
</script>

<style scoped lang="scss">
.video-card {
  display: block;
  overflow: hidden;
  border-radius: 18px;
  background: #fff;
  transition: transform 0.2s ease, box-shadow 0.2s ease;

  &:hover {
    transform: translateY(-4px);
    box-shadow: 0 18px 40px rgba(15, 23, 42, 0.08);
  }

  &__cover {
    position: relative;
    aspect-ratio: 16 / 10;
    border-radius: 18px;
    overflow: hidden;

    img {
      width: 100%;
      height: 100%;
      object-fit: cover;
      display: block;
    }
  }

  &__overlay {
    position: absolute;
    inset: auto 0 0 0;
    padding: 10px;
    display: flex;
    justify-content: flex-end;
    background: linear-gradient(180deg, transparent, rgba(15, 23, 42, 0.72));
  }

  &__duration {
    padding: 4px 8px;
    border-radius: 999px;
    background: rgba(15, 23, 42, 0.76);
    color: #fff;
    font-size: 12px;
  }

  &__body {
    padding: 12px 2px 2px;
    display: grid;
    gap: 6px;
  }

  &__title {
    margin: 0;
    font-size: 15px;
    line-height: 1.45;
    color: #0f172a;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
  }

  &__author {
    margin: 0;
    color: #64748b;
    font-size: 13px;
  }

  &__meta {
    display: flex;
    gap: 10px;
    color: #94a3b8;
    font-size: 12px;
  }
}
</style>