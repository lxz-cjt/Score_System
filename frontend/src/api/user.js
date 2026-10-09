import request from './request'

/** 学生管理（教务） */
export const getStudents = (params) => request.get('/api/students', { params })
export const createStudent = (data) => request.post('/api/students', data)
export const updateStudent = (id, data) => request.put(`/api/students/${id}`, data)
export const deleteStudent = (id) => request.delete(`/api/students/${id}`)

/** 教师管理（教务） */
export const getTeachers = (params) => request.get('/api/teachers', { params })
export const createTeacher = (data) => request.post('/api/teachers', data)
export const updateTeacher = (id, data) => request.put(`/api/teachers/${id}`, data)
export const deleteTeacher = (id) => request.delete(`/api/teachers/${id}`)

/** 教务人员管理（教务） */
export const getStaffList = (params) => request.get('/api/staff', { params })
export const createStaff = (data) => request.post('/api/staff', data)
export const updateStaff = (id, data) => request.put(`/api/staff/${id}`, data)
export const deleteStaff = (id) => request.delete(`/api/staff/${id}`)
