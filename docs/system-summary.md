# 系统概要与设计总结

## 1. 系统概要设计

本项目是一个类哔哩哔哩视频平台，采用前后端分离架构。

- 后端：`Spring Boot 3 + Maven 多模块`
- 前端：`Vue 3 + TypeScript + Vite`
- 当前部署形态：模块化单体
- 未来演进方向：可按业务域平滑拆分到 `Spring Cloud`

系统覆盖的核心业务域如下：
- 用户与认证
- 视频内容
- 互动行为
- 消息通知
- 审核管理
- 推荐服务
- 管理后台

设计原则：
- 当前先完成可联调、可运行、可演示的模块化单体
- 模块边界明确，不跨模块直接操作对方 mapper
- 推荐先做“数据驱动 + 协同过滤”，不做重型算法平台
- 所有能力优先围绕现有数据库表结构落地

## 2. 功能结构设计

### 2.1 用户与认证
- 注册
- 登录
- JWT 鉴权
- 获取当前用户
- 用户资料修改
- 用户设置
- 用户主页
- 关注 / 取关
- 粉丝 / 关注列表

### 2.2 视频内容
- 视频发布
- 草稿保存
- 视频编辑
- 视频删除 / 下架
- 视频详情
- 首页列表
- 分区列表
- 用户投稿列表
- 分P管理
- 分类标签绑定
- 合集管理
- 视频统计查询

### 2.3 互动行为
- 点赞 / 取消点赞
- 点踩 / 取消点踩
- 投币
- 收藏夹与收藏
- 稍后再看
- 评论与回复
- 评论点赞
- 弹幕发送与查询
- 用户行为日志与推荐埋点

### 2.4 消息通知
- 私信会话列表
- 会话详情
- 发送消息
- 会话已读
- 通知中心
- 未读数统计

### 2.5 审核管理
- 视频审核
- 评论审核
- 举报处理
- 封禁 / 禁言
- 风控日志

### 2.6 推荐服务
- 首页推荐
- 相关推荐
- 分区推荐
- 热门榜
- 推荐请求记录
- 推荐结果记录
- 曝光日志
- 行为事件回流
- ItemCF 相似视频中间表

## 3. 系统时序图设计（文本形式）

### 3.1 用户登录
1. 前端调用 `/api/auth/login`
2. `AuthController` 接收请求并校验参数
3. `UserInfoServiceImpl` 校验账号和密码
4. 服务生成 JWT 并返回用户基础身份信息
5. 前端保存 token，后续请求通过 `Authorization: Bearer <token>` 访问

### 3.2 视频发布
1. 创作者中心调用 `/api/videos`
2. `VideoController` 校验登录态和参数
3. `VideoService` 保存视频主记录
4. `VideoPartService` 保存分P信息
5. 分类与标签关系落库
6. 返回视频 ID 和发布结果

### 3.3 点赞行为
1. 前端调用 `/api/interactions/videos/{videoId}/like`
2. `VideoInteractionController` 校验用户与视频状态
3. `VideoActionServiceImpl` 写入点赞行为并更新统计
4. 发布行为事件 `UserBehaviorTrackEvent`
5. 推荐模块监听后写入 `user_behavior_event`
6. 返回点赞结果

### 3.4 首页推荐请求
1. 前端调用 `/api/recommend/home`
2. `RecommendController` 接收分页参数
3. `RecommendationServiceImpl` 创建 `recommend_request`
4. 服务构建热门、最新、偏好、ItemCF 候选池
5. 过滤无效视频并融合打分
6. 写入 `recommend_result`
7. 写入 `video_exposure_log`
8. 返回推荐视频列表

### 3.5 审核通过视频
1. 管理员调用 `/api/audit/videos/{auditId}/approve`
2. `AuditAdminController` 校验管理员权限
3. `AuditTaskServiceImpl` 更新审核记录
4. 回写视频发布状态
5. 发送站内通知
6. 作者在通知中心查看审核结果

## 4. 系统功能模块设计

### common
- 统一响应对象
- 分页对象
- 错误码与全局异常
- JWT 工具
- 安全上下文工具
- OpenAPI 配置
- MyBatis-Plus 分页配置

### auth-module
- 注册
- 登录
- 当前用户身份获取
- JWT 生成与解析

### user-module
- 用户资料
- 用户设置
- 用户主页聚合
- 关注关系管理
- 用户统计

### video-module
- 视频主数据
- 草稿与发布状态
- 分P管理
- 分类标签绑定
- 合集管理
- 详情聚合与统计查询
- `VideoFacade` 供其他模块读取公开视频信息

### interaction-module
- 点赞 / 点踩 / 投币
- 收藏夹 / 收藏
- 稍后再看
- 评论 / 回复 / 评论点赞
- 弹幕
- 行为埋点发布

### message-module
- 私信会话
- 消息发送
- 会话已读
- 通知中心
- 未读数

### audit-module
- 视频审核
- 评论审核
- 举报处理
- 封禁 / 禁言
- 风控日志

### recommend-module
- 热门榜
- 首页推荐
- 相关推荐
- 分区推荐
- 推荐日志
- 曝光日志
- 行为回流
- ItemCF 相似度重建
- Kafka 异步骨架预留

### web-app
- 聚合所有模块
- 安全配置
- 跨域配置
- 统一启动入口

## 5. E-R 图（文本形式）

