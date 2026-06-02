# 🏋️ 力愿健身房管理系统（Gym Agent System）

基于 **Spring Boot 3 + Vue 3** 的全栈健身房管理系统，集成 **LangChain4j + 阿里通义千问大模型** 的 AI 智能客服助手 **"小力"**。

---

## ✨ 核心功能

### 管理员端
- **数据看板** — 会员 / 员工 / 器材实时统计
- **会员管理** — 新增、编辑、删除、按卡号查询
- **员工管理** — 新增、编辑、删除
- **器材管理** — 新增、编辑、删除，跟踪位置与状态
- **课程管理** — 发布课程、查看选课记录

### 会员端
- **个人信息** — 查看与修改个人资料
- **课程中心** — 浏览课程、报名选课、查看已选课程
- **AI 智能客服 "小力"** — 基于大模型的对话式助手，支持：
  - 个性化欢迎（自动识别会员身份）
  - 智能课程推荐
  - 一键选课 / 退课
  - 已选课程查询

---

## 🛠️ 技术栈

| 层级 | 技术 |
|------|------|
| 前端 | Vue 3 (Composition API)、TypeScript、Vite、Element Plus、Axios |
| 后端 | Spring Boot 3.2、Java 17、MyBatis 3、Maven |
| 数据库 | MySQL 8.0（业务数据）、MongoDB（对话记忆） |
| AI | LangChain4j 1.0、阿里百炼 DashScope（通义千问）、Pinecone 向量数据库 |
| 认证 | Session-based（Cookie），路由守卫 + 拦截器 |

---

## 📁 项目结构

```
gym-agent-system/
├── gym-agent-ui/                    # 前端 — Vue 3 + Vite
│   ├── src/
│   │   ├── api/client.ts            # Axios 实例，Session 认证
│   │   ├── layouts/RoleLayout.vue   # 侧边栏布局（管理员/会员两套菜单）
│   │   ├── pages/                   # 页面组件
│   │   │   ├── AdminLogin.vue       # 管理员登录
│   │   │   ├── AdminMain.vue        # 管理员首页看板
│   │   │   ├── UserLogin.vue        # 会员登录
│   │   │   ├── UserMain.vue         # 会员首页
│   │   │   ├── UserChat.vue         # 🤖 AI 智能客服聊天
│   │   │   ├── Member*.vue          # 会员管理（增删改查）
│   │   │   ├── Employee*.vue        # 员工管理
│   │   │   ├── Equipment*.vue       # 器材管理
│   │   │   ├── Class*.vue           # 课程管理 / 选课记录
│   │   │   ├── UserTo*.vue          # 会员自助操作
│   │   │   └── NotImplemented.vue   # 404 兜底
│   │   ├── router/index.ts          # 路由配置 + 登录守卫
│   │   └── style.css                # 全局样式
│   ├── vite.config.ts               # Vite 配置（API 代理到 :8080）
│   └── package.json
│
└── gym-management-system/           # 后端 — Spring Boot 3
    ├── src/main/java/com/gym/
    │   ├── assistant/
    │   │   └── GymAssistant.java    # LangChain4j @AiService 接口
    │   ├── bean/                    # ChatForm / ChatMessages
    │   ├── config/
    │   │   ├── GymAssistantConfig.java     # AI 记忆 / RAG 检索器
    │   │   ├── EmbeddingStoreConfig.java   # Pinecone 向量存储
    │   │   └── WebMvcConfig.java           # CORS 配置
    │   ├── controller/              # REST API 控制器
    │   │   ├── ApiLoginController.java     # 登录 / 登出
    │   │   ├── ApiMemberController.java    # 会员 CRUD
    │   │   ├── ApiEmployeeController.java  # 员工 CRUD
    │   │   ├── ApiEquipmentController.java # 器材 CRUD
    │   │   ├── ApiClassController.java     # 课程管理
    │   │   ├── ApiUserController.java      # 会员自助操作
    │   │   └── ApiChatController.java      # 🤖 AI 聊天（SSE 流式）
    │   ├── mapper/                  # MyBatis Mapper 接口
    │   ├── pojo/                    # 实体类
    │   │   ├── Admin.java
    │   │   ├── Member.java          # 会员（含会员卡字段）
    │   │   ├── Employee.java
    │   │   ├── Equipment.java
    │   │   ├── ClassTable.java      # 课程
    │   │   └── ClassOrder.java      # 选课记录
    │   ├── security/
    │   │   └── SessionAuthInterceptor.java  # Session 登录拦截器
    │   ├── service/                 # 业务逻辑
    │   ├── store/
    │   │   └── MongoChatMemoryStore.java    # MongoDB 对话记忆
    │   └── tools/
    │       └── GymAssistantTools.java       # AI 工具函数（@Tool）
    ├── src/main/resources/
    │   ├── application.properties   # 数据库 / AI 配置
    │   ├── gym-prompt-template.txt  # 🤖 AI 系统提示词
    │   └── mapper/                  # MyBatis XML
    └── pom.xml
```

---

## 🚀 快速启动

### 环境要求

