<script setup lang="ts">
import { addClazzApi, deleteCLazzByIdApi, getClazzByIdApi, queryClazzPageApi, updateClazzApi } from '@/api/clazz';
import { queryAllEmpApi } from '@/api/emp';
import { ClazzListModel, ClazzModel, EmpModelArray, SearchClazzModel, type PaginationParams } from '@/api/model/model';
import { ElMessage, ElMessageBox, FormInstance, type ElCol, type ElRow, type FormRules } from 'element-plus';
import { id } from 'element-plus/es/locales.mjs';
import { onMounted, ref, watch } from 'vue';

// 搜索班级表单
const searchClazz = ref<SearchClazzModel>({
  name: '',
  begin: '',
  end: '',
  date: [],
})

// 分页查询
const queryPage = async () => {

  // 调api发送请求
  const result = await queryClazzPageApi(searchClazz.value.name, searchClazz.value.begin, searchClazz.value.end, pagination.value.currentPage, pagination.value.pageSize)
  // 2.获取响应数据
  if (result.code) {

    clazzList.value = result.data.rows //分页结果集
    pagination.value.total = result.data.total  //总条数
    console.log(clazzList.value);
  }
}
// -------------------分页----------------------
// 分页处理
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
// 班级数据对象
const clazzList = ref<ClazzListModel>([])
const clazz = ref<ClazzModel>({
  id: '',
  name: '',
  room: '',
  beginDate: '',
  endDate: '',
  masterId: '',
  masterName: '',
  createTime: '',
  updateTime: '',
  status: ''
})

// 钩子函数
onMounted(() => {
  queryPage()
  queryEmpList()
})

// 监听clazzSearch并给begin和end赋值
watch(() => searchClazz.value.date,
  (date) => {
    if (!date) {
      return;
    }
    else if (date?.length == 2) {
      searchClazz.value.begin = searchClazz.value.date[0]
      searchClazz.value.end = searchClazz.value.date[1]
    }
  }, { deep: true })

// -------------------清空按钮----------------
const clean = () => {
  searchClazz.value = {
    name: '',
    begin: '',
    end: '',
    date: [],
  }
  queryPage()
}
// ----------------新增/修改班级对话框---------------------
const formTitle = ref<string>('')
const dialogFormVisiable = ref<boolean>(false)
// 新增员工按钮
const add = () => {
  // 弹出对话框
  dialogFormVisiable.value = true
  formTitle.value = '新增班级'
  // 清空历史数据
  clazz.value = {
    id: '',
    name: '',
    room: '',
    beginDate: '',
    endDate: '',
    masterId: '',
    masterName: '',
    createTime: '',
    updateTime: '',
    status: ''
  }
  // 清空校验数据
  resetForm(clazzFormRef.value)
}

// 查询班主任姓名
const empList = ref<EmpModelArray>([])
const empNameList = ref<{ id: string, name: string }[]>([])
const queryEmpList = async () => {
  const result = await queryAllEmpApi()
  if (result.code) {
    empList.value = result.data
    empNameList.value = empList.value.map(emp => ({
      id: emp.id,
      name: emp.name
    }))
    console.log(empNameList.value);
  }
}

// 输入框宽度
const lableWidth = ref<number>(80)

// 学科列表
const subjectList = ref([{ name: 'Java', value: 1, }, { name: "前端", value: 2 }, { name: "大数据", value: 3 }, { name: 'Python', value: 4 }, { name: 'Go', value: 5 }, { name: '嵌入式', value: 6 }])

// 表单校验
const clazzFormRef = ref<FormInstance>()
const rules = ref<FormRules<ClazzModel>>({
  name: [
    { required: true, message: '班级名称为必填项', trigger: 'blur' },
    { min: 4, max: 30, message: '长度必须为4到30字', trigger: 'blur' },
  ],
  beginDate: [
    { required: true, message: '开课时间为必填项', trigger: 'blur' },
  ],
  endDate: [
    { required: true, message: '结课时间为必填项', trigger: 'blur' },
  ],
  subject: [
    { required: true, message: '学科为必填项', trigger: 'blur' },
  ],
})
// 新增班级确定按钮
const save = async (formEl: FormInstance) => {
  // 校验表单合法性
  if (!formEl) return
  await formEl.validate(async (valid, fields) => {
    if (valid) {  //true 校验成功
      let result = null
      // 根据id判断是新增还是修改
      if (clazz.value.id) {
        result = await updateClazzApi(clazz.value)
      } else {
        result = await addClazzApi(clazz.value)
      }
      if (result.code) {
        ElMessage.success('操作成功')
        queryPage()
        dialogFormVisiable.value = false
      } else {
        ElMessage.error(result.msg)
      }
    }
  })
}

// 重置校验
const resetForm = (empFormRef: FormInstance | undefined) => {
  if (!empFormRef) return
  empFormRef.resetFields()
}

