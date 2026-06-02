<template>
  <div style="padding: 24px">
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px">
      <h2 style="margin: 0">个人信息</h2>
      <el-button @click="router.push('/user/toUpdateInfo')">编辑个人信息</el-button>
    </div>

    <el-card v-if="member" style="max-width: 860px">
      <el-descriptions :column="1" border>
        <el-descriptions-item label="会员账号/卡号">{{ member.memberAccount }}</el-descriptions-item>
        <el-descriptions-item label="姓名">{{ member.memberName }}</el-descriptions-item>
        <el-descriptions-item label="性别">{{ member.memberGender }}</el-descriptions-item>
        <el-descriptions-item label="年龄">{{ member.memberAge }}</el-descriptions-item>
        <el-descriptions-item label="联系方式">{{ member.memberPhone }}</el-descriptions-item>
        <el-descriptions-item label="身高">{{ member.memberHeight }}</el-descriptions-item>
        <el-descriptions-item label="体重">{{ member.memberWeight }}</el-descriptions-item>
        <el-descriptions-item label="办卡时间">{{ member.cardTime }}</el-descriptions-item>
        <el-descriptions-item label="剩余课时">{{ member.cardNextClass }}</el-descriptions-item>
      </el-descriptions>
    </el-card>

    <el-card v-else style="max-width: 860px">未登录或会话失效。</el-card>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import api from '../api/client'

const router = useRouter()
const member = ref(null)

async function load() {
  const resp = await api.get('/api/user/toUserInfo')
  member.value = resp.data?.member || null
}

onMounted(() => {
  load().catch(() => {})
})
</script>

