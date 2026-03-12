<template>
  <div class="home-page">
    <n-grid :x-gap="20" :cols="24" responsive="screen">
      <!-- 文章列表 -->
      <n-grid-item :span="24" :md="17">
        <n-spin :show="loading">
          <n-space v-if="!loading && articleList.length > 0" vertical :size="20">
            <ArticleCard
              v-for="article in articleList"
              :key="article.id"
              :article="article"
            />

            <!-- 分页 -->
            <n-card>
              <div class="pagination">
                <n-pagination
                  v-model:page="currentPage"
                  v-model:page-size="pageSize"
                  :page-count="Math.ceil(total / pageSize)"
                  :page-sizes="[10, 20, 50]"
                  show-size-picker
                  @update:page="loadArticles"
                  @update:page-size="loadArticles"
                />
              </div>
            </n-card>
          </n-space>

          <n-empty v-else-if="!loading" description="暂无文章" />
        </n-spin>
      </n-grid-item>

      <!-- 侧边栏 -->
      <n-grid-item :span="24" :md="7">
        <Sidebar />
      </n-grid-item>
    </n-grid>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { articleApi } from '@stackblog/shared'
import type { ArticleResponseDTO } from '@stackblog/shared'
import ArticleCard from '../components/ArticleCard.vue'
import Sidebar from '../components/Sidebar.vue'

const route = useRoute()

// 文章列表
const articleList = ref<ArticleResponseDTO[]>([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const loading = ref(false)
const searchKeyword = ref('')

// 加载文章列表
const loadArticles = async () => {
  loading.value = true
  try {
    const params: any = {
      page: currentPage.value - 1,
      size: pageSize.value,
      status: 1 // 只加载已发布的文章
    }

    if (searchKeyword.value) {
      params.title = searchKeyword.value
    }

    const res = await articleApi.getList(params)
    articleList.value = res.content || []
    total.value = res.totalElements || 0
  } catch (error) {
    console.error('加载文章失败', error)
    articleList.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

// 监听路由变化（搜索）
watch(() => route.query.keyword, (keyword) => {
  searchKeyword.value = keyword as string || ''
  currentPage.value = 1
  loadArticles()
}, { immediate: true })

onMounted(() => {
  loadArticles()
})
</script>

<style scoped lang="scss">
.home-page {
  .pagination {
    display: flex;
    justify-content: center;
  }
}
</style>
