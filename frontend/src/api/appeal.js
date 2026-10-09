import request from './request'

/** 申诉 */
export const getMyAppeals = (studentId) => request.get('/api/appeals/my', { params: { studentId } })
export const getTeacherAppeals = (params) => request.get('/api/appeals/teacher', { params })
export const getAppeals = (params) => request.get('/api/appeals', { params })
export const getAppealProcesses = (appealId) => request.get(`/api/appeals/${appealId}/processes`)
export const createAppeal = (data) => request.post('/api/appeals', data)
export const cancelAppeal = (appealId, data) => request.put(`/api/appeals/${appealId}/cancel`, data)
export const processAppeal = (appealId, data) => request.put(`/api/appeals/${appealId}/process`, data)
export const reviewAppeal = (appealId, data) => request.put(`/api/appeals/${appealId}/review`, data)
