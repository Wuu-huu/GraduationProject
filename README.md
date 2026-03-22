# 毕业设计

一个类哔哩哔哩视频平台的前后端分离毕业设计项目。

后端采用 `Spring Boot 3 + Maven 多模块`，当前为模块化单体架构；前端采用 `Vue 3 + TypeScript + Vite + Pinia + Element Plus`。项目已经完成认证、用户、视频、互动、消息、审核、推荐以及主要前端页面的联调，并为后续升级到 `Spring Cloud`、`Kafka`、更完整的推荐系统和真实视频播放链路预留了边界。

## 1. 项目结构

```text
GraduationProject/
  common/                 后端公共模块
  auth-module/            认证与登录
  user-module/            用户资料、关注、设置
  video-module/           视频、分P、合集、统计
  interaction-module/     点赞、投币、收藏、评论、弹幕
  message-module/         私信、通知、未读数
  audit-module/           审核、举报、封禁、风控日志
  recommend-module/       首页推荐、相关推荐、热门榜、埋点
  admin-module/           后台基础管理能力预留
  web-app/                后端统一启动模块
  frontend-app/           Vue 3 前端工程
  docs/                   项目文档
  sql/                    演示数据与脚本
```

## 2. 当前已完成能力

### 后端
- 用户注册、登录、JWT 鉴权、当前用户获取
- 用户主页、关注/取关、粉丝/关注列表、个人设置
- 视频发布、草稿、编辑、删除、详情、列表、分P、合集、统计
- 点赞、点踩、投币、收藏夹、稍后再看、评论、回复、弹幕
- 私信会话、消息发送、通知中心、未读数
- 视频审核、评论审核、举报处理、封禁、禁言、风控日志
- 首页推荐、相关推荐、分区推荐、热门榜
- 推荐请求、推荐结果、曝光日志、行为事件、ItemCF 中间表
- 后端统一处理跨域，支持前后端分离联调

### 前端
- 用户端：首页、登录、注册、视频详情、分区、搜索
- 用户中心：个人主页、收藏夹、消息中心、私信会话、设置
- 创作者中心：创作首页、投稿管理、投稿页、合集管理
- 管理后台：首页、视频审核、评论审核、举报处理、用户管理、分类标签管理页面壳
- 推荐结果展示：首页推荐、相关推荐、分区推荐、热门榜
- 原生 `<video>` 播放器已接入视频详情页，支持分P切换

## 3. 关键技术栈

### 后端
- JDK 17
- Spring Boot 3.x
- Spring Security + JWT
- MyBatis-Plus
- MySQL 8
- Redis
- MinIO
- OpenAPI / Swagger
- Maven 多模块

### 前端
- Vue 3
- TypeScript
- Vite
- Pinia
- Vue Router
- Axios
- Element Plus
- SCSS / scoped style

## 4. 启动方式

### 后端
1. 准备 `MySQL 8`、`Redis`、`MinIO`
2. 执行演示数据脚本：[`sql/stage8_demo_seed.sql`](/E:/GraduationProject/GraduationProject/sql/stage8_demo_seed.sql)
3. 配置 [`web-app/src/main/resources/application.yml`](/E:/GraduationProject/GraduationProject/web-app/src/main/resources/application.yml)
4. 启动 `web-app`

### 前端
1. 进入 [`frontend-app`](/E:/GraduationProject/GraduationProject/frontend-app)
2. 执行 `npm install`
3. 执行 `npm run dev`
4. 默认通过 `.env.development` 访问后端接口

## 5. 当前联调状态

已确认可联调的核心链路包括：
- 登录 / 注册 / 获取当前用户
- 首页视频流 / 热门榜 / 推荐流
- 视频详情 / 相关推荐 / 评论 / 弹幕 / 点赞投币收藏
- 用户主页 / 关注关系 / 用户投稿
- 收藏夹 / 消息中心 / 私信会话
- 创作者投稿与合集管理
- 后台审核与举报处理
- 推荐日志、曝光日志、行为埋点落库

## 6. 当前已知限制

- 数据库中的视频地址如果仍是占位 URL，则前端播放器只能展示播放器，无法真实播放
- 部分后台页面目前完成了页面和联调壳，但后台统计能力仍可继续扩展
- Kafka 仅做了预留设计与文档说明，当前未启用
- 推荐系统当前只实现“数据驱动 + ItemCF”，未接入 ES、向量检索、深度学习排序

## 7. 文档索引

- [`docs/system-summary.md`](/E:/GraduationProject/GraduationProject/docs/system-summary.md)：系统概要、模块设计、时序、E-R、算法、问题总结
- [`docs/frontend-integration.md`](/E:/GraduationProject/GraduationProject/docs/frontend-integration.md)：前端工程结构与联调说明
- [`docs/frontend-backend-contract.md`](/E:/GraduationProject/GraduationProject/docs/frontend-backend-contract.md)：前后端接口契约与字段约定
- [`docs/stage8-delivery.md`](/E:/GraduationProject/GraduationProject/docs/stage8-delivery.md)：当前阶段交付总结
- [`docs/project-upgrade-guide.md`](/E:/GraduationProject/GraduationProject/docs/project-upgrade-guide.md)：后续升级路线与建议