<script setup lang="ts">
import { addEmpApi, deleteEmpApi, queryInfoApi, queryPageApi, updateEmpApi } from '@/api/emp';
import { DeptModelArray, type EmpExprModel, type EmpModel, type EmpModelArray, type PaginationParams, type SearchEmpModel } from '@/api/model/model';
import { onMounted, ref, watch } from 'vue';
import { queryAllApi as queryAllDeptsApi } from '@/api/dept';
import { ElButton, ElMessage, ElMessageBox, type FormInstance, type FormRules, type UploadProps } from 'element-plus';


// 搜索表单对象
const searchEmp = ref<SearchEmpModel>({
  name: '',
  gender: '',
  begin: '',
  end: '',
  date: [],
})

//监听date变化，动态设置begin和end
watch(() => searchEmp.value.date, (newVal, oldVal) => {
  if (newVal) {
    searchEmp.value.begin = newVal[0];
    searchEmp.value.end = newVal[1];
  } else {
    searchEmp.value.begin = '';
    searchEmp.value.end = '';
  }
})

// 每页记录数发生变化时触发
const handleSizeChange = (pageSize: number) => {
  pagination.value.pageSize = pageSize
  pageQueery()
}
// 当前页码发生变化时触发
const handleCurrentChange = (page: number) => {
  pagination.value.currentPage = page
  pageQueery()
}
// 展示列表数据
const empList = ref<EmpModelArray>([])
const pagination = ref<PaginationParams>({
  currentPage: 1,//当前页码
  pageSize: 10,      //每页记录数
  total: 0 //总页数
})

// 分页查询
const pageQueery = async () => {
  // 1.调用API发生异步请求
  const result = await queryPageApi(searchEmp.value.begin, searchEmp.value.end, searchEmp.value.gender, searchEmp.value.name, pagination.value.currentPage, pagination.value.pageSize)
  // 2.获取响应数据
  if (result.code) {
    empList.value = result.data.rows  //分页结果集
    pagination.value.total = result.data.total  //总条数
  }
}

// 钩子函数
onMounted(() => {
  pageQueery();
  queryAllDepts();
})

// 清空按钮触发
const clean = () => {
  // 初始化搜索框
  searchEmp.value = {
    name: '',
    gender: '',
    begin: '',
    end: '',
    date: [],
  }
  pageQueery()
}

// -----------------------------新增员工-------------------------
const dialogFormVisiable = ref<boolean>(false);
const formTitle = ref<string>('')
// 封装表单数据对象
const emp = ref<EmpModel>({
  username: '',
  password: '',
  name: '',
  gender: '',
  phone: '',
  job: '',
  salary: '',
  image: '',
  entryDate: '',
  deptId: '',
  exprList: []
})

// 输入框宽度
const lableWidth = ref<number>(80)

// 性别
const genders = ref([{ name: "男", value: "1" }, { name: "女", value: "2" }])
// 职位
const jobs = ref([{ name: '班主任', value: 1 }, { name: '讲师', value: 2 }, { name: '学工主管', value: 3 }, { name: '教研主管', value: 4 }, { name: '咨询师', value: 5 }, { name: '其他', value: 6 }])

// 查询部门列表
const deptList = ref<DeptModelArray>([])
const queryAllDepts = async () => {
  const result = await queryAllDeptsApi()
  deptList.value = result.data
}

// 文件上传
// 文件上传成功钩子函数
const handleAvatarSuccess: UploadProps['onSuccess'] = (
  response,
  uploadFile
) => {
  // imageUrl.value = URL.createObjectURL(uploadFile.raw!)
  emp.value.image = response.data;
}

// 文件上传前的钩子函数 -- 一般定义校验逻辑
const beforeAvatarUpload: UploadProps['beforeUpload'] = (rawFile) => {
  if (rawFile.type !== 'image/jpeg') {
    ElMessage.error('文件格式不正确！')
    return false  //返回false，代表不上传
  } else if (rawFile.size / 1024 / 1024 > 2) {
    ElMessage.error('文件大小不能超过 2MB!')
    return false
  }
  return true
}

// 添加工作经历
const addWorkItem = (expr: EmpExprModel) => {
  // 往emp.exprList数组中添加数据
  emp.value.exprList.push({
    exprDate: [],
    beginDate: '',
    endDate: '',
    company: '',
    job: ''
  });
}

// 删除工作经历
const deleteWorkItem = (expr: EmpExprModel) => {
  // 获取元素在数组中的位置
  const start = emp.value.exprList.indexOf(expr)
  if (start != -1) {
    // 从数组中指定位置的元素
    emp.value.exprList.splice(start, 1)
  }
}

