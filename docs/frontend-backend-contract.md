# 前端所需后端能力清单

## 1. 前端开发前必须明确的后端约定

- 统一响应：`code / message / data`
- 分页响应：`records / total / pageNum / pageSize`
- token 头：`Authorization: Bearer <token>`
- 后端已统一处理跨域，无需前端额外做代理才能联调
- 公开接口与鉴权接口需要在路由守卫中区分

## 2. 推荐模块前端必须接入的接口

- 首页推荐：`GET /api/recommend/home`
- 相关推荐：`GET /api/recommend/videos/{videoId}/related`
- 分区推荐：`GET /api/recommend/zones/{zoneId}`
- 热门榜：`GET /api/recommend/hot`

## 3. 用户端页面建议的接口组合

### 首页

- 首页推荐
- 热门榜
- 首页公开视频列表

### 视频详情页

- 视频详情
- 互动状态
- 评论列表
- 弹幕列表
- 相关推荐

### 用户主页

- 用户资料
- 用户投稿列表
- 关注与粉丝列表

### 消息中心

- 会话列表
- 通知列表
- 未读数

## 4. 创作者中心建议的接口组合

- 投稿列表
- 草稿列表
- 视频发布与编辑
- 分 P 管理
- 合集管理

## 5. 管理后台建议的接口组合

- 视频审核列表与审核操作
- 评论审核列表与审核操作
- 举报处理
- 风控日志

## 6. 前端需要重点兼容的字段

- `sortNo -> partNo`
- `name -> title`
- `isPublic -> visible`
- `favoriteId -> favoriteFolderId`
- `progressMs -> timePointMs`

## 7. 推荐展示组件建议

- 首页推荐视频列表组件
- 视频详情相关推荐组件
- 分区推荐区块组件
- 热门榜面板组件
- 推荐来源与推荐分数字段在调试模式下可展示