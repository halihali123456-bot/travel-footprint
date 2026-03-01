<template>
  <div class="layout">
    <!-- 导航栏 -->
    <el-header class="header">
      <div class="header-content">
        <div class="logo" @click="$router.push('/home')">
          <el-icon size="28" color="#409eff"><Compass /></el-icon>
          <span class="logo-text">旅行足迹</span>
        </div>
        
        <el-menu
          :default-active="activeMenu"
          class="nav-menu"
          mode="horizontal"
          router
          background-color="transparent"
          text-color="#333"
          active-text-color="#409eff"
        >
          <el-menu-item index="/home">
            <el-icon><HomeFilled /></el-icon>
            <span>首页</span>
          </el-menu-item>
          <el-menu-item index="/stories">
            <el-icon><Collection /></el-icon>
            <span>探索</span>
          </el-menu-item>
          <el-menu-item index="/timeline" v-if="isLoggedIn">
            <el-icon><Timer /></el-icon>
            <span>时间线</span>
          </el-menu-item>
          <el-menu-item index="/create" v-if="isLoggedIn">
            <el-icon><EditPen /></el-icon>
            <span>写故事</span>
          </el-menu-item>
        </el-menu>
        
        <div class="user-actions">
          <template v-if="isLoggedIn">
            <el-dropdown @command="handleCommand">
              <div class="user-info">
                <el-avatar :size="36" :src="userAvatar" :icon="UserFilled" />
                <span class="username">{{ userName }}</span>
                <el-icon><ArrowDown /></el-icon>
              </div>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="profile">
                    <el-icon><User /></el-icon>个人中心
                  </el-dropdown-item>
                  <el-dropdown-item command="myStories">
                    <el-icon><Document /></el-icon>我的故事
                  </el-dropdown-item>
                  <el-dropdown-item divided command="logout">
                    <el-icon><SwitchButton /></el-icon>退出登录
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
          <template v-else>
            <el-button type="primary" @click="$router.push('/login')">登录</el-button>
            <el-button @click="$router.push('/register')">注册</el-button>
          </template>
        </div>
      </div>
    </el-header>
    
    <!-- 主内容区 -->
    <el-main class="main-content">
      <router-view />
    </el-main>
    
    <!-- 页脚 -->
    <el-footer class="footer">
      <div class="footer-content">
        <p>© 2024 旅行足迹 - 记录你的每一次旅程</p>
      </div>
    </el-footer>
  </div>
</template>

<script>
import { computed } from 'vue'
import { useStore } from 'vuex'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'

export default {
  name: 'Layout',
  setup() {
    const store = useStore()
    const route = useRoute()
    const router = useRouter()
    
    const isLoggedIn = computed(() => store.getters.isLoggedIn)
    const userName = computed(() => store.getters.currentUser?.nickname || store.getters.currentUser?.username || '用户')
    const userAvatar = computed(() => store.getters.currentUser?.avatar || '')
    const activeMenu = computed(() => route.path)
    
    const handleCommand = (command) => {
      switch (command) {
        case 'profile':
          router.push('/profile')
          break
        case 'myStories':
          router.push('/my-stories')
          break
        case 'logout':
          ElMessageBox.confirm('确定要退出登录吗？', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            store.dispatch('logout')
            ElMessage.success('已退出登录')
            router.push('/home')
          })
          break
      }
    }
    
    return {
      isLoggedIn,
      userName,
      userAvatar,
      activeMenu,
      handleCommand
    }
  }
}
</script>

<style scoped>
.layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.header {
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  z-index: 100;
  padding: 0;
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
}

.logo {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
}

.logo-text {
  font-size: 22px;
  font-weight: bold;
  color: #409eff;
}

.nav-menu {
  flex: 1;
  justify-content: center;
  border-bottom: none;
}

.nav-menu :deep(.el-menu-item) {
  font-size: 15px;
}

.user-actions {
  display: flex;
  align-items: center;
  gap: 10px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 5px 10px;
  border-radius: 20px;
  transition: background 0.3s;
}

.user-info:hover {
  background: #f5f7fa;
}

.username {
  font-size: 14px;
  color: #333;
}

.main-content {
  flex: 1;
  padding: 0;
  background: #f5f7fa;
}

.footer {
  background: #fff;
  border-top: 1px solid #e4e7ed;
  padding: 20px 0;
}

.footer-content {
  max-width: 1200px;
  margin: 0 auto;
  text-align: center;
  color: #909399;
  font-size: 14px;
}
</style>
