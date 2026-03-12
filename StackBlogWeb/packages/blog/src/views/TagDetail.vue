<template>
  <div class="tag-detail-page">
    <!-- 标签信息 -->
    <n-card v-if="tag" :bordered="false" class="tag-header">
      <div class="tag-info">
        <n-icon size="32" class="tag-icon">
          <pricetag-outline />
        </n-icon>
        <div>
          <h1 class="tag-title"># {{ tag.name }}</h1>
          <p class="tag-desc">共 {{ total }} 篇文章</p>
        </div>
      </div>
    </n-card>

    <!-- 文章列表 -->
    <n-card title="文章列表" :bordered="false" style="margin-top: 24px">
      <n-spin :show="loading">
        <n-empty v-if="!loading && articles.length === 0" description="该标签下暂无文章" />
        
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
                <n-space v-if="article.tags">
                  <n-tag
                    v-for="t in article.tags"
                    :key="t.id"
                    type="info"
                    size="small"
                    round
                  >
                    {{ t.name }}
                  </n-tag>
                </n-space>
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
import { PricetagOutline, CalendarOutline, EyeOutline } from '@vicons/ionicons5'
import { tagApi, articleApi } from '@stackblog/shared'
import type { TagResponseDTO, ArticleResponseDTO } from '@stackblog/shared'

const route = useRoute()
const router = useRouter()
const message = useMessage()

const tagId = Number(route.params.id)
const tag = ref<TagResponseDTO | null>(null)
const articles = ref<ArticleResponseDTO[]>([])
const loading = ref(false)
const page = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 加载标签信息
const loadTag = async () => {
  try {
    tag.value = await tagApi.getById(tagId)
  } catch (error) {
    message.error('加载标签失败')
  }
}

// 加载文章列表
const loadArticles = async () => {
  loading.value = true
  try {
    const result = await articleApi.getList({
      page: page.value - 1,
      size: pageSize.value,
      tagId: tagId,
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
  loadTag()
  loadArticles()
})
</script>

<style scoped>
.tag-detail-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: 24px;
}

.tag-header {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  color: white;
}

.tag-info {
  display: flex;
  align-items: center;
  gap: 16px;
}

.tag-icon {
  color: white;
}

.tag-title {
  margin: 0 0 8px 0;
  font-size: 28px;
  font-weight: 600;
  color: white;
}

.tag-desc {
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
