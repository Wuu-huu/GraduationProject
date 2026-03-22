<template>
  <div class="page-shell home-page">
    <section class="focus-shell" v-if="focusMain">
      <RouterLink :to="`/videos/${focusMain.vid}`" class="focus-main-card">
        <img :src="focusMain.coverUrl" :alt="focusMain.title" />
        <div class="focus-main-card__mask">
          <span class="focus-badge">焦点推荐</span>
          <h1>{{ focusMain.title }}</h1>
          <p>{{ focusMain.authorName }}</p>
        </div>
      </RouterLink>

      <div class="focus-side-grid">
        <RouterLink v-for="item in focusSide" :key="item.vid" :to="`/videos/${item.vid}`" class="focus-mini-card">
          <img :src="item.coverUrl" :alt="item.title" />
          <div class="focus-mini-card__mask">
            <strong>{{ item.title }}</strong>
            <span>{{ item.authorName }}</span>
          </div>
        </RouterLink>
      </div>
    </section>

    <section class="section-stack">
      <header class="section-heading">
        <div class="section-title-block">
          <p class="section-eyebrow">快速入口</p>
          <h2>分区导航</h2>
        </div>
      </header>
      <CategoryNav :items="categoryStore.categories" :active-value="categoryStore.activeCategory" @change="handleCategoryChange" />
    </section>

    <section class="section-stack">
      <header class="section-heading">
        <div class="section-title-block">
          <p class="section-eyebrow">内容筛选</p>
          <h2>{{ currentFeedTitle }}</h2>
        </div>
        <div class="feed-switcher">
          <button
            v-for="tab in feedTabs"
            :key="tab.value"
            class="feed-tab"
            :class="{ active: activeFeed === tab.value }"
            type="button"
            @click="handleFeedChange(tab.value)"
          >
            {{ tab.label }}
          </button>
        </div>
      </header>

      <div class="content-board">
        <section class="content-main">
          <VideoList :videos="currentFeedVideos" />
          <AppPagination
            v-if="activeFeed === 'latest'"
            :current-page="videoStore.homePageNum"
            :page-size="videoStore.homePageSize"
            :total="videoStore.homeTotal"
            @change="handleHomePageChange"
          />
        </section>

        <aside class="content-side">
          <HotRankPanel title="热门榜" :videos="recommendStore.hotRecommend" />
          <div class="shortcut-card">
            <div class="section-title-block">
              <p class="section-eyebrow">快速筛选</p>
              <h3>前往结果页</h3>
            </div>
            <div class="shortcut-actions">
              <el-button @click="goToSearch('动画', 'relevance')">动画</el-button>
              <el-button @click="goToSearch('音乐', 'latest')">音乐</el-button>
              <el-button @click="goToSearch('游戏', 'hot')">游戏</el-button>
              <el-button @click="goToSearch('知识', 'relevance')">知识</el-button>
            </div>
          </div>
        </aside>
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import CategoryNav from '@/components/category/CategoryNav.vue'
import AppPagination from '@/components/common/AppPagination.vue'
import HotRankPanel from '@/components/recommend/HotRankPanel.vue'
import VideoList from '@/components/video/VideoList.vue'
import { useCategoryStore } from '@/stores/categoryStore'
import { useRecommendStore } from '@/stores/recommendStore'
import { useVideoStore } from '@/stores/videoStore'

const route = useRoute()
const router = useRouter()
const categoryStore = useCategoryStore()
const recommendStore = useRecommendStore()
const videoStore = useVideoStore()

const feedTabs = [
  { label: '推荐', value: 'recommend' },
  { label: '热门', value: 'hot' },
  { label: '最新', value: 'latest' }
] as const

const activeFeed = computed(() => {
  const queryValue = typeof route.query.feed === 'string' ? route.query.feed : 'recommend'
  return ['recommend', 'hot', 'latest'].includes(queryValue) ? queryValue : 'recommend'
})

