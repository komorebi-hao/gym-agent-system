<template>
  <div style="padding: 24px">
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px">
      <h2 style="margin: 0">编辑个人信息</h2>
      <el-button @click="router.push('/user/toUserInfo')">返回</el-button>
    </div>

    <el-card style="max-width: 860px" v-if="member">
      <el-form label-width="140px">
        <el-form-item label="会员账号/卡号">
          <el-input v-model="member.memberAccount" disabled />
        </el-form-item>
        <el-form-item label="姓名">
          <el-input v-model="member.memberName" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="member.memberPassword" />
        </el-form-item>
        <el-form-item label="性别">
          <el-input v-model="member.memberGender" />
        </el-form-item>
        <el-form-item label="年龄">
          <el-input v-model="member.memberAge" />
        </el-form-item>
        <el-form-item label="联系方式">
          <el-input v-model="member.memberPhone" />
        </el-form-item>
        <el-form-item label="身高">
          <el-input v-model="member.memberHeight" />
        </el-form-item>
        <el-form-item label="体重">
          <el-input v-model="member.memberWeight" />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="submit">确认修改</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card v-else>加载中...</el-card>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import api, { postForm } from '../api/client'

const router = useRouter()
const member = ref(null)

async function load() {
  const resp = await api.get('/api/user/toUpdateInfo')
  member.value = resp.data?.member || null
}

function validatePhone(phone) {
  // 与原 Thymeleaf 的正则一致
  const reg = /^[1][3,4,5,7,8,9][0-9]{9}$/
  return reg.test(String(phone || ''))
}

async function submit() {
  if (!member.value) return
  if (!validatePhone(member.value.memberPhone)) {
    alert('手机号码错误！')
    return
  }
  await postForm('/api/user/updateInfo', member.value)
  router.push('/user/toUserInfo')
}

onMounted(() => {
  load().catch(() => {})
})
</script>

