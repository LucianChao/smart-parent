<script setup>
import { onMounted, ref, nextTick } from 'vue'
import * as echarts from 'echarts'
import request from '@/utils/request'

const degreeChartRef = ref(null)
const countChartRef = ref(null)

const degreeChart = ref(null)
const countChart = ref(null)
onMounted(async () => {

  const res = await request.get('/report/studentCountData')
  const { clazzList, dataList } = res.data
  // const res = await request.get('/report/empJobData')
  const stuDegree = await request.get('/report/studentDegreeData')
  const degreeList = stuDegree.data

  await nextTick()

  // --------------------------------------------
  countChart.value = echarts.init(countChartRef.value)
  countChart.value.setOption({
    title: {
      text: '班级人数分布',
      left: 'center',
      top: 0,// 距离顶部 20px，可调

    },
    tooltip: {
      trigger: 'item',
      formatter: function (params) {
        return `${params.name}: ${params.value} 人`;
      }
    },
    xAxis: {
      type: 'category',
      data: clazzList,
      axisLabel: {
        rotate: 30,
        fontSize: 12,
        align: 'center',
        verticalAlign: 'bottom',
         margin: 50,
        // interval:1,
        // formatter:function(value){
        //   return value.length > 4 ? value.slice(0, 4) + '...' : value;
        // }
      }
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
  degreeChart.value = echarts.init(degreeChartRef.value)

  degreeChart.value.setOption({
    tooltip: { trigger: 'item' },
    title: { text: '学员学历分布', left: 'center', top: 0 },
    legend: { top: '7%', left: 'center' },
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
        data: degreeList.map((item) => ({
          name: item.name,
          value: item.value,
        }))
      }
    ]
  })
})
window.addEventListener('resize', () => {
  degreeChart.value.resize()
  countChart.value.resize()
})
</script>

<template>
  <el-row :gutter="20" align="middle">
    <el-col :span="12" style="border-right: 1px dashed #ccc;padding-right: 10px;">
      <div ref="countChartRef" style="width: 100%; height: 600px"></div>
    </el-col>
    <el-col :span="12" style="padding-left: 10px;">
      <div ref="degreeChartRef" style="width: 100%; height:600px"></div>
    </el-col>
  </el-row>
</template>
