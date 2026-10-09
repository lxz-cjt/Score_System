<template>
  <div class="page-container">
    <el-card>
      <el-tabs v-model="activeTab" @tab-change="handleTabChange">
        <el-tab-pane label="待审核" name="SUBMITTED_TO_ADMIN" />
        <el-tab-pane label="全部申诉" name="all" />
      </el-tabs>

      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="appealId" label="申诉编号" width="140" />
        <el-table-column label="学生" width="140">
          <template #default="{ row }">{{ row.studentName }}（{{ row.studentId }}）</template>
        </el-table-column>
        <el-table-column label="课程" min-width="140">
          <template #default="{ row }">{{ row.courseName }}（{{ row.courseId }}）</template>
        </el-table-column>
        <el-table-column prop="teacherName" label="处理教师" width="100" />
        <el-table-column prop="appealReason" label="申诉理由" min-width="160" show-overflow-tooltip />
        <el-table-column label="状态" width="110" align="center">
          <template #default="{ row }">
            <el-tag :type="statusTag(row.appealStatus)" size="small">{{ row.statusLabel }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="openTimeline(row)">过程</el-button>
            <el-button
              v-if="row.appealStatus === 'SUBMITTED_TO_ADMIN'"
              link
              type="danger"
              size="small"
              @click="openReviewDialog(row)"
            >
              审核
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

    <!-- 审核对话框 -->
    <el-dialog v-model="reviewDialogVisible" title="申诉审核" width="560px">
      <el-descriptions :column="1" border class="mb-20">
        <el-descriptions-item label="学生">{{ currentRow.studentName }}（{{ currentRow.studentId }}）</el-descriptions-item>
        <el-descriptions-item label="课程">{{ currentRow.courseName }}</el-descriptions-item>
        <el-descriptions-item label="申诉理由">{{ currentRow.appealReason }}</el-descriptions-item>
      </el-descriptions>

      <!-- 教师处理记录 -->
      <div v-if="teacherOpinion" class="teacher-opinion mb-20">
        <span class="label">教师处理意见：</span>{{ teacherOpinion }}
      </div>

      <el-form ref="reviewFormRef" :model="reviewForm" :rules="reviewRules" label-width="90px">
        <el-form-item label="审核结论" prop="approved">
          <el-radio-group v-model="reviewForm.approved">
            <el-radio :value="true" type="success">通过</el-radio>
            <el-radio :value="false">拒绝</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审核意见" prop="opinion">
          <el-input
            v-model="reviewForm.opinion"
            type="textarea"
            :rows="3"
            maxlength="500"
            show-word-limit
            placeholder="请填写审核意见"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="reviewDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitting" @click="handleReview">提交审核结果</el-button>
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
import { getAppealProcesses, getAppeals, reviewAppeal } from '../../api/appeal'
import { useUserStore } from '../../store/user'

const userStore = useUserStore()

const activeTab = ref('SUBMITTED_TO_ADMIN')
const tableData = ref([])
const total = ref(0)
const loading = ref(false)
const query = reactive({ page: 1, size: 10 })

const reviewDialogVisible = ref(false)
const reviewFormRef = ref()
const submitting = ref(false)
const reviewForm = reactive({ approved: true, opinion: '' })
const reviewRules = {
  opinion: [
    { required: true, message: '请填写审核意见', trigger: 'blur' },
    { min: 2, message: '审核意见至少2个字', trigger: 'blur' }
  ]
}
const currentRow = ref({})
const teacherOpinion = ref('')

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
    const { data } = await getAppeals({
      status: activeTab.value === 'all' ? undefined : activeTab.value,
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

const openReviewDialog = async (row) => {
  currentRow.value = row
  reviewForm.approved = true
  reviewForm.opinion = ''
  reviewDialogVisible.value = true
  // 带出教师的处理意见，便于审核参考
  teacherOpinion.value = ''
  const { data } = await getAppealProcesses(row.appealId)
  const teacherProcess = data.find((p) => p.processStep === '教师处理')
  teacherOpinion.value = teacherProcess?.processOpinion || ''
}

const handleReview = async () => {
  await reviewFormRef.value.validate()
  submitting.value = true
  try {
    await reviewAppeal(currentRow.value.appealId, {
      staffId: userStore.userId,
      approved: reviewForm.approved,
      opinion: reviewForm.opinion
    })
    ElMessage.success('审核完成')
    reviewDialogVisible.value = false
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
.teacher-opinion {
  padding: 10px 14px;
  background: #f4f4f5;
  border-radius: 4px;
  color: #606266;
  font-size: 14px;
}

.teacher-opinion .label {
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

.mb-20 {
  margin-bottom: 20px;
}
</style>
