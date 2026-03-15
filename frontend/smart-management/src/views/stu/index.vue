<script setup lang="ts">
import { queryAllClazzApi } from '@/api/clazz';
import { ClazzListModel, PaginationParams, SearchStuModel, StuModelList, StuViolationApi, type StuModel } from '@/api/model/model';
import { addStuApi, deleteStuApi, getStuByIdApi, queryStuPageApi, updateStuApi, violationApi } from '@/api/stu';
import { ElMessage, ElMessageBox, FormInstance, FormRules, SCOPE } from 'element-plus';
import { onMounted, ref } from 'vue';

// 搜素表单对象
const searchStu = ref<SearchStuModel>({
  name: '',
  degree: '',
  clazzId: ''
})

const stu = ref<StuModel>({
  id: '',
  name: '',
  no: '',
  gender: '',
  phone: '',
  degree: '',
  idCard: '',
  isCollege: '',
  address: '',
  graduationDate: '',
  clazzId: ''
})
// 输入框宽度
const lableWidth = ref<number>(110)

// 性别
const genders = ref([{ name: "男", value: "1" }, { name: "女", value: "2" }])

// 学历
const degrees = ref([{ name: '初中', value: 1 }, { name: '高中', value: 2 }, { name: '大专', value: 3 }, { name: '本科', value: 4 }, { name: '硕士', value: 5 }, { name: '博士', value: 6 }])

// 查询班级列表
const clazzList = ref<ClazzListModel>([])
const queryClazzList = async () => {
  const result = await queryAllClazzApi()
  clazzList.value = result.data
}
// 为数据表格班级名称赋值
const getClazzName = (clazzId: number) => {
  return clazzList.value.find(c => c.id == clazzId)?.name
}
// 为数据表格学历赋值
const getDegreeName = (degree: number) => {
  return degrees.value.find(d => d.value === degree)?.name
}


onMounted(() => {
  queryClazzList()
  pageQuery()
})

// 查询按钮
const pageQuery = async () => {
  // 1.调用API发送异步请求
  const result = await queryStuPageApi(searchStu.value.name, searchStu.value.degree, searchStu.value.clazzId, pagination.value.currentPage, pagination.value.pageSize)
  if (result.code) {
    stuList.value = result.data.rows //分页结果集
    pagination.value.total = result.data.total //总条数
    console.log(stuList.value);
  }
}

// 表格复选框处理
let selectIds = ref<(number | undefined)[]>([])//选择id数组
const handleSelectionChange = (val: StuModel[]) => {
  // val.forEach(ele =>{
  // })
  // map方法遍历数组中的所有元素，对元素进行操作，并且返回一个新的数组
  selectIds.value = val.map(ele => {
    return ele.id
  })
}

// 学生数据列表
const stuList = ref<StuModelList>([])
const pagination = ref<PaginationParams>({
  currentPage: 1,//当前页码
  pageSize: 10,      //每页记录数
  total: 0 //总页数
})
// 每页记录数发生变化时触发
const handleSizeChange = (pageSize: number) => {
  pagination.value.pageSize = pageSize
  pageQuery()
}
// 当前页码发生变化时触发
const handleCurrentChange = (page: number) => {
  pagination.value.currentPage = page
  pageQuery()
}
// 清空按钮
const clean = () => {
  searchStu.value = {
    name: '',
    degree: '',
    clazzId: ''
  }
  pageQuery()
}
// ----------------------------表单校验-----------------------
// 重置校验
const resetForm = (stuFormRef: FormInstance | undefined) => {
  if (!stuFormRef) return
  stuFormRef.resetFields()
}
const stuFormRef = ref<FormInstance>()
const rules = ref<FormRules<StuModel>>({
  name: [
    { required: true, message: '姓名为必填项', trigger: 'blur' },
    { min: 2, max: 20, message: '姓名长度必须为2到20字', trigger: 'blur' },
  ],
  no: [
    { required: true, message: '学号为必填项', trigger: 'blur' },
    { min: 10, max: 10, message: '学号长度必须为10位', trigger: 'blur' },
  ],
  gender: [
    { required: true, message: '性别为必填项', trigger: 'blur' },
  ],
  phone: [
    { required: true, message: '手机号为必填项', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/g, message: '请输入合法的手机号', trigger: 'blur' },
  ],
  idCard: [
    { required: true, message: '身份证号为必填项', trigger: 'blur' },
    { pattern: /^[1-9]\d{5}(18|19|20)\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\d|3[01])\d{3}[\dXx]$/, message: '请输入合法的身份证号', trigger: 'blur' },
  ],
  isCollege: [
    { required: true, message: '是否院校毕业为必填项', trigger: 'blur' },
  ],
  clazzId: [
    { required: true, message: '所属班级为必填项', trigger: 'blur' },
  ]
})
// ---------------------------新增学员------------------------
// 对话框
const formTitle = ref<string>('')
const dialogFormVisiable = ref<boolean>(false)
const violationFormVisiable = ref<boolean>(false)
// 新增学员按钮
const add = () => {
  formTitle.value = '新增学员'
  dialogFormVisiable.value = true
  // 清空历史对话框数据
  stu.value = {
    id: '',
    name: '',
    no: '',
    gender: '',
    phone: '',
    degree: '',
    idCard: '',
    isCollege: '',
    address: '',
    graduationDate: '',
    clazzId: '',
  }
  // 清空校验规则
  resetForm(stuFormRef.value)
}
// 新增学员确定按钮
const save = async (formEl: FormInstance | undefined) => {
  // 先校验表单是否合法
  if (!formEl) return
  await formEl.validate(async (valid) => {
    if (valid) {  //true -- 校验成功
      let result = null
      // 根据id判断新增/修改
      // 发送异步请求，调用后端的新增接口
      if (stu.value.id) {
        //修改
        result = await updateStuApi(stu.value)
      } else {  //没有id，新增
        result = await addStuApi(stu.value)
      }
      // 保存成功
      if (result?.code) {
        ElMessage.success('操作成功')
        dialogFormVisiable.value = false
        pageQuery()
      } else {
        ElMessage.error(result?.msg)
      }
    }
  })
}

