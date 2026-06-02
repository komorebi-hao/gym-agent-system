<template>
  <div style="padding: 24px">
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px">
      <h2 style="margin: 0">报名信息</h2>
      <el-button @click="router.push('/class/selClass')">返回</el-button>
    </div>

    <el-card>
      <div v-if="!classOrderList.length" style="color: #666">暂无报名信息！</div>

      <div v-else style="margin-bottom: 12px">
        <div>课程编号：{{ classOrderList[0].classId }}</div>
        <div>课程名称：{{ classOrderList[0].className }}</div>
        <div>开课时间：{{ classOrderList[0].classBegin }}</div>
      </div>

      <el-table :data="classOrderList" style="width: 100%">
        <el-table-column prop="memberAccount" label="会员账号" width="140" />
        <el-table-column prop="memberName" label="会员姓名" />
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import api from '../api/client'

const route = useRoute()
const router = useRouter()
const classOrderList = ref([])

async function load() {
  const classId = Number(route.query.classId || 0)
  const resp = await api.get('/api/class/selClassOrder', { params: { classId } })
  classOrderList.value = resp.data?.classOrderList || []
}

onMounted(() => {
  load().catch(() => {})
})
</script>

