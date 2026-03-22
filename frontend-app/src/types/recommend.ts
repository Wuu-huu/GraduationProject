export interface RecommendQuery {
  pageNum?: number
  pageSize?: number
}

export interface RecommendVideo {
  vid: number
  title: string
  subtitle?: string | null
  coverUrl: string
  durationSec?: number | null
  authorUid: number
  authorName: string
  authorAvatarUrl?: string | null
  playCount?: number | null
  likeCount?: number | null
  commentCount?: number | null
  favoriteCount?: number | null
  publishTime?: string | null
  source: 'HOT' | 'LATEST' | 'CATEGORY_PREF' | 'TAG_PREF' | 'ITEM_CF' | 'ZONE_HOT' | 'FALLBACK'
  score?: number | null
  positionNo: number
}
