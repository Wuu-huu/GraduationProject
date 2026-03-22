<template>
  <div class="page-shell video-detail-page">
    <section class="detail-top">
      <div class="detail-main">
        <div class="video-player-shell">
          <video
            v-if="currentPart?.videoUrl"
            :key="playerKey"
            class="video-player"
            :poster="videoStore.currentVideo?.coverUrl || undefined"
            controls
            preload="metadata"
            playsinline
          >
            <source :src="currentPart.videoUrl" type="video/mp4" />
            当前浏览器不支持视频播放。
          </video>
          <div v-else class="video-player video-player--empty">
            <span class="player-chip">暂无可播放视频</span>
          </div>
        </div>

        <section v-if="videoParts.length > 1" class="part-panel">
          <div class="section-title-block">
            <p class="section-eyebrow">分 P 列表</p>
            <h3>切换视频分段</h3>
          </div>
          <div class="part-list">
            <button
              v-for="part in videoParts"
              :key="part.partId ?? part.partNo"
              class="part-item"
              :class="{ active: currentPart?.partId === part.partId && currentPart?.partNo === part.partNo }"
              type="button"
              @click="selectPart(part)"
            >
              <strong>P{{ part.partNo }} {{ part.title }}</strong>
              <span>{{ formatDuration(part.durationSec) }}</span>
            </button>
          </div>
        </section>

        <section class="video-meta-card">
          <p class="section-eyebrow">视频详情</p>
          <h1>{{ titleText }}</h1>
          <div class="video-stats">
            <span>{{ videoStore.currentVideo?.stats?.playCount ?? 0 }} 播放</span>
            <span>{{ videoStore.currentVideo?.stats?.likeCount ?? 0 }} 点赞</span>
            <span>{{ videoStore.currentVideo?.stats?.commentCount ?? 0 }} 评论</span>
            <span v-if="interactionState">投币 {{ interactionState.coinCount ?? 0 }}</span>
          </div>
          <p class="video-description">{{ descriptionText }}</p>
          <div class="video-tags" v-if="videoStore.currentVideo?.categoryTag?.tagNames?.length">
            <el-tag v-for="tag in videoStore.currentVideo.categoryTag.tagNames" :key="tag" round>
              {{ tag }}
            </el-tag>
          </div>
          <VideoActionBar
            :state="interactionState"
            :loading="actionLoading"
            @like="handleToggleLike"
            @coin="handleCoin"
            @favorite="handleFavorite"
            @watch-later="handleWatchLater"
          />
        </section>
      </div>

      <aside class="detail-side">
        <AuthorCard :name="authorText" :description="authorDescription" />
        <div class="side-card">
          <div class="section-title-block">
            <p class="section-eyebrow">相关推荐</p>
            <h3>继续观看</h3>
          </div>
          <RecommendVideoList title="相关推荐" :videos="recommendStore.relatedRecommend" />
        </div>
      </aside>
    </section>

    <section class="detail-bottom">
      <div class="comment-panel">
        <div class="comment-panel__head">
          <div class="section-title-block">
            <p class="section-eyebrow">互动区</p>
            <h3>评论区</h3>
          </div>
          <el-input class="danmaku-input" placeholder="输入弹幕内容..." />
        </div>
        <CommentInput :loading="commentSubmitting" @submit="handleCommentSubmit" />
        <CommentList :comments="comments" />
        <EmptyState v-if="comments.length === 0" title="还没有评论" description="来抢第一个评论吧。" />
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import EmptyState from '@/components/common/EmptyState.vue'
import AuthorCard from '@/components/video/AuthorCard.vue'
import VideoActionBar from '@/components/video/VideoActionBar.vue'
import CommentInput from '@/components/comment/CommentInput.vue'
import CommentList from '@/components/comment/CommentList.vue'
import RecommendVideoList from '@/components/recommend/RecommendVideoList.vue'
import { createComment, getVideoComments } from '@/api/comment'
import { createFavoriteFolder, getFavoriteFolders } from '@/api/favorite'
import {
  addWatchLater,
  cancelLikeVideo,
  coinVideo,
  favoriteVideo,
  getVideoInteractionState,
  likeVideo,
  removeWatchLater
} from '@/api/interaction'
import type { CommentItem } from '@/types/comment'
import type { UserVideoState } from '@/types/interaction'
import type { VideoPart } from '@/types/video'
import { useRecommendStore } from '@/stores/recommendStore'
import { useUserStore } from '@/stores/userStore'
import { useVideoStore } from '@/stores/videoStore'

const route = useRoute()
const videoStore = useVideoStore()
const recommendStore = useRecommendStore()
const userStore = useUserStore()
const comments = ref<CommentItem[]>([])
const interactionState = ref<UserVideoState | null>(null)
const actionLoading = ref(false)
const commentSubmitting = ref(false)
const currentPart = ref<VideoPart | null>(null)

const titleText = computed(() => videoStore.currentVideo?.title ?? '视频详情')
const descriptionText = computed(() => videoStore.currentVideo?.description ?? '暂无简介')
const authorText = computed(() => videoStore.currentVideo?.author?.nickname || videoStore.currentVideo?.authorName || '作者')
const authorDescription = computed(() => {
  const category = videoStore.currentVideo?.categoryTag?.categoryName
  return category ? `主分区：${category}` : '作者信息'
})
const videoParts = computed(() => videoStore.currentVideo?.parts ?? [])
const playerKey = computed(() => `${route.params.videoId}-${currentPart.value?.partId ?? currentPart.value?.partNo ?? 0}`)

