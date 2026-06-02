<template>
  <div style="padding: 24px">
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px">
      <h2 style="margin: 0">器材管理</h2>
      <el-button type="primary" @click="router.push('/equipment/toAddEquipment')">添加器材信息</el-button>
    </div>

    <el-table :data="equipmentList" style="width: 100%">
      <el-table-column prop="equipmentId" label="器材id" width="120" />
      <el-table-column prop="equipmentName" label="名称" />
      <el-table-column prop="equipmentLocation" label="位置" width="140" />
      <el-table-column prop="equipmentStatus" label="状态" width="120" />
      <el-table-column prop="equipmentMessage" label="备注信息" />
      <el-table-column label="操作" width="260">
        <template #default="scope">
          <el-button size="small" type="info" @click="edit(scope.row.equipmentId)">编辑</el-button>
          <el-button
            size="small"
            type="danger"
            style="margin-left: 8px"
            @click="del(scope.row.equipmentId)"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <div v-if="!equipmentList.length" style="color: #666; margin-top: 16px">暂无器材数据</div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import api, { postForm } from '../api/client'

const router = useRouter()
const equipmentList = ref([])

async function load() {
  const resp = await api.get('/api/equipment/selEquipment')
  equipmentList.value = resp.data?.equipmentList || []
}

function edit(equipmentId) {
  router.push({ path: '/equipment/toUpdateEquipment', query: { equipmentId } })
}

async function del(equipmentId) {
  if (!confirm('确定要删除吗？')) return
  await postForm('/api/equipment/delEquipment', { equipmentId })
  await load()
}

onMounted(() => {
  load().catch(() => {})
})
</script>

