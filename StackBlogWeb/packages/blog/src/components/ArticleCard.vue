<template>
  <n-card class="article-card" hoverable @click="goToDetail">
    <!-- 置顶和推荐标签 -->
    <div class="badges">
      <n-tag v-if="article.top" type="error" size="small">置顶</n-tag>
      <n-tag v-if="article.recommended" type="warning" size="small">推荐</n-tag>
    </div>

    <div class="card-content">
      <h2 class="article-title">{{ article.title }}</h2>

      <div class="article-meta">
        <span class="meta-item">
          <n-icon :component="PersonOutline" />
          {{ article.author }}
        </span>
        <span class="meta-item">
          <n-icon :component="CalendarOutline" />
          {{ article.createTime }}
        </span>
        <span class="meta-item">
          <n-icon :component="EyeOutline" />
          {{ article.viewCount }} 次浏览
        </span>
        <span class="meta-item">
          <n-icon :component="ChatbubbleOutline" />
          {{ article.commentCount }} 条评论
        </span>
      </div>

      <p class="article-description">{{ article.description }}</p>

      <!-- 标签 -->
      <div v-if="article.tags && article.tags.length > 0" class="article-tags">
        <n-tag
          v-for="tag in article.tags"
          :key="tag.id"
          size="small"
          :bordered="false"
        >
          {{ tag.name }}
        </n-tag>
      </div>

      <div class="article-footer">
        <n-button text type="primary">
          阅读全文
          <template #icon>
            <n-icon :component="ArrowForwardOutline" />
          </template>
        </n-button>
      </div>
    </div>
  </n-card>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import { PersonOutline, CalendarOutline, EyeOutline, ChatbubbleOutline, ArrowForwardOutline } from '@vicons/ionicons5'
import type { ArticleResponseDTO } from '@stackblog/shared'

const props = defineProps<{
  article: ArticleResponseDTO
}>()

const router = useRouter()

const goToDetail = () => {
  router.push(`/article/${props.article.id}`).catch(err => {
    if (err.name !== 'NavigationDuplicated') {
      console.error('Navigation error:', err)
    }
  })
}
</script>

<style scoped lang="scss">
.article-card {
  cursor: pointer;
  transition: all 0.3s;
  position: relative;

  &:hover {
    transform: translateY(-4px);
  }

  .badges {
    position: absolute;
    top: 12px;
    right: 12px;
    display: flex;
    gap: 8px;
    z-index: 10;
  }

  .card-content {
    padding: 10px;
  }

  .article-title {
    font-size: 22px;
    font-weight: 600;
    color: #303133;
    margin: 0 0 12px;
    line-height: 1.4;
    transition: color 0.3s;

    &:hover {
      color: #18a058;
    }
  }

  .article-meta {
    display: flex;
    flex-wrap: wrap;
    gap: 16px;
    margin-bottom: 12px;
    font-size: 13px;
    color: #909399;

    .meta-item {
      display: flex;
      align-items: center;
      gap: 4px;
    }
  }

  .article-description {
    font-size: 14px;
    color: #606266;
    line-height: 1.8;
    margin-bottom: 12px;
    display: -webkit-box;
    -webkit-line-clamp: 3;
    -webkit-box-orient: vertical;
    overflow: hidden;
  }

  .article-tags {
    display: flex;
    flex-wrap: wrap;
    gap: 8px;
    margin-bottom: 12px;
  }

  .article-footer {
    display: flex;
    justify-content: flex-end;
  }
}
</style>
