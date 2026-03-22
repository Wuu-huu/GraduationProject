import request from './request'
import type { ApiResponse } from '@/types/api'
import type { UserVideoState } from '@/types/interaction'

export function getVideoInteractionState(videoId: number): Promise<ApiResponse<UserVideoState>> {
  return request.get(`/api/interactions/videos/${videoId}/state`)
}

export function likeVideo(videoId: number): Promise<ApiResponse<UserVideoState>> {
  return request.post(`/api/interactions/videos/${videoId}/like`)
}

export function cancelLikeVideo(videoId: number): Promise<ApiResponse<UserVideoState>> {
  return request.delete(`/api/interactions/videos/${videoId}/like`)
}

export function coinVideo(videoId: number, coinCount = 1): Promise<ApiResponse<UserVideoState>> {
  return request.post(`/api/interactions/videos/${videoId}/coin`, { coinCount })
}

export function addWatchLater(videoId: number): Promise<ApiResponse<UserVideoState>> {
  return request.post(`/api/interactions/videos/${videoId}/watch-later`)
}

export function removeWatchLater(videoId: number): Promise<ApiResponse<UserVideoState>> {
  return request.delete(`/api/interactions/videos/${videoId}/watch-later`)
}

export function favoriteVideo(videoId: number, favoriteId: number): Promise<ApiResponse<UserVideoState>> {
  return request.post(`/api/interactions/videos/${videoId}/favorite`, { favoriteId })
}
