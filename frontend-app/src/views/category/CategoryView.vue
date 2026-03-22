<template>
  <div class="page-shell zone-page">
    <section class="zone-hero">
      <div>
        <p class="section-eyebrow">分区页面</p>
        <h1>{{ zoneTitle }}</h1>
        <p class="zone-summary">根据分区推荐和公开内容进行浏览，可以通过按钮切换排序并快速进入筛选结果页。</p>
      </div>
      <div class="zone-actions">
        <button
          v-for="option in sortOptions"
          :key="option.value"
          class="sort-pill"
          :class="{ active: sortValue === option.value }"
          type="button"
          @click="sortValue = option.value"
        >
          {{ option.label }}
        </button>
        <el-button @click="goToSearch">进入筛选结果</el-button>
      </div>
    </section>

    <section class="zone-board">
      <div class="zone-main">
        <section class="section-stack">
          <header class="section-heading">
            <div class="section-title-block">
              <p class="section-eyebrow">分区推荐</p>
              <h2>适合继续浏览的内容</h2>
            </div>
          </header>
          <RecommendVideoList title="分区推荐" :videos="recommendStore.zoneRecommend" />
        </section>

        <section class="section-stack">
          <header class="section-heading">
            <div class="section-title-block">
              <p class="section-eyebrow">分区视频</p>
              <h2>{{ sortValue === 'hot' ? '按热度排序' : '按发布时间排序' }}</h2>
            </div>
          </header>
          <VideoList :videos="displayedZoneVideos" />
          <AppPagination
            :current-page="videoStore.zonePageNum"
            :page-size="videoStore.zonePageSize"
            :total="videoStore.zoneTotal"
            @change="handleZonePageChange"
          />
        </section>
      </div>

      <aside class="zone-side">
        <div class="zone-side-card">
          <div class="section-title-block">
            <p class="section-eyebrow">分区入口</p>
            <h3>切换浏览</h3>
          </div>
          <CategoryNav :items="categoryStore.categories" :active-value="zoneId" @change="handleCategoryJump" />
        </div>
      </aside>
    </section>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import AppPagination from '@/components/common/AppPagination.vue'
import CategoryNav from '@/components/category/CategoryNav.vue'
import RecommendVideoList from '@/components/recommend/RecommendVideoList.vue'
import VideoList from '@/components/video/VideoList.vue'
import { useCategoryStore } from '@/stores/categoryStore'
import { useRecommendStore } from '@/stores/recommendStore'
import { useVideoStore } from '@/stores/videoStore'

const route = useRoute()
const router = useRouter()
const categoryStore = useCategoryStore()
const recommendStore = useRecommendStore()
const videoStore = useVideoStore()
const sortValue = ref<'latest' | 'hot'>('latest')

const sortOptions = [
  { label: '最新发布', value: 'latest' },
  { label: '最多播放', value: 'hot' }
] as const

const zoneId = computed(() => Number(route.params.zoneId))
const zoneTitle = computed(() => {
  const current = categoryStore.categories.find((item) => Number(item.value) === zoneId.value)
  return current ? `${current.label} 分区` : '分区内容'
})

const displayedZoneVideos = computed(() => {
  const list = [...videoStore.zoneVideos]
  if (sortValue.value === 'hot') {
    return list.sort((a, b) => (b.playCount ?? 0) - (a.playCount ?? 0))
  }
  return list.sort((a, b) => (Date.parse(b.publishTime ?? '') || 0) - (Date.parse(a.publishTime ?? '') || 0))
})

async function loadZoneData(page = 1) {
  if (!Number.isNaN(zoneId.value)) {
    await Promise.all([
      recommendStore.fetchZoneRecommend(zoneId.value),
      videoStore.fetchZoneVideos(zoneId.value, page, videoStore.zonePageSize)
    ])
  }
}

onMounted(() => {
  loadZoneData()
})

watch(zoneId, () => {
  loadZoneData(1)
})

function handleZonePageChange(page: number) {
  loadZoneData(page)
}

function handleCategoryJump(value: number) {
  categoryStore.setActiveCategory(value)
  router.push(`/zones/${value}`)
}

function goToSearch() {
  const keyword = categoryStore.categories.find((item) => Number(item.value) === zoneId.value)?.label ?? '视频'
  router.push({ path: '/search', query: { keyword, sort: sortValue.value } })
}
</script>

<style scoped lang="scss">
.zone-page,
.zone-main,
.zone-side,
.section-stack {
  display: grid;
  gap: 18px;
}

.zone-hero {
  padding: 28px;
  border-radius: 28px;
  background: linear-gradient(135deg, #eff6ff, #fff7ed 58%, #fef3c7);
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 20px;
}

.zone-summary {
  max-width: 640px;
  margin: 10px 0 0;
  color: #64748b;
  line-height: 1.7;
}

.zone-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  justify-content: flex-end;
}

.sort-pill {
  padding: 10px 16px;
  border: 1px solid #d8e1ee;
  border-radius: 999px;
  background: #fff;
  color: #475569;
  cursor: pointer;
}

.sort-pill.active {
  background: #0f172a;
  color: #fff;
  border-color: #0f172a;
}

.zone-board {
  display: grid;
  grid-template-columns: minmax(0, 1fr) 320px;
  gap: 22px;
  align-items: start;
}

.zone-side-card {
  padding: 22px;
  border-radius: 24px;
  border: 1px solid #e7ebf3;
  background: #fff;
  box-shadow: 0 14px 32px rgba(15, 23, 42, 0.05);
}

.zone-side-card h3 {
  margin: 0;
}
</style>