// ---------------批量删除-------------------
const deleteBatch = () => {
  // 弹出警告对话框
  ElMessageBox.confirm(
    '您确认要删除所选学员？',
    '删除学员',
    {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then(async () => { //成功回调
    // 发送异步请求，删除学员数据
    const result = await deleteStuApi(selectIds.value.toString())
    if (result.code) {
      pageQuery()
      ElMessage.success('删除成功')
    }
  }).catch(() => {//失败回调
    ElMessage.info('取消删除')
  })
}

// ------------------------修改员工--------------------
const updateStu = async (id: number) => {
  // 弹出对话框
  dialogFormVisiable.value = true
  formTitle.value = '修改员工'
  // 清空校验规则
  resetForm(stuFormRef.value)
  // 调用api发送请求，进行回显
  const result = await getStuByIdApi(id)
  if (result.code) {
    // 为表单对象赋值
    stu.value = result.data
  }
}
// ----------------------违纪操作-----------------------
const stuViolation = ref<StuViolationApi>({
  id: '',
  score: ''
})
const violation = (id: number) => {
  violationFormVisiable.value = true
  stuViolation.value = {
    id: '',
    score: ''
  }
  stuViolation.value.id = id
}
const violationConfirm = async (vio: StuViolationApi) => {
  const result = await violationApi(vio.id, vio.score)
  if (result.code) {
    ElMessage.success('操作成功')
    violationFormVisiable.value = false
    pageQuery()
  } else {
    ElMessage.error(result.msg)
  }
}
// -------------------删除学员---------------
const deleteStu = (id: number) => {
  // 弹出警告对话框
  ElMessageBox.confirm(
    '您确认要删除该学员?',
    '删除员工',
    {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then(async () => {
    const result = await deleteStuApi(`${id}`)
    if (result.code) {
      pageQuery()
      ElMessage('删除成功')
    }
  }).catch(() => {
    ElMessage.info('取消删除')
  })
}
</script>


<template>
  <h2>学员管理</h2>
  <br>
  <!-- 搜索表单 -->
  <el-form :inline="true" :model="searchStu" class="demo-form-inline">
    <el-form-item label="姓名">
      <el-input v-model="searchStu.name" placeholder="请输入学生姓名"></el-input>
    </el-form-item>

    <el-form-item label="学历">
      <el-select v-model="searchStu.degree" placeholder="请选择学历" style="width: 200px;">
        <el-option v-for="(degree) in degrees" :key="degree.value" :label="degree.name" :value="degree.value">
        </el-option>
      </el-select>
    </el-form-item>

    <el-form-item label="所属班级">
      <el-select v-model="searchStu.clazzId" placeholder="请选择所属班级" style="width: 200px;">
        <el-option v-for="(clazz) in clazzList" :key="clazz.id" :label="clazz.name" :value="clazz.id">
        </el-option>
      </el-select>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="pageQuery">查询</el-button>
      <el-button @click="clean">清空</el-button>
    </el-form-item>
  </el-form>
  <!-- 功能按钮 -->
  <el-button type="success" @click="add">+ 新增学员</el-button>
  <el-button type="danger" @click="deleteBatch">- 批量删除</el-button>
  <br><br>
  <!-- 数据表格 -->
  <el-table :data="stuList" border style="width: 100%;" fit @selection-change="handleSelectionChange">
    <el-table-column type="selection" :selectable="selectable" width="50" align="center" />
    <el-table-column prop="name" label="姓名" width="100px" align="center" />
    <el-table-column prop="no" label="学号" width="130" align="center" />
    <el-table-column prop="clazzName" label="班级" width="180" align="center">
      <template #default="scope">
        {{ getClazzName(scope.row.clazzId) }}
      </template>
    </el-table-column>
    <el-table-column prop="gender" label="性别" width="100px" align="center">
      <template #default="scope">
        {{ scope.row.gender == 1 ? '男' : '女' }}
      </template>
    </el-table-column>
    <el-table-column prop="phone" label="手机号" width="130" align="center" />
    <el-table-column prop="degree" label="学历" align="center" width="100">
      <template #default="scope">
        {{ getDegreeName(scope.row.degree) }}
      </template>
    </el-table-column>
    <el-table-column prop="violationCount" label="违纪次数" width="100" align="center" />
    <el-table-column prop="violationScore" label="违纪扣分" width="100" align="center" />
    <el-table-column prop="updateTime" label="最后修改时间" width="200" align="center" />
    <el-table-column label="操作" align="center">
      <template #default="scope">
        <el-button size="small" type="primary" @click="updateStu(scope.row.id)">编辑</el-button>
        <el-button size="small" type="warning" @click="violation(scope.row.id)">违纪</el-button>
        <el-button size="small" type="danger" @click="deleteStu(scope.row.id)">删除</el-button>
      </template>
    </el-table-column>
  </el-table>
  <!-- 分页组件 -->
  <el-pagination v-model:current-page="pagination.currentPage" v-model:page-size="pagination.pageSize" ,
    :page-sizes="[5, 10, 20, 50, 100]" :total="pagination.total" layout="total,sizes,prev, pager, next,jumper"
    @size-change="handleSizeChange" @current-change="handleCurrentChange" />

  <!-- 新增/修改对话框 -->
  <el-dialog v-model="dialogFormVisiable" :title="formTitle">
    <el-form :model="stu" ref="stuFormRef" :rules="rules">
      <el-row>
        <el-col :span="12">
          <el-form-item label="姓名" prop="name" :label-width="lableWidth">
            <el-input v-model="stu.name" placeholder="请输入学员姓名" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="学号" prop="no" :label-width="lableWidth">
            <el-input v-model="stu.no" placeholder="请输入学员学号" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="性别" :label-width="lableWidth" prop="gender">
            <el-select v-model="stu.gender" placeholder="请选择性别">
              <el-option v-for="(gender) in genders" :key="gender.value" :label="gender.name" :value="gender.value" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="手机号" prop="phone" :label-width="lableWidth">
            <el-input v-model="stu.phone" placeholder="请输入手机号" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="身份证号" prop="idCard" :label-width="lableWidth">
            <el-input v-model="stu.idCard" placeholder="请输入身份证号" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否院校毕业" :label-width="lableWidth" prop="isCollege">
            <el-select v-model="stu.isCollege" placeholder="请选择">
              <el-option label="是" value=1 />
              <el-option label="否" value=0 />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="联系地址" :label-width="lableWidth" prop="address">
            <el-input v-model="stu.address" placeholder="请输入联系地址" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="最高学历" :label-width="lableWidth">
            <el-select v-model="stu.degree" placeholder="请选择">
              <el-option v-for="(degree) in degrees" :key="degree.value" :label="degree.name"
                :value="degree.value"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="毕业时间" :label-width="lableWidth">
            <el-date-picker v-model="stu.graduationDate" type="date" placeholder="请选择入职日期" value-format="YYYY-MM-DD"
              style="width: 100%;" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="所属班级" prop="clazzId" :label-width="lableWidth">
            <el-select v-model="stu.clazzId">
              <el-option v-for="(clazz, index) in clazzList" :key="index" :label="clazz.name" :value="clazz.id" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogFormVisiable = false">取消</el-button>
        <el-button type="primary" @click="save(stuFormRef)">确定</el-button>
      </span>
    </template>
  </el-dialog>
  <el-dialog v-model="violationFormVisiable" title='提示' width="500">
    请输入违纪扣分
    <br><br>
    <el-form :model="stuViolation">
      <el-input v-model="stuViolation.score"></el-input>
    </el-form>
    <br>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="violationFormVisiable = false">取消</el-button>
        <el-button type="primary" @click="violationConfirm(stuViolation)">确定</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<style></style>
