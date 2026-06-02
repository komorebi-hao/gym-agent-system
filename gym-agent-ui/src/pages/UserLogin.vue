<template>
  <div style="display: flex; justify-content: center; padding: 40px">
    <el-card style="width: 420px">
      <h2 style="margin: 0 0 16px 0; text-align: center">会员登录</h2>

      <el-input v-model="form.memberAccount" placeholder="账号" />
      <el-input
        v-model="form.memberPassword"
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
        <el-link type="primary" @click="router.push('/')">转到管理员登录</el-link>
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
  memberAccount: '',
  memberPassword: ''
})

const msg = ref('')



async function submit() {

  msg.value = ''
  try {
    const resp = await postForm('/api/userLogin', {
      memberAccount: form.memberAccount,
      memberPassword: form.memberPassword
    })
    
    if (resp.data && resp.data.success) {
      const memberId = form.memberAccount
      localStorage.setItem('memberId', memberId) // 将会员ID存储到localStorage中，以便在其他页面使用
      router.push('/toUserMain')
    } else {
      msg.value = resp.data?.message || '登录失败'
    }
  } catch (e) {
    msg.value = e?.response?.data?.message || '登录失败'
  }
}
</script>

