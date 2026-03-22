export interface VideoCard {
  vid: number
  title: string
  subtitle?: string | null
  coverUrl: string
  durationSec?: number | null
  authorUid: number
  authorName: string
  authorAvatarUrl?: string | null
  playCount?: number
  likeCount?: number
  commentCount?: number
  favoriteCount?: number
  publishTime?: string | null
}

export interface VideoDetail extends VideoCard {
  description?: string | null
  visibility?: number
  publishStatus?: number
  parts?: VideoPart[]
  seriesList?: VideoSeries[]
  stats?: {
    playCount?: number
    likeCount?: number
    commentCount?: number
    favoriteCount?: number
    coinCount?: number
    danmuCount?: number
  }
  author?: {
    uid: number
    username?: string | null
    nickname?: string | null
    avatarUrl?: string | null
  }
  categoryTag?: {
    categoryId?: number | null
    categoryName?: string | null
    tagIds?: number[]
    tagNames?: string[]
  }
}

export interface VideoPart {
  partId?: number
  partNo: number
  title: string
  videoUrl: string
  durationSec: number
  sizeBytes?: number | null
}

export interface SaveVideoPayload {
  title: string
  subtitle?: string
  sourceType: number
  copyrightType: number
  coverUrl?: string
  description?: string
  visibility: number
  durationSec: number
  primaryCategoryId?: number
  tagIds?: number[]
  parts: VideoPart[]
}

export interface VideoSeries {
  seriesId: number
  uid?: number
  title: string
  description?: string | null
  coverUrl?: string | null
  visible: number
  createTime?: string
  updateTime?: string
  videos: VideoCard[]
}

export interface SaveVideoSeriesPayload {
  title: string
  description?: string
  coverUrl?: string
  visible: number
  videoIds: number[]
}
