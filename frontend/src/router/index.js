import { createRouter, createWebHashHistory } from 'vue-router'
import Layout from '@/views/Layout.vue'
import Home from '@/views/Home.vue'
import Login from '@/views/Login.vue'
import Register from '@/views/Register.vue'
import Stories from '@/views/Stories.vue'
import StoryDetail from '@/views/StoryDetail.vue'
import CreateStory from '@/views/CreateStory.vue'
import MyStories from '@/views/MyStories.vue'
import Timeline from '@/views/Timeline.vue'
import Profile from '@/views/Profile.vue'

const routes = [
  {
    path: '/',
    component: Layout,
    redirect: '/home',
    children: [
      {
        path: '/home',
        name: 'Home',
        component: Home
      },
      {
        path: '/stories',
        name: 'Stories',
        component: Stories
      },
      {
        path: '/story/:id',
        name: 'StoryDetail',
        component: StoryDetail
      },
      {
        path: '/create',
        name: 'CreateStory',
        component: CreateStory
      },
      {
        path: '/my-stories',
        name: 'MyStories',
        component: MyStories
      },
      {
        path: '/timeline',
        name: 'Timeline',
        component: Timeline
      },
      {
        path: '/profile',
        name: 'Profile',
        component: Profile
      }
    ]
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/register',
    name: 'Register',
    component: Register
  }
]

const router = createRouter({
  history: createWebHashHistory('/travel-footprint/'),
  routes
})

export default router
