<template>
  <div class="category-page">
    <n-card title="文章分类" :bordered="false">
      <n-spin :show="loading">
        <n-empty v-if="!loading && categories.length === 0" description="暂无分类" />
        
        <n-grid v-else :cols="1" :x-gap="12" :y-gap="12">
          <n-grid-item v-for="category in categories" :key="category.id">
            <n-card 
              :bordered="true" 
              hoverable
              class="category-card"
              @click="goToCategory(category.id)"
            >
              <div class="category-content">
                <div class="category-info">
                  <n-icon size="24" class="category-icon">
                    <folder-outline />
                  </n-icon>
                  <div class="category-details">
                    <h3 class="category-name">{{ category.name }}</h3>
                    <p class="category-desc">{{ category.description || '暂无描述' }}</p>
                  </div>
                </div>
                <div class="category-count">
                  <n-tag type="info" round>
                    {{ category.articleCount }} 篇文章
                  </n-tag>
                </div>
              </div>
            </n-card>
          </n-grid-item>
        </n-grid>
      </n-spin>
    </n-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useMessage } from 'naive-ui'
import { FolderOutline } from '@vicons/ionicons5'
import { categoryApi } from '@stackblog/shared'
import type { CategoryResponseDTO } from '@stackblog/shared'

const router = useRouter()
const message = useMessage()

const categories = ref<CategoryResponseDTO[]>([])
const loading = ref(false)

// 加载分类列表
const loadCategories = async () => {
  loading.value = true
  try {
    categories.value = await categoryApi.getList()
  } catch (error) {
    message.error('加载分类失败')
  } finally {
    loading.value = false
  }
}

// 跳转到分类详情
const goToCategory = (id: number) => {
  router.push(`/category/${id}`).catch(err => {
    if (err.name !== 'NavigationDuplicated') {
      console.error('Navigation error:', err)
    }
  })
}

onMounted(() => {
  loadCategories()
})
</script>

<style scoped>
.category-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: 24px;
}

.category-card {
  cursor: pointer;
  transition: all 0.3s;
}

.category-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.category-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.category-info {
  display: flex;
  align-items: center;
  gap: 16px;
  flex: 1;
}

.category-icon {
  color: #409eff;
}

.category-details {
  flex: 1;
}

.category-name {
  margin: 0 0 8px 0;
  font-size: 18px;
  font-weight: 600;
  color: #333;
}

.category-desc {
  margin: 0;
  font-size: 14px;
  color: #666;
}

.category-count {
  margin-left: 16px;
}
</style>
