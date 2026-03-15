// 封装ts代码--主要用于请求后端业务
// 1.导入axios request实例
import request from '@/utils/request'
import type { DeptModel, ResultModel } from './model/model'

// 部门列表查询
export const queryAllApi = () => request.get<any, ResultModel>('/depts')

// 部门列表查询
export const addApi = (dept: DeptModel) => request.post<any, ResultModel>('/depts',dept)

// 根据id查询部门
export const getDeptApi = (id: number) => request.get<any, ResultModel>(`/depts/${id}`)

// 根据id修改部门
export const updateApi = (dept: DeptModel) => request.put<any, ResultModel>(`/depts`,dept)

// 根据id删除部门
export const deleteApi = (id: number) => request.delete<any, ResultModel>(`/depts?id=${id}`)
