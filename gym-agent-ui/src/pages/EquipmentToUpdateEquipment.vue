<template>
  <div style="padding: 24px">
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px">
      <h2 style="margin: 0">编辑器材</h2>
      <el-button @click="router.push('/equipment/selEquipment')">返回</el-button>
    </div>

    <el-card style="max-width: 860px" v-if="equipment">
      <el-form label-width="140px">
        <el-form-item label="器材id">
          <el-input v-model="equipment.equipmentId" disabled />
        </el-form-item>
        <el-form-item label="器材名称">
          <el-input v-model="equipment.equipmentName" />
        </el-form-item>
        <el-form-item label="器材位置">
          <el-input v-model="equipment.equipmentLocation" />
        </el-form-item>
        <el-form-item label="器材状态">
          <el-input v-model="equipment.equipmentStatus" />
        </el-form-item>
        <el-form-item label="备注信息">
          <el-input v-model="equipment.equipmentMessage" />
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
import { useRoute, useRouter } from 'vue-router'
import api, { postForm } from '../api/client'

const route = useRoute()
const router = useRouter()
const equipment = ref(null)

async function load() {
  const equipmentId = Number(route.query.equipmentId || 0)
  const resp = await api.get('/api/equipment/toUpdateEquipment', { params: { equipmentId } })
  const list = resp.data?.equipmentList || []
  equipment.value = list[0] || null
}

async function submit() {
  if (!equipment.value) return
  await postForm('/api/equipment/updateEquipment', equipment.value)
  router.push('/equipment/selEquipment')
}

onMounted(() => {
  load().catch(() => {})
})
</script>

