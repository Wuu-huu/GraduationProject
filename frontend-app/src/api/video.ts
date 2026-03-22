import request from './request'
import type { ApiResponse, PageResponse } from '@/types/api'
import type { SaveVideoPayload, SaveVideoSeriesPayload, VideoCard, VideoDetail, VideoSeries } from '@/types/video'

export function getHomeVideos(pageNum = 1, pageSize = 12): Promise<ApiResponse<PageResponse<VideoCard>>> {
  return request.get('/api/videos', { params: { pageNum, pageSize } })
}

export function getVideoDetail(videoId: number): Promise<ApiResponse<VideoDetail>> {
  return request.get(`/api/videos/${videoId}`)
}

export function getZoneVideos(zoneId: number, pageNum = 1, pageSize = 12): Promise<ApiResponse<PageResponse<VideoCard>>> {
  return request.get(`/api/videos/zone/${zoneId}`, { params: { pageNum, pageSize } })
}

export function getUserVideos(uid: number, pageNum = 1, pageSize = 12): Promise<ApiResponse<PageResponse<VideoCard>>> {
  return request.get(`/api/users/${uid}/videos`, { params: { pageNum, pageSize } })
}

export function publishVideo(data: SaveVideoPayload): Promise<ApiResponse<VideoDetail>> {
  return request.post('/api/videos', data)
}

export function saveVideoDraft(data: SaveVideoPayload): Promise<ApiResponse<VideoDetail>> {
  return request.post('/api/videos/drafts', data)
}

export function updateVideo(videoId: number, data: SaveVideoPayload): Promise<ApiResponse<VideoDetail>> {
  return request.put(`/api/videos/${videoId}`, data)
}

export function deleteVideo(videoId: number): Promise<ApiResponse<void>> {
  return request.delete(`/api/videos/${videoId}`)
}

export function createVideoSeries(data: SaveVideoSeriesPayload): Promise<ApiResponse<VideoSeries>> {
  return request.post('/api/video-series', data)
}

export function updateVideoSeries(seriesId: number, data: SaveVideoSeriesPayload): Promise<ApiResponse<VideoSeries>> {
  return request.put(`/api/video-series/${seriesId}`, data)
}

export function deleteVideoSeries(seriesId: number): Promise<ApiResponse<void>> {
  return request.delete(`/api/video-series/${seriesId}`)
}

export function getVideoSeriesDetail(seriesId: number): Promise<ApiResponse<VideoSeries>> {
  return request.get(`/api/video-series/${seriesId}`)
}
