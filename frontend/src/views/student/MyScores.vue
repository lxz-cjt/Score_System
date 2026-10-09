<template>
  <div class="page-container">
    <!-- 成绩概览 -->
    <el-card class="mb-20" v-loading="summaryLoading">
      <el-row>
        <el-col :span="6" class="stat-card">
          <div class="stat-value">{{ summary.scoreCount ?? '-' }}</div>
          <div class="stat-label">成绩门数</div>
        </el-col>
        <el-col :span="6" class="stat-card">
          <div class="stat-value">{{ summary.averageScore ?? '-' }}</div>
          <div class="stat-label">平均成绩</div>
        </el-col>
        <el-col :span="6" class="stat-card">
          <div class="stat-value">{{ summary.gpa ?? '-' }}</div>
          <div class="stat-label">GPA 绩点</div>
        </el-col>
        <el-col :span="6" class="stat-card">
          <div class="stat-value">{{ summary.totalCredits ?? '-' }}</div>
          <div class="stat-label">已修学分</div>
        </el-col>
      </el-row>
    </el-card>

    <!-- 成绩列表 -->
    <el-card>
      <template #header>我的成绩</template>
      <el-table :data="scores" v-loading="loading" stripe>
        <el-table-column prop="courseId" label="课程编号" width="110" />
        <el-table-column prop="courseName" label="课程名称" min-width="170" />
        <el-table-column prop="courseCredit" label="学分" width="70" align="center" />
        <el-table-column prop="dailyScore" label="平时成绩" width="100" align="center" />
        <el-table-column prop="examScore" label="考试成绩" width="100" align="center" />
        <el-table-column label="总评成绩" width="110" align="center">
          <template #default="{ row }">
            <span :class="row.totalScore < 60 ? 'score-fail' : 'score-pass'">{{ row.totalScore }}</span>
          </template>
        </el-table-column>
        <el-table-column label="学分获得" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="conditionTag(row.creditGainCondition)" size="small">
              {{ row.creditGainCondition }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="补考" width="80" align="center">
          <template #default="{ row }">
            <el-tag v-if="row.makeUpExam" type="danger" size="small">需补考</el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
      </el-table>
      <el-empty v-if="!loading && scores.length === 0" description="暂无成绩记录" />
    </el-card>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { getStudentScores, getStudentSummary } from '../../api/score'
import { useUserStore } from '../../store/user'

const userStore = useUserStore()
const scores = ref([])
const summary = ref({})
const loading = ref(false)
const summaryLoading = ref(false)

const conditionTag = (condition) => {
  return { 优秀: 'success', 通过: 'primary', 不通过: 'danger' }[condition] || 'info'
}

onMounted(async () => {
  loading.value = true
  summaryLoading.value = true
  try {
    const [{ data: scoreData }, { data: summaryData }] = await Promise.all([
      getStudentScores(userStore.userId),
      getStudentSummary(userStore.userId)
    ])
    scores.value = scoreData
    summary.value = summaryData
  } finally {
    loading.value = false
    summaryLoading.value = false
  }
})
</script>

<style scoped>
.score-pass {
  font-weight: 600;
  color: #67c23a;
}

.score-fail {
  font-weight: 600;
  color: #f56c6c;
}
</style>
