import type { RouteRecordRaw } from 'vue-router'

export const routes: RouteRecordRaw[] = [
  {
    path: '/',
    component: () => import('@/layouts/MainLayout.vue'),
    meta: { title: '首页', layout: 'main' },
    children: [
      {
        path: '',
        name: 'home',
        component: () => import('@/views/home/HomeView.vue'),
        meta: { title: '首页' }
      },
      {
        path: 'videos/:videoId',
        name: 'video-detail',
        component: () => import('@/views/video/VideoDetailView.vue'),
        meta: { title: '视频详情' }
      },
      {
        path: 'zones/:zoneId',
        name: 'category',
        component: () => import('@/views/category/CategoryView.vue'),
        meta: { title: '分区页' }
      },
      {
        path: 'search',
        name: 'search',
        component: () => import('@/views/search/SearchResultView.vue'),
        meta: { title: '搜索' }
      },
      {
        path: 'users/:uid',
        name: 'user-profile',
        component: () => import('@/views/user/UserProfileView.vue'),
        meta: { title: '用户主页' }
      },
      {
        path: 'favorites',
        name: 'favorite',
        component: () => import('@/views/favorite/FavoriteView.vue'),
        meta: { title: '收藏夹', requiresAuth: true }
      },
      {
        path: 'messages',
        name: 'message-center',
        component: () => import('@/views/message/MessageCenterView.vue'),
        meta: { title: '消息中心', requiresAuth: true }
      },
      {
        path: 'messages/conversations/:conversationId',
        name: 'conversation',
        component: () => import('@/views/message/ConversationView.vue'),
        meta: { title: '私信会话', requiresAuth: true }
      },
      {
        path: 'settings',
        name: 'settings',
        component: () => import('@/views/user/UserSettingsView.vue'),
        meta: { title: '个人设置', requiresAuth: true }
      }
    ]
  },
  {
    path: '/',
    component: () => import('@/layouts/BlankLayout.vue'),
    children: [
      {
        path: 'login',
        name: 'login',
        component: () => import('@/views/auth/LoginView.vue'),
        meta: { title: '登录', layout: 'blank' }
      },
      {
        path: 'register',
        name: 'register',
        component: () => import('@/views/auth/RegisterView.vue'),
        meta: { title: '注册', layout: 'blank' }
      }
    ]
  },
  {
    path: '/creator',
    component: () => import('@/layouts/CreatorLayout.vue'),
    meta: { title: '创作中心', requiresAuth: true, roles: ['user', 'creator', 'admin'] },
    children: [
      {
        path: '',
        name: 'creator-dashboard',
        component: () => import('@/views/creator/CreatorDashboardView.vue'),
        meta: { title: '创作首页' }
      },
      {
        path: 'videos',
        name: 'creator-videos',
        component: () => import('@/views/creator/CreatorVideoManageView.vue'),
        meta: { title: '投稿管理' }
      },
      {
        path: 'upload',
        name: 'creator-upload',
        component: () => import('@/views/creator/CreatorUploadView.vue'),
        meta: { title: '视频投稿' }
      },
      {
        path: 'series',
        name: 'creator-series',
        component: () => import('@/views/creator/CreatorSeriesView.vue'),
        meta: { title: '合集管理' }
      }
    ]
  },
  {
    path: '/admin',
    component: () => import('@/layouts/AdminLayout.vue'),
    meta: { title: '管理后台', requiresAuth: true, roles: ['admin'] },
    children: [
      {
        path: '',
        name: 'admin-dashboard',
        component: () => import('@/views/admin/AdminDashboardView.vue'),
        meta: { title: '后台首页' }
      },
      {
        path: 'users',
        name: 'admin-users',
        component: () => import('@/views/admin/AdminUserManageView.vue'),
        meta: { title: '用户管理' }
      },
      {
        path: 'audit/videos',
        name: 'admin-audit-videos',
        component: () => import('@/views/admin/AdminVideoAuditView.vue'),
        meta: { title: '视频审核' }
      },
      {
        path: 'audit/comments',
        name: 'admin-audit-comments',
        component: () => import('@/views/admin/AdminCommentManageView.vue'),
        meta: { title: '评论审核' }
      },
      {
        path: 'reports',
        name: 'admin-reports',
        component: () => import('@/views/admin/AdminReportView.vue'),
        meta: { title: '举报处理' }
      },
      {
        path: 'categories',
        name: 'admin-categories',
        component: () => import('@/views/admin/AdminCategoryManageView.vue'),
        meta: { title: '分类标签管理' }
      }
    ]
  }
]