<template>
  <div class="auth-page">
    <div class="auth-container">
      <div class="auth-header">
        <el-icon size="48" color="#409eff"><Compass /></el-icon>
        <h1>欢迎回来</h1>
        <p>登录你的旅行足迹账号</p>
      </div>
      
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        class="auth-form"
        @keyup.enter="handleSubmit"
      >
        <el-form-item prop="username">
          <el-input
            v-model="form.username"
            placeholder="用户名"
            size="large"
            :prefix-icon="User"
          />
        </el-form-item>
        
        <el-form-item prop="password">
          <el-input
            v-model="form.password"
            type="password"
            placeholder="密码"
            size="large"
            :prefix-icon="Lock"
            show-password
          />
        </el-form-item>
        
        <el-form-item>
          <el-button
            type="primary"
            size="large"
            :loading="loading"
            @click="handleSubmit"
            class="submit-btn"
          >
            登录
          </el-button>
        </el-form-item>
      </el-form>
      
      <div class="auth-footer">
        <p>还没有账号？<el-button type="primary" text @click="$router.push('/register')">立即注册</el-button></p>
        <el-button text @click="$router.push('/home')">返回首页</el-button>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, reactive } from 'vue'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { login } from '../api/user'

export default {
  name: 'Login',
  setup() {
    const store = useStore()
    const router = useRouter()
    const formRef = ref(null)
    const loading = ref(false)
    
    const form = reactive({
      username: '',
      password: ''
    })
    
    const rules = {
      username: [
        { required: true, message: '请输入用户名', trigger: 'blur' },
        { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
      ],
      password: [
        { required: true, message: '请输入密码', trigger: 'blur' },
        { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
      ]
    }
    
    const handleSubmit = async () => {
      const valid = await formRef.value.validate().catch(() => false)
      if (!valid) return
      
      loading.value = true
      try {
        const res = await login(form)
        const { data } = res
        
        store.dispatch('login', {
          user: data,
          token: data.token
        })
        
        ElMessage.success('登录成功')
        router.push('/home')
      } catch (error) {
        console.error('登录失败:', error)
      } finally {
        loading.value = false
      }
    }
    
    return {
      formRef,
      form,
      rules,
      loading,
      handleSubmit
    }
  }
}
</script>

<style scoped>
.auth-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
}

.auth-container {
  background: #fff;
  border-radius: 16px;
  padding: 40px;
  width: 100%;
  max-width: 420px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.2);
}

.auth-header {
  text-align: center;
  margin-bottom: 30px;
}

.auth-header h1 {
  font-size: 28px;
  color: #333;
  margin: 15px 0 8px;
}

.auth-header p {
  color: #909399;
  font-size: 14px;
}

.auth-form {
  margin-bottom: 20px;
}

.submit-btn {
  width: 100%;
}

.auth-footer {
  text-align: center;
  color: #606266;
  font-size: 14px;
}

.auth-footer p {
  margin-bottom: 10px;
}
</style>
