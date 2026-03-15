<script setup lang="ts">
import { LogApi } from '@/api/log';
import type { LogListModel } from '@/api/model/model';
import { onMounted, ref } from 'vue';

// 日志列表数据
const logList = ref<LogListModel>([])
const pagination = ref<PaginationParams>({
  currentPage: 1,//当前页码
  pageSize: 10,      //每页记录数
  total: 0 //总页数
})
// 每页记录数发生变化时触发
const handleSizeChange = (pageSize: number) => {
  pagination.value.pageSize = pageSize
  queryPage()
}
// 当前页码发生变化时触发
const handleCurrentChange = (page: number) => {
  pagination.value.currentPage = page
  queryPage()
}
// 分页查询
const queryPage = async () => {
  // 调api
  const result = await LogApi(pagination.value.currentPage, pagination.value.pageSize)
  // 获取数据
  if (result.code) {
    logList.value = result.data.rows
    pagination.value.total = result.data.total  //总条数
  }
}
onMounted(() => {
  queryPage()
})
</script>
<template>
  <h2>日志管理</h2>
  <br>
  {{  }}
  <el-table border :data="logList">
    <el-table-column  prop="operateEmpName" label="操作人" />
    <el-popover effect="light" trigger="hover" placement="top" width="auto" prop="operateTime">
    <el-table-column  label="操作时间">

    </el-table-column>
  </el-popover>
    <el-table-column prop="className" label="类名"></el-table-column>
    <el-table-column prop="methodName" label="方法名"></el-table-column>
    <el-table-column prop="costTime" label="操作耗时(ms)"></el-table-column>
    <el-table-column prop="methodParams" label="请求参数"></el-table-column>
    <el-table-column prop="returnValue" label="返回值"></el-table-column>
  </el-table>
  <!-- 分页组件 -->
  <el-pagination v-model:current-page="pagination.currentPage" v-model:page-size="pagination.pageSize" ,
    :page-sizes="[5, 10, 20, 50, 100]" :total="pagination.total" layout="total,sizes,prev, pager, next,jumper"
    @size-change="handleSizeChange" @current-change="handleCurrentChange" />
</template>
<style></style>
