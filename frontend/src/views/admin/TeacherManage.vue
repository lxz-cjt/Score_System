<template>
  <div class="page-container">
    <el-card>
      <div class="table-toolbar">
        <el-input
          v-model="query.keyword"
          placeholder="按姓名 / 学院搜索"
          clearable
          style="width: 240px"
          :prefix-icon="Search"
          @change="handleSearch"
        />
        <el-button type="primary" :icon="Plus" @click="openDialog()">新增教师</el-button>
      </div>

      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="teacherId" label="工号" width="120" />
        <el-table-column prop="teacherName" label="姓名" width="140" />
        <el-table-column prop="college" label="所属学院" min-width="200" />
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

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑教师' : '新增教师'" width="480px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="工号" prop="teacherId">
          <el-input v-model="form.teacherId" :disabled="isEdit" placeholder="如 T009" />
        </el-form-item>
        <el-form-item label="姓名" prop="teacherName">
          <el-input v-model="form.teacherName" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="所属学院" prop="college">
          <el-input v-model="form.college" placeholder="请输入学院" />
        </el-form-item>
        <el-form-item :label="isEdit ? '重置密码' : '初始密码'" prop="password">
          <el-input
            v-model="form.password"
            type="password"
            show-password
            :placeholder="isEdit ? '留空表示不修改密码' : '至少6位'"
          />
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
import { createTeacher, deleteTeacher, getTeachers, updateTeacher } from '../../api/user'

const tableData = ref([])
const total = ref(0)
const loading = ref(false)
const query = reactive({ keyword: '', page: 1, size: 10 })

const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref()
const submitting = ref(false)
const form = reactive({ teacherId: '', teacherName: '', college: '', password: '' })

const rules = {
  teacherId: [{ required: true, message: '请输入工号', trigger: 'blur' }],
  teacherName: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  college: [{ required: true, message: '请输入学院', trigger: 'blur' }],
  password: [
    {
      validator: (rule, value, callback) => {
        if (!isEdit.value && !value) {
          callback(new Error('新增教师时初始密码不能为空'))
        } else if (value && value.length < 6) {
          callback(new Error('密码至少6位'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

const loadData = async () => {
  loading.value = true
  try {
    const { data } = await getTeachers({ ...query, keyword: query.keyword || undefined })
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
    ? { teacherId: row.teacherId, teacherName: row.teacherName, college: row.college, password: '' }
    : { teacherId: '', teacherName: '', college: '', password: '' })
  dialogVisible.value = true
}

const handleSubmit = async () => {
  await formRef.value.validate()
  submitting.value = true
  try {
    const payload = { ...form, password: form.password || undefined }
    if (isEdit.value) {
      await updateTeacher(form.teacherId, payload)
      ElMessage.success('更新成功')
    } else {
      await createTeacher(payload)
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
    `确定删除教师「${row.teacherName}」吗？若存在课程安排将无法删除。`,
    '删除确认',
    { type: 'warning', confirmButtonText: '确定删除', cancelButtonText: '取消' }
  )
  await deleteTeacher(row.teacherId)
  ElMessage.success('删除成功')
  loadData()
}

onMounted(loadData)
</script>
