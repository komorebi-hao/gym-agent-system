<template>
  <div style="padding: 24px">
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px">
      <h2 style="margin: 0">课程管理</h2>
      <el-button type="primary" @click="router.push('/class/toAddClass')">添加课程信息</el-button>
    </div>

    <el-table :data="classList" style="width: 100%">
      <el-table-column prop="classId" label="编号" width="90" />
      <el-table-column prop="className" label="名称" />
      <el-table-column prop="classBegin" label="时间" width="180" />
      <el-table-column prop="classTime" label="时长" width="120" />
      <el-table-column prop="coach" label="教练" width="160" />
      <el-table-column label="操作" width="260">
        <template #default="scope">
          <el-button size="small" type="info" @click="goOrder(scope.row.classId)">报名信息</el-button>
          <el-button size="small" type="danger" @click="delClass(scope.row.classId)" style="margin-left: 8px">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <div v-if="!classList.length" style="color: #666; margin-top: 16px">暂无课程数据</div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import api, { postForm } from '../api/client'

const router = useRouter()
const classList = ref([])

async function load() {
  const resp = await api.get('/api/class/selClass')
  classList.value = resp.data?.classList || []
}

function goOrder(classId) {
  router.push({ path: '/class/selClassOrder', query: { classId } })
}

async function delClass(classId) {
  if (!confirm('确定要删除吗？')) return
  await postForm('/api/class/delClass', { classId })
  await load()
}

onMounted(() => {
  load().catch(() => {})
})
</script>

