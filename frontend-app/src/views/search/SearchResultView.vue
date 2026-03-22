<template>
  <div class="page-shell search-page">
    <section class="search-hero">
      <div>
        <p class="section-eyebrow">搜索结果</p>
        <h1>查找你想看的内容</h1>
      </div>
      <PageFilterBar>
        <el-input v-model="searchValue" placeholder="输入搜索关键词" @keyup.enter="handleSearch" />
        <el-select v-model="sortValue" placeholder="排序方式" style="width: 160px">
          <el-option label="相关度" value="relevance" />
          <el-option label="最新发布" value="latest" />
          <el-option label="最多播放" value="hot" />
        </el-select>
        <el-button type="primary" @click="handleSearch">搜索</el-button>
      </PageFilterBar>
    </section>

    <div v-if="keyword" class="search-board">
      <section class="search-main">
        <header class="search-result-head">
          <div class="section-title-block">
            <p class="section-eyebrow">关键词</p>
            <h2>{{ keyword }}</h2>
          </div>
          <div class="search-result-meta">共 {{ filteredResults.length }} 条结果</div>
        </header>
        <VideoList :videos="pagedResults" />
        <AppPagination :current-page="pageNum" :page-size="pageSize" :total="filteredResults.length" @change="handlePageChange" />
      </section>

      <aside class="search-side">
        <div class="search-side-card">
          <div class="section-title-block">
            <p class="section-eyebrow">快速筛选</p>
            <h3>切换关键词</h3>
          </div>
          <div class="search-side-actions">
            <el-button @click="applyPreset('动画', 'relevance')">动画</el-button>
            <el-button @click="applyPreset('音乐', 'latest')">音乐</el-button>
            <el-button @click="applyPreset('游戏', 'hot')">游戏</el-button>
            <el-button @click="applyPreset('知识', 'relevance')">知识</el-button>
          </div>
        </div>
      </aside>
    </div>

    <EmptyState v-else title="请输入关键词" description="输入关键词后即可查看搜索结果。" />
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import AppPagination from '@/components/common/AppPagination.vue'
import EmptyState from '@/components/common/EmptyState.vue'
import PageFilterBar from '@/components/common/PageFilterBar.vue'
import VideoList from '@/components/video/VideoList.vue'
import { useRecommendStore } from '@/stores/recommendStore'
import { useVideoStore } from '@/stores/videoStore'

const route = useRoute()
const router = useRouter()
const videoStore = useVideoStore()
const recommendStore = useRecommendStore()
const keyword = computed(() => String(route.query.keyword ?? '').trim())
const routeSort = computed(() => {
  const value = typeof route.query.sort === 'string' ? route.query.sort : 'relevance'
  return ['relevance', 'latest', 'hot'].includes(value) ? value as 'relevance' | 'latest' | 'hot' : 'relevance'
})
const searchValue = ref(keyword.value)
const sortValue = ref<'relevance' | 'latest' | 'hot'>(routeSort.value)
const pageNum = ref(1)
const pageSize = 8

const candidateResults = computed(() => {
  const map = new Map<number, (typeof videoStore.homeVideos)[number]>()
  ;[...videoStore.homeVideos, ...recommendStore.homeRecommend, ...recommendStore.hotRecommend].forEach((item) => {
    map.set(item.vid, item)
  })
  return Array.from(map.values())
})

const filteredResults = computed(() => {
  const value = keyword.value.toLowerCase()
  const list = candidateResults.value.filter((item) => {
    const title = item.title?.toLowerCase?.() ?? ''
    const author = item.authorName?.toLowerCase?.() ?? ''
    return title.includes(value) || author.includes(value)
  })

  if (sortValue.value === 'latest') {
    return list.sort((a, b) => (Date.parse(b.publishTime ?? '') || 0) - (Date.parse(a.publishTime ?? '') || 0))
  }

  if (sortValue.value === 'hot') {
    return list.sort((a, b) => (b.playCount ?? 0) - (a.playCount ?? 0))
  }

  return list.sort((a, b) => {
    const aTitle = a.title?.toLowerCase?.() ?? ''
    const bTitle = b.title?.toLowerCase?.() ?? ''
    return Number(bTitle.includes(value)) - Number(aTitle.includes(value))
  })
})

const pagedResults = computed(() => {
  const start = (pageNum.value - 1) * pageSize
  return filteredResults.value.slice(start, start + pageSize)
})

onMounted(async () => {
  const tasks: Promise<unknown>[] = []
  if (videoStore.homeVideos.length === 0) tasks.push(videoStore.fetchHomeVideos())
  if (recommendStore.homeRecommend.length === 0) tasks.push(recommendStore.fetchHomeRecommend())
  if (recommendStore.hotRecommend.length === 0) tasks.push(recommendStore.fetchHotRecommend())
  await Promise.all(tasks)
})

watch(keyword, (value) => {
  searchValue.value = value
  pageNum.value = 1
})

watch(routeSort, (value) => {
  sortValue.value = value
  pageNum.value = 1
})

watch(sortValue, () => {
  pageNum.value = 1
})

function handleSearch() {
  router.push({
    path: '/search',
    query: searchValue.value ? { keyword: searchValue.value, sort: sortValue.value } : {}
  })
}

function handlePageChange(page: number) {
  pageNum.value = page
}

function applyPreset(keywordValue: string, sort: 'relevance' | 'latest' | 'hot') {
  searchValue.value = keywordValue
  sortValue.value = sort
  handleSearch()
}
</script>

<style scoped lang="scss">
.search-page,
.search-main,
.search-side {
  display: grid;
  gap: 18px;
}

.search-hero {
  padding: 24px 28px;
  border-radius: 28px;
  background: linear-gradient(135deg, #eff6ff, #fff7ed 60%, #fef3c7);
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 20px;
}

.search-board {
  display: grid;
  grid-template-columns: minmax(0, 1fr) 300px;
  gap: 22px;
  align-items: start;
}

.search-result-head {
  display: flex;
  align-items: flex-end;
  justify-content: space-between;
  gap: 16px;
}

.search-result-head h2 {
  margin: 0;
}

.search-result-meta {
  color: #64748b;
  font-size: 13px;
}

.search-side-card {
  padding: 22px;
  border-radius: 24px;
  border: 1px solid #e7ebf3;
  background: #fff;
  box-shadow: 0 14px 32px rgba(15, 23, 42, 0.05);
}

.search-side-card h3 {
  margin: 0;
}

.search-side-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}
</style>