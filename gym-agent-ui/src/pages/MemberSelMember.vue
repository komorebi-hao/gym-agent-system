<template>
  <div style="padding: 24px">
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px">
      <h2 style="margin: 0">会员管理</h2>
      <el-button type="primary" @click="router.push('/member/toAddMember')">添加会员信息</el-button>
    </div>

    <el-table :data="memberList" style="width: 100%">
      <el-table-column prop="memberAccount" label="会员账号/卡号" width="140" />
      <el-table-column prop="memberName" label="姓名" />
      <el-table-column prop="cardTime" label="办卡时间" width="140" />
      <el-table-column prop="memberGender" label="性别" width="90" />
      <el-table-column prop="memberAge" label="年龄" width="80" />
      <el-table-column prop="memberPhone" label="联系方式" width="140" />
      <el-table-column prop="memberHeight" label="身高" width="90" />
      <el-table-column prop="memberWeight" label="体重" width="90" />
      <el-table-column prop="cardClass" label="购买课时" width="100" />
      <el-table-column prop="cardNextClass" label="剩余课时" width="100" />
      <el-table-column label="操作" width="260">
        <template #default="scope">
          <el-button size="small" type="info" @click="edit(scope.row.memberAccount)">编辑</el-button>
          <el-button size="small" type="danger" @click="del(scope.row.memberAccount)" style="margin-left: 8px">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <div v-if="!memberList.length" style="color: #666; margin-top: 16px">暂无数据</div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import api, { postForm } from '../api/client'

const router = useRouter()
const memberList = ref([])

async function load() {
  const resp = await api.get('/api/member/selMember')
  memberList.value = resp.data?.memberList || []
}

function edit(memberAccount) {
  router.push({ path: '/member/toUpdateMember', query: { memberAccount } })
}

async function del(memberAccount) {
  if (!confirm('确定要删除吗？')) return
  await postForm('/api/member/delMember', { memberAccount })
  await load()
}

onMounted(() => {
  load().catch(() => {})
})
</script>

