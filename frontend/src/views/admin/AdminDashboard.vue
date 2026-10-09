<template>
  <div class="page-container" v-loading="loading">
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="mb-20">
      <el-col :span="4">
        <el-card class="stat-card">
          <div class="stat-value">{{ dashboard.totalStudents ?? '-' }}</div>
          <div class="stat-label">学生总数</div>
        </el-card>
      </el-col>
      <el-col :span="5">
        <el-card class="stat-card">
          <div class="stat-value">{{ dashboard.totalTeachers ?? '-' }}</div>
          <div class="stat-label">教师总数</div>
        </el-card>
      </el-col>
      <el-col :span="5">
        <el-card class="stat-card">
          <div class="stat-value">{{ dashboard.totalCourses ?? '-' }}</div>
          <div class="stat-label">课程总数</div>
        </el-card>
      </el-col>
      <el-col :span="5">
        <el-card class="stat-card">
          <div class="stat-value">{{ dashboard.totalScores ?? '-' }}</div>
          <div class="stat-label">成绩记录</div>
        </el-card>
      </el-col>
      <el-col :span="5">
        <el-card class="stat-card" style="cursor: pointer" @click="$router.push('/admin/appeals')">
          <div class="stat-value warn-text">{{ dashboard.pendingReviewCount ?? '-' }}</div>
          <div class="stat-label">待审核申诉</div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <!-- 成绩分布 -->
      <el-col :span="9">
        <el-card>
          <template #header>成绩分数段分布</template>
          <div ref="distributionRef" class="chart"></div>
        </el-card>
      </el-col>
      <!-- 申诉状态 -->
      <el-col :span="6">
        <el-card>
          <template #header>申诉状态分布</template>
          <div ref="appealRef" class="chart"></div>
        </el-card>
      </el-col>
      <!-- 课程均分 -->
      <el-col :span="9">
        <el-card>
          <template #header>各课程平均成绩</template>
          <div ref="courseRef" class="chart"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { nextTick, onMounted, ref } from 'vue'
import * as echarts from 'echarts'
import { getAdminDashboard } from '../../api/dashboard'
import { useUserStore } from '../../store/user'

const userStore = useUserStore()
const loading = ref(false)
const dashboard = ref({})

const distributionRef = ref()
const appealRef = ref()
const courseRef = ref()
const charts = []

const renderCharts = () => {
  // 成绩分数段分布（柱状图）
  const distribution = dashboard.value.scoreDistribution || []
  const distributionChart = echarts.init(distributionRef.value)
  distributionChart.setOption({
    tooltip: { trigger: 'axis' },
    grid: { left: 40, right: 20, top: 30, bottom: 40 },
    xAxis: { type: 'category', data: distribution.map((i) => i.name), axisLabel: { interval: 0, rotate: 20 } },
    yAxis: { type: 'value', minInterval: 1 },
    series: [{
      type: 'bar',
      data: distribution.map((i) => i.value),
      barMaxWidth: 36,
      itemStyle: {
        color: (params) => ['#f56c6c', '#e6a23c', '#409eff', '#67c23a', '#13ce66'][params.dataIndex]
      }
    }]
  })
  charts.push(distributionChart)

  // 申诉状态分布（饼图）
  const appeals = (dashboard.value.appealStatusCount || []).filter((i) => i.value > 0)
  const appealChart = echarts.init(appealRef.value)
  appealChart.setOption({
    tooltip: { trigger: 'item' },
    legend: { bottom: 0 },
    series: [{
      type: 'pie',
      radius: ['40%', '68%'],
      label: { formatter: '{b}: {c}' },
      data: appeals.length ? appeals : [{ name: '暂无数据', value: 1, itemStyle: { color: '#e4e7ed' } }]
    }]
  })
  charts.push(appealChart)

  // 课程均分（横向柱状图）
  const courses = dashboard.value.courseAverages || []
  const courseChart = echarts.init(courseRef.value)
  courseChart.setOption({
    tooltip: { trigger: 'axis' },
    grid: { left: 10, right: 30, top: 20, bottom: 30, containLabel: true },
    xAxis: { type: 'value', max: 100 },
    yAxis: { type: 'category', data: courses.map((i) => i.name), axisLabel: { width: 90, overflow: 'truncate' } },
    series: [{ type: 'bar', data: courses.map((i) => i.value), itemStyle: { color: '#409eff' }, barMaxWidth: 18 }]
  })
  charts.push(courseChart)
}

onMounted(async () => {
  loading.value = true
  try {
    const { data } = await getAdminDashboard(userStore.userId)
    dashboard.value = data
    await nextTick()
    renderCharts()
  } finally {
    loading.value = false
  }
  window.addEventListener('resize', () => charts.forEach((c) => c.resize()))
})
</script>

<style scoped>
.warn-text {
  color: #e6a23c;
}

.chart {
  height: 300px;
}
</style>
