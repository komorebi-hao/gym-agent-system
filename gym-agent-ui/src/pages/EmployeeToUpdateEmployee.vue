<template>
  <div style="padding: 24px">
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px">
      <h2 style="margin: 0">编辑员工</h2>
      <el-button @click="router.push('/employee/selEmployee')">返回</el-button>
    </div>

    <el-card style="max-width: 860px" v-if="employee">
      <el-form label-width="140px">
        <el-form-item label="工号">
          <el-input v-model="employee.employeeAccount" disabled />
        </el-form-item>
        <el-form-item label="姓名">
          <el-input v-model="employee.employeeName" />
        </el-form-item>
        <el-form-item label="性别">
          <el-input v-model="employee.employeeGender" />
        </el-form-item>
        <el-form-item label="年龄">
          <el-input v-model="employee.employeeAge" />
        </el-form-item>
        <el-form-item label="职务">
          <el-input v-model="employee.staff" />
        </el-form-item>
        <el-form-item label="备注信息">
          <el-input v-model="employee.employeeMessage" />
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
const employee = ref(null)

async function load() {
  const employeeAccount = Number(route.query.employeeAccount || 0)
  const resp = await api.get('/api/employee/toUpdateEmployee', { params: { employeeAccount } })
  const list = resp.data?.employeeList || []
  employee.value = list[0] || null
}

async function submit() {
  if (!employee.value) return
  await postForm('/api/employee/updateEmployee', employee.value)
  router.push('/employee/selEmployee')
}

onMounted(() => {
  load().catch(() => {})
})
</script>

