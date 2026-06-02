<template>
  <div style="padding: 24px">
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px">
      <h2 style="margin: 0">添加课程</h2>
      <el-button @click="router.push('/class/selClass')">返回</el-button>
    </div>

    <el-card style="max-width: 720px">
      <el-form label-width="120px">
        <el-form-item label="编号">
          <el-input v-model="form.classId" placeholder="4位编号" />
        </el-form-item>
        <el-form-item label="名称">
          <el-input v-model="form.className" placeholder="课程名称" />
        </el-form-item>
        <el-form-item label="时间">
          <el-input v-model="form.classBegin" placeholder="xxxx年xx月xx日 00：00" />
        </el-form-item>
        <el-form-item label="时长">
          <el-input v-model="form.classTime" placeholder="xx分钟" />
        </el-form-item>
        <el-form-item label="教练">
          <el-input v-model="form.coach" placeholder="教练" />
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
  classId: '',
  className: '',
  classBegin: '',
  classTime: '',
  coach: ''
})

async function submit() {
  msg.value = ''
  if (!form.classId) return alert('请输入课程编号！')
  if (!form.className) return alert('请输入课程名称！')
  if (!form.classBegin) return alert('请输入课程时间！')
  if (!form.classTime) return alert('请输入课程时长！')
  if (!form.coach) return alert('请输入教练！')

  await postForm('/api/class/addClass', { ...form, classId: Number(form.classId) })
  router.push('/class/selClass')
}
</script>

