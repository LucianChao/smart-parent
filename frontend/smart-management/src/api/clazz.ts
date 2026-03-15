import request from '@/utils/request'
import type { PageResultModel } from './model/model'
import { id } from 'element-plus/es/locales.mjs'

// 分页条件查询/列表查询
export const queryClazzPageApi = (
  name: string,
  begin: string,
  end: string,
  page: number,
  pageSize: number,
) =>
  request.get<any, PageResultModel>(
    `/clazzs?name=${name}&begin=${begin}&end=${end}&page=${page}&pageSize=${pageSize}`,
  )

// 添加班级
export const addClazzApi = (data) => request.post<any, PageResultModel>(`/clazzs`, data)

// 修改班级
export const updateClazzApi = (data) => request.put<any, PageResultModel>(`/clazzs`, data)

// 根据id查询
export const getClazzByIdApi = (id) => request.get<any, PageResultModel>(`/clazzs/${id}`)

// 根据id删除班级
export const deleteCLazzByIdApi = (id) => request.delete<any,PageResultModel>(`/clazzs/${id}`)

// 查询所有班级
export const queryAllClazzApi = ()=> request.get<any,PageResultModel>(`/clazzs/list`)
