import { createRouter, createWebHistory } from 'vue-router'
import { ElMessage } from 'element-plus'
import store from '../store'

const routes = [
  {
    path: '/',
    name: 'Layout',
    component: () => import('../views/Layout.vue'),
    redirect: '/home',
    children: [
      {
        path: '/home',
        name: 'Home',
        component: () => import('../views/Home.vue'),
        meta: { title: '首页' }
      },
      {
        path: '/stories',
        name: 'Stories',
        component: () => import('../views/Stories.vue'),
        meta: { title: '探索故事' }
      },
      {
        path: '/story/:id',
        name: 'StoryDetail',
        component: () => import('../views/StoryDetail.vue'),
        meta: { title: '故事详情' }
      },
      {
        path: '/timeline',
        name: 'Timeline',
        component: () => import('../views/Timeline.vue'),
        meta: { title: '时间线', requireAuth: true }
      },
      {
        path: '/create',
        name: 'CreateStory',
        component: () => import('../views/CreateStory.vue'),
        meta: { title: '创建故事', requireAuth: true }
      },
      {
        path: '/edit/:id',
        name: 'EditStory',
        component: () => import('../views/CreateStory.vue'),
        meta: { title: '编辑故事', requireAuth: true }
      },
      {
        path: '/profile',
        name: 'Profile',
        component: () => import('../views/Profile.vue'),
        meta: { title: '个人中心', requireAuth: true }
      },
      {
        path: '/my-stories',
        name: 'MyStories',
        component: () => import('../views/MyStories.vue'),
        meta: { title: '我的故事', requireAuth: true }
      }
    ]
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue'),
    meta: { title: '注册' }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  // 设置页面标题
  if (to.meta.title) {
    document.title = to.meta.title + ' - 旅行足迹'
  }
  
  // 检查是否需要登录
  if (to.meta.requireAuth && !store.getters.isLoggedIn) {
    ElMessage.warning('请先登录')
    next('/login')
  } else {
    next()
  }
})

export default router
