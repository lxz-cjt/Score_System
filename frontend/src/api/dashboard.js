import request from './request'

/** 各角色 Dashboard */
export const getStudentDashboard = (studentId) => request.get(`/api/dashboard/student/${studentId}`)
export const getTeacherDashboard = (teacherId) => request.get(`/api/dashboard/teacher/${teacherId}`)
export const getAdminDashboard = (staffId) => request.get(`/api/dashboard/admin/${staffId}`)
