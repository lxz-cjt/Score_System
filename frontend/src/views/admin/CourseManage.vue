<template>
  <div class="page-container">
    <el-card>
      <div class="table-toolbar">
        <el-input
          v-model="query.keyword"
          placeholder="按课程名称搜索"
          clearable
          style="width: 240px"
          :prefix-icon="Search"
          @change="handleSearch"
        />
        <el-button type="primary" :icon="Plus" @click="openDialog()">新增课程</el-button>
      </div>

      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="courseId" label="课程编号" width="140" />
        <el-table-column prop="courseName" label="课程名称" min-width="220" />
        <el-table-column prop="courseCredit" label="学分" width="100" align="center" />
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

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑课程' : '新增课程'" width="480px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="课程编号" prop="courseId">
          <el-input v-model="form.courseId" :disabled="isEdit" placeholder="如 CS111" />
        </el-form-item>
        <el-form-item label="课程名称" prop="courseName">
          <el-input v-model="form.courseName" placeholder="请输入课程名称" />
        </el-form-item>
        <el-form-item label="学分" prop="courseCredit">
          <el-input-number v-model="form.courseCredit" :min="1" :max="30" style="width: 100%" />
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
import { Plus, Search } from '@element-plus/icons-vue'
import { createCourse, deleteCourse, getCourses, updateCourse } from '../../api/course'

const tableData = ref([])
const total = ref(0)
const loading = ref(false)
const query = reactive({ keyword: '', page: 1, size: 10 })

const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref()
const submitting = ref(false)
const form = reactive({ courseId: '', courseName: '', courseCredit: 3 })

const rules = {
  courseId: [{ required: true, message: '请输入课程编号', trigger: 'blur' }],
  courseName: [{ required: true, message: '请输入课程名称', trigger: 'blur' }],
  courseCredit: [{ required: true, message: '请输入学分', trigger: 'blur' }]
}

const loadData = async () => {
  loading.value = true
  try {
    const { data } = await getCourses({ ...query, keyword: query.keyword || undefined })
    tableData.value = data.records
    total.value = data.total
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  query.page = 1
  loadData()
}

const openDialog = (row) => {
  isEdit.value = Boolean(row)
  Object.assign(form, row
    ? { courseId: row.courseId, courseName: row.courseName, courseCredit: row.courseCredit }
    : { courseId: '', courseName: '', courseCredit: 3 })
  dialogVisible.value = true
}

const handleSubmit = async () => {
  await formRef.value.validate()
  submitting.value = true
  try {
    if (isEdit.value) {
      await updateCourse(form.courseId, form)
      ElMessage.success('更新成功')
    } else {
      await createCourse(form)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    loadData()
  } finally {
    submitting.value = false
  }
}

const handleDelete = async (row) => {
  await ElMessageBox.confirm(
    `确定删除课程「${row.courseName}」吗？若存在成绩或课程安排将无法删除。`,
    '删除确认',
    { type: 'warning', confirmButtonText: '确定删除', cancelButtonText: '取消' }
  )
  await deleteCourse(row.courseId)
  ElMessage.success('删除成功')
  loadData()
}

onMounted(loadData)
</script>
