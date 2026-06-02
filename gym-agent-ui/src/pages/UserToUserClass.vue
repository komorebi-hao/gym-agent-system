<template>
  <div style="padding: 24px">
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px">
      <h2 style="margin: 0">我的课程</h2>
      <el-button @click="router.push('/toUserMain')">返回主页</el-button>
    </div>

    <el-card>
      <el-table :data="classOrderList" style="width: 100%">
        <el-table-column prop="classId" label="编号" width="120" />
        <el-table-column prop="className" label="名称" />
        <el-table-column prop="classBegin" label="时间" width="180" />
        <el-table-column prop="coach" label="教练" width="160" />
        <el-table-column label="操作" width="160">
          <template #default="scope">
            <el-button size="small" type="danger" @click="del(scope.row.classOrderId)">退课</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div v-if="!classOrderList.length" style="color: #666; margin-top: 16px">暂无课程</div>
    </el-card>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import api, { postForm } from '../api/client'

const router = useRouter()
const classOrderList = ref([])

async function load() {
  const resp = await api.get('/api/user/toUserClass')
  classOrderList.value = resp.data?.classOrderList || []
}

async function del(classOrderId) {
  if (!confirm('确定要退课吗？')) return
  await postForm('/api/user/delUserClass', { classOrderId })
  await load()
}

onMounted(() => {
  load().catch(() => {})
})
</script>

