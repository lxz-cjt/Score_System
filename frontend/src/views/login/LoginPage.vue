<template>
  <div class="login-page">
    <div class="login-card">
      <div class="login-header">
        <el-icon :size="40" color="#409eff"><Reading /></el-icon>
        <h1 class="title">高校成绩管理系统</h1>
        <p class="subtitle">Score Management System</p>
      </div>

      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        size="large"
        @keyup.enter="handleLogin"
      >
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="学号 / 工号" :prefix-icon="User" clearable />
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="form.password"
            type="password"
            placeholder="密码"
            :prefix-icon="Lock"
            show-password
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" class="login-btn" :loading="loading" @click="handleLogin">
            登 录
          </el-button>
        </el-form-item>
      </el-form>

      <el-divider content-position="center">演示账号</el-divider>
      <div class="demo-accounts">
        <div class="account-item" @click="fillAccount('2021001', '123456')">
          <el-tag type="success" size="small">学生</el-tag>
          <span>2021001 / 123456</span>
        </div>
        <div class="account-item" @click="fillAccount('T001', 'teacher123')">
          <el-tag type="warning" size="small">教师</el-tag>
          <span>T001 / teacher123</span>
        </div>
        <div class="account-item" @click="fillAccount('AAS001', 'admin123')">
          <el-tag type="danger" size="small">教务</el-tag>
          <span>AAS001 / admin123</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue'
import { login } from '../../api/auth'
import { useUserStore } from '../../store/user'
import { ROLE_HOME } from '../../router'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const formRef = ref()
const loading = ref(false)
const form = reactive({ username: '', password: '' })

const rules = {
  username: [{ required: true, message: '请输入学号/工号', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const fillAccount = (username, password) => {
  form.username = username
  form.password = password
}

const handleLogin = async () => {
  await formRef.value.validate()
  loading.value = true
  try {
    const { data } = await login(form)
    userStore.setLogin(data.token, data.user)
    ElMessage.success(`欢迎回来，${data.user.name}`)
    const redirect = route.query.redirect || ROLE_HOME[data.user.role] || '/'
    router.push(redirect)
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-page {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-card {
  width: 400px;
  padding: 40px 36px 28px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.18);
}

.login-header {
  text-align: center;
  margin-bottom: 28px;
}

.title {
  font-size: 22px;
  color: #303133;
  margin-top: 10px;
}

.subtitle {
  font-size: 13px;
  color: #909399;
  margin-top: 4px;
}

.login-btn {
  width: 100%;
}

.demo-accounts {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.account-item {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 13px;
  color: #606266;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 4px;
}

.account-item:hover {
  background: #f5f7fa;
}
</style>
