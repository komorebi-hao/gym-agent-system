<template>
  <div style="padding: 24px">
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px">
      <h2 style="margin: 0">添加员工</h2>
      <el-button @click="router.push('/employee/selEmployee')">返回</el-button>
    </div>

    <el-card style="max-width: 860px">
      <el-form label-width="140px">
        <el-form-item label="姓名">
          <el-input v-model="form.employeeName" />
        </el-form-item>
        <el-form-item label="性别">
          <el-input v-model="form.employeeGender" />
        </el-form-item>
        <el-form-item label="年龄">
          <el-input v-model="form.employeeAge" />
        </el-form-item>
        <el-form-item label="职务">
          <el-input v-model="form.staff" />
        </el-form-item>
        <el-form-item label="备注信息">
          <el-input v-model="form.employeeMessage" />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="submit">添加</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { postForm } from '../api/client'

const router = useRouter()
const msg = ref('')
const form = reactive({
  employeeName: '',
  employeeGender: '',
  employeeAge: '',
  staff: '',
  employeeMessage: ''
})

async function submit() {
  msg.value = ''
  if (!form.employeeName) return alert('请输入姓名！')
  if (!form.employeeGender) return alert('请输入性别！')
  if (!form.employeeAge) return alert('请输入年龄！')
  if (!form.staff) return alert('请输入职务！')

  await postForm('/api/employee/addEmployee', {
    employeeName: form.employeeName,
    employeeGender: form.employeeGender,
    employeeAge: Number(form.employeeAge),
    staff: form.staff,
    employeeMessage: form.employeeMessage
  })

  router.push('/employee/selEmployee')
}
</script>

