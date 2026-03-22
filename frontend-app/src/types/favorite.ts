export interface FavoriteFolder {
  fid: number
  uid?: number
  folderType?: number
  title: string
  description?: string | null
  coverUrl?: string | null
  visible: number
  sortNo?: number
  itemCount?: number
  createTime?: string
  updateTime?: string
}

export interface FavoriteFolderDetail {
  folder: FavoriteFolder
  videos: import('./video').VideoCard[]
}