- **JDK 17**+
- **Node.js** 18+
- **MySQL** 8.0+
- **MongoDB** 4.0+
- **阿里百炼 API Key**（[申请地址](https://bailian.console.aliyun.com/)）
- **Pinecone API Key**（[申请地址](https://www.pinecone.io/)）

### 1. 数据库初始化

创建 MySQL 数据库并执行建表脚本：

```sql
CREATE DATABASE IF NOT EXISTS gym_management_system
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;
```

> 后端启动时 MyBatis 会根据 Mapper XML 自动匹配已有表结构，请确保 `admin`、`member`、`employee`、`equipment`、`class_table`、`class_order` 表已创建。

### 2. 环境变量

```bash
# 阿里百炼 API Key（用于通义千问大模型 + 文本向量）
export A_LI_API_KEY="sk-xxxxxxxxxxxxxxxx"

# Pinecone API Key（用于向量存储）
export PINECONE_API_KEY="xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"
```

### 3. 配置数据库连接

编辑 `gym-management-system/src/main/resources/application.properties`：

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/gym_management_system?...
spring.datasource.username=root
spring.datasource.password=你的密码

spring.data.mongodb.uri=mongodb://localhost:27017/chat_memory_db
```

### 4. 启动后端

```bash
cd gym-management-system
./mvnw spring-boot:start
```

后端运行在 `http://localhost:8080`。

### 5. 启动前端

```bash
cd gym-agent-ui
npm install
npm run dev
```

前端运行在 `http://localhost:5173`，API 请求自动代理到后端 `:8080`。

### 6. 访问系统

| 入口 | 地址 | 角色 |
|------|------|------|
| 管理员登录 | `http://localhost:5173/` | 管理员 |
| 会员登录 | `http://localhost:5173/toUserLogin` | 会员 |

---

## 🤖 AI 智能助手架构

```
会员浏览器                    Spring Boot 后端                   阿里百炼
   │                             │                                │
   │  POST /api/chat/query       │                                │
   │  (SSE streaming)            │                                │
   ├────────────────────────────>│                                │
   │                             │  GymAssistant.chat()           │
   │                             │  ┌─ @SystemMessage (提示词)     │
   │                             │  ├─ ChatMemory (MongoDB)       │
   │                             │  ├─ @Tool 调用                 │
   │                             │  │   ├─ getMemberInfo()        │
   │                             │  │   ├─ queryEnrolledClasses() │
   │                             │  │   ├─ queryAllClasses()      │
   │                             │  │   ├─ enrollClass()          │
   │                             │  │   └─ cancelClass()          │
   │                             │  └─ ContentRetriever (Pinecone)│
   │                             │                                │
   │                             ├───────────────────────────────>│ qwen-max
   │                             │<───────────────────────────────│ (流式)
   │  SSE: text/event-stream     │                                │
   │<────────────────────────────┤                                │
```

- **对话模型**：`qwen-max`（普通）/ `qwen-plus`（流式）
- **向量模型**：`text-embedding-v3` → Pinecone
- **记忆存储**：MongoDB `chat_memory_db`，窗口大小 20 条
- **RAG 检索**：最小相似度 0.8，返回 Top-1

---

## 🔐 认证流程

- 采用 **Session + Cookie** 认证
- 前端 Axios 配置 `withCredentials: true`
- 后端 `SessionAuthInterceptor` 拦截 `/api/**` 请求
  - 管理员接口要求 Session 中存在 `admin` 属性
  - 会员接口要求 Session 中存在 `user` 属性
  - 未登录返回 `401`

---

## 📋 API 概览

### 登录
| 方法 | 端点 | 说明 |
|------|------|------|
| POST | `/api/adminLogin` | 管理员登录 |
| POST | `/api/userLogin` | 会员登录 |
| POST | `/api/logout` | 退出登录 |
| GET | `/api/toAdminMain` | 管理员首页（统计数） |
| GET | `/api/toUserMain` | 会员首页（个人信息） |

### 会员管理 (admin)
| 方法 | 端点 | 说明 |
|------|------|------|
| GET | `/api/member/selMember` | 会员列表 |
| POST | `/api/member/addMember` | 新增会员 |
| POST | `/api/member/delMember` | 删除会员 |
| POST | `/api/member/updateMember` | 更新会员 |
| POST | `/api/member/selByCard` | 按卡号查询 |

### 员工 / 器材 / 课程
类似 CRUD 结构，路径前缀分别为 `/api/employee`、`/api/equipment`、`/api/class`。

### 会员自助
| 方法 | 端点 | 说明 |
|------|------|------|
| GET | `/api/user/toUserInfo` | 查看个人信息 |
| POST | `/api/user/updateInfo` | 修改个人信息 |
| GET | `/api/user/toUserClass` | 我的课程 |
| POST | `/api/user/delUserClass` | 取消选课 |
| GET | `/api/user/toApplyClass` | 可选课程列表 |
| POST | `/api/user/applyClass` | 报名选课 |

### AI 对话
| 方法 | 端点 | 说明 |
|------|------|------|
| POST | `/api/chat/query` | SSE 流式对话（需会员登录） |

---

## 📝 License

仅供学习交流使用。