### 5.1 用户域
- `user_info(1) -> (1) user_profile`
- `user_info(1) -> (1) user_setting`
- `user_info(1) -> (1) user_stat`
- `user_info(1) -> (N) user_follow`（作为关注者）
- `user_info(1) -> (N) user_follow`（作为被关注者）

### 5.2 视频域
- `user_info(1) -> (N) video`
- `video(1) -> (N) video_part`
- `video(1) -> (N) video_stat` 或在统计表中聚合
- `video(N) -> (N) tag` 通过关系表绑定
- `video(N) -> (1) category`
- `video_series(1) -> (N) video`

### 5.3 互动域
- `user_info(1) -> (N) video_action`
- `user_info(1) -> (N) favorite_folder`
- `favorite_folder(1) -> (N) favorite_item`
- `video(1) -> (N) comment`
- `comment(1) -> (N) comment`（楼中楼回复）
- `video(1) -> (N) danmu`

### 5.4 消息与审核域
- `conversation(1) -> (N) private_message`
- `user_info(1) -> (N) notification`
- `audit_task(N) -> (1) video/comment/report`
- `ban_record(N) -> (1) user_info`
- `risk_control_log(N) -> (1) user_info`

### 5.5 推荐域
- `recommend_request(1) -> (N) recommend_result`
- `recommend_request(1) -> (N) video_exposure_log`
- `user_info(1) -> (N) user_behavior_event`
- `video(1) -> (N) recommend_item_similarity`（vid）
- `video(1) -> (N) recommend_item_similarity`（related_vid）
- `user_info(1) -> (1) user_interest_profile`
- `video(1) -> (1) video_feature_profile`

## 6. 核心算法实现

### 6.1 热门榜
热门榜主要基于视频统计数据进行打分，综合考虑：
- 播放量
- 点赞量
- 评论量
- 收藏量
- 投币量
- 发布时间衰减

实现特点：
- 使用配置化权重
- 过滤不可见视频
- 作为首页推荐和分区推荐的兜底候选源

### 6.2 首页推荐
分为未登录和已登录两种场景。

未登录：
- 热门候选
- 最新候选
- 少量探索项

已登录：
- 分类偏好
- 标签偏好
- ItemCF 候选
- 热门兜底

### 6.3 相关推荐
- 当前视频的标签 / 分区相似召回
- ItemCF 中间表召回
- 过滤当前视频自身
- 最终按综合得分排序

### 6.4 ItemCF
当前实现是简化版离线 ItemCF：
- 从用户行为中提取 “用户-视频” 偏好关系
- 根据视频共现情况计算相似度
- 将结果写入 `recommend_item_similarity`
- 推荐接口查询时直接读取中间表

### 6.5 埋点与回流
- 推荐请求写入 `recommend_request`
- 推荐结果写入 `recommend_result`
- 曝光写入 `video_exposure_log`
- 用户互动行为写入 `user_behavior_event`
- Kafka 目前只保留预留骨架，未启用

## 7. 遇到的问题与解决方案

### 7.1 文档和源码乱码
问题：部分历史文件编码混乱，出现中文乱码。
解决：统一按 `UTF-8 无 BOM` 重写关键文件和文档。

### 7.2 MyBatis XML 路径与包名错误
问题：早期 mapper XML 路径和 namespace 存在拼写错误，导致启动失败。
解决：修正 XML 路径、namespace 与扫描配置。

### 7.3 分页 total 不回填
问题：列表接口返回 records 正常但 total 为 0。
解决：补充 MyBatis-Plus 分页拦截器。

### 7.4 推荐匿名请求写库失败
问题：匿名用户访问推荐接口时，日志表 `uid` 非空约束导致接口报错。
解决：匿名请求统一写 `uid = 0`，但推荐逻辑仍按匿名场景处理。

### 7.5 前后端跨域
问题：前端本地开发跨域。
解决：由后端统一配置 CORS，在 `web-app` 中集中处理。

### 7.6 视频播放链路只有详情没有真实播放器
问题：前期只有播放器占位，没有真实播放能力。
解决：前端视频详情页接入原生 `<video>`，按 `parts[].videoUrl` 播放。

## 8. 后续可做的优化

- 将推荐模块独立为服务，接入 Spring Cloud
- 启用 Kafka，改造行为事件和曝光日志为异步流处理
- 接入 Elasticsearch 完善搜索
- 引入真实的视频播放链路、转码、切片、鉴权
- 扩展推荐效果分析，如 CTR、完播率、转化率
- 完成后台统计大盘和分类标签管理的完整功能闭环
- 引入对象存储与 CDN，替换测试占位视频地址

## 9. 尚存在的问题与建议方案

### 9.1 视频地址仍可能是占位 URL
现状：前端播放器已接通，但数据库里的视频地址不一定是真实资源。
建议：后续将上传链路接到 MinIO，并在视频表或分P表中保存真实可访问地址。

### 9.2 Kafka 仅做了预留
现状：异步事件仍以同步写库为主。
建议：后续启用 Kafka producer / consumer，优先迁移推荐曝光与行为事件。

### 9.3 推荐算法仍是基础版
现状：当前只做热门、偏好和 ItemCF。
建议：后续可扩展 UserCF、召回融合、重排模型和在线特征。

### 9.4 后台部分页面仍偏轻量
现状：后台已具备核心审核链路，但数据运营与系统管理能力不足。
建议：后续补后台统计、分类标签实际管理、用户搜索与筛选。