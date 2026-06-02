<template>
  <div class="chat-page">
    <div class="chat-header">
      <h2 class="chat-title">聊天</h2>
      <div class="chat-subtitle">与你的健身房智能助手对话</div>
    </div>

    <el-card class="chat-card" shadow="never">
      <el-scrollbar ref="scrollbarRef" class="chat-scroll">
        <div class="chat-list">
          <div v-for="m in messages" :key="m.id" class="chat-row" :class="m.role">
            <div class="bubble">
              <div class="bubble-meta">{{ m.role === 'user' ? '我' : 'AI' }}</div>
              <div class="bubble-text">{{ m.text }}</div>
            </div>
          </div>
        </div>
      </el-scrollbar>

      <div class="chat-input">
        <el-input
          v-model="draft"
          type="textarea"
          :autosize="{ minRows: 2, maxRows: 4 }"
          placeholder="输入你想问的问题，例如：如何安排每周训练计划？"
          @keydown.enter.exact.prevent="send()"
        />
        <div class="chat-actions">
          <el-button :disabled="isSending || !draft.trim()" type="primary" @click="send()">
            {{ isSending ? '发送中…' : '发送' }}
          </el-button>
          <el-button :disabled="isSending || messages.length <= 1" @click="clearChat()">清空</el-button>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { nextTick, onMounted, ref, watch } from 'vue'


const STORAGE_KEYS = {
  MESSAGES: 'chat_messages',
  MEMORY_ID: 'chat_memory_id',
  HAS_EXECUTED_HELLO: 'chat_has_executed_hello'
}


type ChatRole = 'user' | 'assistant'
type ChatMessage = {
  id: string
  role: ChatRole
  text: string
  createdAt: number
}

const draft = ref('')
const isSending = ref(false)
const scrollbarRef = ref<any>(null)
const abortController = ref<AbortController | null>(null)


const memoryId = ref(
  sessionStorage.getItem(STORAGE_KEYS.MEMORY_ID) || crypto.randomUUID()
)

const messages = ref<ChatMessage[]>([])


function loadChatHistory() {
  try {
    const savedMessages = sessionStorage.getItem(STORAGE_KEYS.MESSAGES)
    if (savedMessages) {
      messages.value = JSON.parse(savedMessages)
    }
  } catch (e) {
    console.error('加载聊天记录失败:', e)
  }
}


function saveChatHistory() {
  try {
    sessionStorage.setItem(STORAGE_KEYS.MESSAGES, JSON.stringify(messages.value))
    sessionStorage.setItem(STORAGE_KEYS.MEMORY_ID, memoryId.value)
  } catch (e) {
    console.error('保存聊天记录失败:', e)
  }
}


async function streamChat(
  message: string,
  onChunk: (text: string) => void,
  signal: AbortSignal
) {
  const memberId = localStorage.getItem('memberId') || 'unknown'
  const response = await fetch('/api/chat/query', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ memoryId: memoryId.value, memberId, message }),
    signal
  })

  if (!response.ok) {
    let errMsg = `HTTP ${response.status}`
    try {
      const errData = await response.json()
      errMsg = errData?.message || errMsg
    } catch { /* ignore parse error */ }
    throw new Error(errMsg)
  }

  const reader = response.body?.getReader()
  if (!reader) throw new Error('浏览器不支持流式响应')

  const decoder = new TextDecoder()
  let buffer = ''

  try {
    while (true) {
      const { done, value } = await reader.read()
      if (done) break

      buffer += decoder.decode(value, { stream: true })

      // 按 \n\n 分割 SSE 事件
      const events = buffer.split('\n\n')
      buffer = events.pop() || ''

      for (const event of events) {
        if (!event.trim()) continue
        // 解析 SSE data 行
        const lines = event.split('\n')
        for (const line of lines) {
          if (line.startsWith('data:')) {
            const chunk = line.slice(5)
            const text = chunk.startsWith(' ') ? chunk.slice(1) : chunk
            if (text && text !== '[DONE]') {
              onChunk(text)
            }
          }
        }
      }
    }

    // 处理 buffer 中剩余的内容
    if (buffer.trim()) {
      for (const line of buffer.split('\n')) {
        if (line.startsWith('data:')) {
          const chunk = line.slice(5)
          const text = chunk.startsWith(' ') ? chunk.slice(1) : chunk
          if (text && text !== '[DONE]') {
            onChunk(text)
          }
        }
      }
    }
  } finally {
    reader.releaseLock()
  }
}

const hello = async () => {
  try {
    const assistantId = pushMessage('assistant', '')
    const ac = new AbortController()
    abortController.value = ac

    await streamChat(
      '你好',
      (text) => {
        const msg = messages.value.find(m => m.id === assistantId)
        if (msg) {
          msg.text += text
        }
        scrollToBottom()
      },
      ac.signal
    )
  } catch (e: any) {
    // 如果是主动取消，不显示错误
    if (e?.name === 'AbortError') return
    const msg = e?.message || '请求失败'
    console.error('hello failed:', e)
    // 找到刚才创建的空白 assistant 消息并填充错误信息
    const lastMsg = messages.value[messages.value.length - 1]
    if (lastMsg && lastMsg.role === 'assistant') {
      lastMsg.text = lastMsg.text || `接口返回错误：${msg}`
    }
  } finally {
    isSending.value = false
    abortController.value = null
    await scrollToBottom()
  }
}