onMounted(async () => {
  await loadVideoPage()
})

watch(
  () => route.params.videoId,
  () => {
    loadVideoPage()
  }
)

watch(videoParts, (parts) => {
  if (!parts.length) {
    currentPart.value = null
    return
  }

  const existed = parts.find((item) => item.partId === currentPart.value?.partId)
  currentPart.value = existed ?? parts[0]
})

async function loadVideoPage() {
  const videoId = Number(route.params.videoId)
  if (Number.isNaN(videoId)) return

  const [, , commentResponse, stateResponse] = await Promise.all([
    videoStore.fetchVideoDetail(videoId),
    recommendStore.fetchRelatedRecommend(videoId),
    getVideoComments(videoId, 1, 20),
    userStore.isLoggedIn ? getVideoInteractionState(videoId).catch(() => null) : Promise.resolve(null)
  ])

  comments.value = commentResponse.data.records ?? []
  interactionState.value = stateResponse?.data ?? null
}

function selectPart(part: VideoPart) {
  currentPart.value = part
}

function formatDuration(sec?: number | null) {
  const value = sec ?? 0
  const minutes = Math.floor(value / 60)
  const seconds = String(value % 60).padStart(2, '0')
  return `${minutes}:${seconds}`
}

async function handleCommentSubmit(content: string) {
  const videoId = Number(route.params.videoId)
  if (!ensureLogin()) return

  commentSubmitting.value = true
  try {
    await createComment(videoId, content)
    const response = await getVideoComments(videoId, 1, 20)
    comments.value = response.data.records ?? []
    await videoStore.fetchVideoDetail(videoId)
    ElMessage.success('评论已发布')
  } finally {
    commentSubmitting.value = false
  }
}

async function handleToggleLike() {
  const videoId = Number(route.params.videoId)
  if (!ensureLogin()) return

  actionLoading.value = true
  try {
    const response = interactionState.value?.liked ? await cancelLikeVideo(videoId) : await likeVideo(videoId)
    interactionState.value = response.data
    await videoStore.fetchVideoDetail(videoId)
  } finally {
    actionLoading.value = false
  }
}

async function handleCoin() {
  const videoId = Number(route.params.videoId)
  if (!ensureLogin()) return

  actionLoading.value = true
  try {
    const response = await coinVideo(videoId, 1)
    interactionState.value = response.data
    await videoStore.fetchVideoDetail(videoId)
    ElMessage.success('投币成功')
  } finally {
    actionLoading.value = false
  }
}

async function handleWatchLater() {
  const videoId = Number(route.params.videoId)
  if (!ensureLogin()) return

  actionLoading.value = true
  try {
    const response = interactionState.value?.watchLater ? await removeWatchLater(videoId) : await addWatchLater(videoId)
    interactionState.value = response.data
  } finally {
    actionLoading.value = false
  }
}

async function handleFavorite() {
  const videoId = Number(route.params.videoId)
  if (!ensureLogin()) return

  actionLoading.value = true
  try {
    const folders = await getFavoriteFolders()
    let favoriteId = folders.data[0]?.fid

    if (!favoriteId) {
      const created = await createFavoriteFolder({ name: '默认收藏夹', isPublic: 1 })
      favoriteId = created.data.fid
    }

    const response = await favoriteVideo(videoId, favoriteId)
    interactionState.value = response.data
    await videoStore.fetchVideoDetail(videoId)
    ElMessage.success('已加入收藏')
  } finally {
    actionLoading.value = false
  }
}

function ensureLogin() {
  if (!userStore.isLoggedIn) {
    ElMessage.warning('请先登录')
    return false
  }
  return true
}
</script>

<style scoped lang="scss">
.video-detail-page,
.detail-main,
.detail-side,
.detail-bottom,
.comment-panel,
.side-card,
.part-panel {
  display: grid;
  gap: 18px;
}

.detail-top {
  display: grid;
  grid-template-columns: minmax(0, 1.65fr) 360px;
  gap: 24px;
  align-items: start;
}

.video-player-shell {
  border-radius: 28px;
  overflow: hidden;
  background: #0f172a;
  box-shadow: 0 20px 46px rgba(15, 23, 42, 0.16);
}

.video-player {
  width: 100%;
  min-height: 520px;
  background: #0f172a;
}

.video-player--empty {
  display: grid;
  place-items: center;
}

.player-chip {
  padding: 8px 12px;
  border-radius: 999px;
  background: rgba(255,255,255,0.12);
  color: #fff;
  font-size: 12px;
}

.part-panel,
.video-meta-card,
.side-card,
.comment-panel {
  padding: 22px;
  border-radius: 24px;
  border: 1px solid #e7ebf3;
  background: #fff;
}

.part-list {
  display: grid;
  gap: 10px;
}

.part-item {
  padding: 14px 16px;
  border: 1px solid #e7ebf3;
  border-radius: 16px;
  background: #f8fafc;
  text-align: left;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  cursor: pointer;
}

.part-item.active {
  border-color: #0f172a;
  background: #0f172a;
  color: #fff;
}

.part-item span {
  color: inherit;
  font-size: 12px;
}

.video-meta-card h1 {
  margin: 0;
  line-height: 1.3;
}

.video-description {
  margin: 0;
  color: #64748b;
  line-height: 1.8;
}

.video-tags,
.video-stats {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.video-stats {
  color: #64748b;
  font-size: 13px;
}

.comment-panel__head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
}

.comment-panel__head h3 {
  margin: 0;
}

.danmaku-input {
  width: 280px;
}
</style>