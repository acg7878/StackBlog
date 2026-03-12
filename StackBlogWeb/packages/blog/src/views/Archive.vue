<template>
  <div class="archive-page">
    <n-card title="文章归档" hoverable>
      <n-spin :show="loading">
        <div v-if="!loading && archiveMap.size > 0" class="archive-list">
          <div v-for="[year, articles] in archiveMap" :key="year" class="archive-year">
            <h3 class="year-title">{{ year }} 年 ({{ articles.length }} 篇)</h3>
            <div class="article-list">
              <div
                v-for="article in articles"
                :key="article.id"
                class="article-item"
                @click="router.push(`/article/${article.id}`)"
              >
                <n-text depth="3" class="article-date">{{ formatDate(article.createTime) }}</n-text>
                <n-text class="article-title">{{ article.title }}</n-text>
              </div>
            </div>
          </div>
        </div>

        <n-empty v-else-if="!loading" description="暂无文章" />
      </n-spin>
    </n-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { articleApi } from '@stackblog/shared'
import type { ArticleResponseDTO } from '@stackblog/shared'

const router = useRouter()

const loading = ref(false)
const archiveMap = ref<Map<string, ArticleResponseDTO[]>>(new Map())

// 格式化日期
const formatDate = (dateStr: string) => {
  return dateStr.split(' ')[0]
}

// 加载归档
const loadArchive = async () => {
  loading.value = true
  try {
    const res = await articleApi.getList({ page: 0, size: 100, status: 1 })
    const articles = res.content || []

    // 按年份分组
    const map = new Map<string, ArticleResponseDTO[]>()
    articles.forEach(article => {
      const year = article.createTime.split('-')[0]
      if (!map.has(year)) {
        map.set(year, [])
      }
      map.get(year)!.push(article)
    })

    // 按年份倒序排序
    archiveMap.value = new Map([...map.entries()].sort((a, b) => b[0].localeCompare(a[0])))
  } catch (error) {
    console.error('加载归档失败', error)
    archiveMap.value = new Map()
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadArchive()
})
</script>

<style scoped lang="scss">
.archive-page {
  .archive-list {
    .archive-year {
      margin-bottom: 30px;

      &:last-child {
        margin-bottom: 0;
      }

      .year-title {
        font-size: 20px;
        font-weight: 600;
        color: #18a058;
        margin: 0 0 16px;
        padding-bottom: 8px;
        border-bottom: 2px solid #18a058;
      }

      .article-list {
        .article-item {
          display: flex;
          align-items: center;
          padding: 12px 0;
          border-bottom: 1px solid #f0f0f0;
          cursor: pointer;
          transition: all 0.3s;

          &:last-child {
            border-bottom: none;
          }

          &:hover {
            background: #f5f7fa;
            padding-left: 12px;

            .article-title {
              color: #18a058;
            }
          }

          .article-date {
            flex-shrink: 0;
            width: 100px;
            font-size: 13px;
          }

          .article-title {
            flex: 1;
            font-size: 14px;
            transition: color 0.3s;
          }
        }
      }
    }
  }
}
</style>
