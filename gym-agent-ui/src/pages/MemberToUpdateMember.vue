<template>
  <div style="padding: 24px">
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px">
      <h2 style="margin: 0">编辑会员</h2>
      <el-button @click="router.push('/member/selMember')">返回</el-button>
    </div>

    <el-card style="max-width: 860px" v-if="member">
      <el-form label-width="140px">
        <el-form-item label="会员账号/卡号">
          <el-input v-model="member.memberAccount" disabled />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="member.memberPassword" disabled />
        </el-form-item>
        <el-form-item label="姓名">
          <el-input v-model="member.memberName" />
        </el-form-item>
        <el-form-item label="性别">
          <el-input v-model="member.memberGender" />
        </el-form-item>
        <el-form-item label="年龄">
          <el-input v-model="member.memberAge" />
        </el-form-item>
        <el-form-item label="联系方式">
          <el-input v-model="member.memberPhone" />
        </el-form-item>
        <el-form-item label="身高（cm）">
          <el-input v-model="member.memberHeight" />
        </el-form-item>
        <el-form-item label="体重（kg）">
          <el-input v-model="member.memberWeight" />
        </el-form-item>
        <el-form-item label="购买课时">
          <el-input v-model="member.cardClass" />
        </el-form-item>
        <el-form-item label="剩余课时">
          <el-input v-model="member.cardNextClass" />
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
const member = ref(null)

async function load() {
  const memberAccount = Number(route.query.memberAccount || 0)
  const resp = await api.get('/api/member/toUpdateMember', { params: { memberAccount } })
  const list = resp.data?.memberList || []
  if (list.length) {
    member.value = list[0]
  }
}

async function submit() {
  if (!member.value) return
  await postForm('/api/member/updateMember', member.value)
  router.push('/member/selMember')
}

onMounted(() => {
  load().catch(() => {})
})
</script>

