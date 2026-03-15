import request from '@/utils/request'
import type { PageResultModel } from './model/model'

// 获取自动分析结果
export const autoAnalysisApi = () => request.get<any, PageResultModel>('/api/autoAnalysis')

// 获取详细分析报告
export const detailedAnalysisApi = () => request.get<any, PageResultModel>('/api/detailedAnalysis')