const focusVideos = computed(() => {
  const merged = [...recommendStore.homeRecommend, ...videoStore.homeVideos]
  const map = new Map<number, typeof merged[number]>()
  merged.forEach((item) => map.set(item.vid, item))
  return Array.from(map.values()).slice(0, 5)
})

const focusMain = computed(() => focusVideos.value[0] ?? null)
const focusSide = computed(() => focusVideos.value.slice(1, 5))

const currentFeedVideos = computed(() => {
  if (activeFeed.value === 'hot') return recommendStore.hotRecommend
  if (activeFeed.value === 'latest') return videoStore.homeVideos
  return recommendStore.homeRecommend
})

const currentFeedTitle = computed(() => {
  if (activeFeed.value === 'hot') return '热门内容'
  if (activeFeed.value === 'latest') return '最新投稿'
  return '首页推荐'
})

onMounted(async () => {
  await Promise.all([
    recommendStore.fetchHomeRecommend(),
    recommendStore.fetchHotRecommend(),
    videoStore.fetchHomeVideos()
  ])
})

function handleHomePageChange(page: number) {
  videoStore.fetchHomeVideos(page, videoStore.homePageSize)
}

function handleFeedChange(feed: 'recommend' | 'hot' | 'latest') {
  router.push({ path: '/', query: { ...route.query, feed } })
}

function handleCategoryChange(value: number) {
  categoryStore.setActiveCategory(value)
  router.push(`/zones/${value}`)
}

function goToSearch(keyword: string, sort: 'relevance' | 'latest' | 'hot') {
  router.push({ path: '/search', query: { keyword, sort } })
}
</script>

<style scoped lang="scss">
.home-page,
.section-stack,
.content-main,
.content-side {
  display: grid;
  gap: 18px;
}

.focus-shell {
  display: grid;
  grid-template-columns: minmax(0, 1.45fr) minmax(0, 1fr);
  gap: 18px;
}

.focus-main-card,
.focus-mini-card {
  position: relative;
  overflow: hidden;
  border-radius: 26px;
  background: #0f172a;
  min-height: 220px;
  box-shadow: 0 18px 40px rgba(15, 23, 42, 0.12);

  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    display: block;
  }
}

.focus-main-card {
  min-height: 430px;
}

.focus-main-card__mask,
.focus-mini-card__mask {
  position: absolute;
  inset: auto 0 0 0;
  padding: 22px;
  color: #fff;
  background: linear-gradient(180deg, transparent, rgba(15, 23, 42, 0.88));
}

.focus-main-card__mask h1 {
  margin: 12px 0 8px;
  font-size: 30px;
  line-height: 1.2;
}

.focus-main-card__mask p,
.focus-mini-card__mask span {
  margin: 0;
  color: rgba(255, 255, 255, 0.82);
  font-size: 13px;
}

.focus-side-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 18px;
}

.focus-mini-card {
  min-height: 205px;
}

.focus-mini-card__mask strong {
  display: block;
  margin-bottom: 6px;
  line-height: 1.5;
}

.focus-badge {
  display: inline-flex;
  padding: 6px 10px;
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.16);
  backdrop-filter: blur(8px);
  font-size: 12px;
}

.feed-switcher,
.shortcut-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.feed-tab {
  padding: 10px 16px;
  border: 1px solid #d8e1ee;
  border-radius: 999px;
  background: #fff;
  color: #475569;
  cursor: pointer;
  transition: all 0.2s ease;
}

.feed-tab.active {
  background: #0f172a;
  color: #fff;
  border-color: #0f172a;
}

.content-board {
  display: grid;
  grid-template-columns: minmax(0, 1fr) 320px;
  gap: 22px;
  align-items: start;
}

.shortcut-card {
  padding: 22px;
  border: 1px solid #e7ebf3;
  border-radius: 24px;
  background: #fff;
  box-shadow: 0 14px 32px rgba(15, 23, 42, 0.05);
}

.shortcut-card h3 {
  margin: 0;
}
</style>