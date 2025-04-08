<template>
  <div class="card" style="width: 50%">
    <div style="font-size: 16px">修改密码</div>
    <el-form ref="formRef" :rules="data.rules" :model="data.user" label-width="80px" style="padding: 20px 30px 10px 0">
      <el-form-item prop="password" label="原密码">
        <el-input show-password v-model="data.user.password" autocomplete="off" prefix-icon="Lock" placeholder="请输入密码" />
      </el-form-item>
      <el-form-item prop="newPassword" label="新密码">
        <el-input show-password v-model="data.user.newPassword" autocomplete="off" prefix-icon="Lock" placeholder="请输入密码" />
      </el-form-item>
      <el-form-item prop="new2Password" label="确认密码">
        <el-input show-password v-model="data.user.new2Password" autocomplete="off" prefix-icon="Lock" placeholder="请输入密码" />
      </el-form-item>
    </el-form>
    <div style="text-align: center">
      <el-button type="primary" style="padding: 18px" @click="updatePassword">保存</el-button>
    </div>
  </div>
</template>
<script setup>
import {reactive, ref} from "vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";

const data = reactive({
  user: JSON.parse(localStorage.getItem('code_user') || '{}'),
  rules: {
    Password: [
      { required: true, message: '请填写原密码', trigger: 'blur' }
    ],
    newPassword: [
      { required: true, message: '请填写新密码', trigger: 'blur' }
    ],
    new2Password: [
      { required: true, message: '请确认新密码', trigger: 'blur' }
    ]
  }
})

const formRef = ref()

const updatePassword = () => {
  formRef.value.validate(valid => {
    if (valid) {
      request.post('/updatePassword', data.user).then(res => {
        if (res.code === '200') {
          ElMessage.success('修改成功')
          setInterval(() => {
            localStorage.removeItem('code_user')
            location.href = '/login'
          }, 500)
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}
</script>