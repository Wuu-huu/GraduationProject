import request from './request'
import type { ApiResponse, PageResponse } from '@/types/api'
import type { UserHome, UserProfile, UserSettings } from '@/types/user'

export function getUserProfile(uid: number): Promise<ApiResponse<UserHome>> {
  return request.get(`/api/users/${uid}/profile`)
}

export function getMyProfile(): Promise<ApiResponse<UserProfile>> {
  return request.get('/api/users/me/profile')
}

export function updateMyProfile(data: Partial<UserProfile>): Promise<ApiResponse<UserProfile>> {
  return request.put('/api/users/me/profile', data)
}

export function getMySettings(): Promise<ApiResponse<UserSettings>> {
  return request.get('/api/users/me/settings')
}

export function updateMySettings(data: UserSettings): Promise<ApiResponse<UserSettings>> {
  return request.put('/api/users/me/settings', data)
}

export function followUser(uid: number): Promise<ApiResponse<void>> {
  return request.post(`/api/users/${uid}/follow`)
}

export function unfollowUser(uid: number): Promise<ApiResponse<void>> {
  return request.delete(`/api/users/${uid}/follow`)
}

export function getFollowers(uid: number, pageNum = 1, pageSize = 20): Promise<ApiResponse<PageResponse<UserProfile>>> {
  return request.get(`/api/users/${uid}/followers`, { params: { pageNum, pageSize } })
}

export function getFollowing(uid: number, pageNum = 1, pageSize = 20): Promise<ApiResponse<PageResponse<UserProfile>>> {
  return request.get(`/api/users/${uid}/following`, { params: { pageNum, pageSize } })
}
