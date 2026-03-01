<template>
  <div class="create-story-page page-container">
    <div class="form-container">
      <h1 class="page-title">{{ isEdit ? '编辑故事' : '创建新故事' }}</h1>
      
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-position="top"
        class="story-form"
      >
        <el-form-item label="标题" prop="title">
          <el-input
            v-model="form.title"
            placeholder="给你的故事起个标题"
            size="large"
            maxlength="200"
            show-word-limit
          />
        </el-form-item>
        
        <el-form-item label="旅行地点">
          <el-input
            v-model="form.location"
            placeholder="例如：杭州西湖"
            size="large"
          >
            <template #prefix>
              <el-icon><Location /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        
        <el-form-item label="旅行日期">
          <el-date-picker
            v-model="form.travelDate"
            type="date"
            placeholder="选择旅行日期"
            size="large"
            style="width: 100%"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        
        <el-form-item label="故事内容" prop="content">
          <el-input
            v-model="form.content"
            type="textarea"
            :rows="8"
            placeholder="分享你的旅行经历..."
            maxlength="5000"
            show-word-limit
          />
        </el-form-item>
        
        <el-form-item label="上传图片">
          <el-upload
            action="#"
            list-type="picture-card"
            :auto-upload="false"
            :on-change="handleImageChange"
            :on-remove="handleImageRemove"
            :file-list="fileList"
            :limit="9"
            accept="image/*"
          >
            <el-icon><Plus /></el-icon>
            <template #tip>
              <div class="upload-tip">最多上传9张图片，每张不超过10MB</div>
            </template>
          </el-upload>
        </el-form-item>
        
        <el-form-item label="隐私设置">
          <el-radio-group v-model="form.isPublic">
            <el-radio :label="1">公开 - 所有人可见</el-radio>
            <el-radio :label="0">私密 - 仅自己可见</el-radio>
          </el-radio-group>
        </el-form-item>
        
        <el-form-item>
          <div class="form-actions">
            <el-button size="large" @click="$router.back()">取消</el-button>
            <el-button
              type="primary"
              size="large"
              :loading="submitting"
              @click="handleSubmit"
            >
              {{ isEdit ? '保存修改' : '发布故事' }}
            </el-button>
          </div>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import { ref, reactive, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { createStory, updateStory, getStoryById } from '../api/story'
import { uploadImage } from '../api/upload'

export default {
  name: 'CreateStory',
  setup() {
    const route = useRoute()
    const router = useRouter()
    const formRef = ref(null)
    const submitting = ref(false)
    const fileList = ref([])
    const uploadedImages = ref([])
    
    const isEdit = computed(() => !!route.params.id)
    
    const form = reactive({
      title: '',
      content: '',
      location: '',
      travelDate: '',
      isPublic: 1
    })
    
    const rules = {
      title: [
        { required: true, message: '请输入标题', trigger: 'blur' },
        { min: 2, max: 200, message: '长度在 2 到 200 个字符', trigger: 'blur' }
      ],
      content: [
        { required: true, message: '请输入内容', trigger: 'blur' },
        { min: 10, message: '内容至少10个字符', trigger: 'blur' }
      ]
    }
    
    const handleImageChange = (file, fileListData) => {
      fileList.value = fileListData
    }
    
    const handleImageRemove = (file, fileListData) => {
      fileList.value = fileListData
      // 从已上传列表中移除
      const index = uploadedImages.value.findIndex(img => img.name === file.name)
      if (index > -1) {
        uploadedImages.value.splice(index, 1)
      }
    }
    
    const uploadImages = async () => {
      const newImages = []
      
      for (const file of fileList.value) {
        // 如果已经上传过，跳过
        if (file.url && file.url.startsWith('/uploads/')) {
          newImages.push({
            imageUrl: file.url,
            description: ''
          })
          continue
        }
        
        try {
          const res = await uploadImage(file.raw)
          newImages.push({
            imageUrl: res.data.url,
            description: ''
          })
        } catch (error) {
          console.error('上传图片失败:', error)
          ElMessage.error(`上传图片失败: ${file.name}`)
        }
      }
      
      return newImages
    }
    
    const handleSubmit = async () => {
      const valid = await formRef.value.validate().catch(() => false)
      if (!valid) return
      
      submitting.value = true
      
      try {
        // 上传图片
        const images = await uploadImages()
        
        const storyData = {
          ...form,
          images
        }
        
        if (isEdit.value) {
          await updateStory(route.params.id, storyData)
          ElMessage.success('修改成功')
        } else {
          await createStory(storyData)
          ElMessage.success('发布成功')
        }
        
        router.push('/my-stories')
      } catch (error) {
        console.error('提交失败:', error)
      } finally {
        submitting.value = false
      }
    }
    
    const fetchStory = async () => {
      if (!isEdit.value) return
      
      try {
        const res = await getStoryById(route.params.id)
        const story = res.data
        
        form.title = story.title
        form.content = story.content
        form.location = story.location
        form.travelDate = story.travelDate
        form.isPublic = story.isPublic
        
        // 加载已有图片
        if (story.images && story.images.length > 0) {
          fileList.value = story.images.map((img, index) => ({
            name: `image-${index}`,
            url: img.imageUrl
          }))
          uploadedImages.value = story.images.map(img => ({
            name: img.imageUrl,
            url: img.imageUrl
          }))
        }
      } catch (error) {
        console.error('获取故事失败:', error)
        ElMessage.error('获取故事信息失败')
        router.push('/my-stories')
      }
    }
    
    onMounted(() => {
      fetchStory()
    })
    
    return {
      formRef,
      form,
      rules,
      submitting,
      fileList,
      isEdit,
      handleImageChange,
      handleImageRemove,
      handleSubmit
    }
  }
}
</script>

<style scoped>
.create-story-page {
  padding-top: 30px;
  padding-bottom: 50px;
}

.form-container {
  max-width: 800px;
  margin: 0 auto;
  background: #fff;
  border-radius: 12px;
  padding: 30px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.page-title {
  font-size: 24px;
  font-weight: bold;
  color: #333;
  margin-bottom: 25px;
  text-align: center;
}

.story-form :deep(.el-form-item__label) {
  font-weight: 500;
  color: #333;
}

.upload-tip {
  font-size: 12px;
  color: #909399;
  margin-top: 8px;
}

.form-actions {
  display: flex;
  justify-content: center;
  gap: 20px;
  margin-top: 20px;
}

:deep(.el-upload--picture-card) {
  width: 120px;
  height: 120px;
}

:deep(.el-upload-list--picture-card .el-upload-list__item) {
  width: 120px;
  height: 120px;
}
</style>
