<template>
  <div class="home">
    <!-- Hero区域 -->
    <div class="hero-section">
      <div class="hero-content">
        <h1 class="hero-title">记录你的每一次旅程</h1>
        <p class="hero-subtitle">用文字和照片，留住旅途中的美好瞬间</p>
        <div class="hero-actions">
          <el-button type="primary" size="large" @click="$router.push('/stories')">
            <el-icon><View /></el-icon>探索故事
          </el-button>
          <el-button size="large" @click="$router.push('/create')" v-if="isLoggedIn">
            <el-icon><EditPen /></el-icon>写故事
          </el-button>
          <el-button size="large" @click="$router.push('/login')" v-else>
            <el-icon><User /></el-icon>登录开始
          </el-button>
        </div>
      </div>
      <div class="hero-stats">
        <div class="stat-item">
          <div class="stat-number">{{ stats.stories }}</div>
          <div class="stat-label">旅行故事</div>
        </div>
        <div class="stat-item">
          <div class="stat-number">{{ stats.users }}</div>
          <div class="stat-label">旅行者</div>
        </div>
        <div class="stat-item">
          <div class="stat-number">{{ stats.places }}</div>
          <div class="stat-label">目的地</div>
        </div>
      </div>
    </div>
    
    <!-- 热门故事 -->
    <div class="section hot-stories">
      <div class="section-header">
        <h2 class="section-title">
          <el-icon><TrendCharts /></el-icon>热门故事
        </h2>
        <el-button text @click="$router.push('/stories')">查看更多<el-icon><ArrowRight /></el-icon></el-button>
      </div>
      
      <el-row :gutter="20">
        <el-col :xs="24" :sm="12" :md="8" v-for="story in hotStories" :key="story.id">
          <div class="story-card" @click="viewStory(story.id)">
            <div class="story-cover">
              <el-image 
                :src="story.coverImage || story.images?.[0]?.imageUrl || defaultCover" 
                fit="cover"
                class="cover-image"
              >
                <template #error>
                  <div class="image-placeholder">
                    <el-icon><Picture /></el-icon>
                  </div>
                </template>
              </el-image>
              <div class="story-location" v-if="story.location">
                <el-icon><Location /></el-icon>{{ story.location }}
              </div>
            </div>
            <div class="story-info">
              <h3 class="story-title">{{ story.title }}</h3>
              <p class="story-content text-ellipsis-2">{{ story.content }}</p>
              <div class="story-meta">
                <div class="author">
                  <el-avatar :size="24" :src="story.userAvatar" :icon="UserFilled" />
                  <span>{{ story.username }}</span>
                </div>
                <div class="stats">
                  <span><el-icon><View /></el-icon>{{ story.viewCount }}</span>
                  <span><el-icon><Star /></el-icon>{{ story.likeCount }}</span>
                </div>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
    
    <!-- 功能特色 -->
    <div class="section features">
      <h2 class="section-title text-center">功能特色</h2>
      <el-row :gutter="30">
        <el-col :xs="24" :sm="12" :md="8">
          <div class="feature-card">
            <div class="feature-icon">
              <el-icon><EditPen /></el-icon>
            </div>
            <h3>记录故事</h3>
            <p>用文字和照片记录旅途中的点点滴滴，保存珍贵回忆</p>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :md="8">
          <div class="feature-card">
            <div class="feature-icon">
              <el-icon><Timer /></el-icon>
            </div>
            <h3>时间线视图</h3>
            <p>按时间顺序回顾你的旅行历程，见证成长的足迹</p>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :md="8">
          <div class="feature-card">
            <div class="feature-icon">
              <el-icon><Share /></el-icon>
            </div>
            <h3>分享交流</h3>
            <p>与志同道合的旅行者分享故事，发现更多精彩目的地</p>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'
import { getHotStories } from '../api/story'
import { ElMessage } from 'element-plus'

