# 系统总结文档

## 1. 系统概要设计

本项目是一个类哔哩哔哩视频平台后端系统，采用 Spring Boot 多模块单体架构实现。系统以业务域拆分模块，在当前阶段保持单体部署、统一启动，但模块之间已经按清晰边界组织，为后续平滑升级到 Spring Cloud 做准备。

当前后端已覆盖核心业务链路：

- 用户注册、登录、鉴权
- 用户资料、关注、设置
- 视频发布、草稿、详情、分 P、合集、统计
- 点赞、投币、收藏、评论、弹幕、稍后再看
- 私信、通知、未读数
- 视频审核、评论审核、举报、封禁禁言
- 首页推荐、相关推荐、分区推荐、热门榜

项目统一采用：

- 统一响应模型 `code / message / data`
- Spring Security + JWT 认证
- MyBatis-Plus 持久层
- 基于事件的行为埋点与推荐日志
- 后端统一处理跨域，支持前后端分离联调

## 2. 功能结构设计

### 2.1 用户端功能

- 首页
- 登录与注册
- 视频详情页
- 分区页
- 搜索结果页预留
- 用户主页
- 收藏夹页
- 消息中心
- 私信会话页
- 个人设置页

### 2.2 创作者中心

- 创作者首页
- 投稿管理
- 视频投稿
- 合集管理

### 2.3 管理后台

- 后台首页
- 视频审核
- 评论审核
- 举报处理
- 风控日志
- 分类标签管理预留

### 2.4 推荐相关能力

- 首页推荐
- 相关推荐
- 分区推荐
- 热门榜
- 推荐请求记录
- 推荐结果记录
- 曝光记录
- 行为埋点回流
- ItemCF 相似度中间表

## 3. 系统时序图设计（文本形式）

### 3.1 用户登录

1. 前端调用 `/api/auth/login`
2. `AuthController` 接收请求并校验参数
3. `UserInfoServiceImpl` 校验账号密码
4. 服务层生成 JWT 并返回登录结果
5. 前端保存 token，后续请求在 `Authorization` 头中携带

### 3.2 视频发布

1. 创作者前端调用 `/api/videos`
2. `VideoController` 校验登录态和参数
3. `VideoService` 写入视频主表
4. `VideoPartService` 写入分 P 信息
5. `VideoCategory/VideoTag` 关系表落库
6. 返回视频 ID 和发布结果

### 3.3 点赞行为

1. 前端调用 `/api/interactions/videos/{videoId}/like`
2. `VideoInteractionController` 校验用户与视频
3. `VideoActionServiceImpl` 写入点赞行为
4. 同步更新视频统计
5. 发布 `UserBehaviorTrackEvent`
6. 推荐模块监听事件并写入 `user_behavior_event`
7. 返回点赞结果

### 3.4 首页推荐请求

1. 前端调用 `/api/recommend/home`
2. `RecommendController` 接收分页参数
3. `RecommendationServiceImpl` 创建 `recommend_request`
4. 服务层构建热门、最新、偏好、ItemCF 候选集
5. 过滤不可见视频并合并打分
6. 写入 `recommend_result`
7. 写入 `video_exposure_log`
8. 返回推荐视频分页结果

### 3.5 审核通过视频

1. 管理员调用 `/api/audit/videos/{auditId}/approve`
2. `AuditAdminController` 校验管理员权限
3. `AuditTaskServiceImpl` 更新审核单状态
4. 回写视频发布状态
5. 生成站内通知
6. 作者在通知中心查看审核结果

## 4. 系统功能模块设计

### 4.1 common

- 统一响应对象
- 统一分页对象
- 错误码与全局异常
- JWT 工具与安全上下文
- OpenAPI 配置

### 4.2 auth-module

- 注册
- 登录
- 当前用户获取
- JWT 生成与解析

### 4.3 user-module

- 用户资料
- 关注关系
- 粉丝与关注列表
- 用户设置
- 用户统计

### 4.4 video-module

- 视频主数据
- 草稿与发布状态
- 视频详情聚合
- 分 P 管理
- 分类标签绑定
- 合集管理
- 视频统计查询

### 4.5 interaction-module

- 点赞 / 点踩
- 投币
- 收藏夹与收藏
- 稍后再看
- 评论与回复
- 评论点赞
- 弹幕
- 行为埋点事件发布

### 4.6 message-module

- 私信会话
- 发送消息
- 已读状态
- 通知中心
- 未读数聚合

### 4.7 audit-module

- 视频审核
- 评论审核
- 举报处理
- 封禁与禁言
- 风控日志

### 4.8 recommend-module

- 热门榜
- 首页推荐
- 相关推荐
- 分区推荐
- 推荐请求与结果追踪
- 曝光日志
- 行为事件落库
- ItemCF 相似度重建
- Kafka 异步预留骨架

### 4.9 web-app

- 聚合所有模块
- 统一安全配置
- 统一跨域配置
- 启动入口

## 5. E-R 图（文本形式）

### 5.1 用户域

- `user_info` 1 - 1 `user_profile`
- `user_info` 1 - 1 `user_setting`
- `user_info` 1 - 1 `user_stat`
- `user_info` 1 - n `user_follow`（关注者）
- `user_info` 1 - n `user_follow`（被关注者）

### 5.2 视频域

- `user_info` 1 - n `video`
- `video` 1 - n `video_part`
- `video` n - n `category` 通过 `video_category`
- `video` n - n `tag` 通过 `video_tag`
- `video_series` 1 - n `video_series_item`
- `video` 1 - 1 `video_stat`

### 5.3 互动域

