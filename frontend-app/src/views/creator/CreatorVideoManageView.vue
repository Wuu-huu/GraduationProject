<template>
  <div class="page-shell">
    <section class="panel-card">
      <div class="panel-head">
        <div>
          <p class="section-eyebrow">投稿管理</p>
          <h2>我的视频列表</h2>
        </div>
      </div>
      <div class="video-manage-list">
        <article v-for="item in videos" :key="item.vid" class="video-manage-item">
          <div>
            <strong>{{ item.title }}</strong>
            <p>{{ item.subtitle || '暂无副标题' }}</p>
          </div>
          <div class="video-manage-meta">
            <span>{{ item.playCount ?? 0 }} 播放</span>
            <span>{{ item.likeCount ?? 0 }} 点赞</span>
            <el-button type="danger" text @click="handleDelete(item.vid)">删除</el-button>
          </div>
        </article>
      </div>
      <AppPagination :current-page="pageNum" :page-size="pageSize" :total="total" @change="handlePageChange" />
    </section>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import AppPagination from '@/components/common/AppPagination.vue'
import { deleteVideo, getUserVideos } from '@/api/video'
import type { VideoCard } from '@/types/video'
import { useUserStore } from '@/stores/userStore'

const userStore = useUserStore()
const videos = ref<VideoCard[]>([])
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)

onMounted(async () => {
  if (!userStore.currentUser) {
    await userStore.fetchCurrentUser()
  }
  await loadVideos()
})

async function loadVideos() {
  if (!userStore.currentUser?.uid) return
  const response = await getUserVideos(userStore.currentUser.uid, pageNum.value, pageSize.value)
  videos.value = response.data.records
  total.value = response.data.total
  pageNum.value = response.data.pageNum
  pageSize.value = response.data.pageSize
}

async function handlePageChange(page: number) {
  pageNum.value = page
  await loadVideos()
}

async function handleDelete(videoId: number) {
  await ElMessageBox.confirm('确认删除该视频吗？', '提示', { type: 'warning' })
  await deleteVideo(videoId)
  ElMessage.success('视频已删除')
  await loadVideos()
}
</script>

<style scoped lang="scss">
.panel-card {
  padding: 22px;
  border-radius: 24px;
  border: 1px solid #e7ebf3;
  background: #fff;
  display: grid;
  gap: 16px;
}

.panel-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.section-eyebrow {
  margin: 0 0 6px;
  color: #94a3b8;
  font-size: 12px;
  letter-spacing: 0.08em;
  text-transform: uppercase;
}

.video-manage-list {
  display: grid;
}

.video-manage-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  padding: 16px 0;
  border-top: 1px solid #eef2f7;

  p {
    margin: 6px 0 0;
    color: #64748b;
  }
}

.video-manage-meta {
  display: flex;
  align-items: center;
  gap: 14px;
  color: #64748b;
  font-size: 13px;
}
</style>