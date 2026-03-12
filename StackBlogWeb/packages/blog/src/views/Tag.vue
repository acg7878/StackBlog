<template>
  <div class="tag-page">
    <n-card title="文章标签" :bordered="false">
      <n-spin :show="loading">
        <n-empty v-if="!loading && tags.length === 0" description="暂无标签" />
        
        <div v-else class="tag-cloud">
          <n-tag
            v-for="tag in tags"
            :key="tag.id"
            :type="getRandomType()"
            :size="getRandomSize()"
            round
            class="tag-item"
            @click="goToTag(tag.id)"
          >
            {{ tag.name }} ({{ tag.articleCount }})
          </n-tag>
        </div>
      </n-spin>
    </n-card>

    <!-- 标签列表 -->
    <n-card title="标签列表" :bordered="false" style="margin-top: 24px">
      <n-spin :show="loading">
        <n-list v-if="!loading && tags.length > 0" bordered>
          <n-list-item v-for="tag in tags" :key="tag.id">
            <div class="tag-list-item" @click="goToTag(tag.id)">
              <div class="tag-info">
                <n-icon size="20" class="tag-icon">
                  <pricetag-outline />
                </n-icon>
                <span class="tag-name">{{ tag.name }}</span>
              </div>
              <div class="tag-stats">
                <n-text depth="3">{{ tag.articleCount }} 篇文章</n-text>
              </div>
            </div>
          </n-list-item>
        </n-list>
      </n-spin>
    </n-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useMessage } from 'naive-ui'
import { PricetagOutline } from '@vicons/ionicons5'
import { tagApi } from '@stackblog/shared'
import type { TagResponseDTO } from '@stackblog/shared'

const router = useRouter()
const message = useMessage()

const tags = ref<TagResponseDTO[]>([])
const loading = ref(false)

// 加载标签列表
const loadTags = async () => {
  loading.value = true
  try {
    tags.value = await tagApi.getList()
  } catch (error) {
    message.error('加载标签失败')
  } finally {
    loading.value = false
  }
}

// 跳转到标签详情
const goToTag = (id: number) => {
  router.push(`/tag/${id}`).catch(err => {
    if (err.name !== 'NavigationDuplicated') {
      console.error('Navigation error:', err)
    }
  })
}

// 获取随机标签类型（用于标签云样式）
const getRandomType = () => {
  const types = ['default', 'success', 'info', 'warning', 'error']
  return types[Math.floor(Math.random() * types.length)] as any
}

// 获取随机标签大小（用于标签云样式）
const getRandomSize = () => {
  const sizes = ['small', 'medium', 'large']
  return sizes[Math.floor(Math.random() * sizes.length)] as any
}

onMounted(() => {
  loadTags()
})
</script>

<style scoped>
.tag-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: 24px;
}

.tag-cloud {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  padding: 12px 0;
}

.tag-item {
  cursor: pointer;
  transition: all 0.3s;
}

.tag-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}

.tag-list-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  cursor: pointer;
  padding: 8px 0;
  transition: all 0.3s;
}

.tag-list-item:hover {
  background: #f5f5f5;
  padding: 8px 12px;
  margin: 0 -12px;
  border-radius: 4px;
}

.tag-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.tag-icon {
  color: #409eff;
}

.tag-name {
  font-size: 16px;
  font-weight: 500;
  color: #333;
}

.tag-stats {
  color: #666;
}
</style>
