import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'

// 路由配置
const routes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { title: '登录', requiresAuth: false }
  },
  {
    path: '/',
    component: () => import('@/components/layout/Layout.vue'),
    redirect: '/dashboard',
    meta: { requiresAuth: true },
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/Dashboard.vue'),
        meta: { title: '仪表盘', icon: 'DataAnalysis' }
      },
      {
        path: 'article',
        name: 'Article',
        redirect: '/article/list',
        meta: { title: '文章管理', icon: 'Document' },
        children: [
          {
            path: 'list',
            name: 'ArticleList',
            component: () => import('@/views/article/List.vue'),
            meta: { title: '文章列表' }
          },
          {
            path: 'create',
            name: 'ArticleCreate',
            component: () => import('@/views/article/Edit.vue'),
            meta: { title: '创建文章' }
          },
          {
            path: 'edit/:id',
            name: 'ArticleEdit',
            component: () => import('@/views/article/Edit.vue'),
            meta: { title: '编辑文章', hidden: true }
          }
        ]
      },
      {
        path: 'comment',
        name: 'Comment',
        component: () => import('@/views/comment/CommentList.vue'),
        meta: { title: '评论管理', icon: 'ChatDotRound' }
      },
      {
        path: 'category',
        name: 'Category',
        component: () => import('@/views/category/CategoryList.vue'),
        meta: { title: '分类管理', icon: 'FolderOpened' }
      },
      {
        path: 'tag',
        name: 'Tag',
        component: () => import('@/views/tag/TagList.vue'),
        meta: { title: '标签管理', icon: 'CollectionTag' }
      },
      {
        path: 'user',
        name: 'User',
        component: () => import('@/views/user/List.vue'),
        meta: { title: '用户管理', icon: 'User', requiresAdmin: true }
      },
      {
        path: 'settings',
        name: 'Settings',
        component: () => import('@/views/settings/Index.vue'),
        meta: { title: '系统设置', icon: 'Setting' }
      }
    ]
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('@/views/NotFound.vue'),
    meta: { title: '404' }
  }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  const requiresAuth = to.meta.requiresAuth !== false
  const requiresAdmin = to.meta.requiresAdmin === true

  // 设置页面标题
  document.title = to.meta.title ? `${to.meta.title} - StackBlog 管理后台` : 'StackBlog 管理后台'

  // 需要登录但未登录
  if (requiresAuth && !userStore.isLoggedIn) {
    ElMessage.warning('请先登录')
    next({ name: 'Login', query: { redirect: to.fullPath } })
    return
  }

  // 需要管理员权限但不是管理员
  if (requiresAdmin && !userStore.isAdmin) {
    ElMessage.error('没有权限访问')
    next(false)
    return
  }

  // 已登录访问登录页，跳转到首页
  if (to.name === 'Login' && userStore.isLoggedIn) {
    next({ name: 'Dashboard' })
    return
  }

  next()
})

export default router
