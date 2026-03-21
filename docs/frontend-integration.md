# 前端联调说明

## 技术栈约束

- Vue 3
- TypeScript
- Vite
- Pinia
- Vue Router
- Axios
- Element Plus
- SCSS / scoped style

## 推荐的目录结构

```text
src/
  layouts/
  router/
  stores/
  api/
  views/
    home/
    auth/
    video/
    category/
    search/
    user/
    favorite/
    message/
    creator/
    admin/
  components/
  types/
  utils/
  assets/
  styles/
```

## Pinia Store 建议

- `userStore`：登录态、当前用户、权限
- `appStore`：全局加载状态、导航状态、主题和通用配置
- `videoStore`：视频详情、视频列表、投稿状态
- `messageStore`：会话、通知、未读数
- `categoryStore`：分区、标签、筛选条件
- `recommendStore`：首页推荐、相关推荐、分区推荐、热门榜

## 统一响应结构

```ts
interface ApiResponse<T> {
  code: number
  message: string
  data: T
}

interface PageResponse<T> {
  records: T[]
  total: number
  pageNum: number
  pageSize: number
}
```

## 推荐接口

### `src/api/recommend.ts`

```ts
export interface RecommendQuery {
  pageNum?: number
  pageSize?: number
}

export interface RecommendVideoVO {
  vid: number
  title: string
  subtitle: string | null
  coverUrl: string
  durationSec: number | null
  authorUid: number
  authorName: string
  authorAvatarUrl: string | null
  playCount: number | null
  likeCount: number | null
  commentCount: number | null
  favoriteCount: number | null
  publishTime: string | null
  source: 'HOT' | 'LATEST' | 'CATEGORY_PREF' | 'TAG_PREF' | 'ITEM_CF' | 'ZONE_HOT' | 'FALLBACK'
  score: number | null
  positionNo: number
}
```

建议封装：

- `getHomeRecommend(params)` -> `GET /api/recommend/home`
- `getRelatedRecommend(videoId, params)` -> `GET /api/recommend/videos/{videoId}/related`
- `getZoneRecommend(zoneId, params)` -> `GET /api/recommend/zones/{zoneId}`
- `getHotRecommend(params)` -> `GET /api/recommend/hot`

## 其他 API 文件建议

- `src/api/auth.ts`
- `src/api/user.ts`
- `src/api/video.ts`
- `src/api/comment.ts`
- `src/api/favorite.ts`
- `src/api/message.ts`
- `src/api/admin.ts`
- `src/api/recommend.ts`

## 页面到接口映射建议

### 用户端

- 首页
  - `/api/recommend/home`
  - `/api/recommend/hot`
  - `/api/videos`
- 视频详情页
  - `/api/videos/{videoId}`
  - `/api/recommend/videos/{videoId}/related`
  - `/api/videos/{videoId}/comments`
  - `/api/videos/{videoId}/danmakus`
  - `/api/interactions/videos/{videoId}/state`
- 分区页
  - `/api/videos/zone/{zoneId}`
  - `/api/recommend/zones/{zoneId}`
- 用户主页
  - `/api/users/{uid}/profile`
  - `/api/users/{uid}/videos`
- 收藏夹页
  - `/api/favorites`
  - `/api/favorites/{favoriteId}`
- 消息中心
  - `/api/messages/conversations`
  - `/api/messages/notifications`
  - `/api/messages/unread-count`

### 创作者中心

- 投稿管理
  - `/api/users/{uid}/videos`
  - `/api/videos`
  - `/api/videos/drafts`
  - `/api/videos/{videoId}`
- 视频投稿页
  - `/api/videos`
  - `/api/videos/{videoId}/parts`
  - `/api/videos/{videoId}/category-tags`
- 合集管理
  - `/api/video-series`
  - `/api/video-series/{seriesId}`
  - `/api/video-series/{seriesId}/videos`

### 管理后台

- 视频审核
  - `/api/audit/videos`
- 评论管理
  - `/api/audit/comments`
- 举报处理
  - `/api/audit/reports`
- 风控日志
  - `/api/audit/risk-logs`
- 推荐调试
  - `/api/recommend/admin/itemcf/{videoId}/rebuild`

## 公开访问与权限建议

### 匿名可访问

- `/`
- `/login`
- `/register`
- `/videos/:videoId`
- `/zones/:zoneId`
- 首页推荐、热门榜、相关推荐、分区推荐
- 评论列表、回复列表

### 登录后访问

- 点赞、投币、收藏、评论、弹幕
- 个人设置
- 收藏夹
- 私信与通知
- 创作者中心

### 管理员访问

- 审核后台
- 举报处理
- 风控日志
- ItemCF 重建接口

## 组件拆分建议

- `components/layout/AppHeader.vue`
- `components/category/CategoryNav.vue`
- `components/video/VideoCard.vue`
- `components/video/VideoList.vue`
- `components/recommend/RecommendVideoList.vue`
- `components/recommend/HotRankPanel.vue`
- `components/video/AuthorCard.vue`
- `components/comment/CommentList.vue`
- `components/comment/CommentInput.vue`
- `components/common/EmptyState.vue`
- `components/common/AppPagination.vue`
- `components/upload/UploadArea.vue`
- `components/form/TagInput.vue`
- `components/common/PageFilterBar.vue`

## 路由建议

- `/`
- `/login`
- `/register`
- `/videos/:videoId`
- `/zones/:zoneId`
- `/search`
- `/users/:uid`
- `/favorites`
- `/messages`
- `/messages/conversations/:conversationId`
- `/settings`
- `/creator`
- `/creator/videos`
- `/creator/upload`
- `/creator/series`
- `/admin`
- `/admin/audit/videos`
- `/admin/audit/comments`
- `/admin/reports`
- `/admin/categories`

## Axios 封装建议

- 统一处理 `code`
- `20000` 视为未登录或登录过期，跳转登录页
- `10001` 视为业务错误，直接用 Element Plus 消息提示
- token 使用 `Authorization: Bearer <token>`

## 可先用 mock 占位的页面

- 搜索结果页
- 创作者中心概览统计
- 管理后台分类标签管理

这些页面可以先保留 API 封装和页面结构，等后续模块进一步补齐后再接实数。

## 联调时可直接复用的字段兼容说明

- 视频分 P 请求兼容 `sortNo -> partNo`
- 收藏夹创建兼容 `name -> title`
- 收藏夹公开状态兼容 `isPublic -> visible`
- 收藏行为兼容 `favoriteId -> favoriteFolderId`
- 弹幕发送兼容 `progressMs -> timePointMs`