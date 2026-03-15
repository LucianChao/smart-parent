<script setup lang="ts">
import { loginApi } from '@/api/login';
import type { LoginEmp } from '@/api/model/model';
import router from '@/router';
import { useLoginEmpStore } from '@/stores/loginEmp';
import { ElMessage } from 'element-plus';
import { ref } from 'vue';

// 使用Store
const store = useLoginEmpStore()


let loginForm = ref<LoginEmp>({ username: '', password: '' })

// 登录
const login = async () => {
  // 发起异步请求--登录操作
  const result = await loginApi(loginForm.value)
  if (result.code) {
    ElMessage.success('登录成功')
    // 登录成功，跳转到首页-----通过路由进行跳转
    router.push('/index')

    // 往Pinia中存入令牌信息--->就是往store对象中存储登录返回的用户信息（包含JWT令牌）
    store.setLoginEmp(result.data)
  } else {
    ElMessage.error(result.msg)
  }
}

// 重置
const clean = () => {
  // 清理表单数据
  loginForm.value = { username: '', password: '' }
}
</script>
<template>
  <div id="container">
    <div class="login-form">
      <el-form label-width="80px">
        <p class="title">智能学习辅助系统</p>
        <el-form-item label="用户名" prop="username">
          <el-input v-model="loginForm.username" placeholder="请输入用户名"></el-input>
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input type="password" v-model="loginForm.password" placeholder="请输入密码"></el-input>
        </el-form-item>

        <el-form-item>
          <el-button class="button" type="primary" @click="login">登录</el-button>
          <el-button class="button" type="primary" @click="clean">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>
<style scoped>
#container {
  padding: 10%;
  height: 410px;
  background-image: url('../../assets/bg1.jpg');
  background-repeat: no-repeat;
  background-size: cover;
}

.login-form {
  max-width: 400px;
  padding: 30px;
  margin: 0 auto;
  border: 1px solid #e0e0e0;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);
  background-color: white;
}

.title {
  font-size: 30px;
  font-family: '楷体';
  text-align: center;
  margin-bottom: 30px;
  font-weight: bold;
}

.button {
  margin-top: 30px;
  width: 120px;
}
</style>
