<template>
  <div class="stories-page page-container">
    <!-- 搜索栏 -->
    <div class="search-section">
      <el-input
        v-model="searchKeyword"
        placeholder="搜索目的地、故事标题..."
        size="large"
        clearable
        @keyup.enter="handleSearch"
      >
        <template #append>
          <el-button @click="handleSearch">
            <el-icon><Search /></el-icon>
          </el-button>
        </template>
      </el-input>
    </div>
    
    <!-- 故事列表 -->
    <div class="stories-section">
      <div class="section-header">
        <h2>{{ searchMode ? `搜索结果: "${searchKeyword}"` : '探索故事' }}</h2>
        <el-button v-if="searchMode" text @click="clearSearch">清除搜索</el-button>
      </div>
      
      <el-row :gutter="20">
        <el-col :xs="24" :sm="12" :md="8" v-for="story in stories" :key="story.id">
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
              <div class="story-date" v-if="story.travelDate">
                <el-icon><Calendar /></el-icon>{{ formatDate(story.travelDate) }}
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
      
      <!-- 空状态 -->
      <el-empty v-if="stories.length === 0 && !loading" description="暂无故事" />
      
      <!-- 加载更多 -->
      <div class="load-more" v-if="stories.length > 0 && hasMore">
        <el-button :loading="loading" @click="loadMore">加载更多</el-button>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getPublicStories, searchStories } from '../api/story'

export default {
  name: 'Stories',
  setup() {
    const router = useRouter()
    const stories = ref([])
    const loading = ref(false)
    const searchKeyword = ref('')
    const searchMode = ref(false)
    const currentPage = ref(1)
    const pageSize = ref(12)
    const hasMore = ref(true)
    const defaultCover = 'https://via.placeholder.com/400x300?text=No+Image'
    
    const fetchStories = async () => {
      if (loading.value) return
      loading.value = true
      
      try {
        const res = await getPublicStories(currentPage.value, pageSize.value)
        const newStories = res.data.records || []
        
        if (currentPage.value === 1) {
          stories.value = newStories
        } else {
          stories.value.push(...newStories)
        }
        
        hasMore.value = newStories.length === pageSize.value
      } catch (error) {
        console.error('获取故事列表失败:', error)
      } finally {
        loading.value = false
      }
    }
    
    const handleSearch = async () => {
      if (!searchKeyword.value.trim()) {
        clearSearch()
        return
      }
      
      searchMode.value = true
      currentPage.value = 1
      loading.value = true
      
      try {
        const res = await searchStories(searchKeyword.value, currentPage.value, pageSize.value)
        stories.value = res.data.records || []
        hasMore.value = stories.value.length === pageSize.value
      } catch (error) {
        console.error('搜索失败:', error)
      } finally {
        loading.value = false
      }
    }
    
    const clearSearch = () => {
      searchKeyword.value = ''
      searchMode.value = false
      currentPage.value = 1
      fetchStories()
    }
    
    const loadMore = () => {
      currentPage.value++
      if (searchMode.value) {
        handleSearch()
      } else {
        fetchStories()
      }
    }
    
    const viewStory = (id) => {
      router.push(`/story/${id}`)
    }
    
    const formatDate = (date) => {
      if (!date) return ''
      return new Date(date).toLocaleDateString('zh-CN')
    }
    
    onMounted(() => {
      fetchStories()
    })
    
    return {
      stories,
      loading,
      searchKeyword,
      searchMode,
      hasMore,
      defaultCover,
      handleSearch,
      clearSearch,
      loadMore,
      viewStory,
      formatDate
    }
  }
}
</script>

<style scoped>
.stories-page {
  padding-top: 30px;
  padding-bottom: 50px;
}

.search-section {
  max-width: 600px;
  margin: 0 auto 30px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.section-header h2 {
  font-size: 20px;
  color: #333;
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
  margin-bottom: 8px;
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

.story-date {
  font-size: 12px;
  color: #c0c4cc;
  display: flex;
  align-items: center;
  gap: 4px;
}

.load-more {
  text-align: center;
  margin-top: 30px;
}
</style>
