# 前端工程与联调说明

## 1. 前端技术栈

- Vue 3
- TypeScript
- Vite
- Pinia
- Vue Router
- Axios
- Element Plus
- SCSS / scoped style

## 2. 前端目录结构

```text
frontend-app/
  src/
    api/
    assets/
    components/
    layouts/
    router/
    stores/
    styles/
    types/
    utils/
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
```

## 3. 路由与页面

### 用户端
- 首页
- 登录页
- 注册页
- 视频详情页
- 分区页
- 搜索结果页
- 用户主页
- 收藏夹页
- 消息中心页
- 私信会话页
- 个人设置页

### 创作者中心
- 创作者首页
- 投稿管理页
- 视频投稿页
- 合集管理页

### 管理后台
- 后台首页
- 用户管理页
- 视频审核页
- 评论审核页
- 举报处理页
- 分类标签管理页

## 4. Pinia Store 设计

- `userStore`：token、当前用户、登录态
- `appStore`：全局 loading、布局状态
- `videoStore`：视频详情、列表、分页状态
- `messageStore`：会话、通知、未读数
- `categoryStore`：分区与筛选状态
- `recommendStore`：推荐流、热门榜、相关推荐

## 5. 当前前端实现重点

### 5.1 首页
- 信息结构参考 B 站首页
- 焦点推荐区 + 分区导航 + 内容筛选 + 热门榜
- 支持“推荐 / 热门 / 最新”切换
- 可通过按钮跳转到带筛选参数的搜索结果页

### 5.2 视频详情页
- 左侧播放器主内容区
- 标题、简介、互动区
- 分P切换
- 右侧作者卡与相关推荐
- 下方评论区与弹幕输入
- 已接入原生 `<video>` 播放器

### 5.3 用户中心
- 个人主页使用顶部资料区 + 统计卡 + 投稿流结构
- 设置页使用分组配置卡片布局
- 收藏夹页为左侧列表、右侧详情结构
- 消息中心为会话与通知双栏布局

### 5.4 创作者中心
- 创作首页
- 投稿管理
- 投稿页
- 合集管理

### 5.5 管理后台
- 页面壳与核心审核联调已接通
- 风格统一到与前台一致的卡片布局语言

## 6. 联调配置

### 6.1 环境变量
前端通过 `.env.development` 和 `.env.production` 管理后端地址。

### 6.2 Axios 封装
统一入口：[`frontend-app/src/api/request.ts`](/E:/GraduationProject/GraduationProject/frontend-app/src/api/request.ts)

能力包括：
- 自动注入 token
- 统一响应解包
- 登录失效处理
- 统一错误提示

### 6.3 路由守卫
路由守卫位于：[`frontend-app/src/router/guard.ts`](/E:/GraduationProject/GraduationProject/frontend-app/src/router/guard.ts)

当前已实现：
- 未登录拦截受保护页面
- 已登录访问登录 / 注册页时重定向
- 创作者权限校验
- 管理员权限校验

## 7. 当前前端与后端的关键联调结论

已联调通过的主要场景：
- 登录 / 注册 / 当前用户
- 首页视频流 / 推荐流 / 热门榜
- 视频详情 / 评论 / 弹幕 / 相关推荐 / 视频播放字段获取
- 用户主页 / 投稿列表 / 关注关系
- 收藏夹 / 消息中心 / 私信会话
- 创作者投稿、草稿、合集
- 后台审核与举报处理

## 8. 前端后续升级建议

- 将视频真实上传和真实播放地址接入 MinIO
- 补搜索结果真实搜索逻辑
- 补后台统计与分类标签完整操作页
- 为推荐列表加入曝光上报与点击上报的显式前端埋点
- 根据正式业务决定是否做移动端适配