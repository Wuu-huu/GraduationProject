<template>
  <section class="hot-rank-panel">
    <div class="hot-rank-panel__header">
      <h3>{{ title }}</h3>
      <RouterLink to="/search?sort=hot">更多</RouterLink>
    </div>
    <ol>
      <li v-for="item in videos" :key="item.vid">
        <span class="rank-no">{{ item.positionNo }}</span>
        <div class="rank-body">
          <RouterLink :to="`/videos/${item.vid}`" class="rank-title">{{ item.title }}</RouterLink>
          <p>{{ item.authorName }} · {{ sourceText(item.source) }}</p>
        </div>
      </li>
    </ol>
  </section>
</template>

<script setup lang="ts">
import type { RecommendVideo } from '@/types/recommend'

defineProps<{
  title: string
  videos: RecommendVideo[]
}>()

function sourceText(source: RecommendVideo['source']) {
  const map: Record<RecommendVideo['source'], string> = {
    HOT: '热门召回',
    LATEST: '最新召回',
    CATEGORY_PREF: '分区偏好',
    TAG_PREF: '标签偏好',
    ITEM_CF: '协同过滤',
    ZONE_HOT: '分区热门',
    FALLBACK: '兜底推荐'
  }
  return map[source]
}
</script>

<style scoped lang="scss">
.hot-rank-panel {
  padding: 20px;
  background: #fff;
  border: 1px solid #e7ebf3;
  border-radius: 24px;

  &__header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 14px;

    h3 {
      margin: 0;
    }
  }

  ol {
    margin: 0;
    padding: 0;
    list-style: none;
    display: grid;
    gap: 14px;
  }

  li {
    display: flex;
    gap: 12px;
    align-items: flex-start;
  }
}

.rank-no {
  width: 30px;
  height: 30px;
  display: grid;
  place-items: center;
  border-radius: 10px;
  background: #eff6ff;
  color: #1d4ed8;
  font-weight: 700;
  flex-shrink: 0;
}

.rank-body {
  min-width: 0;

  p {
    margin: 6px 0 0;
    color: #94a3b8;
    font-size: 12px;
  }
}

.rank-title {
  color: #0f172a;
  line-height: 1.5;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>