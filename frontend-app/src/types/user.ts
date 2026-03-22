export interface UserProfile {
  uid: number
  username?: string | null
  nickname?: string | null
  avatarUrl?: string | null
  backgroundUrl?: string | null
  signature?: string | null
  fansCount?: number
  followingCount?: number
  videoCount?: number
  followed?: boolean
}

export interface UserHome {
  uid: number
  username?: string | null
  profile?: UserProfile
  fansCount?: number
  followingCount?: number
  likeReceivedCount?: number
  videoCount?: number
  playReceivedCount?: number
  followed?: boolean
}

export interface UserSettings {
  openRecommend: boolean
  openPush: boolean
  openDm: boolean
  openFollowVisible: boolean
  openFavoriteVisible: boolean
}
