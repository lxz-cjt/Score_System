import request from './request'

/** 成绩 */
export const getScores = (params) => request.get('/api/scores', { params })
export const getStudentScores = (studentId) => request.get(`/api/scores/student/${studentId}`)
export const getStudentSummary = (studentId) => request.get(`/api/scores/student/${studentId}/summary`)
export const createScore = (data) => request.post('/api/scores', data)
export const updateScore = (id, data) => request.put(`/api/scores/${id}`, data)
export const deleteScore = (id, operatorId) => request.delete(`/api/scores/${id}`, { params: { operatorId } })

/** 成绩日志 */
export const getScoreLogs = (params) => request.get('/api/score-logs', { params })
