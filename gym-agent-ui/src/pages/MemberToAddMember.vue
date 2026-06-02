<template>
  <div style="padding: 24px">
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px">
      <h2 style="margin: 0">添加会员</h2>
      <el-button @click="router.push('/member/selMember')">返回</el-button>
    </div>

    <el-card style="max-width: 860px">
      <el-form label-width="140px">
        <el-form-item label="姓名">
          <el-input v-model="form.memberName" />
        </el-form-item>
        <el-form-item label="性别">
          <el-input v-model="form.memberGender" />
        </el-form-item>
        <el-form-item label="年龄">
          <el-input v-model="form.memberAge" />
        </el-form-item>
        <el-form-item label="联系方式（11位手机号）">
          <el-input v-model="form.memberPhone" />
        </el-form-item>
        <el-form-item label="身高（cm）">
          <el-input v-model="form.memberHeight" />
        </el-form-item>
        <el-form-item label="体重（kg）">
          <el-input v-model="form.memberWeight" />
        </el-form-item>
        <el-form-item label="购买课时">
          <el-input v-model="form.cardClass" />
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
  memberName: '',
  memberGender: '',
  memberAge: '',
  memberPhone: '',
  memberHeight: '',
  memberWeight: '',
  cardClass: ''
})

async function submit() {
  msg.value = ''
  if (!form.memberName) return alert('请输入姓名！')
  if (!form.memberGender) return alert('请输入性别！')
  if (!form.memberAge) return alert('请输入年龄！')
  if (!form.memberPhone) return alert('请输入联系方式！')
  if (!form.memberHeight) return alert('请输入身高！')
  if (!form.memberWeight) return alert('请输入体重！')
  if (!form.cardClass) return alert('请输入购买课时！')

  await postForm('/api/member/addMember', {
    memberName: form.memberName,
    memberGender: form.memberGender,
    memberAge: Number(form.memberAge),
    memberPhone: Number(form.memberPhone),
    memberHeight: Number(form.memberHeight),
    memberWeight: Number(form.memberWeight),
    cardClass: Number(form.cardClass)
  })

  router.push('/member/selMember')
}
</script>

