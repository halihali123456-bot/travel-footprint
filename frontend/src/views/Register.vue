<template>
  <div class="auth-page">
    <div class="auth-container">
      <div class="auth-header">
        <el-icon size="48" color="#409eff"><Compass /></el-icon>
        <h1>创建账号</h1>
        <p>开始记录你的旅行足迹</p>
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
        
        <el-form-item prop="nickname">
          <el-input
            v-model="form.nickname"
            placeholder="昵称（选填）"
            size="large"
            :prefix-icon="Avatar"
          />
        </el-form-item>
        
        <el-form-item prop="email">
          <el-input
            v-model="form.email"
            placeholder="邮箱（选填）"
            size="large"
            :prefix-icon="Message"
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
        
        <el-form-item prop="confirmPassword">
          <el-input
            v-model="form.confirmPassword"
            type="password"
            placeholder="确认密码"
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
            注册
          </el-button>
        </el-form-item>
      </el-form>
      
      <div class="auth-footer">
        <p>已有账号？<el-button type="primary" text @click="$router.push('/login')">立即登录</el-button></p>
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
import { register } from '../api/user'

export default {
  name: 'Register',
  setup() {
    const store = useStore()
    const router = useRouter()
    const formRef = ref(null)
    const loading = ref(false)
    
    const form = reactive({
      username: '',
      nickname: '',
      email: '',
      password: '',
      confirmPassword: ''
    })
    
    const validateConfirmPassword = (rule, value, callback) => {
      if (value !== form.password) {
        callback(new Error('两次输入的密码不一致'))
      } else {
        callback()
      }
    }
    
    const rules = {
      username: [
        { required: true, message: '请输入用户名', trigger: 'blur' },
        { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
      ],
      password: [
        { required: true, message: '请输入密码', trigger: 'blur' },
        { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
      ],
      confirmPassword: [
        { required: true, message: '请确认密码', trigger: 'blur' },
        { validator: validateConfirmPassword, trigger: 'blur' }
      ],
      email: [
        { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
      ]
    }
    
    const handleSubmit = async () => {
      const valid = await formRef.value.validate().catch(() => false)
      if (!valid) return
      
      loading.value = true
      try {
        const { confirmPassword, ...registerData } = form
        const res = await register(registerData)
        const { data } = res
        
        store.dispatch('login', {
          user: data,
          token: data.token
        })
        
        ElMessage.success('注册成功')
        router.push('/home')
      } catch (error) {
        console.error('注册失败:', error)
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
