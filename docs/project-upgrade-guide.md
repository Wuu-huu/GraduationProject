# 项目升级指南

## 1. 升级阅读顺序

建议后续接手者按以下顺序阅读文档：
1. [`README.md`](/E:/GraduationProject/GraduationProject/README.md)
2. [`docs/system-summary.md`](/E:/GraduationProject/GraduationProject/docs/system-summary.md)
3. [`docs/frontend-backend-contract.md`](/E:/GraduationProject/GraduationProject/docs/frontend-backend-contract.md)
4. [`docs/frontend-integration.md`](/E:/GraduationProject/GraduationProject/docs/frontend-integration.md)
5. [`docs/stage8-delivery.md`](/E:/GraduationProject/GraduationProject/docs/stage8-delivery.md)

## 2. 当前架构边界

### 后端
当前为模块化单体，但已经具备按领域拆分的基础：
- `auth-module`
- `user-module`
- `video-module`
- `interaction-module`
- `message-module`
- `audit-module`
- `recommend-module`

升级时应坚持：
- 不跨服务直接访问对方数据库
- 对外统一暴露 facade 或 API
- 优先从读多、写分散、吞吐敏感的模块开始拆分

### 前端
当前前端为单仓单工程，已通过布局和路由区分：
- 用户端
- 创作者中心
- 管理后台

后续如果业务继续扩大，可视情况拆分为：
- 主站前台
- 创作者工作台
- 管理后台

## 3. 推荐系统升级路线

### 当前状态
- 热门榜
- 首页推荐
- 相关推荐
- 分区推荐
- 推荐请求 / 推荐结果 / 曝光日志落库
- 行为事件回流
- ItemCF 中间表
- Kafka 骨架预留但未启用

### 建议升级顺序
1. 启用行为曝光和点击的异步流处理
2. 将推荐行为日志迁移到 Kafka 消费模式
3. 从离线 ItemCF 扩展到多路召回
4. 增加效果评估指标，如 CTR、完播率、互动率
5. 后续再考虑 UserCF、向量召回和重排模型

### 需要保持稳定的表和事件
- `recommend_request`
- `recommend_result`
- `video_exposure_log`
- `user_behavior_event`
- `recommend_item_similarity`
- `UserBehaviorTrackEvent`

## 4. Kafka 启用建议

### 当前状态
项目中已经保留 Kafka 的设计预留，但默认禁用。

### 启用步骤建议
1. 增加正式的 Kafka 依赖与配置
2. 按环境开启 `kafka.enabled`
3. 先迁移推荐曝光与行为埋点
4. 保留同步写库兜底，避免切换期数据丢失
5. 验证消费幂等后，再扩大到消息通知或更多事件流

## 5. Spring Cloud 升级建议

### 推荐拆分顺序
1. `recommend-module`
2. `message-module`
3. `audit-module`
4. `interaction-module`

### 原因
- 推荐模块依赖埋点和日志，天然适合独立扩容
- 消息与通知具有异步特征
- 审核模块后台场景明确，容易独立
- 互动模块写多读多，后续也适合拆分

### 升级注意点
- 保持接口契约稳定
- 先抽 facade，再抽服务
- 不在升级时同时重构数据库
- 优先建立可观测性与链路追踪

## 6. 视频播放能力升级建议

### 当前状态
前端播放器已接通，依赖 `video.parts[].videoUrl`。

### 后续要补的能力
- 上传到 MinIO
- 视频转码
- 多码率
- 封面抽帧
- 播放鉴权
- 防盗链与 CDN
- 播放日志精细上报

### 建议顺序
1. 先把上传地址改为真实对象存储地址
2. 再补播放日志上报
3. 最后考虑转码、切片和鉴权

## 7. 前端升级建议

- 将首页推荐曝光、点击显式上报
- 搜索页接入真实搜索接口
- 完善后台数据统计与分类标签编辑
- 在不改变主站结构的前提下逐步抽离更细粒度组件
- 如果后续拆服务，可同步补充前端 API 分层和接口文档生成

## 8. 当前仍需注意的风险

- 历史上存在文件编码污染，后续新增文件必须保持 `UTF-8 无 BOM`
- 推荐相关测试数据不足时，分区推荐和标签推荐可能表现偏弱
- 若数据库表结构变更，需同步检查前端兼容字段和 DTO 映射
- 视频播放是否成功强依赖数据库里的真实资源地址