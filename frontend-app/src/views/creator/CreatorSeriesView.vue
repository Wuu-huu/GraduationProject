<template>
  <div class="page-shell">
    <section class="panel-card">
      <div class="panel-head">
        <div>
          <p class="section-eyebrow">合集管理</p>
          <h2>创建并维护视频合集</h2>
        </div>
      </div>

      <el-form label-position="top" class="series-form">
        <div class="form-grid">
          <el-form-item label="合集 ID">
            <el-input-number v-model="seriesIdInput" :min="1" />
          </el-form-item>
          <el-form-item label="可见范围">
            <el-select v-model="form.visible">
              <el-option label="仅自己可见" :value="0" />
              <el-option label="公开" :value="1" />
            </el-select>
          </el-form-item>
        </div>

        <el-form-item label="合集标题">
          <el-input v-model="form.title" />
        </el-form-item>
        <el-form-item label="合集简介">
          <el-input v-model="form.description" type="textarea" :rows="3" />
        </el-form-item>
        <UploadArea v-model="form.coverUrl" title="合集封面" placeholder="请输入合集封面地址" />
        <el-form-item label="视频 ID 列表">
          <el-input v-model="videoIdsText" placeholder="例如：12,13,15" />
        </el-form-item>

        <div class="form-actions">
          <el-button @click="handleLoadSeries">加载合集</el-button>
          <el-button @click="handleDeleteSeries" :disabled="!seriesIdInput">删除合集</el-button>
          <el-button :loading="saving" @click="handleCreateSeries">创建合集</el-button>
          <el-button type="primary" :loading="saving" :disabled="!seriesIdInput" @click="handleUpdateSeries">更新合集</el-button>
        </div>
      </el-form>
    </section>

    <section class="panel-card">
      <div class="panel-head">
        <div>
          <p class="section-eyebrow">最近合集</p>
          <h2>快速切换</h2>
        </div>
      </div>
      <div class="series-chip-list">
        <el-button v-for="seriesId in recentSeriesIds" :key="seriesId" @click="loadSeriesById(seriesId)">合集 {{ seriesId }}</el-button>
      </div>
      <VideoList v-if="currentSeries" :videos="currentSeries.videos" />
      <EmptyState v-else title="暂无合集内容" description="" />
    </section>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import EmptyState from '@/components/common/EmptyState.vue'
import UploadArea from '@/components/upload/UploadArea.vue'
import VideoList from '@/components/video/VideoList.vue'
import { createVideoSeries, deleteVideoSeries, getVideoSeriesDetail, updateVideoSeries } from '@/api/video'
import type { SaveVideoSeriesPayload, VideoSeries } from '@/types/video'
import { getCreatorSeriesIds, pushCreatorSeriesId, removeCreatorSeriesId } from '@/utils/storage'

const saving = ref(false)
const seriesIdInput = ref<number | undefined>()
const videoIdsText = ref('')
const currentSeries = ref<VideoSeries | null>(null)
const recentSeriesIds = ref<number[]>([])
const form = reactive<SaveVideoSeriesPayload>({
  title: '',
  description: '',
  coverUrl: '',
  visible: 1,
  videoIds: []
})

const parsedVideoIds = computed(() =>
  videoIdsText.value
    .split(',')
    .map((item) => Number(item.trim()))
    .filter((item) => !Number.isNaN(item) && item > 0)
)

onMounted(() => {
  recentSeriesIds.value = getCreatorSeriesIds()
})

function fillForm(series: VideoSeries) {
  form.title = series.title
  form.description = series.description ?? ''
  form.coverUrl = series.coverUrl ?? ''
  form.visible = series.visible
  form.videoIds = series.videos.map((item) => item.vid)
  videoIdsText.value = form.videoIds.join(',')
  currentSeries.value = series
}

async function loadSeriesById(seriesId: number) {
  const response = await getVideoSeriesDetail(seriesId)
  seriesIdInput.value = response.data.seriesId
  fillForm(response.data)
  pushCreatorSeriesId(response.data.seriesId)
  recentSeriesIds.value = getCreatorSeriesIds()
}

async function handleLoadSeries() {
  if (!seriesIdInput.value) {
    ElMessage.warning('请输入合集 ID')
    return
  }

  await loadSeriesById(seriesIdInput.value)
}

async function handleCreateSeries() {
  saving.value = true
  try {
    const response = await createVideoSeries({ ...form, videoIds: parsedVideoIds.value })
    seriesIdInput.value = response.data.seriesId
    fillForm(response.data)
    pushCreatorSeriesId(response.data.seriesId)
    recentSeriesIds.value = getCreatorSeriesIds()
    ElMessage.success('合集创建成功')
  } finally {
    saving.value = false
  }
}

async function handleUpdateSeries() {
  if (!seriesIdInput.value) {
    ElMessage.warning('请先加载一个合集')
    return
  }

  saving.value = true
  try {
    const response = await updateVideoSeries(seriesIdInput.value, { ...form, videoIds: parsedVideoIds.value })
    fillForm(response.data)
    pushCreatorSeriesId(response.data.seriesId)
    recentSeriesIds.value = getCreatorSeriesIds()
    ElMessage.success('合集更新成功')
  } finally {
    saving.value = false
  }
}

async function handleDeleteSeries() {
  if (!seriesIdInput.value) {
    ElMessage.warning('请先加载一个合集')
    return
  }

  await ElMessageBox.confirm('确定要删除这个合集吗？', '删除确认', { type: 'warning' })
  await deleteVideoSeries(seriesIdInput.value)
  removeCreatorSeriesId(seriesIdInput.value)
  recentSeriesIds.value = getCreatorSeriesIds()
  seriesIdInput.value = undefined
  currentSeries.value = null
  form.title = ''
  form.description = ''
  form.coverUrl = ''
  form.visible = 1
  form.videoIds = []
  videoIdsText.value = ''
  ElMessage.success('合集删除成功')
}
</script>

<style scoped lang="scss">
.panel-card {
  padding: 22px;
  border-radius: 24px;
  border: 1px solid #e7ebf3;
  background: #fff;
  display: grid;
  gap: 18px;
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

.series-form {
  display: grid;
  gap: 8px;
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 16px;
}

.form-actions,
.series-chip-list {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.form-actions {
  justify-content: flex-end;
}
</style>