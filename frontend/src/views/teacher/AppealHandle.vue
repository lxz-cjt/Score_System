<template>
  <div class="page-container">
    <el-card>
      <el-tabs v-model="activeTab" @tab-change="handleTabChange">
        <el-tab-pane label="待处理" name="pending" />
        <el-tab-pane label="全部申诉" name="all" />
      </el-tabs>

      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="appealId" label="申诉编号" width="150" />
        <el-table-column label="学生" width="140">
          <template #default="{ row }">{{ row.studentName }}（{{ row.studentId }}）</template>
        </el-table-column>
        <el-table-column label="课程" min-width="150">
          <template #default="{ row }">{{ row.courseName }}（{{ row.courseId }}）</template>
        </el-table-column>
        <el-table-column prop="appealReason" label="申诉理由" min-width="180" show-overflow-tooltip />
        <el-table-column label="状态" width="110" align="center">
          <template #default="{ row }">
            <el-tag :type="statusTag(row.appealStatus)" size="small">{{ row.statusLabel }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="提交时间" width="165">
          <template #default="{ row }">{{ formatTime(row.appealTime) }}</template>
        </el-table-column>
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="openTimeline(row)">过程</el-button>
            <el-button
              v-if="row.appealStatus === 'PENDING'"
              link
              type="warning"
              size="small"
              @click="openProcessDialog(row)"
            >
              处理
            </el-button>
          </template>
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

    <!-- 处理申诉对话框 -->
    <el-dialog v-model="processDialogVisible" title="处理申诉" width="520px">
      <el-descriptions :column="1" border class="mb-20">
        <el-descriptions-item label="学生">{{ currentRow.studentName }}（{{ currentRow.studentId }}）</el-descriptions-item>
        <el-descriptions-item label="课程">{{ currentRow.courseName }}</el-descriptions-item>
        <el-descriptions-item label="申诉理由">{{ currentRow.appealReason }}</el-descriptions-item>
      </el-descriptions>
      <el-form ref="processFormRef" :model="processForm" :rules="processRules" label-width="90px">
        <el-form-item label="处理意见" prop="opinion">
          <el-input
            v-model="processForm.opinion"
            type="textarea"
            :rows="4"
            maxlength="1000"
            show-word-limit
            placeholder="请填写处理意见，提交后将流转至教务审核"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="processDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitting" @click="handleProcess">提交教务审核</el-button>
      </template>
    </el-dialog>

    <!-- 处理过程抽屉 -->
    <el-drawer v-model="timelineVisible" title="申诉处理过程" size="420px">
      <el-timeline v-loading="processLoading" class="timeline">
        <el-timeline-item
          v-for="process in processes"
          :key="process.processId"
          :timestamp="formatTime(process.processTime)"
          placement="top"
        >
          <el-card>
            <h4>{{ process.processStep }}</h4>
            <p class="processor">{{ process.processorName }}（{{ process.processorId }}）</p>
            <p class="opinion">{{ process.processOpinion }}</p>
          </el-card>
        </el-timeline-item>
      </el-timeline>
      <el-empty v-if="!processLoading && processes.length === 0" description="暂无处理记录" />
    </el-drawer>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { getAppealProcesses, getTeacherAppeals, processAppeal } from '../../api/appeal'
import { useUserStore } from '../../store/user'

const userStore = useUserStore()

const activeTab = ref('pending')
const tableData = ref([])
const total = ref(0)
const loading = ref(false)
const query = reactive({ page: 1, size: 10 })

const processDialogVisible = ref(false)
const processFormRef = ref()
const submitting = ref(false)
const processForm = reactive({ opinion: '' })
const processRules = {
  opinion: [
    { required: true, message: '请填写处理意见', trigger: 'blur' },
    { min: 5, message: '处理意见至少5个字', trigger: 'blur' }
  ]
}
const currentRow = ref({})

const timelineVisible = ref(false)
const processLoading = ref(false)
const processes = ref([])

const statusTag = (status) => {
  return {
    PENDING: 'warning',
    SUBMITTED_TO_ADMIN: 'primary',
    APPROVED: 'success',
    REJECTED: 'danger',
    CANCELLED: 'info'
  }[status] || 'info'
}

const formatTime = (time) => (time ? new Date(time).toLocaleString('zh-CN', { hour12: false }) : '-')

const loadData = async () => {
  loading.value = true
  try {
    const { data } = await getTeacherAppeals({
      teacherId: userStore.userId,
      pendingOnly: activeTab.value === 'pending',
      ...query
    })
    tableData.value = data.records
    total.value = data.total
  } finally {
    loading.value = false
  }
}

const handleTabChange = () => {
  query.page = 1
  loadData()
}

const openProcessDialog = (row) => {
  currentRow.value = row
  processForm.opinion = ''
  processDialogVisible.value = true
}

const handleProcess = async () => {
  await processFormRef.value.validate()
  submitting.value = true
  try {
    await processAppeal(currentRow.value.appealId, {
      teacherId: userStore.userId,
      opinion: processForm.opinion
    })
    ElMessage.success('处理成功，已提交教务审核')
    processDialogVisible.value = false
    loadData()
  } finally {
    submitting.value = false
  }
}

const openTimeline = async (row) => {
  timelineVisible.value = true
  processLoading.value = true
  try {
    const { data } = await getAppealProcesses(row.appealId)
    processes.value = data
  } finally {
    processLoading.value = false
  }
}

onMounted(loadData)
</script>

<style scoped>
.timeline {
  padding-left: 4px;
}

.processor {
  color: #909399;
  font-size: 13px;
  margin: 4px 0;
}

.opinion {
  color: #606266;
  font-size: 14px;
}

.mb-20 {
  margin-bottom: 20px;
}
</style>
