<template>
  <div class="page-container">
    <el-card>
      <template #header>我的课程</template>
      <el-table :data="courses" v-loading="loading" stripe>
        <el-table-column prop="courseId" label="课程编号" width="110" />
        <el-table-column prop="courseName" label="课程名称" min-width="170" />
        <el-table-column prop="courseCredit" label="学分" width="70" align="center" />
        <el-table-column prop="classTime" label="上课时间" min-width="180" />
        <el-table-column prop="classroom" label="上课地点" width="130" />
        <el-table-column label="操作" width="120">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="goScoreManage(row)">录入成绩</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-empty v-if="!loading && courses.length === 0" description="暂无课程安排" />
    </el-card>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { getTeacherArrangements } from '../../api/course'
import { useUserStore } from '../../store/user'

const userStore = useUserStore()
const router = useRouter()
const courses = ref([])
const loading = ref(false)

const goScoreManage = (row) => {
  router.push({ path: '/teacher/scores', query: { courseId: row.courseId } })
}

onMounted(async () => {
  loading.value = true
  try {
    const { data } = await getTeacherArrangements(userStore.userId)
    courses.value = data
  } finally {
    loading.value = false
  }
})
</script>
