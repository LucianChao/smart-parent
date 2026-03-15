import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'

// 导入Element Plus及其样式
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import zhCn from "element-plus/es/locale/lang/zh-cn";
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
// 导入Pinia插件（持久化）
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'

// 创建vue应用实例
const app = createApp(App)

// 注册ElementPlus的Icon组件
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

// 创建pinia实例
const pinia=createPinia()
// 为pinia挂载插件
pinia.use(piniaPluginPersistedstate)
//应用pinia、router、element plus插件
app.use(pinia)
app.use(router)
app.use(ElementPlus,{locale:zhCn})

//挂载应用
app.mount('#app')
