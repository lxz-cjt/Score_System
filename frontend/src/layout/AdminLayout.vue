<template>
  <el-container class="admin-layout">
    <!-- 侧边栏 -->
    <el-aside :width="isCollapse ? '64px' : '220px'" class="aside">
      <div class="logo" @click="goHome">
        <el-icon :size="26" color="#fff"><Reading /></el-icon>
        <span v-show="!isCollapse" class="logo-text">成绩管理系统</span>
      </div>
      <el-menu
        :default-active="$route.path"
        :collapse="isCollapse"
        :collapse-transition="false"
        router
        background-color="#001529"
        text-color="#a6adb4"
        active-text-color="#ffffff"
        class="side-menu"
      >
        <el-menu-item v-for="menu in menus" :key="menu.path" :index="menu.path">
          <el-icon><component :is="menu.icon" /></el-icon>
          <template #title>{{ menu.title }}</template>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <!-- 主区域 -->
    <el-container>
      <el-header class="header">
        <div class="header-left">
          <el-icon class="collapse-btn" @click="isCollapse = !isCollapse">
            <Expand v-if="isCollapse" />
            <Fold v-else />
          </el-icon>
          <span class="page-title">{{ $route.meta.title }}</span>
        </div>
        <el-dropdown @command="handleCommand">
          <span class="user-info">
            <el-avatar :size="32" class="avatar">{{ userStore.userName.charAt(0) }}</el-avatar>
            <span class="name">{{ userStore.userName }}</span>
            <el-tag size="small" :type="roleTagType">{{ userStore.roleName }}</el-tag>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item disabled>工号：{{ userStore.userId }}</el-dropdown-item>
              <el-dropdown-item divided command="logout">
                <el-icon><SwitchButton /></el-icon>退出登录
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </el-header>

      <el-main class="main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessageBox } from 'element-plus'
import { useUserStore } from '../store/user'

const userStore = useUserStore()
const router = useRouter()
const isCollapse = ref(false)

/** 按角色生成菜单 */
const MENU_MAP = {
  student: [
    { path: '/student', title: '个人主页', icon: 'HomeFilled' },
    { path: '/student/scores', title: '我的成绩', icon: 'Document' },
    { path: '/student/courses', title: '课程查询', icon: 'Reading' },
    { path: '/student/appeals', title: '我的申诉', icon: 'ChatDotSquare' }
  ],
  teacher: [
    { path: '/teacher', title: '工作台', icon: 'HomeFilled' },
    { path: '/teacher/courses', title: '我的课程', icon: 'Reading' },
    { path: '/teacher/scores', title: '成绩管理', icon: 'EditPen' },
    { path: '/teacher/score-logs', title: '成绩日志', icon: 'Clock' },
    { path: '/teacher/appeals', title: '申诉处理', icon: 'ChatDotSquare' }
  ],
  staff: [
    { path: '/admin', title: '数据看板', icon: 'DataAnalysis' },
    { path: '/admin/students', title: '学生管理', icon: 'User' },
    { path: '/admin/teachers', title: '教师管理', icon: 'Avatar' },
    { path: '/admin/staff', title: '教务人员管理', icon: 'UserFilled' },
    { path: '/admin/courses', title: '课程管理', icon: 'Reading' },
    { path: '/admin/arrangements', title: '课程安排', icon: 'Calendar' },
    { path: '/admin/appeals', title: '申诉审核', icon: 'Stamp' }
  ]
}

const menus = computed(() => MENU_MAP[userStore.role] || [])

const roleTagType = computed(() => {
  return { student: 'success', teacher: 'warning', staff: 'danger' }[userStore.role] || 'info'
})

const goHome = () => router.push('/')

const handleCommand = async (command) => {
  if (command === 'logout') {
    await ElMessageBox.confirm('确定要退出登录吗？', '提示', { type: 'warning' })
    userStore.logout()
    router.push('/login')
  }
}
</script>

<style scoped>
.admin-layout {
  height: 100%;
}

.aside {
  background-color: #001529;
  transition: width 0.2s;
  overflow: hidden;
}

.logo {
  display: flex;
  align-items: center;
  gap: 10px;
  height: 60px;
  padding: 0 18px;
  cursor: pointer;
}

.logo-text {
  color: #fff;
  font-size: 16px;
  font-weight: 600;
  white-space: nowrap;
}

.side-menu {
  border-right: none;
}

:deep(.el-menu-item.is-active) {
  background-color: #409eff !important;
}

.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #fff;
  border-bottom: 1px solid #f0f0f0;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
}

.header-left {
  display: flex;
  align-items: center;
  gap: 14px;
}

.collapse-btn {
  font-size: 20px;
  cursor: pointer;
  color: #606266;
}

.page-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
}

.avatar {
  background: #409eff;
  color: #fff;
}

.name {
  font-size: 14px;
  color: #303133;
}

.main {
  background: #f5f7fa;
  padding: 0;
  overflow-y: auto;
}
</style>
