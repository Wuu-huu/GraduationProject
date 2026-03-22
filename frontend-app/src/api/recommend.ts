import request from './request'
import type { ApiResponse, PageResponse } from '@/types/api'
import type { RecommendQuery, RecommendVideo } from '@/types/recommend'

export function getHomeRecommend(params: RecommendQuery): Promise<ApiResponse<PageResponse<RecommendVideo>>> {
  return request.get('/api/recommend/home', { params })
}

export function getRelatedRecommend(videoId: number, params: RecommendQuery): Promise<ApiResponse<PageResponse<RecommendVideo>>> {
  return request.get(`/api/recommend/videos/${videoId}/related`, { params })
}

export function getZoneRecommend(zoneId: number, params: RecommendQuery): Promise<ApiResponse<PageResponse<RecommendVideo>>> {
  return request.get(`/api/recommend/zones/${zoneId}`, { params })
}

export function getHotRecommend(params: RecommendQuery): Promise<ApiResponse<PageResponse<RecommendVideo>>> {
  return request.get('/api/recommend/hot', { params })
}
