import request from './request'

/** 登录 */
export const login = (data) => request.post('/api/auth/login', data)
