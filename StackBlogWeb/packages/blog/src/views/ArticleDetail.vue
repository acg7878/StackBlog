<template>
  <div class="article-detail-page">
    <n-grid :x-gap="20" :cols="24" responsive="screen">
      <n-grid-item :span="24" :md="17">
        <!-- 加载中 -->
        <n-spin :show="loading">
          <!-- 文章内容 -->
          <n-card v-if="!loading && article" class="article-card" hoverable>
            <!-- 文章标题 -->
            <h1 class="article-title">{{ article.title }}</h1>

            <!-- 文章元信息 -->
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

            <!-- 标签 -->
            <div v-if="article.tags && article.tags.length > 0" class="article-tags">
              <n-tag
                v-for="tag in article.tags"
                :key="tag.id"
                :bordered="false"
              >
                {{ tag.name }}
              </n-tag>
            </div>

            <n-divider />

            <!-- 文章正文 -->
            <div class="article-content">
              <div v-html="article.content"></div>
            </div>

            <n-divider />

            <!-- 版权声明 -->
            <n-alert type="info" class="article-copyright">
              <p><strong>本文作者：</strong>{{ article.author }}</p>
              <p><strong>原创声明：</strong>{{ article.original ? '本文为原创文章，转载请注明出处' : '本文为转载文章' }}</p>
            </n-alert>
          </n-card>

          <!-- 文章不存在 -->
          <n-card v-else-if="!loading">
            <n-empty description="文章不存在" />
          </n-card>
        </n-spin>

        <!-- 评论区 -->
        <n-card v-if="article" title="评论" hoverable style="margin-top: 20px;">
          <template #header-extra>
            ({{ article.commentCount }})
          </template>
          <n-empty description="评论功能开发中..." />
        </n-card>
      </n-grid-item>

      <!-- 侧边栏 -->
      <n-grid-item :span="24" :md="7">
        <Sidebar />
      </n-grid-item>
    </n-grid>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useMessage } from 'naive-ui'
import { PersonOutline, CalendarOutline, EyeOutline, ChatbubbleOutline } from '@vicons/ionicons5'
import { articleApi } from '@stackblog/shared'
import type { ArticleDetailResponseDTO } from '@stackblog/shared'
import Sidebar from '../components/Sidebar.vue'

const route = useRoute()
const message = useMessage()

const article = ref<ArticleDetailResponseDTO | null>(null)
const loading = ref(false)

// 加载文章详情
const loadArticle = async () => {
  const id = Number(route.params.id)
  if (!id) {
    message.error('文章ID无效')
    return
  }

  loading.value = true
  try {
    article.value = await articleApi.getDetail(id)
  } catch (error) {
    message.error('加载文章失败')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadArticle()
})
</script>

<style scoped lang="scss">
.article-detail-page {
  .article-card {
    .article-title {
      font-size: 28px;
      font-weight: 600;
      color: #303133;
      margin: 0 0 16px;
      line-height: 1.4;
    }

    .article-meta {
      display: flex;
      flex-wrap: wrap;
      gap: 16px;
      margin-bottom: 16px;
      font-size: 13px;
      color: #909399;

      .meta-item {
        display: flex;
        align-items: center;
        gap: 4px;
      }
    }

    .article-tags {
      display: flex;
      flex-wrap: wrap;
      gap: 8px;
      margin-bottom: 20px;
    }

    .article-content {
      font-size: 16px;
      line-height: 1.8;
      color: #303133;
      min-height: 200px;

      :deep(h1) {
        font-size: 24px;
        font-weight: 600;
        margin: 24px 0 16px;
      }

      :deep(h2) {
        font-size: 20px;
        font-weight: 600;
        margin: 20px 0 12px;
      }

      :deep(h3) {
        font-size: 18px;
        font-weight: 600;
        margin: 16px 0 8px;
      }

      :deep(p) {
        margin: 12px 0;
      }

      :deep(code) {
        background: #f5f7fa;
        padding: 2px 6px;
        border-radius: 3px;
        font-family: 'Courier New', monospace;
      }

      :deep(pre) {
        background: #282c34;
        color: #abb2bf;
        padding: 16px;
        border-radius: 4px;
        overflow-x: auto;
        margin: 16px 0;

        code {
          background: none;
          padding: 0;
          color: inherit;
        }
      }

      :deep(img) {
        max-width: 100%;
        height: auto;
        border-radius: 4px;
        margin: 16px 0;
      }
    }

    .article-copyright {
      p {
        margin: 8px 0;
      }
    }
  }
}
</style>
