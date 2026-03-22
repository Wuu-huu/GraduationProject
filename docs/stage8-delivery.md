# 当前阶段交付说明

## 1. 交付范围

### 后端交付
- 阶段 1：方案与骨架设计
- 阶段 2：基础工程、JWT、安全、统一返回、异常、Swagger
- 阶段 3：认证与用户模块
- 阶段 4：视频模块
- 阶段 5：互动模块
- 阶段 6：消息与审核模块
- 阶段 7：推荐模块
- 阶段 8：文档整理、演示脚本、跨域处理、联调收尾

### 前端交付
- 阶段 1：结构设计
- 阶段 2：基础工程骨架
- 阶段 3：用户端核心页面
- 阶段 4：用户中心与消息页面
- 阶段 5：创作者中心
- 阶段 6：管理后台页面
- 阶段 7：统一优化、中文化、路由守卫、视觉收口

## 2. 当前可演示能力

- 用户注册、登录与登录态保持
- 首页推荐、热门榜、首页公开视频流
- 视频详情、评论、弹幕、互动操作
- 用户主页、收藏夹、消息中心、私信会话
- 创作者投稿、草稿、分P、合集管理
- 后台审核、举报处理、封禁禁言
- 推荐日志、曝光日志、行为日志与 ItemCF 中间表
- 视频详情页基于 `videoUrl` 的播放器接入

## 3. 跨域与联调

跨域由后端统一解决，关键配置位于：
- [`web-app/src/main/java/com/zzk/webapp/config/CorsConfig.java`](/E:/GraduationProject/GraduationProject/web-app/src/main/java/com/zzk/webapp/config/CorsConfig.java)
- [`web-app/src/main/java/com/zzk/webapp/config/SecurityConfig.java`](/E:/GraduationProject/GraduationProject/web-app/src/main/java/com/zzk/webapp/config/SecurityConfig.java)

当前前端本地开发地址可直接与后端联调，不必在前端额外做代理才能访问接口。

## 4. 演示数据

演示数据脚本：[`sql/stage8_demo_seed.sql`](/E:/GraduationProject/GraduationProject/sql/stage8_demo_seed.sql)

推荐相关新增表包括：
- `recommend_request`
- `recommend_result`
- `video_exposure_log`
- `user_behavior_event`
- `video_feature_profile`
- `user_interest_profile`
- `video_play_log`
- `search_log`
- `recommend_item_similarity`

## 5. 当前残留问题

- 如果 `videoUrl` 是占位地址，视频无法真实播放
- 后台部分能力目前偏轻量，更侧重审核链路演示
- 推荐系统已可用，但仍是基础版策略
- Kafka 仅做预留，未实际启用

## 6. 建议后续接手顺序

1. 先阅读 [`docs/system-summary.md`](/E:/GraduationProject/GraduationProject/docs/system-summary.md)
2. 再阅读 [`docs/frontend-backend-contract.md`](/E:/GraduationProject/GraduationProject/docs/frontend-backend-contract.md)
3. 然后阅读 [`docs/frontend-integration.md`](/E:/GraduationProject/GraduationProject/docs/frontend-integration.md)
4. 最后参考 [`docs/project-upgrade-guide.md`](/E:/GraduationProject/GraduationProject/docs/project-upgrade-guide.md) 制定升级计划