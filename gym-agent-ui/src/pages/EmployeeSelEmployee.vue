<template>
  <div style="padding: 24px">
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px">
      <h2 style="margin: 0">员工管理</h2>
      <el-button type="primary" @click="router.push('/employee/toAddEmployee')">添加员工信息</el-button>
    </div>

    <el-table :data="employeeList" style="width: 100%">
      <el-table-column prop="employeeAccount" label="工号" width="120" />
      <el-table-column prop="employeeName" label="姓名" />
      <el-table-column prop="employeeGender" label="性别" width="90" />
      <el-table-column prop="employeeAge" label="年龄" width="80" />
      <el-table-column prop="entryTime" label="入职时间" width="160" />
      <el-table-column prop="staff" label="职务" />
      <el-table-column prop="employeeMessage" label="备注信息" />
      <el-table-column label="操作" width="260">
        <template #default="scope">
          <el-button size="small" type="info" @click="edit(scope.row.employeeAccount)">编辑</el-button>
          <el-button
            size="small"
            type="danger"
            style="margin-left: 8px"
            @click="del(scope.row.employeeAccount)"
          >
            解雇
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <div v-if="!employeeList.length" style="color: #666; margin-top: 16px">暂无员工数据</div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import api, { postForm } from '../api/client'

const router = useRouter()
const employeeList = ref([])

async function load() {
  const resp = await api.get('/api/employee/selEmployee')
  employeeList.value = resp.data?.employeeList || []
}

function edit(employeeAccount) {
  router.push({ path: '/employee/toUpdateEmployee', query: { employeeAccount } })
}

async function del(employeeAccount) {
  if (!confirm('确定要解雇吗？')) return
  await postForm('/api/employee/delEmployee', { employeeAccount })
  await load()
}

onMounted(() => {
  load().catch(() => {})
})
</script>

