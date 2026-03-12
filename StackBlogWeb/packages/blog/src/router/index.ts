import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    component: () => import('../components/layout/Layout.vue'),
    children: [
      {
        path: '',
        name: 'Home',
        component: () => import('../views/Home.vue'),
        meta: { title: '首页' }
      },
      {
        path: 'article/:id',
        name: 'ArticleDetail',
        component: () => import('../views/ArticleDetail.vue'),
        meta: { title: '文章详情' }
      },
      {
        path: 'archive',
        name: 'Archive',
        component: () => import('../views/Archive.vue'),
        meta: { title: '归档' }
      },
      {
        path: 'category',
        name: 'Category',
        component: () => import('../views/Category.vue'),
        meta: { title: '分类' }
      },
      {
        path: 'category/:id',
        name: 'CategoryDetail',
        component: () => import('../views/CategoryDetail.vue'),
        meta: { title: '分类详情' }
      },
      {
        path: 'tag',
        name: 'Tag',
        component: () => import('../views/Tag.vue'),
        meta: { title: '标签' }
      },
      {
        path: 'tag/:id',
        name: 'TagDetail',
        component: () => import('../views/TagDetail.vue'),
        meta: { title: '标签详情' }
      },
      {
        path: 'about',
        name: 'About',
        component: () => import('../views/About.vue'),
        meta: { title: '关于' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition) {
      return savedPosition
    } else {
      return { top: 0 }
    }
  }
})

// 路由守卫 - 设置页面标题
router.beforeEach((to) => {
  document.title = to.meta.title ? `${to.meta.title} - StackBlog` : 'StackBlog'
})

export default router
