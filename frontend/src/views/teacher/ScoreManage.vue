<template>
  <div class="page-container">
    <el-card>
      <div class="table-toolbar">
        <div class="filters">
          <el-select
            v-model="query.courseId"
            placeholder="按课程过滤"
            clearable
            style="width: 240px"
            @change="loadData"
          >
            <el-option
              v-for="course in myCourses"
              :key="course.courseId"
              :label="`${course.courseName}（${course.courseId}）`"
              :value="course.courseId"
            />
          </el-select>
          <el-input
            v-model="query.studentId"
            placeholder="按学号过滤"
            clearable
            style="width: 180px"
            @change="loadData"
          />
        </div>
        <el-button type="primary" :icon="Plus" @click="openCreateDialog">录入成绩</el-button>
      </div>

      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="studentId" label="学号" width="110" />
        <el-table-column prop="studentName" label="姓名" width="100" />
        <el-table-column prop="courseName" label="课程" min-width="150">
          <template #default="{ row }">{{ row.courseName }}（{{ row.courseId }}）</template>
        </el-table-column>
        <el-table-column prop="dailyScore" label="平时" width="70" align="center" />
        <el-table-column prop="examScore" label="考试" width="70" align="center" />
        <el-table-column label="总评" width="90" align="center">
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
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="openEditDialog(row)">修改</el-button>
            <el-button link type="danger" size="small" @click="handleDelete(row)">删除</el-button>
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

    <!-- 录入成绩对话框 -->
    <el-dialog v-model="createDialogVisible" title="录入成绩" width="480px">
      <el-alert type="info" :closable="false" class="mb-20"
        title="总评成绩 = 平时成绩 × 30% + 考试成绩 × 70%，由系统自动计算" />
      <el-form ref="createFormRef" :model="createForm" :rules="scoreRules" label-width="90px">
        <el-form-item label="学号" prop="studentId">
          <el-input v-model="createForm.studentId" placeholder="请输入学号，如 2021006" />
        </el-form-item>
        <el-form-item label="课程" prop="courseId">
          <el-select v-model="createForm.courseId" placeholder="选择课程" style="width: 100%">
            <el-option
              v-for="course in myCourses"
              :key="course.courseId"
              :label="`${course.courseName}（${course.courseId}）`"
              :value="course.courseId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="平时成绩" prop="dailyScore">
          <el-input-number v-model="createForm.dailyScore" :min="0" :max="100" style="width: 100%" />
        </el-form-item>
        <el-form-item label="考试成绩" prop="examScore">
          <el-input-number v-model="createForm.examScore" :min="0" :max="100" style="width: 100%" />
        </el-form-item>
        <el-form-item label="预计总评">
          <el-tag size="large" :type="previewTotal < 60 ? 'danger' : 'success'">{{ previewTotal }}</el-tag>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="createDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitting" @click="handleCreate">确定录入</el-button>
      </template>
    </el-dialog>

    <!-- 修改成绩对话框 -->
    <el-dialog v-model="editDialogVisible" title="修改成绩" width="480px">
      <el-form ref="editFormRef" :model="editForm" :rules="scoreRules" label-width="90px">
        <el-form-item label="学生">
          <span>{{ editingRow.studentName }}（{{ editingRow.studentId }}）</span>
        </el-form-item>
        <el-form-item label="课程">
          <span>{{ editingRow.courseName }}</span>
        </el-form-item>
        <el-form-item label="平时成绩" prop="dailyScore">
          <el-input-number v-model="editForm.dailyScore" :min="0" :max="100" style="width: 100%" />
        </el-form-item>
        <el-form-item label="考试成绩" prop="examScore">
          <el-input-number v-model="editForm.examScore" :min="0" :max="100" style="width: 100%" />
        </el-form-item>
        <el-form-item label="预计总评">
          <el-tag size="large" :type="editPreviewTotal < 60 ? 'danger' : 'success'">{{ editPreviewTotal }}</el-tag>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitting" @click="handleEdit">确定修改</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { getTeacherArrangements } from '../../api/course'
