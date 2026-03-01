<template>
  <div class="profile-page page-container">
    <div class="profile-card">
      <div class="profile-header">
        <el-avatar :size="100" :src="user.avatar" :icon="UserFilled" />
        <div class="profile-info">
          <h2 class="nickname">{{ user.nickname || user.username }}</h2>
          <p class="username">@{{ user.username }}</p>
          <p class="email" v-if="user.email">
            <el-icon><Message /></el-icon>{{ user.email }}
          </p>
        </div>
        <el-button type="primary" @click="showEditDialog = true">
          <el-icon><EditPen /></el-icon>编辑资料
        </el-button>
      </div>
      
      <el-divider />
      
      <div class="profile-stats">
        <div class="stat-item">
          <div class="stat-value">{{ stats.stories }}</div>
          <div class="stat-label">故事</div>
        </div>
        <div class="stat-item">
          <div class="stat-value">{{ stats.views }}</div>
          <div class="stat-label">浏览</div>
        </div>
        <div class="stat-item">
          <div class="stat-value">{{ stats.likes }}</div>
          <div class="stat-label">获赞</div>
        </div>
      </div>
    </div>
    
    <!-- 编辑资料对话框 -->
    <el-dialog
      v-model="showEditDialog"
      title="编辑资料"
      width="500px"
    >
      <el-form :model="editForm" label-position="top">
        <el-form-item label="昵称">
          <el-input v-model="editForm.nickname" placeholder="输入昵称" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="editForm.email" placeholder="输入邮箱" />
        </el-form-item>
        <el-form-item label="头像">
          <el-upload
            class="avatar-uploader"
            action="#"
            :auto-upload="false"
            :show-file-list="false"
            :on-change="handleAvatarChange"
            accept="image/*"
          >
            <img v-if="editForm.avatarPreview" :src="editForm.avatarPreview" class="avatar-preview" />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showEditDialog = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="saveProfile">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, computed, onMounted } from 'vue'
import { useStore } from 'vuex'
import { ElMessage } from 'element-plus'
import { getUserInfo, updateUserInfo } from '../api/user'
import { uploadImage } from '../api/upload'
import { getMyStories } from '../api/story'

export default {
  name: 'Profile',
  setup() {
    const store = useStore()
    
    const user = computed(() => store.getters.currentUser || {})
    const showEditDialog = ref(false)
    const saving = ref(false)
    const stats = ref({
      stories: 0,
      views: 0,
      likes: 0
    })
    
    const editForm = reactive({
      nickname: '',
      email: '',
      avatar: '',
      avatarPreview: '',
      avatarFile: null
    })
    
    const fetchStats = async () => {
      try {
        const res = await getMyStories(1, 1000)
        const stories = res.data.records || []
        stats.value.stories = stories.length
        stats.value.views = stories.reduce((sum, s) => sum + (s.viewCount || 0), 0)
        stats.value.likes = stories.reduce((sum, s) => sum + (s.likeCount || 0), 0)
      } catch (error) {
        console.error('获取统计数据失败:', error)
      }
    }
    
    const initEditForm = () => {
      editForm.nickname = user.value.nickname || ''
      editForm.email = user.value.email || ''
      editForm.avatar = user.value.avatar || ''
      editForm.avatarPreview = user.value.avatar || ''
      editForm.avatarFile = null
    }
    
    const handleAvatarChange = (file) => {
      editForm.avatarFile = file.raw
      editForm.avatarPreview = URL.createObjectURL(file.raw)
    }
    
    const saveProfile = async () => {
      saving.value = true
      
      try {
        // 如果有新头像，先上传
        if (editForm.avatarFile) {
          const res = await uploadImage(editForm.avatarFile)
          editForm.avatar = res.data.url
        }
        
        // 更新用户信息
        const updateData = {
          nickname: editForm.nickname,
          email: editForm.email,
          avatar: editForm.avatar
        }
        
        const res = await updateUserInfo(updateData)
        store.dispatch('updateUser', res.data)
        
        ElMessage.success('保存成功')
        showEditDialog.value = false
      } catch (error) {
        console.error('保存失败:', error)
      } finally {
        saving.value = false
      }
    }
    
    onMounted(() => {
      fetchStats()
      initEditForm()
    })
    
    return {
      user,
      stats,
      showEditDialog,
      editForm,
      saving,
      handleAvatarChange,
      saveProfile
    }
  }
}
</script>

<style scoped>
.profile-page {
  padding-top: 30px;
  padding-bottom: 50px;
}

.profile-card {
  max-width: 600px;
  margin: 0 auto;
  background: #fff;
  border-radius: 12px;
  padding: 30px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.profile-header {
  display: flex;
  align-items: center;
  gap: 20px;
}

.profile-info {
  flex: 1;
}

.nickname {
  font-size: 22px;
  font-weight: bold;
  color: #333;
  margin-bottom: 5px;
}

.username {
  font-size: 14px;
  color: #909399;
  margin-bottom: 5px;
}

.email {
  font-size: 13px;
  color: #666;
  display: flex;
  align-items: center;
  gap: 5px;
}

.profile-stats {
  display: flex;
  justify-content: center;
  gap: 60px;
  padding: 10px 0;
}

.stat-item {
  text-align: center;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #409eff;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
}

.avatar-uploader {
  text-align: center;
}

.avatar-uploader :deep(.el-upload) {
  border: 1px dashed #d9d9d9;
  border-radius: 50%;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 100px;
  height: 100px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.avatar-uploader :deep(.el-upload:hover) {
  border-color: #409eff;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
}

.avatar-preview {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  object-fit: cover;
}

@media (max-width: 768px) {
  .profile-header {
    flex-direction: column;
    text-align: center;
  }
  
  .profile-stats {
    gap: 30px;
  }
  
  .stat-value {
    font-size: 22px;
  }
}
</style>
