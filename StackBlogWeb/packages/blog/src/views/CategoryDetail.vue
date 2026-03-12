<template>
  <div class="category-detail-page">
    <!-- 分类信息 -->
    <n-card v-if="category" :bordered="false" class="category-header">
      <div class="category-info">
        <n-icon size="32" class="category-icon">
          <folder-outline />
        </n-icon>
        <div>
          <h1 class="category-title">{{ category.name }}</h1>
          <p class="category-desc">{{ category.description || '暂无描述' }}</p>
        </div>
      </div>
    </n-card>

    <!-- 文章列表 -->
    <n-card title="文章列表" :bordered="false" style="margin-top: 24px">
      <n-spin :show="loading">
        <n-empty v-if="!loading && articles.length === 0" description="该分类下暂无文章" />
        
        <div v-else class="article-list">
          <n-card
            v-for="article in articles"
            :key="article.id"
            hoverable
            class="article-card"
            @click="goToArticle(article.id)"
          >
            <h3 class="article-title">{{ article.title }}</h3>
            <p class="article-desc">{{ article.description }}</p>
            <div class="article-meta">
              <n-space>
                <n-text depth="3">
                  <n-icon><calendar-outline /></n-icon>
                  {{ article.createTime }}
                </n-text>
                <n-text depth="3">
                  <n-icon><eye-outline /></n-icon>
                  {{ article.viewCount }}
                </n-text>
              </n-space>
            </div>
          </n-card>
        </div>

        <!-- 分页 -->
        <div v-if="total > pageSize" class="pagination">
          <n-pagination
            v-model:page="page"
            :page-count="Math.ceil(total / pageSize)"
            @update:page="loadArticles"
          />
        </div>
      </n-spin>
    </n-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useMessage } from 'naive-ui'
import { FolderOutline, CalendarOutline, EyeOutline } from '@vicons/ionicons5'
import { categoryApi, articleApi } from '@stackblog/shared'
import type { CategoryResponseDTO, ArticleResponseDTO } from '@stackblog/shared'

const route = useRoute()
const router = useRouter()
const message = useMessage()

const categoryId = Number(route.params.id)
const category = ref<CategoryResponseDTO | null>(null)
const articles = ref<ArticleResponseDTO[]>([])
const loading = ref(false)
const page = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 加载分类信息
const loadCategory = async () => {
  try {
    category.value = await categoryApi.getById(categoryId)
  } catch (error) {
    message.error('加载分类失败')
  }
}

// 加载文章列表
const loadArticles = async () => {
  loading.value = true
  try {
    const result = await articleApi.getList({
      page: page.value - 1,
      size: pageSize.value,
      typeId: categoryId,
      status: 1
    })
    articles.value = result.content || []
    total.value = result.totalElements || 0
  } catch (error) {
    message.error('加载文章失败')
    articles.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

// 跳转到文章详情
const goToArticle = (id: number) => {
  router.push(`/article/${id}`).catch(err => {
    if (err.name !== 'NavigationDuplicated') {
      console.error('Navigation error:', err)
    }
  })
}

onMounted(() => {
  loadCategory()
  loadArticles()
})
</script>

<style scoped>
.category-detail-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: 24px;
}

.category-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.category-info {
  display: flex;
  align-items: center;
  gap: 16px;
}

.category-icon {
  color: white;
}

.category-title {
  margin: 0 0 8px 0;
  font-size: 28px;
  font-weight: 600;
  color: white;
}

.category-desc {
  margin: 0;
  font-size: 16px;
  color: rgba(255, 255, 255, 0.9);
}

.article-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.article-card {
  cursor: pointer;
  transition: all 0.3s;
}

.article-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.article-title {
  margin: 0 0 12px 0;
  font-size: 20px;
  font-weight: 600;
  color: #333;
}

.article-desc {
  margin: 0 0 12px 0;
  font-size: 14px;
  color: #666;
  line-height: 1.6;
}

.article-meta {
  font-size: 14px;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 24px;
}
</style>
