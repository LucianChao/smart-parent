import request from '@/utils/request'
import type { PageResultModel } from './model/model'

// 日志请求
export const LogApi = (page: number, pageSize: number) =>
  request.get<any, PageResultModel>(`/log/page?page=${page}&pageSize=${pageSize}`)
