import request from './request'

/** 课程管理 */
export const getCourses = (params) => request.get('/api/courses', { params })
export const createCourse = (data) => request.post('/api/courses', data)
export const updateCourse = (id, data) => request.put(`/api/courses/${id}`, data)
export const deleteCourse = (id) => request.delete(`/api/courses/${id}`)

/** 课程安排 */
export const getArrangements = (params) => request.get('/api/course-arrangings', { params })
export const getTeacherArrangements = (teacherId) => request.get(`/api/course-arrangings/teacher/${teacherId}`)
export const createArrangement = (data) => request.post('/api/course-arrangings', data)
export const updateArrangement = (id, data) => request.put(`/api/course-arrangings/${id}`, data)
export const deleteArrangement = (id) => request.delete(`/api/course-arrangings/${id}`)