// -----------------编辑/修改班级--------------------
const updateClazz = async (id: number) => {
  // 弹出对话框
  dialogFormVisiable.value = true
  formTitle.value = '修改员工'
  // 清空校验规则
  resetForm(clazzFormRef.value)
  // 调用api发送异步请求，进行数据回显
  const result = await getClazzByIdApi(id)
  if (result.code) {
    // 给clazz对象赋值
    clazz.value = result.data
  }
}
// --------------------删除班级---------------
const deleteClazz = (id: number) => {
  // 警告对话框
  ElMessageBox.confirm(
    '您确认要删除该班级？',
    '删除班级',
    {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then(async () => {  //成功回调函数
    // 发送异步请求，删除部门数据
    const result = await deleteCLazzByIdApi(`${id}`)
    if (result.code) {
      // 刷新页面
      queryPage()
      ElMessage.success('删除成功')
    }
  }).catch(() => {   //失败回调函数
    ElMessage.info('取消删除')
  })
}
</script>

<template>
  <h2>班级管理</h2>
  <br>
  <!-- 搜索表单 -->
  <el-form :inline="true" :model="searchClazz" class="demo-form-inline">
    <el-form-item label="班级名称">
      <el-input v-model="searchClazz.name" placeholder="请输入班级名称"  />
    </el-form-item>
    <el-form-item label="结课时间">
      <el-date-picker v-model="searchClazz.date" type="daterange" range-separator="到" start-placeholder="开始时间"
        end-placeholder="结束时间" value-format="YYYY-MM-DD"  />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="queryPage">查询</el-button>
      <el-button @click="clean">清空</el-button>
    </el-form-item>
  </el-form>
  <!-- 新增按钮 -->
  <el-button type="success" @click="add">+ 新增班级</el-button>
  <br><br>
  <!-- 数据表格 -->
  <el-table :data="clazzList" border style="width: 100%;">
    <el-table-column type="index" label="序号" align="center"></el-table-column>
    <el-table-column prop="name" label="班级名称" width="180" align="center"></el-table-column>
    <el-table-column prop="room" label="班级教室" width="200" align="center"></el-table-column>
    <el-table-column prop="masterName" label="班主任" width="200" align="center"></el-table-column>
    <el-table-column prop="beginDate" label="开课时间" width="100" align="center"></el-table-column>
    <el-table-column prop="endDate" label="结课时间" width="100" align="center"></el-table-column>
    <el-table-column prop="status" label="状态" width="200" align="center"></el-table-column>
    <el-table-column prop="updateTime" label="最后修改时间" width="200" align="center"></el-table-column>
    <el-table-column label="操作" align="center">
      <template #default="scope">
        <el-button size="small" type="primary" @click="updateClazz(scope.row.id)">编辑</el-button>
        <el-button size="small" type="danger" @click="deleteClazz(scope.row.id)">删除</el-button>
      </template>
    </el-table-column>
    <div class="demo-pagination-block">
      <div class="demonstration">All combined</div>
    </div>
  </el-table>
  <br>
  <el-pagination v-model:current-page="pagination.currentPage" v-model:page-size="pagination.pageSize" ,
    :page-sizes="[5, 10, 20, 50, 100]" layout="total, sizes, prev, pager, next, jumper" :total="pagination.total"
    @size-change="handleSizeChange" @current-change="handleCurrentChange" />


  <!-- 新增班级表单 -->
  <el-dialog v-model="dialogFormVisiable" :title="formTitle" width="500">
    <el-form :model="clazz" ref="clazzFormRef" :rules="rules">
      <el-row>
        <el-col :span="24">

          <el-form-item label="班级名称" prop="name" :label-width="lableWidth">
            <el-input v-model="clazz.name" placeholder="请输入班级名称">
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="班级教室" prop="room" :label-width="lableWidth">
            <el-input v-model="clazz.room" placeholder="请输入班级教室">
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="开课时间" prop="beginDate" :label-width="lableWidth">
            <el-date-picker v-model="clazz.beginDate" placeholder="请选择开课时间" type="date" style="width: 100%;">
            </el-date-picker>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="结课时间" prop="endDate" :label-width="lableWidth">
            <el-date-picker v-model="clazz.endDate" placeholder="请选择结课时间" type="date" style="width: 100%;">
            </el-date-picker>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="班主任" prop="masterId" :label-width="lableWidth">
            <el-select v-model="clazz.masterId" placeholder="请选择班主任">
              <el-option v-for="(emp) in empNameList" :key="emp.id" :value="emp.id" :label="emp.name">
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="学科" prop="subject" :label-width="lableWidth">
            <el-select v-model="clazz.subject" placeholder="请选择学科">
              <el-option v-for="(subject) in subjectList" :key="subject.value" :value="subject.value"
                :label="subject.name"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <!-- 底部插槽，自动处理样式 -->
    <template #footer>
      <el-button @click="dialogFormVisiable = false">取消</el-button>
      <el-button type="primary" @click="save(clazzFormRef)">确定</el-button>
    </template>
  </el-dialog>
</template>

<style></style>
