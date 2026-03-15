import { createRouter, createWebHistory } from 'vue-router'
// import HomeView from '../views/HomeView.vue'

//路由实例--管理路由与组件的映射关系
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: () => import('../views/layout/index.vue'),
      redirect: 'index', //重定向至index子路由
      children: [
        {
          path: 'index',
          name: 'index',
          component: () => import('../views/index/index.vue'), //首页组件
        },
        {
          path: 'emp',
          name: 'emp',
          component: () => import('../views/emp/index.vue'), //员工管理组件
        },
        {
          path: 'dept',
          name: 'dept',
          component: () => import('../views/dept/index.vue'), //部门管理组件
        },
        {
          path: 'clazz',
          name: 'clazz',
          component: () => import('../views/clazz/index.vue'), //班级管理组件
        },
        {
          path: 'stu',
          name: 'stu',
          component: () => import('../views/stu/index.vue'), //学员管理组件
        },
        {
          path: 'log',
          name: 'log',
          component:()=>import('../views/log/index.vue')
        },
        {
          path: '/report',
          name: '/report',
          children: [
            {
              path: 'emp',
              name: 'empRe',
              component: () => import('../views/report/emp/index.vue'),
            },
            {
              path:'stu',
              name:'stuRe',
              component:()=>import('../views/report/stu/index.vue')
            }
          ],
        },
      ],
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/login/index.vue'), //登录页面
    },
  ],
})

export default router
