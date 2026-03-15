import request from '@/utils/request'
import type { PageResultModel } from './model/model'

// 分页查询
export const queryStuPageApi = (
  name: string,
  degree: number,
  clazzId: number,
  page: number,
  pageSize: number,
) =>
  request.get<any, PageResultModel>(
    `/students?name=${name}&degree=${degree}&clazzId=${clazzId}&page=${page}&pageSize=${pageSize}`,
  )

// 新增学员
export const addStuApi = (data) => request.post<any, PageResultModel>(`/students`, data)

//根据id查询
export const getStuByIdApi = (id) => request.get<any, PageResultModel>(`/students/${id}`)

// 删除学员
export const deleteStuApi = (ids) => request.delete<any, PageResultModel>(`/students/${ids}`)

// 修改学员
export const updateStuApi = (data) => request.put<any, PageResultModel>(`/students`, data)

// 违纪
export const violationApi = (id, score) =>
  request.put<any, PageResultModel>(`/students/violation/${id}/${score}`)
