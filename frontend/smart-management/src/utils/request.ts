import router from '@/router'
import { useLoginEmpStore } from '@/stores/loginEmp'
import axios from 'axios'
import { ElMessage } from 'element-plus'

const store = useLoginEmpStore()
//创建axios实例
const request = axios.create({
  baseURL: '/api', //基础路径
  timeout: 600000, //请求超时时间
})

// 添加请求拦截器
request.interceptors.request.use(
  function (config) {
    // 发起请求前
    // 将令牌存入到请求头中
    const loginEmp = store.getLoginEmp()
    if (loginEmp && loginEmp.token) {
      config.headers['token'] = loginEmp.token
    }

    return config
  },
  function (error) {
    // 请求错误处理
    return Promise.reject(error)
  },
)

//axios的响应 response 请求拦截器
request.interceptors.response.use(
  (Response) => {
    //响应成功
    return Response.data
  },
  (error) => {
    if (error.response.status == 401) {
      // 通过路由进行跳转到登录页面
      ElMessage.error('登录失效，请重新登录！')
      router.push('/login')
    }else{
      ElMessage.error('接口访问异常')
    }
    //响应失败
    return Promise.reject(error)
  },
)

export default request
