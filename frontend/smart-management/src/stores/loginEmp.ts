import { computed, ref } from 'vue'
import { defineStore } from 'pinia'
import type { LoginInfo } from '@/api/model/model'

// Store对象--通过defineStore创建--用来管理所有的共享状态
// 第一个参数==>counter：一个唯一标识
// 第二个参数==>定义函数
// 第三个参数==>{ persist: true }声明要持久化
export const useLoginEmpStore = defineStore(
  'loginEmpInfo',
  () => {
    // 1.定义一个对象--存储所有用户登录信息
    const loginEmp = ref<LoginInfo>({})

    // 2.存储用户登录信息---loginEMmp对象数据
    const setLoginEmp = (loginInfo: LoginInfo) => {
      loginEmp.value = loginInfo
    }

    // 3.获取用户登录信息---loginEMmp对象数据
    const getLoginEmp = () => {
      return loginEmp.value
    }

    // 4.清除用户登录信息---loginEMmp对象数据
    const removeLoginEmp = () => {
      loginEmp.value = {}
    }

     // ====== 权限相关计算属性（核心） ======

    // 是否登录
    const isLogin = computed(() => !!loginEmp.value.token)

    // 是否管理员
    const isAdmin = computed(() => loginEmp.value.role === 'ADMIN')

    // 是否教师
    const isTeacher = computed(() => loginEmp.value.role === 'TEACHER')
    // return { loginEmp, setLoginEmp, getLoginEmp, removeLoginEmp }
    return {
      loginEmp,
      setLoginEmp,
      getLoginEmp,
      removeLoginEmp,
      isLogin,
      isAdmin,
      isTeacher
    }
  },
  { persist: true },
)
