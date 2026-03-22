# 前后端接口契约与联调约定

## 1. 通用约定

### 1.1 统一响应结构
后端统一返回：

```json
{
  "code": 0,
  "message": "success",
  "data": {}
}
```

分页统一返回：

```json
{
  "records": [],
  "total": 0,
  "pageNum": 1,
  "pageSize": 10
}
```

### 1.2 鉴权方式
- 请求头：`Authorization: Bearer <token>`
- 登录后前端统一保存 token
- 路由守卫根据登录态和角色信息控制访问

### 1.3 跨域
- 跨域由后端统一处理
- 前端本地开发无需再额外做代理才能联调
- 默认前端开发地址为 `http://localhost:5173`

## 2. 前端当前实际使用的 API 模块

- [`frontend-app/src/api/auth.ts`](/E:/GraduationProject/GraduationProject/frontend-app/src/api/auth.ts)
- [`frontend-app/src/api/user.ts`](/E:/GraduationProject/GraduationProject/frontend-app/src/api/user.ts)
- [`frontend-app/src/api/video.ts`](/E:/GraduationProject/GraduationProject/frontend-app/src/api/video.ts)
- [`frontend-app/src/api/comment.ts`](/E:/GraduationProject/GraduationProject/frontend-app/src/api/comment.ts)
- [`frontend-app/src/api/favorite.ts`](/E:/GraduationProject/GraduationProject/frontend-app/src/api/favorite.ts)
- [`frontend-app/src/api/message.ts`](/E:/GraduationProject/GraduationProject/frontend-app/src/api/message.ts)
- [`frontend-app/src/api/admin.ts`](/E:/GraduationProject/GraduationProject/frontend-app/src/api/admin.ts)
- [`frontend-app/src/api/recommend.ts`](/E:/GraduationProject/GraduationProject/frontend-app/src/api/recommend.ts)
- [`frontend-app/src/api/interaction.ts`](/E:/GraduationProject/GraduationProject/frontend-app/src/api/interaction.ts)

## 3. 页面到接口映射

### 3.1 首页
- `GET /api/recommend/home`
- `GET /api/recommend/hot`
- `GET /api/videos`

### 3.2 登录与注册
- `POST /api/auth/login`
- `POST /api/auth/register`
- `GET /api/auth/me`

### 3.3 视频详情页
- `GET /api/videos/{videoId}`
- `GET /api/videos/{videoId}/parts`
- `GET /api/videos/{videoId}/stats`
- `GET /api/recommend/videos/{videoId}/related`
- `GET /api/videos/{videoId}/comments`
- `GET /api/comments/{commentId}/replies`
- `GET /api/videos/{videoId}/danmakus`
- `GET /api/interactions/videos/{videoId}/state`
- `POST /api/interactions/videos/{videoId}/like`
- `POST /api/interactions/videos/{videoId}/dislike`
- `POST /api/interactions/videos/{videoId}/coin`
- `POST /api/interactions/videos/{videoId}/favorite`
- `POST /api/interactions/videos/{videoId}/watch-later`
- `POST /api/comments`
- `POST /api/comments/{commentId}/reply`
- `POST /api/videos/{videoId}/danmakus`

### 3.4 分区页
- `GET /api/videos/zone/{zoneId}`
- `GET /api/recommend/zones/{zoneId}`

### 3.5 用户主页与设置
- `GET /api/users/{uid}/profile`
- `GET /api/users/{uid}/videos`
- `POST /api/users/{uid}/follow`
- `DELETE /api/users/{uid}/follow`
- `GET /api/users/{uid}/followers`
- `GET /api/users/{uid}/following`
- `GET /api/users/me/profile`
- `PUT /api/users/me/profile`
- `GET /api/users/me/settings`
- `PUT /api/users/me/settings`

### 3.6 收藏夹
- `GET /api/favorites`
- `GET /api/favorites/{favoriteId}`
- `POST /api/favorites`
- `DELETE /api/favorites/{favoriteId}`
- `POST /api/favorites/{favoriteId}/videos/{videoId}`
- `DELETE /api/favorites/{favoriteId}/videos/{videoId}`

### 3.7 消息中心
- `GET /api/messages/conversations`
- `GET /api/messages/conversations/{conversationId}`
- `POST /api/messages/conversations`
- `POST /api/messages/conversations/{conversationId}/messages`
- `PUT /api/messages/conversations/{conversationId}/read`
- `GET /api/messages/notifications`
- `PUT /api/messages/notifications/read`
- `GET /api/messages/unread-count`

### 3.8 创作者中心
- `POST /api/videos`
- `POST /api/videos/drafts`
- `PUT /api/videos/{videoId}`
- `DELETE /api/videos/{videoId}`
- `GET /api/videos/{videoId}/parts`
- `POST /api/videos/{videoId}/parts`
- `PUT /api/videos/{videoId}/parts/{partId}`
- `DELETE /api/videos/{videoId}/parts/{partId}`
- `PUT /api/videos/{videoId}/category-tags`
- `POST /api/video-series`
- `PUT /api/video-series/{seriesId}`
- `DELETE /api/video-series/{seriesId}`
- `GET /api/video-series/{seriesId}`
- `GET /api/video-series/{seriesId}/videos`

### 3.9 管理后台
- `GET /api/audit/videos`
- `POST /api/audit/videos/{auditId}/approve`
- `POST /api/audit/videos/{auditId}/reject`
- `GET /api/audit/comments`
- `POST /api/audit/comments/{auditId}/approve`
- `POST /api/audit/comments/{auditId}/reject`
- `GET /api/audit/reports`
- `POST /api/audit/reports/{reportId}/handle`
- `POST /api/audit/users/{uid}/ban`
- `POST /api/audit/users/{uid}/mute`
- `GET /api/audit/risk-logs`

## 4. 推荐模块接口说明

### 4.1 首页推荐
`GET /api/recommend/home?pageNum=1&pageSize=10`

### 4.2 相关推荐
`GET /api/recommend/videos/{videoId}/related?pageNum=1&pageSize=10`

### 4.3 分区推荐
`GET /api/recommend/zones/{zoneId}?pageNum=1&pageSize=10`

### 4.4 热门榜
`GET /api/recommend/hot?pageNum=1&pageSize=10`

### 4.5 ItemCF 重建（管理调试）
`POST /api/recommend/admin/itemcf/{videoId}/rebuild`

## 5. 前端兼容字段说明

为兼容前端更自然的字段命名，后端已经在 DTO 层做了兼容：
- `sortNo -> partNo`
- `name -> title`
- `isPublic -> visible`
- `favoriteId -> favoriteFolderId`
- `progressMs -> timePointMs`

这意味着前端历史调用方式在当前版本中仍可工作，但后续建议逐步统一回后端标准字段名。

## 6. 视频播放链路说明

前端视频详情页当前通过 `video.parts[].videoUrl` 播放。

关键字段：
- `partId`
- `partNo`
- `title`
- `videoUrl`
- `durationSec`

说明：
- 当前前端使用原生 `<video>` 播放器
- 如果数据库中 `videoUrl` 仍是占位地址，则播放器只能展示，不能真实播放
- 建议后续将上传链路接入 MinIO 或对象存储，保证地址可访问

## 7. 公开访问与权限说明

公开访问：
- 首页
- 视频详情
- 分区列表
- 搜索结果结构页
- 相关推荐
- 热门榜
- 回复列表

需要登录：
- 互动操作
- 收藏夹
- 消息中心
- 用户设置
- 创作者中心

需要管理员：
- 审核相关接口
- 风控日志
- 封禁 / 禁言
- 推荐重建调试接口