- `user_info` 1 - n `video_action`
- `video` 1 - n `video_action`
- `video` 1 - n `comment`
- `comment` 1 - n `comment`（楼中楼回复）
- `video` 1 - n `danmu`
- `user_info` 1 - n `favorite_folder`
- `favorite_folder` 1 - n `favorite_item`

### 5.4 消息与审核域

- `message_conversation` 1 - n `message_record`
- `user_info` 1 - n `notification`
- `audit_task` 关联 `video` 或 `comment`
- `report_record` 关联 `video`、`comment` 或 `user`
- `ban_record` n - 1 `user_info`
- `risk_control_log` n - 1 `user_info`

### 5.5 推荐域

- `recommend_request` 1 - n `recommend_result`
- `recommend_request` 1 - n `video_exposure_log`
- `user_info` 1 - n `user_behavior_event`
- `video` 1 - n `recommend_item_similarity`（vid）
- `video` 1 - n `recommend_item_similarity`（related_vid）
- `video` 1 - 1 `video_feature_profile`
- `user_info` 1 - 1 `user_interest_profile`
- `video` 1 - n `video_play_log`
- `user_info` 1 - n `search_log`

## 6. 核心算法实现

### 6.1 热门榜

热门榜基于视频统计数据计算，优先使用：

- 热度分
- 播放量
- 点赞量
- 评论量
- 收藏量

排序策略是按热度优先，再按播放量补充稳定排序。

### 6.2 首页推荐

未登录用户：

- 热门候选
- 最新候选
- 少量兜底候选

已登录用户：

- 热门候选
- 最新候选
- 分类偏好候选
- 标签偏好候选
- ItemCF 候选

最终通过加权融合得到结果，并做去重、分页、可见性过滤。

### 6.3 相关推荐

相关推荐以当前视频为中心，召回顺序为：

1. ItemCF 相似视频
2. 同分类视频
3. 同标签视频
4. 热门兜底视频

### 6.4 ItemCF 实现

当前阶段采用轻量 ItemCF：

1. 从 `video_action` 中取当前视频的正向行为用户集合
2. 找出这些用户也发生过正向行为的其他视频
3. 按行为类型赋予不同权重
4. 聚合得到相似视频分数
5. 将结果写入 `recommend_item_similarity`

正向行为包括：

- 点赞
- 投币
- 收藏
- 稍后再看
- 评论
- 弹幕

### 6.5 埋点与异步预留

当前阶段真实生效的是同步写库：

- `recommend_request`
- `recommend_result`
- `video_exposure_log`
- `user_behavior_event`

同时预留 Kafka 异步化骨架，但默认关闭，后续可切换为事件异步消费。

## 7. 遇到的问题和解决方案

### 7.1 MyBatis XML 路径与历史拼写错误

问题：

- 早期 XML 路径和包名存在 `authmoudule` 拼写错误
- 启动时会导致 Mapper XML 解析失败

解决：

- 修正 XML namespace 和 type
- 调整 `mapper-locations` 扫描路径

### 7.2 事件监听与事务传播问题

问题：

- `@TransactionalEventListener` 与普通事务注解组合不当，导致启动失败

解决：

- 将监听器事务传播改为 `REQUIRES_NEW`
- 保证注册后初始化用户扩展数据能稳定提交

### 7.3 分页总数不回填

问题：

- 粉丝/关注分页接口 `records` 正常，但 `total = 0`

解决：

- 增加 MyBatis-Plus 分页拦截器

### 7.4 DTO 字段名与前端传参不一致

问题：

- `sortNo`、`name`、`isPublic`、`favoriteId`、`progressMs` 等字段与后端原 DTO 不一致

解决：

- 在 DTO 中增加兼容映射
- 减少前后端联调成本

### 7.5 UTF-8 与历史乱码问题

问题：

- 早期源码与文档部分存在历史编码污染

解决：

- 新增要求：所有新增和修改文件统一使用 UTF-8 无 BOM
- 对关键接口、DTO、文档进行重写与清理

### 7.6 前后端分离联调跨域问题

问题：

- 前端本地开发时浏览器会拦截跨域请求

解决：

- 在 `web-app` 中新增统一跨域配置
- 允许常见开发阶段的跨域请求、方法、请求头与响应头

## 8. 后续可以做的优化

- 将推荐行为回流改造为 Kafka 异步链路
- 增加推荐效果分析与点击转化追踪
- 增加播放开始、播放完成等更细粒度埋点
- 将 UserCF 作为补充召回策略接入
- 增加推荐去重与探索机制
- 引入缓存策略优化热门榜与首页推荐性能
- 完善后台分类标签管理与搜索页能力
- 增加定时任务，周期性重建 ItemCF 相似度
- 增加更细的审核策略和风控规则

## 9. 尚存在的问题和建议方案

### 9.1 部分历史文件仍带乱码注释

现状：

- 个别旧文件的生成器注释仍有乱码，但不影响运行

建议方案：

- 分阶段重写关键模块文件头与注释
- 优先处理对 Swagger、异常信息、联调体验有影响的文件

### 9.2 推荐算法仍偏轻量

现状：

- 当前推荐更适合课程设计、演示和联调

建议方案：

- 后续引入更细粒度行为权重
- 加入播放完成率、点击率、停留时长等特征

### 9.3 搜索与后台管理尚未完全收口

现状：

- 搜索页只预留普通结构
- 分类标签后台能力还需要继续补

建议方案：

- 第八阶段收口 admin-module
- 前端先按 API 壳与占位结构推进

### 9.4 本地编译依赖环境权限

现状：

- 本地 Maven 插件缓存写权限可能阻塞自动编译验证

建议方案：

- 使用你当前手动编译方式继续作为主验证路径
- 后续统一整理 Maven 本地仓库权限