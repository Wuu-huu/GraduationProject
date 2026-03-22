# frontend-app

毕业设计项目前端工程，基于 `Vue 3 + TypeScript + Vite + Pinia + Element Plus`。

## 1. 目录说明

```text
src/
  api/          接口封装
  assets/       静态资源
  components/   通用组件与业务组件
  layouts/      主站 / 创作者 / 后台布局
  router/       路由与守卫
  stores/       Pinia 状态管理
  styles/       全局样式
  types/        类型定义
  utils/        工具函数
  views/        页面视图
```

## 2. 启动方式

```bash
npm install
npm run dev
```

## 3. 当前已实现页面

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
- 设置页

### 创作者中心
- 创作者首页
- 投稿管理
- 投稿页
- 合集管理

### 管理后台
- 后台首页
- 用户管理
- 视频审核
- 评论审核
- 举报处理
- 分类标签管理

## 4. 联调说明

- 后端统一处理跨域
- 开发环境下通过 `.env.development` 配置后端地址
- token 通过 Axios 拦截器自动注入
- 路由守卫负责登录态和角色控制

## 5. 重点说明

- 首页、分区页、搜索页的布局参考 B 站信息结构，但保留当前项目更轻量的卡片风格
- 视频详情页已接入原生 `<video>` 播放器
- 如果后端返回的 `videoUrl` 不是可访问的真实资源地址，播放器将无法真实播放