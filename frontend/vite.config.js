import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],
  server: {
    port: 5173,
    proxy: {
      // 开发环境将 /api 代理到本地后端，避免跨域
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  },
  build: {
    rollupOptions: {
      output: {
        // 第三方依赖拆分为独立 chunk，利用浏览器缓存
        manualChunks: {
          'element-plus': ['element-plus', '@element-plus/icons-vue'],
          echarts: ['echarts'],
          vendor: ['vue', 'vue-router', 'pinia', 'axios']
        }
      }
    }
  }
})
