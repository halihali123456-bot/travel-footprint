<template>
  <div class="story-detail-page page-container">
    <div v-if="story" class="story-content">
      <!-- 故事头部 -->
      <div class="story-header">
        <h1 class="story-title">{{ story.title }}</h1>
        <div class="story-meta">
          <div class="author-info">
            <el-avatar :size="40" :src="story.userAvatar" :icon="UserFilled" />
            <div class="author-detail">
              <div class="author-name">{{ story.username }}</div>
              <div class="publish-time">{{ formatDate(story.createTime) }}</div>
            </div>
          </div>
          <div class="story-stats">
            <span><el-icon><View /></el-icon>{{ story.viewCount }} 浏览</span>
            <span><el-icon><Star /></el-icon>{{ story.likeCount }} 点赞</span>
          </div>
        </div>
        <div class="story-location" v-if="story.location">
          <el-icon><Location /></el-icon>
          <span>{{ story.location }}</span>
          <span v-if="story.travelDate" class="travel-date">
            <el-icon><Calendar /></el-icon>{{ formatDate(story.travelDate) }}
          </span>
        </div>
      </div>
      
      <!-- 故事图片 -->
      <div class="story-gallery" v-if="story.images && story.images.length > 0">
        <el-carousel height="400px" :interval="5000" arrow="always">
          <el-carousel-item v-for="(image, index) in story.images" :key="index">
            <el-image 
              :src="image.imageUrl" 
              fit="contain"
              :preview-src-list="story.images.map(img => img.imageUrl)"
              class="gallery-image"
            />
            <div class="image-desc" v-if="image.description">{{ image.description }}</div>
          </el-carousel-item>
        </el-carousel>
      </div>
      
      <!-- 故事内容 -->
      <div class="story-body">
        <div class="story-text" v-html="formatContent(story.content)"></div>
      </div>
      
      <!-- 操作栏 -->
      <div class="story-actions">
        <el-button 
          :type="story.isLiked ? 'danger' : 'default'" 
          size="large"
          @click="toggleLike"
        >
          <el-icon><Star /></el-icon>
          {{ story.isLiked ? '已点赞' : '点赞' }}
        </el-button>
        <el-button size="large" @click="shareStory">
          <el-icon><Share /></el-icon>分享
        </el-button>
        <template v-if="isOwner">
          <el-button type="primary" size="large" @click="editStory">
            <el-icon><EditPen /></el-icon>编辑
          </el-button>
          <el-button type="danger" size="large" @click="deleteStory">
            <el-icon><Delete /></el-icon>删除
          </el-button>
        </template>
      </div>
    </div>
    
    <!-- 加载中 -->
    <el-skeleton v-else :rows="10" animated />
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { useStore } from 'vuex'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getStoryById, likeStory, unlikeStory, deleteStory as apiDeleteStory } from '../api/story'

export default {
  name: 'StoryDetail',
  setup() {
    const store = useStore()
    const route = useRoute()
    const router = useRouter()
    
    const story = ref(null)
    const loading = ref(false)
    
    const isLoggedIn = computed(() => store.getters.isLoggedIn)
    const currentUser = computed(() => store.getters.currentUser)
    const isOwner = computed(() => {
      return isLoggedIn.value && currentUser.value?.id === story.value?.userId
    })
    
    const fetchStory = async () => {
      loading.value = true
      try {
        const res = await getStoryById(route.params.id)
        story.value = res.data
      } catch (error) {
        console.error('获取故事详情失败:', error)
        ElMessage.error('获取故事详情失败')
        router.push('/stories')
      } finally {
        loading.value = false
      }
    }
    
    const toggleLike = async () => {
      if (!isLoggedIn.value) {
        ElMessage.warning('请先登录')
        router.push('/login')
        return
      }
      
      try {
        if (story.value.isLiked) {
          await unlikeStory(story.value.id)
          story.value.isLiked = false
          story.value.likeCount--
          ElMessage.success('已取消点赞')
        } else {
          await likeStory(story.value.id)
          story.value.isLiked = true
          story.value.likeCount++
          ElMessage.success('点赞成功')
        }
      } catch (error) {
        console.error('操作失败:', error)
      }
    }
    
    const shareStory = () => {
      // 复制链接到剪贴板
      const url = window.location.href
      navigator.clipboard.writeText(url).then(() => {
        ElMessage.success('链接已复制到剪贴板')
      }).catch(() => {
        ElMessage.error('复制失败')
      })
    }
    
    const editStory = () => {
      router.push(`/edit/${story.value.id}`)
    }
    
    const deleteStory = () => {
      ElMessageBox.confirm('确定要删除这个故事吗？此操作不可恢复。', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await apiDeleteStory(story.value.id)
          ElMessage.success('删除成功')
          router.push('/my-stories')
        } catch (error) {
          console.error('删除失败:', error)
        }
      })
    }
    
    const formatDate = (date) => {
      if (!date) return ''
      return new Date(date).toLocaleDateString('zh-CN', {
        year: 'numeric',
        month: 'long',
        day: 'numeric'
      })
    }
    
    const formatContent = (content) => {
      if (!content) return ''
      // 将换行符转换为HTML换行
      return content.replace(/\n/g, '<br>')
    }
    
    onMounted(() => {
      fetchStory()
    })
    
    return {
      story,
      loading,
      isOwner,
      toggleLike,
      shareStory,
      editStory,
      deleteStory,
      formatDate,
      formatContent
    }
  }
}
</script>

<style scoped>
.story-detail-page {
  padding-top: 30px;
  padding-bottom: 50px;
}

.story-content {
  max-width: 800px;
  margin: 0 auto;
  background: #fff;
  border-radius: 12px;
  padding: 30px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.story-header {
  margin-bottom: 25px;
  padding-bottom: 20px;
  border-bottom: 1px solid #e4e7ed;
}

.story-title {
  font-size: 28px;
  font-weight: bold;
  color: #333;
  margin-bottom: 15px;
  line-height: 1.4;
}

.story-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 15px;
  margin-bottom: 10px;
}

.author-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.author-detail {
  display: flex;
  flex-direction: column;
}

.author-name {
  font-size: 15px;
  font-weight: 500;
  color: #333;
}

.publish-time {
  font-size: 13px;
  color: #909399;
}

.story-stats {
  display: flex;
  gap: 20px;
  font-size: 14px;
  color: #909399;
}

.story-stats span {
  display: flex;
  align-items: center;
  gap: 5px;
}

.story-location {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #409eff;
  font-size: 14px;
  margin-top: 10px;
}

.travel-date {
  color: #909399;
  margin-left: 15px;
}

.story-gallery {
  margin-bottom: 25px;
  border-radius: 8px;
  overflow: hidden;
}

.gallery-image {
  width: 100%;
  height: 100%;
  object-fit: contain;
  background: #f5f7fa;
}

.image-desc {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: rgba(0, 0, 0, 0.6);
  color: #fff;
  padding: 10px 15px;
  font-size: 13px;
}

.story-body {
  margin-bottom: 30px;
}

.story-text {
  font-size: 16px;
  line-height: 1.8;
  color: #333;
}

.story-actions {
  display: flex;
  gap: 15px;
  justify-content: center;
  padding-top: 20px;
  border-top: 1px solid #e4e7ed;
}

@media (max-width: 768px) {
  .story-content {
    padding: 20px;
  }
  
  .story-title {
    font-size: 22px;
  }
  
  .story-meta {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .story-actions {
    flex-wrap: wrap;
  }
}
</style>
