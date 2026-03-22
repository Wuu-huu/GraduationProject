<template>
  <div class="page-shell settings-page">
    <section class="settings-hero">
      <div class="section-title-block">
        <p class="section-eyebrow">偏好设置</p>
        <h1>个人设置</h1>
      </div>
      <el-button type="primary" :loading="saving" @click="handleSave">保存设置</el-button>
    </section>

    <section class="settings-grid">
      <div class="settings-card">
        <div class="section-title-block">
          <p class="section-eyebrow">内容推荐</p>
          <h2>推荐与通知</h2>
        </div>
        <el-form label-position="top">
          <el-form-item label="开启个性化推荐">
            <el-switch v-model="settings.openRecommend" />
          </el-form-item>
          <el-form-item label="开启通知推送">
            <el-switch v-model="settings.openPush" />
          </el-form-item>
          <el-form-item label="允许私信">
            <el-switch v-model="settings.openDm" />
          </el-form-item>
        </el-form>
      </div>

      <div class="settings-card">
        <div class="section-title-block">
          <p class="section-eyebrow">隐私可见性</p>
          <h2>公开范围</h2>
        </div>
        <el-form label-position="top">
          <el-form-item label="公开关注列表">
            <el-switch v-model="settings.openFollowVisible" />
          </el-form-item>
          <el-form-item label="公开收藏夹">
            <el-switch v-model="settings.openFavoriteVisible" />
          </el-form-item>
        </el-form>
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { getMySettings, updateMySettings } from '@/api/user'

const saving = ref(false)
const settings = reactive({
  openRecommend: true,
  openPush: true,
  openDm: true,
  openFollowVisible: true,
  openFavoriteVisible: true
})

onMounted(async () => {
  const response = await getMySettings()
  Object.assign(settings, response.data)
})

async function handleSave() {
  saving.value = true
  try {
    await updateMySettings({ ...settings })
    ElMessage.success('设置已保存')
  } finally {
    saving.value = false
  }
}
</script>

<style scoped lang="scss">
.settings-page,
.settings-grid {
  display: grid;
  gap: 22px;
}

.settings-hero {
  padding: 26px 28px;
  border-radius: 28px;
  background: linear-gradient(135deg, #eff6ff, #fff7ed 58%, #fae8ff);
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 20px;
}

.settings-grid {
  grid-template-columns: repeat(2, minmax(0, 1fr));
}

.settings-card {
  padding: 22px;
  border-radius: 24px;
  border: 1px solid #e7ebf3;
  background: #fff;
}

.settings-card h2 {
  margin: 0 0 16px;
}
</style>