<template>
  <div class="not-found-container">
    <el-result
      icon="warning"
      title="404"
      sub-title="抱歉，您访问的页面不存在"
    >
      <template #extra>
        <div class="tips">
          <p>您访问的路径：<strong>{{ currentPath }}</strong></p>
          <p v-if="suggestion" class="suggestion">
            您是否要访问：
            <el-link type="primary" @click="goToSuggestion">{{ suggestion }}</el-link>
          </p>
        </div>
        <el-space :size="20" style="margin-top: 20px">
          <el-button type="primary" @click="goHome">返回首页</el-button>
          <el-button @click="goBack">返回上一页</el-button>
        </el-space>
      </template>
    </el-result>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()

const currentPath = computed(() => route.fullPath)

// 路径建议映射
const pathSuggestions: Record<string, { path: string; name: string }> = {
  '/create': { path: '/article/create', name: '创建文章' },
  '/list': { path: '/article/list', name: '文章列表' },
  '/article': { path: '/article/list', name: '文章列表' },
  '/comment': { path: '/comment', name: '评论管理' },
  '/category': { path: '/category', name: '分类管理' },
  '/tag': { path: '/tag', name: '标签管理' },
  '/user': { path: '/user', name: '用户管理' },
  '/settings': { path: '/settings', name: '系统设置' }
}

const suggestion = computed(() => {
  const path = route.path
  const match = pathSuggestions[path]
  return match ? match.name : null
})

const goToSuggestion = () => {
  const path = route.path
  const match = pathSuggestions[path]
  if (match) {
    router.push(match.path)
  }
}

const goHome = () => {
  router.push('/')
}

const goBack = () => {
  router.back()
}
</script>

<style scoped lang="scss">
.not-found-container {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.tips {
  margin-bottom: 20px;
  padding: 20px;
  background: #f5f7fa;
  border-radius: 8px;
  text-align: left;
  max-width: 500px;
}

.tips p {
  margin: 8px 0;
  color: #606266;
}

.tips strong {
  color: #409eff;
  font-family: 'Courier New', monospace;
}

.suggestion {
  margin-top: 12px;
  font-size: 14px;
}

:deep(.el-result) {
  padding: 60px 32px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
}

:deep(.el-result__icon svg) {
  width: 80px;
  height: 80px;
}

:deep(.el-result__title) {
  font-size: 48px;
  font-weight: 700;
  margin-top: 20px;
}

:deep(.el-result__subtitle) {
  font-size: 18px;
  margin-top: 12px;
}
</style>
