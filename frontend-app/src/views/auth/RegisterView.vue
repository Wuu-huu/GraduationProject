<template>
  <div class="auth-layout">
    <el-card class="auth-card">
      <div class="auth-card__head">
        <h1>注册</h1>
        <p>创建账号后即可同步收藏、消息和创作记录。</p>
      </div>
      <el-form label-position="top" @submit.prevent="handleRegister">
        <el-form-item label="用户名">
          <el-input v-model="form.username" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" type="password" show-password />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email" />
        </el-form-item>
        <el-button type="primary" block :loading="loading" @click="handleRegister">立即注册</el-button>
      </el-form>
      <div class="auth-card__footer">
        <span>已经有账号？</span>
        <RouterLink to="/login">去登录</RouterLink>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/userStore'

const router = useRouter()
const userStore = useUserStore()
const loading = ref(false)
const form = reactive({
  username: '',
  password: '',
  email: ''
})

async function handleRegister() {
  if (!form.username || !form.password) {
    ElMessage.warning('请完整填写用户名和密码')
    return
  }

  loading.value = true
  try {
    await userStore.register({ ...form })
    await router.push('/')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped lang="scss">
.auth-layout {
  width: 100%;
  display: flex;
  justify-content: center;
}

.auth-card {
  width: min(440px, 100%);
  border-radius: 28px;
}

.auth-card__head {
  display: grid;
  gap: 8px;
  margin-bottom: 18px;
}

.auth-card__head h1,
.auth-card__head p,
.auth-card__footer {
  margin: 0;
}

.auth-card__head p,
.auth-card__footer {
  color: #64748b;
}

.auth-card__footer {
  margin-top: 18px;
  display: flex;
  justify-content: center;
  gap: 8px;
}
</style>