function pushMessage(role: ChatRole, text: string): string {
  const id = crypto.randomUUID()
  messages.value.push({
    id,
    role,
    text,
    createdAt: Date.now()
  })
  return id
}

async function scrollToBottom() {
  // 等待 DOM 更新
  await nextTick()
  // 再等一帧，确保 el-scrollbar 内部已经完成尺寸计算
  await new Promise(resolve => requestAnimationFrame(resolve))
  if (scrollbarRef.value) {
    // 先让 scrollbar 重新计算滚动条尺寸
    scrollbarRef.value.update?.()
    const wrap = scrollbarRef.value.wrapRef
    if (wrap) {
      wrap.scrollTop = wrap.scrollHeight
    }
  }
}

async function send() {
  const content = draft.value.trim()
  if (!content || isSending.value) return

  draft.value = ''
  pushMessage('user', content)
  isSending.value = true

  // 创建空的 AI 消息占位，后续流式填充
  const assistantId = pushMessage('assistant', '')
  const ac = new AbortController()
  abortController.value = ac

  try {
    await streamChat(
      content,
      (text) => {
        const msg = messages.value.find(m => m.id === assistantId)
        if (msg) {
          msg.text += text
        }
        scrollToBottom()
      },
      ac.signal
    )
  } catch (e: any) {
    if (e?.name === 'AbortError') return
    const msg = e?.message || '请求失败'
    console.error('chat query failed:', e)
    const assistantMsg = messages.value.find(m => m.id === assistantId)
    if (assistantMsg) {
      assistantMsg.text = assistantMsg.text || `请求失败：${msg}`
    }
  } finally {
    isSending.value = false
    abortController.value = null
    await scrollToBottom()
  }
}


function clearChat() {
  // 中断进行中的流式请求
  if (abortController.value) {
    abortController.value.abort()
    abortController.value = null
  }
  isSending.value = false
  // 清空所有消息
  messages.value = []
  sessionStorage.removeItem(STORAGE_KEYS.MESSAGES)
  sessionStorage.setItem(STORAGE_KEYS.MEMORY_ID, memoryId.value)
}


watch(
  [messages, memoryId],
  () => {
    saveChatHistory()
  },
  { deep: true }
)

// 监听消息数量变化，自动滚动到底部
watch(
  () => messages.value.length,
  () => {
    scrollToBottom()
  }
)


onMounted(() => {
  if (typeof crypto?.randomUUID !== 'function') {
    // 极少数环境不支持 randomUUID；这里不做降级也不会影响主流程
  }

  // 先加载历史聊天记录
  loadChatHistory()

  // 如果有历史消息，滚动到底部
  if (messages.value.length > 0) {
    scrollToBottom()
  }

  // 检查是否已经执行过hello()
  const hasExecutedHello = sessionStorage.getItem(STORAGE_KEYS.HAS_EXECUTED_HELLO)
  
  // 只有在没有执行过且没有历史消息的情况下才执行hello()
  if (!hasExecutedHello && messages.value.length === 0) {
    hello()
    // 标记为已执行
    sessionStorage.setItem(STORAGE_KEYS.HAS_EXECUTED_HELLO, 'true')
  }
})
</script>

<style scoped>
.chat-page {
  padding: 24px;
}

.chat-header {
  margin-bottom: 12px;
}

.chat-title {
  margin: 0;
}

.chat-subtitle {
  color: #666;
  margin-top: 6px;
  font-size: 13px;
}

.chat-card {
  border: 1px solid #ebeef5;
}

.chat-scroll {
  height: min(60svh, 520px);
  padding: 8px 8px 0 8px;
}

.chat-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
  padding-bottom: 8px;
}

.chat-row {
  display: flex;
}

.chat-row.user {
  justify-content: flex-end;
}

.chat-row.assistant {
  justify-content: flex-start;
}

.bubble {
  max-width: min(680px, 88%);
  padding: 10px 12px;
  border-radius: 12px;
  line-height: 1.5;
  border: 1px solid rgba(0, 0, 0, 0.06);
  background: #ffffff;
}

.chat-row.assistant .bubble {
  background: #f8f9fa;
}

.chat-row.user .bubble {
  background: #ecf5ff;
  border-color: rgba(64, 158, 255, 0.25);
}

.bubble-meta {
  font-size: 12px;
  color: rgba(0, 0, 0, 0.55);
  margin-bottom: 4px;
}

.chat-row.user .bubble-meta {
  text-align: right;
}

.chat-row.assistant .bubble-meta {
  text-align: left;
}

.bubble-text {
  white-space: pre-wrap;
  word-break: break-word;
  text-align: left;
}

.chat-input {
  border-top: 1px solid #ebeef5;
  padding: 12px;
  display: grid;
  gap: 10px;
}

.chat-actions {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
}
</style>