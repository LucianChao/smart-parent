<script setup lang="ts">
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { autoAnalysisApi, detailedAnalysisApi } from '@/api/analysis'
import { marked } from 'marked'

// AI 分析结果
const aiReport = ref<string>('')
const aiReportHtml = ref<string>('')
const autoAnalysisSummary = ref<string>('')
const autoAnalysisReport = ref<string>('')
const analysisMode = ref<'auto' | 'detailed'>('detailed')
const loading = ref(false)

// 辅助函数：提取 AI 报告中的实际 Markdown 文本
const extractMarkdownText = (data: unknown): string => {
  // 如果已经是字符串
  if (typeof data === 'string') {
    try {
      // 尝试解析为 JSON
      const parsed = JSON.parse(data)
      // 检查是否是 LLM 返回的格式
      if (parsed?.output?.text) {
        return parsed.output.text
      }
      return data
    } catch {
      return data
    }
  }

  // 如果是对象，检查是否有 output.text 字段
  if (data && typeof data === 'object' && 'output' in data) {
    const obj = data as Record<string, unknown>
    if (obj.output && typeof obj.output === 'object' && 'text' in obj.output) {
      return String((obj.output as Record<string, unknown>).text)
    }
  }

  return ''
}

// 自动分析（快速分析）
const runAutoAnalysis = async () => {
  loading.value = true
  try {
    const result = await autoAnalysisApi()
    console.log('autoAnalysis result:', result)
    if (result.code === 1 || result.code > 0) {
      const analysisData = result.data || {}
      autoAnalysisSummary.value = analysisData.summary || ''
      autoAnalysisReport.value = extractMarkdownText(analysisData.report) || ''
      analysisMode.value = 'auto'
      aiReport.value = `【分析摘要】\n${autoAnalysisSummary.value}\n\n【AI报告】\n${autoAnalysisReport.value}`
      // 渲染 HTML
      try {
        const html = await marked(aiReport.value)
        aiReportHtml.value = html
      } catch (err) {
        console.warn('Markdown render error:', err)
        aiReportHtml.value = ''
      }
      ElMessage.success('自动分析完成')
    } else {
      ElMessage.error(`自动分析失败: ${result.msg}`)
      console.warn('Analysis failed - code:', result.code, 'message:', result.msg)
    }
  } catch (e) {
    console.error('autoAnalysis error:', e)
    ElMessage.error('自动分析异常，请检查网络连接')
  } finally {
    loading.value = false
  }
}

// 详细分析（深度分析）
const runDetailedAnalysis = async () => {
  loading.value = true
  aiReport.value = ''
  aiReportHtml.value = ''
  try {
    const result = await detailedAnalysisApi()
    console.log('detailedAnalysis result:', result)
    if (result.code === 1 || result.code > 0) {
      analysisMode.value = 'detailed'
      // 提取实际的 Markdown 文本
      const reportText = extractMarkdownText(result.data)
      aiReport.value = reportText
      // 渲染 HTML
      try {
        const html = await marked(reportText)
        aiReportHtml.value = html
      } catch (err) {
        console.warn('Markdown render error:', err)
        aiReportHtml.value = ''
      }
      ElMessage.success('详细分析完成')
    } else {
      ElMessage.error(`详细分析失败: ${result.msg}`)
      console.warn('Analysis failed - code:', result.code, 'message:', result.msg)
    }
  } catch (e) {
    console.error('detailedAnalysis error:', e)
    ElMessage.error('详细分析异常，请检查网络连接')
  } finally {
    loading.value = false
  }
}

// 清空报告
const clearReport = () => {
  aiReport.value = ''
  aiReportHtml.value = ''
  autoAnalysisSummary.value = ''
  autoAnalysisReport.value = ''
}
</script>

<template>
  <div class="home">

    <!-- 顶部欢迎 -->
    <el-card class="welcome-card">
      <div class="welcome">
        <div>
          <h2>🎓 智能学习辅助系统</h2>
          <p>基于全量数据的大模型智能分析</p>
        </div>
        <div class="button-group">
          <el-button type="success" size="large" :loading="loading" @click="runAutoAnalysis">
            ⚡ 快速分析
          </el-button>
          <el-button type="primary" size="large" :loading="loading" @click="runDetailedAnalysis">
            🤖 深度分析
          </el-button>
          <el-button v-if="aiReport" type="info" size="large" @click="clearReport">
            🗑️ 清空报告
          </el-button>
        </div>
      </div>
    </el-card>

    <!-- AI 报告 -->
    <el-card v-if="aiReport" class="report-card">
      <template #header>
        <div class="report-header">
          <span>📊 AI 智能分析报告</span>
          <el-tag v-if="analysisMode === 'auto'" type="success">快速分析</el-tag>
          <el-tag v-else type="primary">深度分析</el-tag>
        </div>
      </template>

      <el-scrollbar height="400px">
        <!-- Markdown HTML 渲染模式 -->
        <div v-if="aiReportHtml" class="report-markdown" v-html="aiReportHtml"></div>
        <!-- 纯文本备用模式 -->
        <pre v-else class="report-text">{{ aiReport }}</pre>
      </el-scrollbar>
    </el-card>

    <!-- 原首页图片 -->
    <el-card class="image-card">
      <img src="@/assets/index.png" class="home-img" />
    </el-card>

  </div>
