<template>
  <div style="padding: 24px">
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px">
      <h2 style="margin: 0">管理员主页</h2>
      <el-button type="danger" @click="logout">退出登录</el-button>
    </div>

    <el-row :gutter="16">
      <el-col :span="6">
        <el-card>健身房总人数：{{ totals.humanTotal }} 人</el-card>
      </el-col>
      <el-col :span="6">
        <el-card>员工人数：{{ totals.employeeTotal }} 人</el-card>
      </el-col>
      <el-col :span="6">
        <el-card>会员人数：{{ totals.memberTotal }} 人</el-card>
      </el-col>
      <el-col :span="6">
        <el-card>器材数：{{ totals.equipmentTotal }} 件</el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { onMounted, reactive } from 'vue'
import { useRouter } from 'vue-router'
import api, { postForm } from '../api/client'

const router = useRouter()
const totals = reactive({
  memberTotal: 0,
  employeeTotal: 0,
  humanTotal: 0,
  equipmentTotal: 0
})

async function load() {
  const resp = await api.get('/api/toAdminMain')
  const data = resp.data || {}
  totals.memberTotal = data.memberTotal || 0
  totals.employeeTotal = data.employeeTotal || 0
  totals.humanTotal = data.humanTotal || 0
  totals.equipmentTotal = data.equipmentTotal || 0
}

async function logout() {
  await postForm('/api/logout', {})
  router.push('/')
}

onMounted(() => {
  load().catch(() => {})
})
</script>

