<template>
  <div class="my-stories-page page-container">
    <div class="page-header">
      <h1 class="page-title">我的故事</h1>
      <el-button type="primary" @click="$router.push('/create')">
        <el-icon><Plus /></el-icon>创建新故事
      </el-button>
    </div>
    
    <!-- 故事列表 -->
    <div class="stories-list" v-if="stories.length > 0">
      <div class="story-item" v-for="story in stories" :key="story.id">
        <div class="story-cover" @click="viewStory(story.id)">
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
          <div class="privacy-badge" :class="story.isPublic ? 'public' : 'private'">
            {{ story.isPublic ? '公开' : '私密' }}
          </div>
        </div>
        <div class="story-info">
          <h3 class="story-title" @click="viewStory(story.id)">{{ story.title }}</h3>
          <p class="story-content text-ellipsis-2">{{ story.content }}</p>
          <div class="story-meta">
            <span class="story-date">
              <el-icon><Calendar /></el-icon>{{ formatDate(story.travelDate || story.createTime) }}
            </span>
            <span class="story-location" v-if="story.location">
              <el-icon><Location /></el-icon>{{ story.location }}
            </span>
          </div>
          <div class="story-stats">
            <span><el-icon><View /></el-icon>{{ story.viewCount }}</span>
            <span><el-icon><Star /></el-icon>{{ story.likeCount }}</span>
          </div>
        </div>
        <div class="story-actions">
          <el-button type="primary" text @click="editStory(story.id)">
            <el-icon><EditPen /></el-icon>编辑
          </el-button>
          <el-button type="danger" text @click="deleteStory(story)">
            <el-icon><Delete /></el-icon>删除
          </el-button>
        </div>
      </div>
      
      <!-- 加载更多 -->
      <div class="load-more" v-if="hasMore">
        <el-button :loading="loading" @click="loadMore">加载更多</el-button>
      </div>
    </div>
    
    <!-- 空状态 -->
    <el-empty v-else description="还没有创建任何故事">
      <el-button type="primary" @click="$router.push('/create')">创建第一个故事</el-button>
    </el-empty>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getMyStories, deleteStory as apiDeleteStory } from '../api/story'

export default {
  name: 'MyStories',
  setup() {
    const router = useRouter()
    const stories = ref([])
    const loading = ref(false)
    const currentPage = ref(1)
    const pageSize = ref(10)
    const hasMore = ref(true)
    const defaultCover = 'https://via.placeholder.com/200x150?text=No+Image'
    
    const fetchStories = async () => {
      if (loading.value) return
      loading.value = true
      
      try {
        const res = await getMyStories(currentPage.value, pageSize.value)
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
    
    const loadMore = () => {
      currentPage.value++
      fetchStories()
    }
    
    const viewStory = (id) => {
      router.push(`/story/${id}`)
    }
    
    const editStory = (id) => {
      router.push(`/edit/${id}`)
    }
    
    const deleteStory = (story) => {
      ElMessageBox.confirm(`确定要删除故事 "${story.title}" 吗？此操作不可恢复。`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await apiDeleteStory(story.id)
          ElMessage.success('删除成功')
          stories.value = stories.value.filter(s => s.id !== story.id)
        } catch (error) {
          console.error('删除失败:', error)
        }
      })
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
      hasMore,
      defaultCover,
      loadMore,
      viewStory,
      editStory,
      deleteStory,
      formatDate
    }
  }
}
</script>

<style scoped>
.my-stories-page {
  padding-top: 30px;
  padding-bottom: 50px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 25px;
}

.page-title {
  font-size: 24px;
  font-weight: bold;
  color: #333;
}

.story-item {
  background: #fff;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  display: flex;
  margin-bottom: 20px;
  transition: box-shadow 0.3s;
}

.story-item:hover {
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
}

.story-cover {
  position: relative;
  width: 200px;
  min-height: 150px;
  flex-shrink: 0;
  cursor: pointer;
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
  font-size: 32px;
}

.privacy-badge {
  position: absolute;
  top: 10px;
  right: 10px;
  padding: 3px 10px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
}

.privacy-badge.public {
  background: #67c23a;
  color: #fff;
}

.privacy-badge.private {
  background: #909399;
  color: #fff;
}

.story-info {
  flex: 1;
  padding: 15px 20px;
}

.story-title {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin-bottom: 8px;
  cursor: pointer;
}

.story-title:hover {
  color: #409eff;
}

.story-content {
  font-size: 14px;
  color: #666;
  line-height: 1.5;
  margin-bottom: 10px;
  height: 42px;
}

.story-meta {
  display: flex;
  gap: 15px;
  font-size: 12px;
  color: #909399;
  margin-bottom: 8px;
}

.story-meta span {
  display: flex;
  align-items: center;
  gap: 4px;
}

.story-stats {
  display: flex;
  gap: 15px;
  font-size: 13px;
  color: #909399;
}

.story-stats span {
  display: flex;
  align-items: center;
  gap: 4px;
}

.story-actions {
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: 15px;
  border-left: 1px solid #e4e7ed;
  gap: 10px;
}

.load-more {
  text-align: center;
  margin-top: 30px;
}

@media (max-width: 768px) {
  .story-item {
    flex-direction: column;
  }
  
  .story-cover {
    width: 100%;
    height: 180px;
  }
  
  .story-actions {
    flex-direction: row;
    border-left: none;
    border-top: 1px solid #e4e7ed;
    justify-content: flex-end;
  }
}
</style>
