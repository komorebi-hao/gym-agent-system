<template>
  <div style="padding: 24px">
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px">
      <h2 style="margin: 0">会员卡查询</h2>
      <el-button @click="router.push('/toAdminMain')">返回主页</el-button>
    </div>

    <el-card style="max-width: 860px; margin-bottom: 16px">
      <el-form label-width="140px">
        <el-form-item label="会员卡号/账号">
          <el-input v-model="memberAccount" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submit">查询</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card v-if="noMessage" style="max-width: 860px; color: #d00">{{ noMessage }}</el-card>

    <el-table :data="memberList" style="width: 100%" v-else>
      <el-table-column prop="memberAccount" label="会员账号/卡号" width="160" />
      <el-table-column prop="memberName" label="姓名" />
      <el-table-column prop="cardTime" label="办卡时间" width="140" />
      <el-table-column prop="memberGender" label="性别" width="90" />
      <el-table-column prop="memberAge" label="年龄" width="80" />
      <el-table-column prop="memberPhone" label="联系方式" width="140" />
      <el-table-column prop="memberHeight" label="身高" width="90" />
      <el-table-column prop="memberWeight" label="体重" width="90" />
      <el-table-column prop="cardClass" label="购买课时" width="100" />
      <el-table-column prop="cardNextClass" label="剩余课时" width="100" />
      <el-table-column label="操作" width="180">
        <template #default="scope">
          <el-button size="small" type="info" @click="edit(scope.row.memberAccount)">编辑</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import api, { postForm } from '../api/client'

const router = useRouter()
const memberAccount = ref('')
const memberList = ref([])
const noMessage = ref('')

async function submit() {
  noMessage.value = ''
  memberList.value = []
  const account = Number(memberAccount.value || 0)
  if (!account) return alert('请输入会员卡号！')

  const resp = await postForm('/api/member/selByCard', { memberAccount: account })
  const data = resp.data || {}
  if (data.memberList && data.memberList.length) {
    memberList.value = data.memberList
  } else {
    noMessage.value = data.noMessage || '未查询到数据'
  }
}

function edit(memberAccount) {
  router.push({ path: '/member/toUpdateMember', query: { memberAccount } })
}

onMounted(() => {
  // 不强制加载，用户点击查询后展示结果
})
</script>

