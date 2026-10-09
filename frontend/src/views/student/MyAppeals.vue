<template>
  <div class="page-container">
    <el-card>
      <div class="table-toolbar">
        <span class="card-title">我的申诉</span>
        <el-button type="primary" :icon="Plus" @click="openCreateDialog">发起申诉</el-button>
      </div>

      <el-table :data="appeals" v-loading="loading" stripe>
        <el-table-column prop="appealId" label="申诉编号" width="150" />
        <el-table-column prop="courseName" label="课程" min-width="140">
          <template #default="{ row }">{{ row.courseName }}（{{ row.courseId }}）</template>
        </el-table-column>
        <el-table-column prop="appealReason" label="申诉理由" min-width="200" show-overflow-tooltip />
        <el-table-column label="状态" width="110" align="center">
          <template #default="{ row }">
            <el-tag :type="statusTag(row.appealStatus)" size="small">{{ row.statusLabel }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="teacherName" label="处理教师" width="100">
          <template #default="{ row }">{{ row.teacherName || '-' }}</template>
        </el-table-column>
        <el-table-column prop="appealTime" label="提交时间" width="170">
          <template #default="{ row }">{{ formatTime(row.appealTime) }}</template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="openTimeline(row)">处理过程</el-button>
            <el-button
              v-if="row.appealStatus === 'PENDING'"
              link
              type="danger"
              size="small"
              @click="handleCancel(row)"
            >
              取消申诉
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-empty v-if="!loading && appeals.length === 0" description="暂无申诉记录" />
    </el-card>

    <!-- 发起申诉对话框 -->
    <el-dialog v-model="createDialogVisible" title="发起成绩申诉" width="480px">
      <el-form ref="createFormRef" :model="createForm" :rules="createRules" label-width="90px">
        <el-form-item label="申诉课程" prop="courseId">
          <el-select v-model="createForm.courseId" placeholder="选择要申诉的课程" style="width: 100%">
            <el-option
              v-for="score in myScores"
              :key="score.courseId"
              :label="`${score.courseName}（总评 ${score.totalScore} 分）`"
              :value="score.courseId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="申诉理由" prop="reason">
          <el-input
            v-model="createForm.reason"
            type="textarea"
            :rows="4"
            maxlength="500"
            show-word-limit
            placeholder="请说明申诉理由（500字以内）"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="createDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitting" @click="handleCreate">提交申诉</el-button>
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
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { cancelAppeal, createAppeal, getAppealProcesses, getMyAppeals } from '../../api/appeal'
import { getStudentScores } from '../../api/score'
import { useUserStore } from '../../store/user'

const userStore = useUserStore()

const appeals = ref([])
const myScores = ref([])
const loading = ref(false)

const createDialogVisible = ref(false)
const createFormRef = ref()
const submitting = ref(false)
const createForm = reactive({ courseId: '', reason: '' })
const createRules = {
  courseId: [{ required: true, message: '请选择申诉课程', trigger: 'change' }],
  reason: [
    { required: true, message: '请填写申诉理由', trigger: 'blur' },
    { min: 5, message: '申诉理由至少5个字', trigger: 'blur' }
  ]
}

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
    const { data } = await getMyAppeals(userStore.userId)
    appeals.value = data
  } finally {
    loading.value = false
  }
}

const openCreateDialog = async () => {
  createForm.courseId = ''
  createForm.reason = ''
  createDialogVisible.value = true
  if (myScores.value.length === 0) {
    const { data } = await getStudentScores(userStore.userId)
    myScores.value = data
  }
}

const handleCreate = async () => {
  await createFormRef.value.validate()
  submitting.value = true
  try {
    await createAppeal({ studentId: userStore.userId, ...createForm })
    ElMessage.success('申诉提交成功，等待教师处理')
    createDialogVisible.value = false
    loadData()
  } finally {
    submitting.value = false
  }
}

const handleCancel = async (row) => {
  const { value: reason } = await ElMessageBox.prompt('请输入取消原因', '取消申诉', {
    confirmButtonText: '确定取消',
    cancelButtonText: '再想想',
    inputPlaceholder: '例如：已与教师沟通解决',
    inputValidator: (value) => (value && value.trim().length >= 2) || '取消原因至少2个字'
  })
  await cancelAppeal(row.appealId, { studentId: userStore.userId, reason: reason.trim() })
  ElMessage.success('申诉已取消')
  loadData()
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
.card-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

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
</style>
