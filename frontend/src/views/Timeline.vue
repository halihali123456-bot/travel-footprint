<template>
  <div class="timeline-page page-container">
    <div class="timeline-header">
      <h1 class="page-title">我的旅行时间线</h1>
      <p class="page-subtitle">回顾每一段旅程，见证成长的足迹</p>
    </div>
    
    <div class="timeline-content" v-if="stories.length > 0">
      <el-timeline>
        <el-timeline-item
          v-for="story in stories"
          :key="story.id"
          :timestamp="formatDate(story.travelDate)"
          placement="top"
          :type="getTimelineType(story)"
        >
          <div class="timeline-card" @click="viewStory(story.id)">
            <div class="timeline-image" v-if="story.images && story.images.length > 0">
              <el-image 
                :src="story.images[0].imageUrl" 
                fit="cover"
                class="story-thumb"
              />
            </div>
            <div class="timeline-info">
              <h3 class="timeline-title">{{ story.title }}</h3>
              <p class="timeline-location" v-if="story.location">
                <el-icon><Location /></el-icon>{{ story.location }}
              </p>
              <p class="timeline-desc text-ellipsis-2">{{ story.content }}</p>
              <div class="timeline-stats">
                <span><el-icon><View /></el-icon>{{ story.viewCount }}</span>
                <span><el-icon><Star /></el-icon>{{ story.likeCount }}</span>
              </div>
            </div>
          </div>
        </el-timeline-item>
      </el-timeline>
    </div>
    
    <!-- 空状态 -->
    <el-empty v-else description="还没有旅行故事，快去创建第一个吧！">
      <el-button type="primary" @click="$router.push('/create')">创建故事</el-button>
    </el-empty>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getTimeline } from '../api/story'
import { ElMessage } from 'element-plus'

export default {
  name: 'Timeline',
  setup() {
    const router = useRouter()
    const stories = ref([])
    const loading = ref(false)
    
    const fetchTimeline = async () => {
      loading.value = true
      try {
        const res = await getTimeline()
        stories.value = res.data || []
      } catch (error) {
        console.error('获取时间线失败:', error)
        ElMessage.error('获取时间线失败')
      } finally {
        loading.value = false
      }
    }
    
    const viewStory = (id) => {
      router.push(`/story/${id}`)
    }
    
    const formatDate = (date) => {
      if (!date) return '未知日期'
      const d = new Date(date)
      return `${d.getFullYear()}年${d.getMonth() + 1}月${d.getDate()}日`
    }
    
    const getTimelineType = (story) => {
      // 根据月份返回不同的颜色
      const month = new Date(story.travelDate).getMonth()
      const types = ['primary', 'success', 'warning', 'danger']
      return types[month % 4]
    }
    
    onMounted(() => {
      fetchTimeline()
    })
    
    return {
      stories,
      loading,
      viewStory,
      formatDate,
      getTimelineType
    }
  }
}
</script>

<style scoped>
.timeline-page {
  padding-top: 30px;
  padding-bottom: 50px;
}

.timeline-header {
  text-align: center;
  margin-bottom: 40px;
}

.page-title {
  font-size: 28px;
  font-weight: bold;
  color: #333;
  margin-bottom: 10px;
}

.page-subtitle {
  color: #909399;
  font-size: 16px;
}

.timeline-content {
  max-width: 800px;
  margin: 0 auto;
}

.timeline-card {
  background: #fff;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  transition: transform 0.3s, box-shadow 0.3s;
  display: flex;
}

.timeline-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
}

.timeline-image {
  width: 150px;
  min-height: 120px;
  flex-shrink: 0;
}

.story-thumb {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.timeline-info {
  flex: 1;
  padding: 15px 20px;
}

.timeline-title {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin-bottom: 8px;
}

.timeline-location {
  font-size: 13px;
  color: #409eff;
  display: flex;
  align-items: center;
  gap: 4px;
  margin-bottom: 8px;
}

.timeline-desc {
  font-size: 14px;
  color: #666;
  line-height: 1.5;
  margin-bottom: 10px;
  height: 42px;
}

.timeline-stats {
  display: flex;
  gap: 15px;
  font-size: 13px;
  color: #909399;
}

.timeline-stats span {
  display: flex;
  align-items: center;
  gap: 4px;
}

:deep(.el-timeline-item__node) {
  width: 14px;
  height: 14px;
}

:deep(.el-timeline-item__timestamp) {
  font-size: 14px;
  color: #666;
  font-weight: 500;
}

@media (max-width: 768px) {
  .timeline-card {
    flex-direction: column;
  }
  
  .timeline-image {
    width: 100%;
    height: 150px;
  }
}
</style>
