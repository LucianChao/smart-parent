<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { DeptModel, DeptModelArray } from '@/api/model/model';

import { addApi, queryAllApi, getDeptApi, updateApi, deleteApi } from '@/api/dept';
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus';

// 查询部门列表
const deptList = ref<DeptModelArray>([])
const queryAll = async () => {
  // const result = await axios.get(`https://m1.apifoxmock.com/m1/7450294-7184633-default/depts`)
  const result = await queryAllApi();
  console.log(result);
  deptList.value = result.data;
}
// 钩子函数
onMounted(() => {
  queryAll();
  console.log('页面加载完成');
})

// 新增部门
const dialogFormVisible = ref<boolean>(false);
const deptForm = ref<DeptModel>({
  name: '',
});
const formTitle = ref<string>('')
// 点击新增按钮触发函数
const add = () => {
  dialogFormVisible.value = true;
  formTitle.value = '新增部门';
  resetForm(deptFormRef.value);
  deptForm.value = {
    name: '',
  };
}

//点击确定按钮，保存数据，发送请求
const save = async (formEl: FormInstance | undefined) => {
  // 保存数据之前，先进行表单校验
  if (!formEl) return
  await formEl.validate(async (valid, fields) => {
    if (valid) {    //valid为true，说明校验通过
      // 优化--判断是新增还是修改
      let result = null;
      if (deptForm.value.id) {    //id存在，说明是修改
        result = await updateApi(deptForm.value);
      } else {  //新增
        // 1.发送异步请求
        result = await addApi(deptForm.value);
      }
      // 2.获取数据
      if (result.code) {
        //成功响应
        ElMessage.success('操作成功');
        dialogFormVisible.value = false;
        queryAll();
      } else {
        ElMessage.error(result.msg)
      }
    }
  })
}

// 修改部门
const update = async (id: number) => {
  dialogFormVisible.value = true;
  formTitle.value = '修改部门';
  // 根据id查询部门信息，并回显到表单
  const result = await getDeptApi(id);
  if (result.code) {
    deptForm.value = result.data;
  } else {
    ElMessage.error(result.msg)
  }
}

//删除部门--根据id
const deleteById = (id: number) => {
  ElMessageBox.confirm(
    '您确认要删除该部门?',
    '删除部门',
    {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      type: 'warning',
    }
  )
    .then(async () => {   //成功回调函数--点击确认触发
      // 业务代码--发送删除请求
      const result = await deleteApi(id);
      if (result.code) {
        ElMessage.success('删除成功')
        queryAll();
      } else {
        ElMessage.error(result.msg)
      }
    })
    .catch(() => {  //失败回调函数--点击取消触发
      // ElMessage({
      //   type: 'info',
      //   message: '取消删除',
      // })
      ElMessage.info('取消删除')

    })
}

// 定义校验规则
const deptFormRef = ref<FormInstance>();
const rules = ref<FormRules<DeptModel>>({
  name: [
    // 判断是否为空，为空响应提示,  trigger: 触发时机  blur:失去焦点时触发
    { required: true, message: '部门名称不能为空', trigger: 'blur' },
    // 判断名称长度
    { min: 2, max: 10, message: '长度必须为2-10位', trigger: 'blur' },
  ],
})

// 重置校验信息
const resetForm = (formEl: FormInstance | undefined) => {
  if (!formEl) return
  formEl.resetFields()
}
</script>

<template>
  <h2>部门管理</h2>
  <!-- 按钮 -->
  <el-button type="success" style="float: right;" @click="add">+新增部门</el-button><br><br>
  <!-- 表格 -->
  <el-table :data="deptList" style="width: 100%" border>
    <el-table-column type="index" label="序号" width="150" align="center" />
    <el-table-column prop="name" label="部门名称"  align="center" />
    <el-table-column prop="updateTime" label="最后操作时间"  align="center" />
    <el-table-column label="操作" align="center">
      <template #default="scope">
        <el-button size="small" type="primary" @click="update(scope.row.id)">修改</el-button>
        <el-button size="small" type="danger" @click="deleteById(scope.row.id)">删除</el-button>
      </template>
    </el-table-column>
  </el-table>
  <!-- 新增|修改 部门对话框 -->
  <el-dialog v-model="dialogFormVisible" :title="formTitle" width="500">
    <el-form :model="deptForm" :rules="rules" ref="deptFormRef">
      <el-form-item label="部门名称" prop="name">
        <el-input v-model="deptForm.name" autocomplete="off" />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="save(deptFormRef)">
          确定
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<style></style>
