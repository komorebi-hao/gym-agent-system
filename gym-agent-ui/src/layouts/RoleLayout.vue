<template>
  <el-container class="role-layout">
    <el-aside width="220px" class="role-aside">
      <div class="role-title">健身房管理系统</div>

      <el-menu
        :router="true"
        :default-active="activePath"
        class="role-menu"
        background-color="#304156"
        text-color="#bfc5d1"
        active-text-color="#ffd04b"
      >
        <template v-if="role === 'admin'">
          <el-menu-item index="/toAdminMain">管理员主页</el-menu-item>
          <el-menu-item index="/member/toSelByCard">会员卡查询</el-menu-item>
          <el-menu-item index="/member/selMember">会员管理</el-menu-item>
          <el-menu-item index="/employee/selEmployee">员工管理</el-menu-item>
          <el-menu-item index="/equipment/selEquipment">器材管理</el-menu-item>
          <el-menu-item index="/class/selClass">课程管理</el-menu-item>
          <el-menu-item index="/class/selClassOrder">报名信息</el-menu-item>
        </template>

        <template v-else>
          <el-menu-item index="/toUserMain">会员主页</el-menu-item>
          <el-menu-item index="/user/toUserInfo">个人信息</el-menu-item>
          <el-menu-item index="/user/toChat">聊天</el-menu-item>
          <el-sub-menu index="user-course">
            <template #title>课程管理</template>
            <el-menu-item index="/user/toApplyClass">报名选课</el-menu-item>
            <el-menu-item index="/user/toUserClass">我的课程</el-menu-item>
          </el-sub-menu>
        </template>
      </el-menu>

      <div class="role-footer">
        <div class="role-footer-title">你好！</div>
        <div class="role-footer-text">{{ role === 'admin' ? '管理员' : '尊敬的会员' }}</div>
      </div>
    </el-aside>

    <el-main class="role-main">
      <slot />
    </el-main>
  </el-container>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'

type Role = 'admin' | 'user'

const props = defineProps<{
  role: Role
}>()

const route = useRoute()
const activePath = computed(() => route.path)
</script>

<style scoped>
.role-layout {
  min-height: 100svh;
}

.role-aside {
  background: #304156;
  color: #bfc5d1;
  display: flex;
  flex-direction: column;
}

.role-title {
  padding: 16px 12px;
  font-weight: 600;
  color: #ffffff;
}

.role-menu {
  border-right: none;
  flex: 1;
}

.role-footer {
  padding: 12px;
  border-top: 1px solid rgba(255, 255, 255, 0.08);
}

.role-footer-title {
  font-size: 12px;
  opacity: 0.9;
}

.role-footer-text {
  margin-top: 4px;
  font-size: 14px;
  color: #ffffff;
}

.role-main {
  padding: 0;
}
</style>

