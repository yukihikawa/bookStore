import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { apiBase } from '@/api/http'

/**
 * 路由 base：
 * - 开发模式：'/'（vite 在 http://localhost:5173/ 下直接提供 SPA）
 * - 生产模式：<contextPath>/app/（静态文件在 webapp/app/）
 *
 * apiBase() 返回的是不带尾斜杠的 contextPath（如 /bookStore，根 context 时为空串），
 * 必须自己补 '/'：漏掉会拼成 /bookStoreapp/，路由跳到页面上不存在的路径，
 * 并让 apiBase() 反过来失配变成空串（验证码就会请求裸 /kaptcha.jpg 而 404）。
 */
const routerBase = import.meta.env.DEV ? '/' : `${apiBase()}/app/`

const router = createRouter({
  history: createWebHistory(routerBase),
  routes: [
    { path: '/', redirect: '/books' },

    // C 端
    {
      path: '/books',
      name: 'books',
      component: () => import('@/views/BooksView.vue'),
      meta: { title: '书城首页' },
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/LoginView.vue'),
      meta: { title: '登录', guest: true },
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('@/views/RegisterView.vue'),
      meta: { title: '注册', guest: true },
    },
    {
      path: '/cart',
      name: 'cart',
      component: () => import('@/views/CartView.vue'),
      meta: { title: '购物车' },
    },
    {
      path: '/orders',
      name: 'my-orders',
      component: () => import('@/views/MyOrdersView.vue'),
      meta: { title: '我的订单', requiresAuth: true },
    },
    {
      path: '/orders/:orderId',
      name: 'my-order-detail',
      component: () => import('@/views/MyOrderDetail.vue'),
      meta: { title: '订单详情', requiresAuth: true },
    },

    // B 端
    {
      path: '/admin',
      component: () => import('@/views/admin/AdminLayout.vue'),
      meta: { title: '后台管理', requiresAdmin: true },
      children: [
        { path: '', redirect: '/admin/books' },
        {
          path: 'books',
          name: 'admin-books',
          component: () => import('@/views/admin/BooksView.vue'),
          meta: { title: '书籍管理', requiresAdmin: true },
        },
        {
          path: 'orders',
          name: 'admin-orders',
          component: () => import('@/views/admin/OrdersView.vue'),
          meta: { title: '订单管理', requiresAdmin: true },
        },
      ],
    },

    {
      path: '/:pathMatch(.*)*',
      name: 'not-found',
      component: () => import('@/views/NotFoundView.vue'),
      meta: { title: '页面不存在' },
    },
  ],
})

/** 路由守卫：先尽力恢复会话，再按 meta 做准入控制 */
router.beforeEach(async (to) => {
  const auth = useAuthStore()
  // 只要本地有 token（或已恢复过），就保证与后端会话一致
  if (!auth.fetched) {
    if (auth.token) {
      await auth.fetchMe()
    } else {
      auth.fetched = true
    }
  }

  if (to.meta.requiresAuth && !auth.user) {
    return { path: '/login', query: { redirect: to.fullPath } }
  }
  if (to.meta.requiresAdmin) {
    if (!auth.user) {
      return { path: '/login', query: { redirect: to.fullPath } }
    }
    if (!auth.user.admin) {
      return { path: '/books', query: { forbidden: '1' } }
    }
  }
  // 已登录用户访问登录/注册页时，引导回首页
  if (to.meta.guest && auth.user) {
    return { path: '/books' }
  }
})

export default router
