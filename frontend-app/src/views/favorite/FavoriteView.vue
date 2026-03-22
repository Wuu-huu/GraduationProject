<template>
  <div class="page-shell favorite-page">
    <section class="favorite-hero">
      <div class="section-title-block">
        <p class="section-eyebrow">收藏夹</p>
        <h1>我的收藏内容</h1>
      </div>
      <div class="create-row">
        <el-input v-model="newFolderName" placeholder="请输入收藏夹名称" style="width: 260px" />
        <el-button type="primary" @click="handleCreateFolder">新建收藏夹</el-button>
      </div>
    </section>

    <section class="favorite-board">
      <div class="favorite-main section-card">
        <header class="section-heading">
          <div class="section-title-block">
            <p class="section-eyebrow">收藏夹列表</p>
            <h2>选择一个收藏夹</h2>
          </div>
        </header>
        <div class="folder-grid">
          <button
            v-for="folder in folders"
            :key="folder.fid"
            class="folder-card"
            type="button"
            @click="selectFolder(folder.fid)"
          >
            <strong>{{ folder.title }}</strong>
            <span>{{ folder.itemCount ?? 0 }} 个视频</span>
          </button>
        </div>
        <EmptyState v-if="folders.length === 0" title="还没有收藏夹" description="创建一个收藏夹开始整理内容。" />
      </div>

      <div class="favorite-detail section-card">
        <header class="section-heading">
          <div class="section-title-block">
            <p class="section-eyebrow">详情</p>
            <h2>{{ activeFolder?.folder.title ?? '请选择一个收藏夹' }}</h2>
          </div>
        </header>
        <VideoList v-if="activeFolder" :videos="activeFolder.videos" />
        <EmptyState v-else title="尚未选择收藏夹" description="点击左侧收藏夹查看其中的视频。" />
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import EmptyState from '@/components/common/EmptyState.vue'
import VideoList from '@/components/video/VideoList.vue'
import { createFavoriteFolder, getFavoriteFolderDetail, getFavoriteFolders } from '@/api/favorite'
import type { FavoriteFolder, FavoriteFolderDetail } from '@/types/favorite'

const folders = ref<FavoriteFolder[]>([])
const activeFolder = ref<FavoriteFolderDetail | null>(null)
const newFolderName = ref('')

async function selectFolder(fid: number) {
  const response = await getFavoriteFolderDetail(fid)
  activeFolder.value = response.data
}

async function loadFolders() {
  const response = await getFavoriteFolders()
  folders.value = response.data
  if (folders.value.length > 0 && !activeFolder.value) {
    await selectFolder(folders.value[0].fid)
  }
}

async function handleCreateFolder() {
  const name = newFolderName.value.trim()
  if (!name) {
    ElMessage.warning('请输入收藏夹名称')
    return
  }

  const created = await createFavoriteFolder({ name, isPublic: 1 })
  newFolderName.value = ''
  await loadFolders()
  await selectFolder(created.data.fid)
  ElMessage.success('收藏夹创建成功')
}

onMounted(async () => {
  await loadFolders()
})
</script>

<style scoped lang="scss">
.favorite-page,
.favorite-board {
  display: grid;
  gap: 22px;
}

.favorite-hero {
  padding: 26px 28px;
  border-radius: 28px;
  background: linear-gradient(135deg, #eff6ff, #fff7ed 58%, #fae8ff);
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 20px;
}

.create-row {
  display: flex;
  align-items: center;
  gap: 12px;
}

.favorite-board {
  grid-template-columns: 380px minmax(0, 1fr);
  align-items: start;
}

.section-card {
  padding: 22px;
  border-radius: 24px;
  border: 1px solid #e7ebf3;
  background: #fff;
}

.folder-grid {
  display: grid;
  gap: 12px;
}

.folder-card {
  padding: 16px 18px;
  border: 1px solid #e7ebf3;
  border-radius: 18px;
  background: #f8fafc;
  display: grid;
  gap: 6px;
  text-align: left;
  cursor: pointer;
}

.folder-card span {
  color: #64748b;
  font-size: 13px;
}
</style>