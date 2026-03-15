<script setup lang="ts">
import router from '@/router';
import { useLoginEmpStore } from '@/stores/loginEmp';
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus';
import { ref } from 'vue';
import { updatePasswordApi } from '@/api/login';

// 使用store
const store = useLoginEmpStore()
// 登录人
const loginName = ref<string | undefined>('')
loginName.value = store.loginEmp.name

// 修改密码相关
const showUpdatePwdDialog = ref(false)
const formEl = ref<FormInstance>()
const updatePwdForm = ref({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

// 表单验证规则
const rules: FormRules = {
  oldPassword: [
    { required: true, message: '请输入旧密码', trigger: 'blur' },
    { min: 6, message: '密码长度至少为6位', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度至少为6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请再次确认密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value === updatePwdForm.value.newPassword) {
          callback()
        } else {
          callback(new Error('两次输入的密码不一致'))
        }
      },
      trigger: 'blur'
    }
  ]
}

// 打开修改密码对话框
const updatePwd = (e: Event) => {
  e.preventDefault()
  showUpdatePwdDialog.value = true
}

// 确认修改密码
const confirmUpdatePwd = async () => {
  if (!formEl.value) return

  await formEl.value.validate(async (valid) => {
    if (valid) {
      try {
        const result = await updatePasswordApi(updatePwdForm.value.oldPassword, updatePwdForm.value.newPassword)
        if (result.code) {
          ElMessage.success('密码修改成功，请重新登录')
          // 清除登录信息
          store.removeLoginEmp()
          // 关闭对话框
          showUpdatePwdDialog.value = false
          // 重置表单
          updatePwdForm.value = { oldPassword: '', newPassword: '', confirmPassword: '' }
          // 跳转到登录页面
          setTimeout(() => {
            router.push('/login')
          }, 500)
        } else {
          ElMessage.error(result.msg || '密码修改失败')
        }
      } catch (err) {
        ElMessage.error('密码修改异常，请检查网络连接')
        console.error('updatePassword error:', err)
      }
    }
  })
}

// 关闭对话框时重置表单
const closePwdDialog = () => {
  showUpdatePwdDialog.value = false
  formEl.value?.resetFields()
  updatePwdForm.value = { oldPassword: '', newPassword: '', confirmPassword: '' }
}

// 退出登录信息
const logout = () => {
  // 1.清楚登录信息
  store.removeLoginEmp()
  //2.跳转到登录页面
  router.push('/login')
  ElMessage.success('退出成功')
}
</script>

<template>
  <div class="common-layout">
    <el-container>
      <!-- 顶栏 -->
      <el-header class="header">
        <span class="title">智能学习辅助系统</span>
        <span class="right-tool">
          <a href="" @click="updatePwd"><el-icon>
              <Setting />
            </el-icon>修改密码</a>&nbsp;&nbsp;|&nbsp;&nbsp;
          <a href="" @click="logout"><el-icon>
              <SwitchButton />
            </el-icon>退出登录【{{ loginName }}】</a>
        </span>
      </el-header>
      <el-container>
        <!-- 侧边栏 -->
        <el-aside width="200px" class="aside">
          <el-scrollbar>
            <!-- 关联路由 -->
            <el-menu router>
              <!-- 首页菜单 -->
              <el-menu-item index="/index">
                <el-icon>
                  <HomeFilled />
                </el-icon>首页
              </el-menu-item>

              <el-sub-menu index="/manage">
                <template #title>
                  <el-icon>
                    <Operation />
                  </el-icon>班级学员管理
                </template>
                <el-menu-item index="/clazz"><el-icon>
                    <School />
                  </el-icon>班级管理</el-menu-item>
                <el-menu-item index="/stu"><el-icon>
                    <UserFilled />
                  </el-icon>学员管理</el-menu-item>
              </el-sub-menu>

              <el-sub-menu v-if="store.loginEmp.role === 'ADMIN'" index="/system">
                <template #title>
                  <el-icon>
                    <Edit />
                  </el-icon>系统信息管理
                </template>
                <el-menu-item index="/dept"><el-icon>
                    <School />
                  </el-icon>部门管理</el-menu-item>
                <el-menu-item index="/emp"><el-icon>
                    <UserFilled />
                  </el-icon>员工管理</el-menu-item>
              </el-sub-menu>

              <el-sub-menu index="data-stats-manage">
                <template #title>
                  <el-icon>
                    <Histogram />
                  </el-icon>数据统计管理
                </template>
                <el-menu-item v-if="store.loginEmp.role === 'ADMIN'" index="/report/emp"><el-icon>
                    <InfoFilled />
                  </el-icon>员工信息统计</el-menu-item>
                <el-menu-item index="/report/stu"><el-icon>
                    <User />
                  </el-icon>学员信息统计</el-menu-item>
                <el-menu-item v-if="store.loginEmp.role === 'ADMIN'" index="/log"><el-icon>
                    <UserFilled />
                  </el-icon>日志信息统计</el-menu-item>
              </el-sub-menu>

            </el-menu>
          </el-scrollbar>
        </el-aside>
        <!-- 主区域 -->
        <el-main>
          <!-- 展示子路由 -->
          <router-view></router-view>
        </el-main>
      </el-container>
    </el-container>

    <!-- 修改密码对话框 -->
    <el-dialog v-model="showUpdatePwdDialog" title="修改密码" width="400px" @close="closePwdDialog">
      <el-form ref="formEl" :model="updatePwdForm" :rules="rules" label-width="100px">
        <el-form-item label="旧密码" prop="oldPassword">
          <el-input v-model="updatePwdForm.oldPassword" type="password" placeholder="请输入旧密码" show-password />
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="updatePwdForm.newPassword" type="password" placeholder="请输入新密码" show-password />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="updatePwdForm.confirmPassword" type="password" placeholder="请再次输入新密码" show-password />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="closePwdDialog">取 消</el-button>
        <el-button type="primary" @click="confirmUpdatePwd">确 定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.header {
  /* background-color: #0000ff;
   */
  background-image: linear-gradient(to right, #4186ee, #00b0ff, #00cfdf, #00e48a, #a8eb12);
  /* background-image: linear-gradient(to right, #4186ee, #1471f1, #0059f0, #003eec, #1910e3); */
  /* text-align: center; */
}

.title {
  font-size: 40px;
  color: white;
  font-family: "楷体";
  line-height: 60px;
  /* 加粗 */
  font-weight: bold;
}

.right-tool {
  float: right;
  line-height: 60px;

}

a {
  color: white;
  /* margin-left: 15px; */
  text-decoration: none;
}
</style>
