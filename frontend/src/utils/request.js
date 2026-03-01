import axios from 'axios'
import { ElMessage } from 'element-plus'
import store from '../store'
import router from '../router'

// 创建axios实例
const request = axios.create({
  baseURL: process.env.NODE_ENV === 'production' ? '/api' : '/api',
  timeout: 30000
})

// 请求拦截器
request.interceptors.request.use(
  config => {
    // 添加token到请求头
    const token = store.getters.token
    if (token) {
      config.headers['Authorization'] = 'Bearer ' + token
    }
    return config
  },
  error => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  response => {
    const res = response.data
    
    // 如果返回的状态码不是200，说明接口有问题
    if (res.code !== 200) {
      ElMessage.error(res.message || '请求失败')
      
      // 401: 未登录或token过期
      if (res.code === 401) {
        store.dispatch('logout')
        router.push('/login')
      }
      
      return Promise.reject(new Error(res.message || '请求失败'))
    }
    
    return res
  },
  error => {
    console.error('响应错误:', error)
    const message = error.response?.data?.message || '网络错误，请稍后重试'
    ElMessage.error(message)
    
    // 401: 未登录或token过期
    if (error.response?.status === 401) {
      store.dispatch('logout')
      router.push('/login')
    }
    
    return Promise.reject(error)
  }
)

export default request
