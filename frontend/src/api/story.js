import request from '../utils/request'

// 创建故事
export function createStory(data) {
  return request({
    url: '/story',
    method: 'post',
    data
  })
}

// 更新故事
export function updateStory(id, data) {
  return request({
    url: `/story/${id}`,
    method: 'put',
    data
  })
}

// 删除故事
export function deleteStory(id) {
  return request({
    url: `/story/${id}`,
    method: 'delete'
  })
}

// 获取故事详情
export function getStoryById(id) {
  return request({
    url: `/story/${id}`,
    method: 'get'
  })
}

// 获取公开故事列表
export function getPublicStories(page = 1, size = 10) {
  return request({
    url: '/story/public',
    method: 'get',
    params: { page, size }
  })
}

// 获取我的故事列表
export function getMyStories(page = 1, size = 10) {
  return request({
    url: '/story/my',
    method: 'get',
    params: { page, size }
  })
}

// 获取用户故事列表
export function getUserStories(userId, page = 1, size = 10) {
  return request({
    url: `/story/user/${userId}`,
    method: 'get',
    params: { page, size }
  })
}

// 搜索故事
export function searchStories(keyword, page = 1, size = 10) {
  return request({
    url: '/story/search',
    method: 'get',
    params: { keyword, page, size }
  })
}

// 获取热门故事
export function getHotStories(limit = 6) {
  return request({
    url: '/story/hot',
    method: 'get',
    params: { limit }
  })
}

// 获取时间线
export function getTimeline() {
  return request({
    url: '/story/timeline',
    method: 'get'
  })
}

// 点赞故事
export function likeStory(id) {
  return request({
    url: `/story/${id}/like`,
    method: 'post'
  })
}

// 取消点赞
export function unlikeStory(id) {
  return request({
    url: `/story/${id}/like`,
    method: 'delete'
  })
}