import { createScore, deleteScore, getScores, updateScore } from '../../api/score'
import { useUserStore } from '../../store/user'

const userStore = useUserStore()
const route = useRoute()

const myCourses = ref([])
const tableData = ref([])
const total = ref(0)
const loading = ref(false)
const query = reactive({ courseId: route.query.courseId || '', studentId: '', page: 1, size: 10 })

const createDialogVisible = ref(false)
const editDialogVisible = ref(false)
const createFormRef = ref()
const editFormRef = ref()
const submitting = ref(false)
const createForm = reactive({ studentId: '', courseId: '', dailyScore: 0, examScore: 0 })
const editForm = reactive({ dailyScore: 0, examScore: 0 })
const editingRow = ref({})

const scoreRules = {
  studentId: [{ required: true, message: '请输入学号', trigger: 'blur' }],
  courseId: [{ required: true, message: '请选择课程', trigger: 'change' }],
  dailyScore: [{ required: true, message: '请输入平时成绩', trigger: 'blur' }],
  examScore: [{ required: true, message: '请输入考试成绩', trigger: 'blur' }]
}

/** 与后端一致的本地总评预览：平时30% + 考试70% */
const calcTotal = (daily, exam) => Math.round((daily || 0) * 0.3 + (exam || 0) * 0.7)
const previewTotal = computed(() => calcTotal(createForm.dailyScore, createForm.examScore))
const editPreviewTotal = computed(() => calcTotal(editForm.dailyScore, editForm.examScore))

const conditionTag = (condition) => {
  return { 优秀: 'success', 通过: 'primary', 不通过: 'danger' }[condition] || 'info'
}

const loadData = async () => {
  loading.value = true
  try {
    const { data } = await getScores({
      ...query,
      courseId: query.courseId || undefined,
      studentId: query.studentId || undefined
    })
    tableData.value = data.records
    total.value = data.total
  } finally {
    loading.value = false
  }
}

const openCreateDialog = () => {
  Object.assign(createForm, { studentId: '', courseId: query.courseId || '', dailyScore: 0, examScore: 0 })
  createDialogVisible.value = true
}

const handleCreate = async () => {
  await createFormRef.value.validate()
  submitting.value = true
  try {
    await createScore({ ...createForm, operatorId: userStore.userId })
    ElMessage.success('成绩录入成功')
    createDialogVisible.value = false
    loadData()
  } finally {
    submitting.value = false
  }
}

const openEditDialog = (row) => {
  editingRow.value = row
  Object.assign(editForm, { dailyScore: row.dailyScore, examScore: row.examScore })
  editDialogVisible.value = true
}

const handleEdit = async () => {
  await editFormRef.value.validate()
  submitting.value = true
  try {
    await updateScore(editingRow.value.scoreId, { ...editForm, operatorId: userStore.userId })
    ElMessage.success('成绩修改成功')
    editDialogVisible.value = false
    loadData()
  } finally {
    submitting.value = false
  }
}

const handleDelete = async (row) => {
  await ElMessageBox.confirm(
    `确定删除 ${row.studentName} 的《${row.courseName}》成绩吗？删除将记录日志。`,
    '删除确认',
    { type: 'warning', confirmButtonText: '确定删除', cancelButtonText: '取消' }
  )
  await deleteScore(row.scoreId, userStore.userId)
  ElMessage.success('删除成功')
  loadData()
}

onMounted(async () => {
  loadData()
  const { data } = await getTeacherArrangements(userStore.userId)
  myCourses.value = data
})
</script>

<style scoped>
.card-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.filters {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.score-pass {
  font-weight: 600;
  color: #67c23a;
}

.score-fail {
  font-weight: 600;
  color: #f56c6c;
}

.mb-20 {
  margin-bottom: 20px;
}
</style>
