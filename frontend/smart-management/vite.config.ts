import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueJsx from '@vitejs/plugin-vue-jsx'
import vueDevTools from 'vite-plugin-vue-devtools'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    vueJsx(),
    vueDevTools(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
  server:{
    // 配置反向代理
    proxy:{
      // http://127.0.0.1:5173/api/depts --> http://localhost:8080/depts
      '/api':{
        target:'http://localhost:8080',//目标地址
        secure:false,//如果是https接口，需要配置这个参数
        changeOrigin:true,//是否跨域
        rewrite:(path)=>path.replace(/^\/api/,'')//重写路径
      }
    }
  }
})
