<template>
  <div style="padding: 24px">
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px">
      <h2 style="margin: 0">添加器材</h2>
      <el-button @click="router.push('/equipment/selEquipment')">返回</el-button>
    </div>

    <el-card style="max-width: 860px">
      <el-form label-width="140px">
        <el-form-item label="器材名称">
          <el-input v-model="form.equipmentName" />
        </el-form-item>
        <el-form-item label="器材位置">
          <el-input v-model="form.equipmentLocation" />
        </el-form-item>
        <el-form-item label="器材状态">
          <el-input v-model="form.equipmentStatus" />
        </el-form-item>
        <el-form-item label="备注信息">
          <el-input v-model="form.equipmentMessage" />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="submit">添加</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import { useRouter } from 'vue-router'
import { postForm } from '../api/client'

const router = useRouter()
const form = reactive({
  equipmentName: '',
  equipmentLocation: '',
  equipmentStatus: '',
  equipmentMessage: ''
})

async function submit() {
  if (!form.equipmentName) return alert('请输入器材名称！')
  if (!form.equipmentLocation) return alert('请输入器材位置！')
  if (!form.equipmentStatus) return alert('请输入器材状态！')
  if (!form.equipmentMessage) return alert('请输入备注信息！')

  await postForm('/api/equipment/addEquipment', {
    equipmentName: form.equipmentName,
    equipmentLocation: form.equipmentLocation,
    equipmentStatus: form.equipmentStatus,
    equipmentMessage: form.equipmentMessage
  })

  router.push('/equipment/selEquipment')
}
</script>

