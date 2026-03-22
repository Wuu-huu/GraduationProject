import { defineStore } from 'pinia'
import { getCurrentUser, login as loginApi, register as registerApi } from '@/api/auth'
import type { CurrentUser, LoginRequest, RegisterRequest } from '@/types/auth'
import { readAccessToken, removeAccessToken, writeAccessToken } from '@/utils/auth'

interface UserState {
  token: string
  currentUser: CurrentUser | null
}

export const useUserStore = defineStore('userStore', {
  state: (): UserState => ({
    token: readAccessToken(),
    currentUser: null
  }),
  getters: {
    isLoggedIn: (state) => Boolean(state.token),
    roleCode: (state) => state.currentUser?.role ?? null
  },
  actions: {
    setToken(token: string) {
      this.token = token
      writeAccessToken(token)
    },
    async login(payload: LoginRequest) {
      const response = await loginApi(payload)
      this.setToken(response.data.accessToken)
      this.currentUser = {
        uid: response.data.uid,
        username: response.data.username,
        role: response.data.role,
        state: 0
      }
      return response.data
    },
    async register(payload: RegisterRequest) {
      const response = await registerApi(payload)
      this.setToken(response.data.accessToken)
      this.currentUser = {
        uid: response.data.uid,
        username: response.data.username,
        role: response.data.role,
        state: 0
      }
      return response.data
    },
    async fetchCurrentUser() {
      if (!this.token) {
        this.currentUser = null
        return null
      }

      const response = await getCurrentUser()
      this.currentUser = response.data
      return response.data
    },
    logout() {
      this.token = ''
      this.currentUser = null
      removeAccessToken()
    }
  }
})
