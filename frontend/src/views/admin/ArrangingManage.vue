<template>
  <div class="page-container">
    <el-card>
      <div class="table-toolbar">
        <div class="filters">
          <el-select
            v-model="query.courseId"
            placeholder="按课程过滤"
            clearable
            filterable
            style="width: 220px"
            @change="loadData"
          >
            <el-option v-for="c in courseOptions" :key="c.courseId" :label="c.courseName" :value="c.courseId" />
          </el-select>
          <el-select
            v-model="query.teacherId"
            placeholder="按教师过滤"
            clearable
            filterable
            style="width: 220px"
            @change="loadData"
          >
            <el-option v-for="t in teacherOptions" :key="t.teacherId" :label="t.teacherName" :value="t.teacherId" />
          </el-select>
        </div>
        <el-button type="primary" :icon="Plus" @click="openDialog()">新增安排</el-button>
      </div>

      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="arrangingId" label="安排编号" width="140" />
        <el-table-column prop="courseName" label="课程" min-width="150">
          <template #default="{ row }">{{ row.courseName }}（{{ row.courseId }}）</template>
        </el-table-column>
        <el-table-column prop="courseCredit" label="学分" width="70" align="center" />
        <el-table-column prop="teacherName" label="授课教师" width="120">
          <template #default="{ row }">{{ row.teacherName }}（{{ row.teacherId }}）</template>
        </el-table-column>
        <el-table-column prop="classTime" label="上课时间" min-width="170" />
        <el-table-column prop="classroom" label="上课地点" width="130" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="openDialog(row)">编辑</el-button>
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

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑课程安排' : '新增课程安排'" width="480px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="课程" prop="courseId">
          <el-select v-model="form.courseId" filterable placeholder="选择课程" style="width: 100%">
            <el-option
              v-for="c in courseOptions"
              :key="c.courseId"
              :label="`${c.courseName}（${c.courseId}）`"
              :value="c.courseId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="教师" prop="teacherId">
          <el-select v-model="form.teacherId" filterable placeholder="选择教师" style="width: 100%">
            <el-option
              v-for="t in teacherOptions"
              :key="t.teacherId"
              :label="`${t.teacherName}（${t.teacherId}）`"
              :value="t.teacherId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="上课时间" prop="classTime">
          <el-input v-model="form.classTime" placeholder="如 周一 1-2节, 周三 3-4节" />
        </el-form-item>
        <el-form-item label="上课地点" prop="classroom">
          <el-input v-model="form.classroom" placeholder="如 计算机楼101" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitting" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import {
  createArrangement,
  deleteArrangement,
  getArrangements,
  getCourses,
  updateArrangement
} from '../../api/course'
import { getTeachers } from '../../api/user'

const tableData = ref([])
const total = ref(0)
const loading = ref(false)
const query = reactive({ courseId: '', teacherId: '', page: 1, size: 10 })

const courseOptions = ref([])
const teacherOptions = ref([])

const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref()
const submitting = ref(false)
const form = reactive({ arrangingId: '', courseId: '', teacherId: '', classTime: '', classroom: '' })

const rules = {
  courseId: [{ required: true, message: '请选择课程', trigger: 'change' }],
  teacherId: [{ required: true, message: '请选择教师', trigger: 'change' }],
  classTime: [{ required: true, message: '请输入上课时间', trigger: 'blur' }],
  classroom: [{ required: true, message: '请输入上课地点', trigger: 'blur' }]
}

const loadData = async () => {
  loading.value = true
  try {
    const { data } = await getArrangements({
      ...query,
      courseId: query.courseId || undefined,
      teacherId: query.teacherId || undefined
    })
    tableData.value = data.records
    total.value = data.total
  } finally {
    loading.value = false
  }
}

const loadOptions = async () => {
  const [{ data: courses }, { data: teachers }] = await Promise.all([
    getCourses({ page: 1, size: 100 }),
    getTeachers({ page: 1, size: 100 })
  ])
  courseOptions.value = courses.records
  teacherOptions.value = teachers.records
}

const openDialog = (row) => {
  isEdit.value = Boolean(row)
  Object.assign(form, row
    ? {
        arrangingId: row.arrangingId,
        courseId: row.courseId,
        teacherId: row.teacherId,
        classTime: row.classTime,
        classroom: row.classroom
      }
    : { arrangingId: '', courseId: '', teacherId: '', classTime: '', classroom: '' })
  dialogVisible.value = true
}

const handleSubmit = async () => {
  await formRef.value.validate()
  submitting.value = true
  try {
    const payload = {
      courseId: form.courseId,
      teacherId: form.teacherId,
      classTime: form.classTime,
      classroom: form.classroom
    }
    if (isEdit.value) {
      await updateArrangement(form.arrangingId, payload)
      ElMessage.success('更新成功')
    } else {
      await createArrangement(payload)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    loadData()
  } finally {
    submitting.value = false
  }
}

const handleDelete = async (row) => {
  await ElMessageBox.confirm(`确定删除课程「${row.courseName}」的该安排吗？`, '删除确认', {
    type: 'warning',
    confirmButtonText: '确定删除',
    cancelButtonText: '取消'
  })
  await deleteArrangement(row.arrangingId)
  ElMessage.success('删除成功')
  loadData()
}

onMounted(() => {
  loadData()
  loadOptions()
})
</script>

<style scoped>
.filters {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}
</style>
