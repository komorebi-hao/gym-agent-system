<template>
  <div style="padding: 24px">
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px">
      <h2 style="margin: 0">会员主页</h2>
      <el-button type="danger" @click="logout">退出登录</el-button>
    </div>

    <el-card v-if="member">
      <h3 style="margin: 0 0 12px 0">{{ member.memberName }} 的主页</h3>
      <div style="color: #666">会员账号/卡号：{{ member.memberAccount }}</div>
    </el-card>
    <el-card v-else>未登录或会话失效。</el-card>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import api, { postForm } from '../api/client'

const router = useRouter()
const member = ref(null)

async function load() {
  const resp = await api.get('/api/toUserMain')
  const data = resp.data || {}
  member.value = data.member || null
}

async function logout() {
  try {
    // 先调用后端登出接口，销毁服务端会话
    await postForm('/api/logout', {})
  } catch (e) {
    // 即使后端接口调用失败，也要强制清除本地数据并退出
    console.error('登出接口调用失败:', e)
  } finally {
    // ==================== 新增：彻底清除所有本地数据 ====================
    // 1. 清除聊天相关的sessionStorage数据
    const chatStorageKeys = [
      'chat_messages',
      'chat_memory_id',
      'chat_has_executed_hello'
    ]
    chatStorageKeys.forEach(key => {
      sessionStorage.removeItem(key)
    })

    // 2. 清除用户相关的localStorage数据
    localStorage.removeItem('memberId')

    // 3. 可选：清除所有sessionStorage（如果没有其他需要保留的数据）
    // sessionStorage.clear()

    // 4. 跳转到登录页
    router.push('/toUserLogin')
  }
}

onMounted(() => {
  load().catch(() => {})
})
</script>

