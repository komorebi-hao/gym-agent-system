<template>
  <RoleLayout v-if="showLayout" :role="layoutRole">
    <router-view />
  </RoleLayout>
  <router-view v-else />
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import RoleLayout from './layouts/RoleLayout.vue'

const route = useRoute()

const showLayout = computed(() => {
  const p = route.path

  // 登录入口/登录页不展示侧边栏
  if (p === '/' || p === '/toAdminMain' || p === '/toUserLogin' || p === '/toUserMain') {
    // 其中到主页面需要展示侧边栏
    return p === '/toAdminMain' || p === '/toUserMain'
  }

  // 主业务区：管理员
  if (p.startsWith('/member') || p.startsWith('/employee') || p.startsWith('/equipment') || p.startsWith('/class')) {
    return true
  }

  // 主业务区：会员
  if (p.startsWith('/user')) {
    return true
  }

  return false
})

const layoutRole = computed(() => {
  const p = route.path
  return p === '/toUserMain' || p.startsWith('/user') ? 'user' : 'admin'
})
</script>