//监听date变化，动态设置begin和end
// V1.0
// watch(emp, (newVal, oldVal) => {
//   if (emp.value.exprList) {
//     emp.value.exprList.forEach((expr) => {
//       expr.beginDate = expr.exprDate[0];
//       expr.endDate = expr.exprDate[1];
//     })
//   }
// }, { deep: true })
// V2.0 -- 优化
watch(
  () => emp.value.exprList,
  (list) => {
    if (!list) {
      return
    }
    list.forEach((expr) => {
      if (expr.exprDate?.length === 2) {
        expr.beginDate = expr.exprDate[0]
        expr.endDate = expr.exprDate[1]
      }
    })
  },
  { deep: true }
)

// 保存员工数据
const add = () => {
  // 点击新增员工，弹出对话框
  dialogFormVisiable.value = true
  formTitle.value = '新增员工'
  // 清空历史对话框数据
  emp.value = {
    username: '',
    password: '',
    name: '',
    gender: '',
    phone: '',
    job: '',
    salary: '',
    image: '',
    entryDate: '',
    deptId: '',
    exprList: []
  }
  // 清除校验历史
  resetForm(empFormRef.value)
}
const save = async (formEl: FormInstance | undefined) => {
  // 先校验表单是否合法
  if (!formEl) return
  await formEl.validate(async (valid, fields) => {
    if (valid) {  //true -- 校验成功
      let result = null
      // 根据id判断是新增还是修改操作---通过员工id判断
      // 1.发送异步请求，调用后端的新增接口
      if (emp.value.id) {   //有id，修改
        result = await updateEmpApi(emp.value)
      } else {   //没有id，新增操作
        result = await addEmpApi(emp.value);
      }
      // 2。如果保存成功，关闭对话框，刷新页面
      if (result.code) {
        ElMessage.success('操作成功')
        dialogFormVisiable.value = false
        pageQueery();
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

// 表单校验规则
const empFormRef = ref<FormInstance>()
const rules = ref<FormRules<EmpModel>>({
  username: [
    { required: true, message: '用户名为必填项', trigger: 'blur' },
    { min: 2, max: 10, message: '用户名长度为2到20位', trigger: 'blur' },
  ],
  name: [
    { required: true, message: '姓名为必填项', trigger: 'blur' },
    { min: 2, max: 10, message: '长度必须为2到10位', trigger: 'blur' },
  ],
  gender: [
    { required: true, message: '性别为必填项', trigger: 'blur' },
  ],
  phone: [
    { required: true, message: '手机号为必填项', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/g, message: '请输入合法的手机号', trigger: 'blur' },
  ],
  salary: [
    { pattern: /^[1-9]\d*$/g, message: '请输入合法的数字', trigger: 'blur' },
  ]
})

// ---------------------编辑员工-------------------------
const updateEmp = async (id: number) => {
  // 弹出对话框
  dialogFormVisiable.value = true
  formTitle.value = '修改员工'
  // 清空校验规则
  resetForm(empFormRef.value)
  // 调用api发送异步请求，进行数据回显
  const result = await queryInfoApi(id)
  if (result.code) {
    //给表单绑定的对象emp赋值
    emp.value = result.data
    // 把begin和end，处理并赋值给exprDate
    const exprList = emp.value.exprList
    exprList.forEach(expr => {
      // expr.exprDate[0] = expr.beginDate
      // expr.exprDate[1] = expr.endDate
      // 注意：由于expr.exprDate对象为空，需要通过new的方式赋值，不能调用push方法或者给元素赋值的方法
      expr.exprDate = [expr.beginDate, expr.endDate]
    })
  }
}

// -------------------删除员工-------------------------
// 根据id删除单个员工
const deleteEmp = (id: number) => {
  // 弹出警告对话框
  ElMessageBox.confirm(
    '您确认要删除该员工?',
    '删除员工',
    {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then(async () => { //成功回调函数
    // 发送异步请求，删除部门数据
    const result = await deleteEmpApi(`${id}`)
    if (result.code) {
      // 刷新页面
      pageQueery()
      ElMessage.success('删除成功')
    }
  }).catch(() => {  //失败回调函数
    ElMessage.info('取消删除')
  })
}

// 表格复选框处理
let selectIds = ref<(number | undefined)[]>([])
const handleSelectionChange = (val: EmpModel[]) => {
  // val.forEach(ele =>{

  // })
  // map方法遍历数组中的所有元素，对元素进行操作，并且返回一个新的数组
  selectIds.value = val.map(ele => {
    return ele.id
  })
}

// 批量删除
const deleteBatch = () => {
// 弹出警告对话框
  ElMessageBox.confirm(
    '您确认要删除所选员工?',
    '删除员工',
    {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then(async () => { //成功回调函数
    // 发送异步请求，删除部门数据
    const result = await deleteEmpApi(selectIds.value.toString())
    if (result.code) {
      // 刷新页面
      pageQueery()
      ElMessage.success('删除成功')
    }
  }).catch(() => {  //失败回调函数
    ElMessage.info('取消删除')
  })
}
// --------------------watch()演示------------------------
/* const a = ref<string>('');
watch(a,(newVal,oldVal)=>{
console.log(`监听到a发生变化newVal:${newVal}->lodVal:${oldVal}`);

})

watch(()=>searchEmp.value.name,()=>{
  console.log('监听到searchEmp.name发生变化:',searchEmp.value.name);
})

watch(searchEmp, (newVal, oldVal) => {
  console.log('监听到searchEmp对象发生变化newVal:', newVal, '->oldVal:', oldVal);
}, { deep: true }) */

</script>
<template>
  <h2>员工管理</h2><br>
  <!-- 搜索框 -->
  <el-form :inline="true" :model="searchEmp" class="demo-form-inline">
    <el-form-item label="姓名">
      <el-input v-model="searchEmp.name" placeholder="请输入姓名"  />
    </el-form-item>

    <el-form-item label="性别">
      <el-select v-model="searchEmp.gender" placeholder="请选择性别"  style="width: 200px;">
        <!-- <el-option label="男" value="1" />
        <el-option label="女" value="2" /> -->
        <!-- 循环遍历genders列表 -->
        <el-option v-for="(gender) in genders" :key="gender.value" :label="gender.name" :value="gender.value" />
      </el-select>
    </el-form-item>

    <el-form-item label="入职日期">
      <el-date-picker v-model="searchEmp.date" type="daterange" range-separator="到" start-placeholder="开始时间"
        end-placeholder="结束时间" value-format="YYYY-MM-DD"  />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="pageQueery">查询</el-button>
      <el-button @click="clean">清空</el-button>
    </el-form-item>
  </el-form>
  <!-- 功能按钮 -->
  <el-button type="success" @click="add">+ 新增员工</el-button>
  <el-button type="danger" @click="deleteBatch">- 批量删除</el-button>
  <br><br>

  <!-- 数据表格 -->
  <el-table :data="empList" border style="width: 100%" fit @selection-change="handleSelectionChange">

    <el-table-column type="selection" :selectable="selectable" width="50" align="center" />

    <el-table-column prop="name" label="姓名" width="130" align="center" />
    <el-table-column label="性别" align="center" width="100px">
      <template #default="scope">
        {{ scope.row.gender == 1 ? '男' : '女' }}
      </template>
    </el-table-column>
    <el-table-column prop="image" label="头像" align="center">
      <template #default="scope">
        <img :src="scope.row.image" width="50px"></img>
      </template>
    </el-table-column>
    <el-table-column prop="deptName" label="所属部门" align="center" />
    <el-table-column prop="job" label="职位" width="100px" align="center">
      <template #default="scope">
        <span v-if="scope.row.job == 1">班主任</span>
        <span v-else-if="scope.row.job == 2">讲师</span>
        <span v-else-if="scope.row.job == 3">学工主管</span>
        <span v-else-if="scope.row.job == 4">教研主管</span>
        <span v-else-if="scope.row.job == 5">咨询师</span>
        <span v-else>其他</span>
      </template>
    </el-table-column>
    <el-table-column prop="entryDate" label="入职时间" width="100" align="center" />
    <el-table-column prop="updateTime" label="最后修改时间" width="200" align="center" />
    <el-table-column label="操作" align="center" width="150">
      <template #default="scope">
        <el-button size="small" type="primary" @click="updateEmp(scope.row.id)">编辑</el-button>
        <el-button size="small" type="danger" @click="deleteEmp(scope.row.id)">删除</el-button>
      </template>
    </el-table-column>
  </el-table>
  <br>
  <!-- 分页组件 -->
  <el-pagination v-model:current-page="pagination.currentPage" v-model:page-size="pagination.pageSize" ,
    :page-sizes="[5, 10, 20, 50, 100]" :total="pagination.total" layout="total,sizes,prev, pager, next,jumper"
    @size-change="handleSizeChange" @current-change="handleCurrentChange" />

  <!-- 新增员工/修改员工DiaLog对话框 -->
  <el-dialog v-model="dialogFormVisiable" :title="formTitle">
    <el-form :model="emp" :rules="rules" ref="empFormRef">
      <!-- 第一行 -->
      <el-row>
        <el-col :span="12">
          <el-form-item label="用户名" :label-width="lableWidth" prop="username">
            <el-input v-model="emp.username" placeholder="请输入用户名，2~20位"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="姓名" :label-width="lableWidth" prop="name">
            <el-input v-model="emp.name" placeholder="请输入姓名，2~10位"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <!-- 第二行 -->
      <el-row>
        <el-col :span="12">
          <el-form-item label="性别" :label-width="lableWidth" prop="gender">
            <el-select v-model="emp.gender" placeholder="请选择性别" >
              <!-- <el-option label="男" value="1" />
              <el-option label="女" value="2" /> -->
              <!-- 循环遍历genders列表 -->
              <el-option v-for="(gender) in genders" :key="gender.value" :label="gender.name" :value="gender.value" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="手机号" :label-width="lableWidth" prop="phone">
            <el-input v-model="emp.phone" placeholder="请输入合法的手机号"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <!-- 第三行 -->
      <el-row>
        <el-col :span="12">
          <el-form-item label="薪资" :label-width="lableWidth" prop="salary">
            <el-input v-model="emp.salary" placeholder="请输入纯数字的薪资"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="入职日期" :label-width="lableWidth">

            <el-date-picker v-model="emp.entryDate" type="date" placeholder="请选择入职日期" value-format="YYYY-MM-DD"
               style="width: 100%;" />

          </el-form-item>
        </el-col>
      </el-row>
      <!-- 第四行 -->
      <el-row>
        <el-col :span="12">
          <el-form-item label="所属部门" :label-width="lableWidth">
            <el-select v-model="emp.deptId" placeholder="请选择" style="width: 100%;">
              <el-option v-for="(dept, index) in deptList" :key="index" :label="dept.name" :value="dept.id" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="职位" :label-width="lableWidth">
            <el-select v-model="emp.job" placeholder="请选择" style="width: 100%;">
              <el-option v-for="(job, index) in jobs" :key="index" :label="job.name" :value="job.value" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <!-- 第五行 -->
      <el-row>
        <el-col :span="12">
          <el-form-item label="头像" :label-width="lableWidth">
            <!-- el-upload: 文件上传 -->
            <!-- action:指定文件上传路径 -->
            <!-- on-success:文件上传成功钩子函数 -->
            <!-- before-upload:文件上传之前的钩子，参数为上传的文件，返回false或返回promise且被reject，则停止上传 -->
            <el-upload class="avatar-uploader" action="/api/upload" :show-file-list="false"
              :on-success="handleAvatarSuccess" :before-upload="beforeAvatarUpload">
              <img v-if="emp.image" :src="emp.image" class="avatar" />
              <el-icon v-else class="avatar-uploader-icon">
                <Plus />
              </el-icon>
            </el-upload>
          </el-form-item>
        </el-col>
      </el-row>
      <!-- 第六行 -->
      <el-row>
        <el-col :span="24">
          <el-form-item label="工作经历" :label-width="lableWidth">
            <el-button type="success" size="samll" @click="addWorkItem">+ 添加工作经历</el-button>
          </el-form-item>
        </el-col>
      </el-row>
      <!-- 第七行 -->
      <el-row :gutter="5" v-for="(expr, index) in emp.exprList" :key="index">
        <el-col :span="10">
          <el-form-item label="时间" size="small" :label-width="lableWidth">
            <el-date-picker v-model="expr.exprDate" type="daterange" range-separator="至" start-placeholder="开始时间"
              end-placeholder="结束时间" value-format="YYYY-MM-DD"  style="width: 100%"></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="公司" size="small">
            <el-input v-model="expr.company" placeholder="公司名称" />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="职位" size="small">
            <el-input v-model="expr.job" placeholder="职位名称" />
          </el-form-item>
        </el-col>
        <el-col :span="2">
          <el-form-item size="small">
            <el-button type="danger" @click="deleteWorkItem(expr)">- 删除</el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogFormVisiable = false">取消</el-button>
        <el-button type="primary" @click="save(empFormRef)">确定</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<style>
.avatar-uploader .avatar {
  width: 178px;
  height: 178px;
  display: block;
}

.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 78px;
  height: 78px;
  text-align: center;
}
</style>
