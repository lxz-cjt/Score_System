<template>
  <div class="page-container">
    <el-card>
      <div class="table-toolbar">
        <span class="card-title">课程安排查询</span>
        <el-input
          v-model="query.courseId"
          placeholder="按课程编号过滤，如 CS101"
          clearable
          style="width: 240px"
          @change="loadData"
        />
      </div>

      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="courseId" label="课程编号" width="110" />
        <el-table-column prop="courseName" label="课程名称" min-width="170" />
        <el-table-column prop="courseCredit" label="学分" width="70" align="center" />
        <el-table-column prop="teacherId" label="教师工号" width="100" />
        <el-table-column prop="teacherName" label="授课教师" width="110" />
        <el-table-column prop="classTime" label="上课时间" min-width="180" />
        <el-table-column prop="classroom" label="上课地点" width="130" />
      </el-table>

      <div class="pagination-bar">
        <el-pagination
          v-model:current-page="query.page"
          v-model:page-size="query.size"
          :total="total"
          :page-sizes="[10, 20, 50]"
          layout="total, sizes, prev, pager, next"
          @change="loadData"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { getArrangements } from '../../api/course'

const tableData = ref([])
const total = ref(0)
const loading = ref(false)
const query = reactive({ courseId: '', page: 1, size: 10 })

const loadData = async () => {
  loading.value = true
  try {
    const { data } = await getArrangements({ ...query, courseId: query.courseId || undefined })
    tableData.value = data.records
    total.value = data.total
  } finally {
    loading.value = false
  }
}

onMounted(loadData)
</script>

<style scoped>
.card-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}
</style>
