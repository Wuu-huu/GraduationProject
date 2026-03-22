<template>
  <div class="page-shell">
    <section class="panel-card">
      <div class="panel-head">
        <div>
          <p class="section-eyebrow">视频投稿</p>
          <h2>发布视频或保存草稿</h2>
        </div>
      </div>

      <el-form label-position="top" class="upload-form">
        <el-form-item label="标题">
          <el-input v-model="form.title" maxlength="200" />
        </el-form-item>
        <el-form-item label="副标题">
          <el-input v-model="form.subtitle" maxlength="200" />
        </el-form-item>
        <el-form-item label="简介">
          <el-input v-model="form.description" type="textarea" :rows="4" maxlength="5000" />
        </el-form-item>

        <div class="form-grid">
          <el-form-item label="来源类型">
            <el-select v-model="form.sourceType">
              <el-option label="原创" :value="1" />
              <el-option label="转载" :value="2" />
            </el-select>
          </el-form-item>
          <el-form-item label="版权类型">
            <el-select v-model="form.copyrightType">
              <el-option label="自制版权" :value="1" />
              <el-option label="授权转载" :value="2" />
            </el-select>
          </el-form-item>
          <el-form-item label="可见范围">
            <el-select v-model="form.visibility">
              <el-option label="仅自己可见" :value="0" />
              <el-option label="公开" :value="1" />
            </el-select>
          </el-form-item>
          <el-form-item label="主分区 ID">
            <el-input-number v-model="form.primaryCategoryId" :min="1" />
          </el-form-item>
        </div>

        <UploadArea v-model="form.coverUrl" title="封面地址" placeholder="请输入封面地址" />
        <UploadArea v-model="form.parts[0].videoUrl" title="视频地址" placeholder="请输入视频地址" />

        <div class="form-grid">
          <el-form-item label="视频时长（秒）">
            <el-input-number v-model="form.durationSec" :min="1" />
          </el-form-item>
          <el-form-item label="分 P 标题">
            <el-input v-model="form.parts[0].title" />
          </el-form-item>
          <el-form-item label="分 P 序号">
            <el-input-number v-model="form.parts[0].partNo" :min="1" />
          </el-form-item>
        </div>

        <el-form-item label="标签">
          <TagInput v-model="tags" />
        </el-form-item>

        <div class="form-actions">
          <el-button :loading="saving" @click="handleSaveDraft">保存草稿</el-button>
          <el-button type="primary" :loading="publishing" @click="handlePublish">立即发布</el-button>
        </div>
      </el-form>
    </section>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import TagInput from '@/components/form/TagInput.vue'
import UploadArea from '@/components/upload/UploadArea.vue'
import { publishVideo, saveVideoDraft } from '@/api/video'
import type { SaveVideoPayload } from '@/types/video'

const saving = ref(false)
const publishing = ref(false)
const tags = ref<string[]>([])
const form = reactive<SaveVideoPayload>({
  title: '',
  subtitle: '',
  sourceType: 1,
  copyrightType: 1,
  coverUrl: '',
  description: '',
  visibility: 1,
  durationSec: 300,
  primaryCategoryId: 1,
  tagIds: [],
  parts: [
    {
      partNo: 1,
      title: '分 P 1',
      videoUrl: '',
      durationSec: 300,
      sizeBytes: 0
    }
  ]
})

function buildPayload(): SaveVideoPayload {
  return {
    ...form,
    tagIds: tags.value.map((_, index) => index + 1),
    parts: form.parts.map((part) => ({ ...part, durationSec: form.durationSec }))
  }
}

async function handleSaveDraft() {
  saving.value = true
  try {
    await saveVideoDraft(buildPayload())
    ElMessage.success('草稿已保存，后续可在投稿管理中继续编辑')
  } finally {
    saving.value = false
  }
}

async function handlePublish() {
  publishing.value = true
  try {
    await publishVideo(buildPayload())
    ElMessage.success('视频发布成功')
  } finally {
    publishing.value = false
  }
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

.upload-form {
  display: grid;
  gap: 8px;
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 16px;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}
</style>