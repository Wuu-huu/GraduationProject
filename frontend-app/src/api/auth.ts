import request from './request'
import type { ApiResponse } from '@/types/api'
import type { CurrentUser, LoginRequest, LoginResult, RegisterRequest } from '@/types/auth'

export function login(data: LoginRequest): Promise<ApiResponse<LoginResult>> {
  return request.post('/api/auth/login', data)
}

export function register(data: RegisterRequest): Promise<ApiResponse<LoginResult>> {
  return request.post('/api/auth/register', data)
}

export function getCurrentUser(): Promise<ApiResponse<CurrentUser>> {
  return request.get('/api/auth/me')
}
