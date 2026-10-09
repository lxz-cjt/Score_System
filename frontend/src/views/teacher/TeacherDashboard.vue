<template>
  <div class="page-container" v-loading="loading">
    <el-card class="mb-20">
      <div class="welcome">
        <div>
          <h2 class="welcome-title">{{ greeting }}，{{ dashboard.teacherName || userStore.userName }} 老师</h2>
          <p class="welcome-sub">{{ dashboard.college }} · 工号 {{ userStore.userId }}</p>
        </div>
        <el-button type="primary" plain @click="$router.push('/teacher/scores')">进入成绩管理</el-button>
      </div>
    </el-card>

    <el-row :gutter="20" class="mb-20">
      <el-col :span="8">
        <el-card class="stat-card">
          <div class="stat-value">{{ dashboard.courseCount ?? '-' }}</div>
          <div class="stat-label">授课课程</div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="stat-card">
          <div class="stat-value">{{ dashboard.studentCount ?? '-' }}</div>
          <div class="stat-label">授课学生人次</div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="stat-card">
          <div class="stat-value" :class="{ 'warn-text': dashboard.pendingAppealCount > 0 }">
            {{ dashboard.pendingAppealCount ?? '-' }}
          </div>
          <div class="stat-label">待处理申诉</div>
        </el-card>
      </el-col>
    </el-row>

    <el-card>
      <template #header>我的课程平均成绩</template>
      <el-empty v-if="!hasData" description="暂无成绩数据" />
      <div v-else ref="chartRef" class="chart"></div>
    </el-card>
  </div>
</template>

<script setup>
import { computed, nextTick, onMounted, ref } from 'vue'
import * as echarts from 'echarts'
import { getTeacherDashboard } from '../../api/dashboard'
import { useUserStore } from '../../store/user'

const userStore = useUserStore()
const loading = ref(false)
const dashboard = ref({})
const chartRef = ref()
let chart = null

const hasData = computed(() => (dashboard.value.courseAverages || []).length > 0)

const greeting = computed(() => {
  const hour = new Date().getHours()
  if (hour < 12) return '上午好'
  if (hour < 18) return '下午好'
  return '晚上好'
})

const renderChart = () => {
  if (!chartRef.value) return
  chart = chart || echarts.init(chartRef.value)
  const items = dashboard.value.courseAverages || []
  chart.setOption({
    tooltip: { trigger: 'axis' },
    grid: { left: 40, right: 20, top: 30, bottom: 60 },
    xAxis: { type: 'category', data: items.map((i) => i.name), axisLabel: { rotate: 30 } },
    yAxis: { type: 'value', max: 100 },
    series: [{ type: 'bar', data: items.map((i) => i.value), itemStyle: { color: '#409eff' }, barMaxWidth: 40 }]
  })
}

onMounted(async () => {
  loading.value = true
  try {
    const { data } = await getTeacherDashboard(userStore.userId)
    dashboard.value = data
    if (hasData.value) {
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

.warn-text {
  color: #e6a23c;
}

.chart {
  height: 320px;
}
</style>
