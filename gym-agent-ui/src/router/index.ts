import { createRouter, createWebHistory } from 'vue-router'
import NotImplemented from '../pages/NotImplemented.vue'
import AdminLogin from '../pages/AdminLogin.vue'
import UserLogin from '../pages/UserLogin.vue'
import AdminMain from '../pages/AdminMain.vue'
import UserMain from '../pages/UserMain.vue'
import ClassSelClass from '../pages/ClassSelClass.vue'
import ClassToAddClass from '../pages/ClassToAddClass.vue'
import ClassSelClassOrder from '../pages/ClassSelClassOrder.vue'
import MemberSelMember from '../pages/MemberSelMember.vue'
import MemberToAddMember from '../pages/MemberToAddMember.vue'
import MemberToUpdateMember from '../pages/MemberToUpdateMember.vue'
import MemberToSelByCard from '../pages/MemberToSelByCard.vue'
import EmployeeSelEmployee from '../pages/EmployeeSelEmployee.vue'
import EmployeeToAddEmployee from '../pages/EmployeeToAddEmployee.vue'
import EmployeeToUpdateEmployee from '../pages/EmployeeToUpdateEmployee.vue'
import EquipmentSelEquipment from '../pages/EquipmentSelEquipment.vue'
import EquipmentToAddEquipment from '../pages/EquipmentToAddEquipment.vue'
import EquipmentToUpdateEquipment from '../pages/EquipmentToUpdateEquipment.vue'
import UserToUserInfo from '../pages/UserToUserInfo.vue'
import UserToUpdateInfo from '../pages/UserToUpdateInfo.vue'
import UserToUserClass from '../pages/UserToUserClass.vue'
import UserToApplyClass from '../pages/UserToApplyClass.vue'
import UserChat from '../pages/UserChat.vue'
import api from '../api/client'

const routes = [
  { path: '/', component: AdminLogin },
  { path: '/toUserLogin', component: UserLogin },

  { path: '/toAdminMain', component: AdminMain, meta: { requiresAuth: true, role: 'admin' } },
  { path: '/toUserMain', component: UserMain, meta: { requiresAuth: true, role: 'user' } },

  // 管理端（先占位，后续把 templates 全量迁移为 Vue 页面组件）
  { path: '/member/selMember', component: MemberSelMember, meta: { requiresAuth: true, role: 'admin' } },
  { path: '/member/toAddMember', component: MemberToAddMember, meta: { requiresAuth: true, role: 'admin' } },
  { path: '/member/toUpdateMember', component: MemberToUpdateMember, meta: { requiresAuth: true, role: 'admin' } },
  { path: '/member/toSelByCard', component: MemberToSelByCard, meta: { requiresAuth: true, role: 'admin' } },

  { path: '/employee/selEmployee', component: EmployeeSelEmployee, meta: { requiresAuth: true, role: 'admin' } },
  { path: '/employee/toAddEmployee', component: EmployeeToAddEmployee, meta: { requiresAuth: true, role: 'admin' } },
  { path: '/employee/toUpdateEmployee', component: EmployeeToUpdateEmployee, meta: { requiresAuth: true, role: 'admin' } },

  { path: '/equipment/selEquipment', component: EquipmentSelEquipment, meta: { requiresAuth: true, role: 'admin' } },
  { path: '/equipment/toAddEquipment', component: EquipmentToAddEquipment, meta: { requiresAuth: true, role: 'admin' } },
  { path: '/equipment/toUpdateEquipment', component: EquipmentToUpdateEquipment, meta: { requiresAuth: true, role: 'admin' } },

  { path: '/class/selClass', component: ClassSelClass, meta: { requiresAuth: true, role: 'admin' } },
  { path: '/class/toAddClass', component: ClassToAddClass, meta: { requiresAuth: true, role: 'admin' } },
  { path: '/class/selClassOrder', component: ClassSelClassOrder, meta: { requiresAuth: true, role: 'admin' } },

  // 用户端
  { path: '/user/toUserInfo', component: UserToUserInfo, meta: { requiresAuth: true, role: 'user' } },
  { path: '/user/toUpdateInfo', component: UserToUpdateInfo, meta: { requiresAuth: true, role: 'user' } },
  { path: '/user/toUserClass', component: UserToUserClass, meta: { requiresAuth: true, role: 'user' } },
  { path: '/user/toApplyClass', component: UserToApplyClass, meta: { requiresAuth: true, role: 'user' } },
  { path: '/user/toChat', component: UserChat, meta: { requiresAuth: true, role: 'user' } },

  // fallback
  { path: '/:pathMatch(.*)*', component: NotImplemented }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach(async (to) => {
  const meta = to.meta as any
  if (!meta?.requiresAuth) return true

  const role = meta.role as string | undefined
  try {
    if (role === 'user') {
      await api.get('/api/toUserMain')
    } else {
      await api.get('/api/toAdminMain')
    }
    return true
  } catch (e) {
    // 未登录/会话失效：按角色回登录页
    return { path: role === 'user' ? '/toUserLogin' : '/' }
  }
})

export default router