export default {
  name: 'Home',
  setup() {
    const store = useStore()
    const router = useRouter()
    
    const isLoggedIn = computed(() => store.getters.isLoggedIn)
    const hotStories = ref([])
    const defaultCover = 'https://via.placeholder.com/400x300?text=No+Image'
    
    const stats = ref({
      stories: 1280,
      users: 356,
      places: 89
    })
    
    const fetchHotStories = async () => {
      try {
        const res = await getHotStories(6)
        hotStories.value = res.data || []
      } catch (error) {
        console.error('获取热门故事失败:', error)
      }
    }
    
    const viewStory = (id) => {
      router.push(`/story/${id}`)
    }
    
    onMounted(() => {
      fetchHotStories()
    })
    
    return {
      isLoggedIn,
      hotStories,
      defaultCover,
      stats,
      viewStory
    }
  }
}
</script>

<style scoped>
.home {
  padding-bottom: 40px;
}

.hero-section {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  padding: 80px 20px;
  text-align: center;
}

.hero-content {
  max-width: 800px;
  margin: 0 auto;
}

.hero-title {
  font-size: 48px;
  font-weight: bold;
  margin-bottom: 20px;
}

.hero-subtitle {
  font-size: 20px;
  opacity: 0.9;
  margin-bottom: 30px;
}

.hero-actions {
  display: flex;
  gap: 15px;
  justify-content: center;
}

.hero-stats {
  display: flex;
  justify-content: center;
  gap: 60px;
  margin-top: 50px;
}

.stat-item {
  text-align: center;
}

.stat-number {
  font-size: 36px;
  font-weight: bold;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 14px;
  opacity: 0.8;
}

.section {
  max-width: 1200px;
  margin: 0 auto;
  padding: 50px 20px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 25px;
}

.section-title {
  font-size: 24px;
  font-weight: bold;
  color: #333;
  display: flex;
  align-items: center;
  gap: 8px;
}

.text-center {
  text-align: center;
  justify-content: center;
}

.hot-stories {
  background: #fff;
  margin-top: -30px;
  border-radius: 20px 20px 0 0;
  position: relative;
  z-index: 1;
}

.story-card {
  background: #fff;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  transition: transform 0.3s, box-shadow 0.3s;
  margin-bottom: 20px;
}

.story-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
}

.story-cover {
  position: relative;
  height: 200px;
  overflow: hidden;
}

.cover-image {
  width: 100%;
  height: 100%;
}

.image-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f7fa;
  color: #909399;
  font-size: 40px;
}

.story-location {
  position: absolute;
  bottom: 10px;
  left: 10px;
  background: rgba(0, 0, 0, 0.6);
  color: #fff;
  padding: 5px 10px;
  border-radius: 15px;
  font-size: 12px;
  display: flex;
  align-items: center;
  gap: 5px;
}

.story-info {
  padding: 15px;
}

.story-title {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin-bottom: 8px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.story-content {
  font-size: 14px;
  color: #666;
  margin-bottom: 12px;
  line-height: 1.5;
  height: 42px;
}

.story-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 13px;
  color: #909399;
}

.author {
  display: flex;
  align-items: center;
  gap: 8px;
}

.stats {
  display: flex;
  gap: 15px;
}

.stats span {
  display: flex;
  align-items: center;
  gap: 4px;
}

.features {
  background: #f5f7fa;
}

.feature-card {
  background: #fff;
  padding: 40px 30px;
  border-radius: 12px;
  text-align: center;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
  transition: transform 0.3s;
  margin-bottom: 20px;
}

.feature-card:hover {
  transform: translateY(-5px);
}

.feature-icon {
  width: 70px;
  height: 70px;
  background: linear-gradient(135deg, #409eff 0%, #67c23a 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 20px;
}

.feature-icon .el-icon {
  font-size: 32px;
  color: #fff;
}

.feature-card h3 {
  font-size: 18px;
  color: #333;
  margin-bottom: 10px;
}

.feature-card p {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
}

@media (max-width: 768px) {
  .hero-title {
    font-size: 32px;
  }
  
  .hero-stats {
    gap: 30px;
  }
  
  .stat-number {
    font-size: 28px;
  }
}
</style>
