<template>
  <div class="page-container" v-loading="loading">
    <!-- 欢迎卡片 -->
    <el-card class="mb-20">
      <div class="welcome">
        <div>
          <h2 class="welcome-title">{{ greeting }}，{{ dashboard.studentName || userStore.userName }}</h2>
          <p class="welcome-sub">{{ dashboard.major }} · 学号 {{ userStore.userId }}</p>
        </div>
        <el-button type="primary" plain @click="$router.push('/student/scores')">查看我的成绩</el-button>
      </div>
    </el-card>

    <!-- 统计卡片 -->
    <el-row :gutter="20" class="mb-20">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-value">{{ dashboard.scoreCount ?? '-' }}</div>
          <div class="stat-label">已修课程</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-value">{{ dashboard.averageScore ?? '-' }}</div>
          <div class="stat-label">平均成绩</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-value">{{ dashboard.gpa ?? '-' }}</div>
          <div class="stat-label">GPA 绩点</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-value">{{ dashboard.getCredit ?? '-' }}</div>
          <div class="stat-label">已获得学分</div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 申诉状态分布 -->
    <el-card>
      <template #header>我的申诉状态分布</template>
      <el-empty v-if="!hasAppeals" description="暂无申诉记录" />
      <div v-else ref="chartRef" class="chart"></div>
    </el-card>
  </div>
</template>

<script setup>
import { computed, nextTick, onMounted, ref } from 'vue'
import * as echarts from 'echarts'
import { getStudentDashboard } from '../../api/dashboard'
import { useUserStore } from '../../store/user'

const userStore = useUserStore()
const loading = ref(false)
const dashboard = ref({})
const chartRef = ref()
let chart = null

const hasAppeals = computed(() =>
  (dashboard.value.appealStatusCount || []).some((item) => item.value > 0)
)

const greeting = computed(() => {
  const hour = new Date().getHours()
  if (hour < 12) return '上午好'
  if (hour < 18) return '下午好'
  return '晚上好'
})

const renderChart = () => {
  if (!chartRef.value) return
  chart = chart || echarts.init(chartRef.value)
  chart.setOption({
    tooltip: { trigger: 'item' },
    legend: { bottom: 0 },
    series: [
      {
        type: 'pie',
        radius: ['40%', '70%'],
        label: { formatter: '{b}: {c}' },
        data: (dashboard.value.appealStatusCount || []).filter((item) => item.value > 0)
      }
    ]
  })
}

onMounted(async () => {
  loading.value = true
  try {
    const { data } = await getStudentDashboard(userStore.userId)
    dashboard.value = data
    if (hasAppeals.value) {
      await nextTick()
      renderChart()
    }
  } finally {
    loading.value = false
  }
  window.addEventListener('resize', () => chart?.resize())
})
</script>

<style scoped>
.welcome {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.welcome-title {
  font-size: 20px;
  color: #303133;
}

.welcome-sub {
  margin-top: 6px;
  color: #909399;
  font-size: 14px;
}

.chart {
  height: 320px;
}
</style>
