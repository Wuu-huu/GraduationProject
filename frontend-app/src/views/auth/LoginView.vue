<template>
  <div class="auth-layout">
    <el-card class="auth-card">
      <div class="auth-card__head">
        <h1>登录</h1>
        <p>欢迎回来，继续观看你喜欢的内容。</p>
      </div>
      <el-form label-position="top" @submit.prevent="handleLogin">
        <el-form-item label="用户名">
          <el-input v-model="form.username" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" type="password" show-password />
        </el-form-item>
        <el-button type="primary" block :loading="loading" @click="handleLogin">立即登录</el-button>
      </el-form>
      <div class="auth-card__footer">
        <span>还没有账号？</span>
        <RouterLink to="/register">去注册</RouterLink>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/userStore'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const loading = ref(false)
const form = reactive({
  username: '',
  password: ''
})

async function handleLogin() {
  if (!form.username || !form.password) {
    ElMessage.warning('请输入用户名和密码')
    return
  }

  loading.value = true
  try {
    await userStore.login({ ...form })
    const redirect = typeof route.query.redirect === 'string' ? route.query.redirect : '/'
    await router.push(redirect)
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