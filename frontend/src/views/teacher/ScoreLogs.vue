<template>
  <div class="page-container">
    <el-card>
      <div class="table-toolbar">
        <span class="card-title">成绩变更日志</span>
        <el-input
          v-model="query.scoreId"
          placeholder="按成绩编号过滤"
          clearable
          style="width: 220px"
          @change="loadData"
        />
      </div>

      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="logId" label="日志号" width="80" />
        <el-table-column prop="scoreId" label="成绩编号" width="150" />
        <el-table-column prop="studentId" label="学号" width="110" />
        <el-table-column prop="courseId" label="课程编号" width="100" />
        <el-table-column label="操作" width="90" align="center">
          <template #default="{ row }">
            <el-tag :type="opTag(row.operationType)" size="small">{{ opLabel(row.operationType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="总评变化" width="130" align="center">
          <template #default="{ row }">
            <span v-if="row.oldScore != null">{{ row.oldScore }} → </span>
            <span v-if="row.newScore != null">{{ row.newScore }}</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column label="学分条件变化" min-width="150">
          <template #default="{ row }">
            <span v-if="row.oldCreditCondition">{{ row.oldCreditCondition }} → </span>
            <span>{{ row.newCreditCondition || '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="operatorId" label="操作人" width="100" />
        <el-table-column label="操作时间" width="170">
          <template #default="{ row }">{{ formatTime(row.operationTime) }}</template>
        </el-table-column>
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
import { getScoreLogs } from '../../api/score'
import { useUserStore } from '../../store/user'

const userStore = useUserStore()

const tableData = ref([])
const total = ref(0)
const loading = ref(false)
// 默认只看自己的操作记录
const query = reactive({ scoreId: '', operatorId: userStore.userId, page: 1, size: 10 })

const opTag = (type) => ({ INSERT: 'success', UPDATE: 'warning', DELETE: 'danger' }[type] || 'info')
const opLabel = (type) => ({ INSERT: '新增', UPDATE: '修改', DELETE: '删除' }[type] || type)
const formatTime = (time) => (time ? new Date(time).toLocaleString('zh-CN', { hour12: false }) : '-')

const loadData = async () => {
  loading.value = true
  try {
    const { data } = await getScoreLogs({ ...query, scoreId: query.scoreId || undefined })
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
