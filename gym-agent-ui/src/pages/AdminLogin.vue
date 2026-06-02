<template>
  <div style="display: flex; justify-content: center; padding: 40px">
    <el-card style="width: 420px">
      <h2 style="margin: 0 0 16px 0; text-align: center">管理员登录</h2>

      <el-input v-model="form.adminAccount" placeholder="账号" />
      <el-input
        v-model="form.adminPassword"
        placeholder="密码"
        type="password"
        show-password
        style="margin-top: 12px"
        @keyup.enter="submit"
      />

      <el-button type="primary" style="width: 100%; margin-top: 16px" @click="submit">
        登录
      </el-button>

      <div style="color: #d00; margin-top: 10px" v-if="msg">{{ msg }}</div>

      <div style="text-align: center; margin-top: 14px">
        <el-link type="primary" @click="router.push('/toUserLogin')">转到会员登录</el-link>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { postForm } from '../api/client'

const router = useRouter()
const form = reactive({
  adminAccount: '',
  adminPassword: ''
})
const msg = ref('')

async function submit() {
  msg.value = ''
  try {
    const resp = await postForm('/api/adminLogin', {
      adminAccount: form.adminAccount,
      adminPassword: form.adminPassword
    })
    if (resp.data && resp.data.success) {
      router.push('/toAdminMain')
    } else {
      msg.value = resp.data?.message || '登录失败'
    }
  } catch (e) {
    msg.value = e?.response?.data?.message || '登录失败'
  }
}
</script>

