import request from './request'
import type { ApiResponse } from '@/types/api'
import type { FavoriteFolder, FavoriteFolderDetail } from '@/types/favorite'

export function getFavoriteFolders(): Promise<ApiResponse<FavoriteFolder[]>> {
  return request.get('/api/favorites')
}

export function getFavoriteFolderDetail(favoriteId: number): Promise<ApiResponse<FavoriteFolderDetail>> {
  return request.get(`/api/favorites/${favoriteId}`)
}

export function createFavoriteFolder(data: {
  name: string
  description?: string
  isPublic?: number
}): Promise<ApiResponse<FavoriteFolder>> {
  return request.post('/api/favorites', {
    folderType: 1,
    name: data.name,
    description: data.description,
    isPublic: data.isPublic ?? 1
  })
}
