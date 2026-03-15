<script setup>
import { onMounted, ref, nextTick } from 'vue'
import * as echarts from 'echarts'
import request from '@/utils/request'

const jobChartRef = ref(null)
const genderChartRef = ref(null)

const jobChart = ref(null)
const genderChart = ref(null)
onMounted(async () => {

  const jobRes = await request.get('/report/empJobData')
  const { jobList, dataList } = jobRes.data
  // const res = await request.get('/report/empJobData')
  const res = await request.get('/report/empGenderData')
  const genderList = res.data

  await nextTick()

  // --------------------------------------------
  jobChart.value = echarts.init(jobChartRef.value)
  jobChart.value.setOption({
    title: {
      text: '员工职位分布',
      left: 'center',
      top: 0 ,// 距离顶部 20px，可调

    },
    tooltip: {
      trigger: 'axis',
    },
    xAxis: {
      type: 'category',
      data: jobList
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '人数',
        type: 'bar',
        data: dataList,
        label: { show: true, position: 'top' },
        itemStyle: {
          color: new echarts.graphic.LinearGradient(
            0, 0, 1, 1, // 0,0 -> 0,1 表示从上到下
            [
              { offset: 0, color: '#83bff6' }, // 上颜色
              { offset: 1, color: '#188df0' }  // 下颜色
            ]
          )
        }
      }
    ]
  })

  // ----------------------------------------------
  genderChart.value = echarts.init(genderChartRef.value)

  genderChart.value.setOption({
    tooltip: { trigger: 'item' },
    title: { text: '员工性别分布', left: 'center', top: 0 },
    legend: { top: '7%', left: 'center' },
    color: ['#4f8ef7', '#f56c6c'], // 按 data 顺序对应颜色
    series: [
      {
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,

        itemStyle: {
          borderRadius: 10,
          borderColor: '#fff',
          borderWidth: 2,
        },
        label: {
          show: false,
          position: 'center',
        },
        emphasis: {
          label: {
            show: true,
            fontSize: 30,
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: false
        },
        data: genderList.map((item) => ({
          name: item.name,
          value: item.value,
        }))
      }
    ]
  })
})
window.addEventListener('resize', () => {
  jobChart.value.resize()
  genderChart.value.resize()
})
</script>

<template>
  <el-row :gutter="20" align="middle">
    <el-col :span="12" style="border-right: 1px dashed #ccc;padding-right: 10px;">
      <div ref="jobChartRef" style="width: 100%; height: 600px"></div>
    </el-col>
    <el-col :span="12" style="padding-left: 10px;">
      <div ref="genderChartRef" style="width: 100%; height:600px"></div>
    </el-col>
  </el-row>
</template>
