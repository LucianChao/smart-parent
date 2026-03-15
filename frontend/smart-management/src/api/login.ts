import request from '@/utils/request'
import type { LoginEmp, ResultModel } from './model/model'

// 登录
export const loginApi = (loginEmp: LoginEmp) => request.post<any, ResultModel>(`/login`, loginEmp)

// 修改密码
export const updatePasswordApi = (oldPassword: string, newPassword: string) =>
  request.put<any, ResultModel>('/emps/updatePassword', { oldPassword, newPassword })
