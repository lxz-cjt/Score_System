import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '../store/user'

/**
 * 路由表：meta.roles 声明可访问角色，未登录访问受限页面会跳转登录页
 */
const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/login/LoginPage.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/',
    component: () => import('../layout/AdminLayout.vue'),
    redirect: () => {
      const userStore = useUserStore()
      return `/${userStore.role || 'student'}`
    },
    children: [
      // ==================== 学生端 ====================
      {
        path: 'student',
        name: 'StudentDashboard',
        component: () => import('../views/student/StudentDashboard.vue'),
        meta: { title: '个人主页', roles: ['student'] }
      },
      {
        path: 'student/scores',
        name: 'MyScores',
        component: () => import('../views/student/MyScores.vue'),
        meta: { title: '我的成绩', roles: ['student'] }
      },
      {
        path: 'student/courses',
        name: 'MyCourses',
        component: () => import('../views/student/CourseList.vue'),
        meta: { title: '课程查询', roles: ['student'] }
      },
      {
        path: 'student/appeals',
        name: 'MyAppeals',
        component: () => import('../views/student/MyAppeals.vue'),
        meta: { title: '我的申诉', roles: ['student'] }
      },
      // ==================== 教师端 ====================
      {
        path: 'teacher',
        name: 'TeacherDashboard',
        component: () => import('../views/teacher/TeacherDashboard.vue'),
        meta: { title: '工作台', roles: ['teacher'] }
      },
      {
        path: 'teacher/courses',
        name: 'TeacherCourses',
        component: () => import('../views/teacher/MyCourses.vue'),
        meta: { title: '我的课程', roles: ['teacher'] }
      },
      {
        path: 'teacher/scores',
        name: 'ScoreManage',
        component: () => import('../views/teacher/ScoreManage.vue'),
        meta: { title: '成绩管理', roles: ['teacher'] }
      },
      {
        path: 'teacher/score-logs',
        name: 'ScoreLogs',
        component: () => import('../views/teacher/ScoreLogs.vue'),
        meta: { title: '成绩日志', roles: ['teacher'] }
      },
      {
        path: 'teacher/appeals',
        name: 'AppealHandle',
        component: () => import('../views/teacher/AppealHandle.vue'),
        meta: { title: '申诉处理', roles: ['teacher'] }
      },
      // ==================== 教务端 ====================
      {
        path: 'admin',
        name: 'AdminDashboard',
        component: () => import('../views/admin/AdminDashboard.vue'),
        meta: { title: '数据看板', roles: ['staff'] }
      },
      {
        path: 'admin/students',
        name: 'StudentManage',
        component: () => import('../views/admin/StudentManage.vue'),
        meta: { title: '学生管理', roles: ['staff'] }
      },
      {
        path: 'admin/teachers',
        name: 'TeacherManage',
        component: () => import('../views/admin/TeacherManage.vue'),
        meta: { title: '教师管理', roles: ['staff'] }
      },
      {
        path: 'admin/staff',
        name: 'StaffManage',
        component: () => import('../views/admin/StaffManage.vue'),
        meta: { title: '教务人员管理', roles: ['staff'] }
      },
      {
        path: 'admin/courses',
        name: 'CourseManage',
        component: () => import('../views/admin/CourseManage.vue'),
        meta: { title: '课程管理', roles: ['staff'] }
      },
      {
        path: 'admin/arrangements',
        name: 'ArrangingManage',
        component: () => import('../views/admin/ArrangingManage.vue'),
        meta: { title: '课程安排', roles: ['staff'] }
      },
      {
        path: 'admin/appeals',
        name: 'AppealReview',
        component: () => import('../views/admin/AppealReview.vue'),
        meta: { title: '申诉审核', roles: ['staff'] }
      }
    ]
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/'
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 全局路由守卫：登录校验 + 角色校验
router.beforeEach((to) => {
  const userStore = useUserStore()
  document.title = to.meta.title ? `${to.meta.title} - 高校成绩管理系统` : '高校成绩管理系统'

  if (to.path === '/login') {
    // 已登录用户访问登录页时跳回首页
    return userStore.isLoggedIn ? '/' : true
  }
  if (!userStore.isLoggedIn) {
    return { path: '/login', query: { redirect: to.fullPath } }
  }
  if (to.meta.roles && !to.meta.roles.includes(userStore.role)) {
    // 无权访问时跳回自己的首页
    return `/${userStore.role}`
  }
  return true
})

export default router
