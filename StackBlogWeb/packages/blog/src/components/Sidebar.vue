<template>
  <n-space vertical :size="20">
    <!-- 推荐文章 -->
    <n-card title="推荐文章" hoverable>
      <template #header-extra>
        <n-icon :component="StarOutline" />
      </template>
      <div v-if="recommendedArticles.length > 0" class="article-list">
        <div
          v-for="article in recommendedArticles"
          :key="article.id"
          class="article-item"
          @click="navigateTo(`/article/${article.id}`)"
        >
          <h4 class="article-title">{{ article.title }}</h4>
          <div class="article-meta">
            <n-icon :component="EyeOutline" />
            <span>{{ article.viewCount }}</span>
          </div>
        </div>
      </div>
      <n-empty v-else description="暂无推荐" size="small" />
    </n-card>

    <!-- 分类 -->
    <n-card title="分类" hoverable>
      <template #header-extra>
        <n-icon :component="FolderOutline" />
      </template>
      <div v-if="categories.length > 0" class="category-list">
        <n-tag
          v-for="category in categories"
          :key="category.id"
          class="category-tag"
          :bordered="false"
          @click="navigateTo(`/category/${category.id}`)"
        >
          {{ category.name }}
        </n-tag>
      </div>
      <n-empty v-else description="暂无分类" size="small" />
    </n-card>

    <!-- 标签云 -->
    <n-card title="标签云" hoverable>
      <template #header-extra>
        <n-icon :component="PricetagsOutline" />
      </template>
      <div v-if="tags.length > 0" class="tag-cloud">
        <n-tag
          v-for="tag in tags"
          :key="tag.id"
          class="tag-item"
          type="info"
          :bordered="false"
          @click="navigateTo(`/tag/${tag.id}`)"
        >
          {{ tag.name }}
        </n-tag>
      </div>
      <n-empty v-else description="暂无标签" size="small" />
    </n-card>
  </n-space>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { StarOutline, EyeOutline, FolderOutline, PricetagsOutline } from '@vicons/ionicons5'
import { articleApi, categoryApi, tagApi } from '@stackblog/shared'
import type { ArticleResponseDTO, ArticleType, Tag } from '@stackblog/shared'

const router = useRouter()

// 推荐文章
const recommendedArticles = ref<ArticleResponseDTO[]>([])

// 分类
const categories = ref<ArticleType[]>([])

// 标签
const tags = ref<Tag[]>([])

// 安全的路由跳转
const navigateTo = (path: string) => {
  router.push(path).catch(err => {
    if (err.name !== 'NavigationDuplicated') {
      console.error('Navigation error:', err)
    }
  })
}

// 加载推荐文章
const loadRecommended = async () => {
  try {
    recommendedArticles.value = await articleApi.getRecommended(5)
  } catch (error) {
    console.error('加载推荐文章失败', error)
  }
}

// 加载分类
const loadCategories = async () => {
  try {
    const allCategories = await categoryApi.getList()
    categories.value = allCategories.filter(c => c.available)
  } catch (error) {
    console.error('加载分类失败', error)
  }
}

// 加载标签
const loadTags = async () => {
  try {
    tags.value = await tagApi.getList()
  } catch (error) {
    console.error('加载标签失败', error)
  }
}

onMounted(() => {
  loadRecommended()
  loadCategories()
  loadTags()
})
</script>

<style scoped lang="scss">
.article-list {
  .article-item {
    padding: 12px 0;
    border-bottom: 1px solid #f0f0f0;
    cursor: pointer;
    transition: all 0.3s;

    &:last-child {
      border-bottom: none;
    }

    &:hover {
      background: #f5f7fa;
      padding-left: 8px;
    }

    .article-title {
      font-size: 14px;
      font-weight: 500;
      color: #303133;
      margin: 0 0 6px;
      display: -webkit-box;
      -webkit-line-clamp: 2;
      -webkit-box-orient: vertical;
      overflow: hidden;
      line-height: 1.5;
    }

    .article-meta {
      font-size: 12px;
      color: #909399;
      display: flex;
      align-items: center;
      gap: 4px;
    }
  }
}

.category-list,
.tag-cloud {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;

  .category-tag,
  .tag-item {
    cursor: pointer;
    transition: all 0.3s;

    &:hover {
      transform: scale(1.05);
    }
  }
}
</style>
