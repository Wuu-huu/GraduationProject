import axios, { type AxiosError, type AxiosInstance, type AxiosResponse } from 'axios'
import { ElMessage } from 'element-plus'
import type { ApiResponse } from '@/types/api'
import { readAccessToken, removeAccessToken } from '@/utils/auth'

const service: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  timeout: 15000
})

service.interceptors.request.use((config) => {
  const token = readAccessToken()

  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }

  return config
})

service.interceptors.response.use(
  (response: AxiosResponse<ApiResponse<unknown>>) => {
    const body = response.data

    if (body.code === 0) {
      return body
    }

    if (body.code === 20000) {
      removeAccessToken()
      ElMessage.error(body.message || '登录状态已失效')
      window.location.href = '/login'
      return Promise.reject(new Error(body.message))
    }

    ElMessage.error(body.message || '请求失败')
    return Promise.reject(new Error(body.message))
  },
  (error: AxiosError) => {
    ElMessage.error(error.message || '网络异常')
    return Promise.reject(error)
  }
)

export default service