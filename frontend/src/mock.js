// 模拟后端数据，不需要真实API
const mockData = {
  user: {
    token: 'demo-token',
    userInfo: {
      username: 'admin',
      nickname: '旅行者',
      avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=admin'
    }
  },
  stories: [
    {
      id: 1,
      title: '🌸 云南大理游记',
      content: '洱海的风，古城的雨，这是一个关于大理的美好回忆...',
      location: '云南大理',
      coverImage: 'https://picsum.photos/400/300?random=1',
      createTime: '2024-03-01',
      likes: 128,
      views: 1024
    },
    {
      id: 2,
      title: '🏯 北京故宫一日游',
      content: '红墙黄瓦，历史的厚重感扑面而来，仿佛穿越回了明清...',
      location: '北京',
      coverImage: 'https://picsum.photos/400/300?random=2',
      createTime: '2024-02-15',
      likes: 256,
      views: 2048
    },
    {
      id: 3,
      title: '🏔️ 西藏拉萨朝圣',
      content: '布达拉宫的震撼，高原的纯净，心灵的洗礼...',
      location: '西藏拉萨',
      coverImage: 'https://picsum.photos/400/300?random=3',
      createTime: '2024-01-20',
      likes: 88,
      views: 666
    }
  ]
}

// 拦截 API 请求
const originalFetch = window.fetch;
window.fetch = function(url, options) {
  console.log('Mock拦截:', url);
  
  if (url.includes('/api/user/login')) {
    return Promise.resolve({
      ok: true,
      json: () => Promise.resolve({ code: 200, data: mockData.user })
    });
  }
  
  if (url.includes('/api/story/list') || url.includes('/api/story/all')) {
    return Promise.resolve({
      ok: true,
      json: () => Promise.resolve({ code: 200, data: mockData.stories })
    });
  }
  
  if (url.includes('/api/story/detail')) {
    return Promise.resolve({
      ok: true,
      json: () => Promise.resolve({ 
        code: 200, 
        data: { ...mockData.stories[0], images: ['https://picsum.photos/800/600?random=1'] }
      })
    });
  }
  
  // 其他请求返回原方法
  return originalFetch(url, options);
};