</template>

<style scoped>
.home {
  padding: 20px;
}

.welcome-card {
  margin-bottom: 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
}

.welcome-card :deep(.el-card__body) {
  background: transparent;
}

.welcome {
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: white;
}

.welcome h2 {
  margin: 0 0 8px 0;
  font-size: 28px;
}

.welcome p {
  margin: 0;
  font-size: 14px;
  opacity: 0.9;
}

.button-group {
  display: flex;
  gap: 12px;
}

.report-card {
  margin-bottom: 20px;
}

.report-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.report-content {
  padding: 16px;
}

.report-text {
  white-space: pre-wrap;
  word-wrap: break-word;
  line-height: 1.8;
  font-size: 13px;
  font-family: "Courier New", monospace;
  color: #333;
  margin: 0;
  background-color: #f9f9f9;
  padding: 16px;
  border-radius: 4px;
  border: 1px solid #e0e0e0;
}

.report-markdown {
  padding: 24px;
}

.report-markdown h1,
.report-markdown h2,
.report-markdown h3,
.report-markdown h4,
.report-markdown h5,
.report-markdown h6 {
  font-weight: 600;
  color: #1f2937;
  margin-bottom: 12px;
}

.report-markdown h1 {
  font-size: 28px;
  border-bottom: 3px solid #667eea;
  padding-bottom: 8px;
  margin-top: 24px;
  margin-bottom: 16px;
}

.report-markdown h2 {
  font-size: 24px;
  border-left: 4px solid #667eea;
  padding-left: 12px;
  margin-top: 20px;
  margin-bottom: 12px;
}

.report-markdown h3 {
  font-size: 18px;
  color: #667eea;
  margin-top: 16px;
  margin-bottom: 8px;
}

.report-markdown h4,
.report-markdown h5,
.report-markdown h6 {
  font-size: 16px;
  margin-top: 12px;
}

.report-markdown p {
  margin: 12px 0;
  line-height: 1.8;
  font-size: 14px;
  color: #333;
}

.report-markdown ul,
.report-markdown ol {
  margin: 16px 0;
  padding-left: 40px;
}

.report-markdown li {
  margin: 8px 0;
  line-height: 1.8;
  font-size: 14px;
}

.report-markdown strong {
  font-weight: 600;
  color: #1f2937;
}

.report-markdown em {
  font-style: italic;
  color: #666;
}

.report-markdown code {
  background-color: #f5f5f5;
  padding: 2px 6px;
  border-radius: 3px;
  font-family: "Courier New", monospace;
  color: #e83e8c;
  font-size: 13px;
}

.report-markdown pre {
  background-color: #f5f5f5;
  border: 1px solid #e0e0e0;
  padding: 12px;
  border-radius: 4px;
  overflow-x: auto;
  margin: 16px 0;
  line-height: 1.6;
}

.report-markdown pre code {
  color: #333;
  background-color: transparent;
  padding: 0;
  font-size: 12px;
}

.report-markdown hr {
  margin: 20px 0;
  border: none;
  border-top: 2px solid #e0e0e0;
}

.report-markdown blockquote {
  border-left: 4px solid #667eea;
  padding-left: 12px;
  margin: 16px 0;
  color: #666;
  background-color: #f9f9f9;
  padding: 8px 0 8px 12px;
  font-style: italic;
}

.report-markdown table {
  border-collapse: collapse;
  width: 100%;
  margin: 16px 0;
  font-size: 13px;
}

.report-markdown table th,
.report-markdown table td {
  border: 1px solid #ddd;
  padding: 10px;
  text-align: left;
}

.report-markdown table th {
  background-color: #667eea;
  color: white;
  font-weight: 600;
}

.report-markdown table tr:nth-child(even) {
  background-color: #f9f9f9;
}

.image-card {
  margin-top: 20px;
}

.home-img {
  width: 100%;
  border-radius: 4px;
}
</style>
