// 封装ts代码--主要用于请求后端业务
// 1.导入axios request实例
import request from '@/utils/request'
import type { PageResultModel } from './model/model'

// 分页条件查询
export const queryPageApi = (
  begin: string,
  end: string,
  gender: string,
  name: string,
  page: number,
  pageSize: number,
) =>
  request.get<any, PageResultModel>(
    `/emps?begin=${begin}&end=${end}&gender=${gender}&name=${name}&page=${page}&pageSize=${pageSize}`,
  )
// 添加员工
export const addEmpApi = (data) => request.post<any,PageResultModel>('/emps', data)

// 根据ID查询员工信息
export const queryInfoApi = (id) => request.get<any, PageResultModel>(`/emps/${id}`)

// 修改员工信息
export const updateEmpApi = (data) => request.put<any, PageResultModel>('/emps',data)

// 删除员工
export const deleteEmpApi = (ids) => request.delete<any, PageResultModel>(`/emps?ids=${ids}`)

// 查询全部员工
export const queryAllEmpApi = ()=> request.get<any,PageResultModel>(`/emps/list`)

