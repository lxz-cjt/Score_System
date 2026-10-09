import { defineStore } from 'pinia'

const STORAGE_KEY = 'score-system-user'

/**
 * 登录用户状态：Pinia 管理 + localStorage 持久化
 */
export const useUserStore = defineStore('user', {
  state: () => {
    const cached = localStorage.getItem(STORAGE_KEY)
    return cached
      ? JSON.parse(cached)
      : { token: '', user: null }
  },

  getters: {
    isLoggedIn: (state) => Boolean(state.token),
    role: (state) => state.user?.role || '',
    userId: (state) => state.user?.id || '',
    userName: (state) => state.user?.name || '',
    roleName: (state) => state.user?.roleName || ''
  },

  actions: {
    setLogin(token, user) {
      this.token = token
      this.user = user
      localStorage.setItem(STORAGE_KEY, JSON.stringify({ token, user }))
    },

    logout() {
      this.token = ''
      this.user = null
      localStorage.removeItem(STORAGE_KEY)
    }
  }
})
