<template>
  <div class="article-list-container">
    <!-- 操作栏 -->
    <div class="toolbar">
      <div class="toolbar-left">
        <el-button type="primary" @click="router.push('/article/create')">
          <el-icon><Plus /></el-icon>
          新建文章
        </el-button>
        <el-button @click="handleRefresh">
          <el-icon><Refresh /></el-icon>
          刷新
        </el-button>
      </div>

      <div class="toolbar-right">
        <el-input
          v-model="searchForm.title"
          placeholder="搜索文章标题"
          clearable
          style="width: 250px"
          @keyup.enter="handleSearch"
        >
          <template #append>
            <el-button :icon="Search" @click="handleSearch" />
          </template>
        </el-input>
      </div>
    </div>

    <!-- 筛选栏 -->
    <el-card shadow="never" class="filter-card">
      <el-form :model="searchForm" inline>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="全部" clearable style="width: 120px">
            <el-option label="全部" :value="undefined" />
            <el-option label="草稿" :value="0" />
            <el-option label="已发布" :value="1" />
          </el-select>
        </el-form-item>

        <el-form-item label="推荐">
          <el-select v-model="searchForm.recommended" placeholder="全部" clearable style="width: 120px">
            <el-option label="全部" :value="undefined" />
            <el-option label="是" :value="true" />
            <el-option label="否" :value="false" />
          </el-select>
        </el-form-item>

        <el-form-item label="置顶">
          <el-select v-model="searchForm.top" placeholder="全部" clearable style="width: 120px">
            <el-option label="全部" :value="undefined" />
            <el-option label="是" :value="true" />
            <el-option label="否" :value="false" />
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 文章列表 -->
    <el-card shadow="never">
      <el-table
        v-loading="loading"
        :data="articleList"
        border
        stripe
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="title" label="标题" min-width="200" show-overflow-tooltip />
        <el-table-column prop="author" label="作者" width="120" />
        <el-table-column label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag v-if="row.status === 0" type="info">草稿</el-tag>
            <el-tag v-else-if="row.status === 1" type="success">已发布</el-tag>
            <el-tag v-else type="danger">已删除</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="标记" width="150" align="center">
          <template #default="{ row }">
            <el-tag v-if="row.top" type="danger" size="small">置顶</el-tag>
            <el-tag v-if="row.recommended" type="warning" size="small">推荐</el-tag>
            <el-tag v-if="row.original" type="success" size="small">原创</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="viewCount" label="浏览量" width="100" align="center" />
        <el-table-column prop="createTime" label="创建时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row.id)">编辑</el-button>
            <el-button link type="danger" @click="handleDelete(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
          v-model:current-page="searchForm.page"
          v-model:page-size="searchForm.size"
          :total="total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSearch"
          @current-change="handleSearch"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Refresh, Search } from '@element-plus/icons-vue'
import { articleApi } from '@shared/api'
import { formatDate } from '@shared/utils'
import type { ArticleResponseDTO } from '@shared/types'

const router = useRouter()

const loading = ref(false)
const articleList = ref<ArticleResponseDTO[]>([])
const total = ref(0)

const searchForm = reactive({
  title: '',
  status: undefined as number | undefined,
  recommended: undefined as boolean | undefined,
  top: undefined as boolean | undefined,
  page: 0,
  size: 10,
  sortBy: 'createTime',
  sortDirection: 'DESC' as const
})

// 获取文章列表
const fetchArticleList = async () => {
  loading.value = true
  try {
    const response = await articleApi.getList(searchForm)
    articleList.value = response.content
    total.value = response.totalElements
  } catch (error: any) {
    ElMessage.error(error.message || '获取文章列表失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  searchForm.page = 0
  fetchArticleList()
}

// 重置
const handleReset = () => {
  searchForm.title = ''
  searchForm.status = undefined
  searchForm.recommended = undefined
  searchForm.top = undefined
  handleSearch()
}

// 刷新
const handleRefresh = () => {
  fetchArticleList()
}

// 编辑
const handleEdit = (id: number) => {
  router.push(`/article/edit/${id}`)
}

// 删除
const handleDelete = (id: number) => {
  ElMessageBox.confirm('确定要删除这篇文章吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await articleApi.delete(id)
      ElMessage.success('删除成功')
      fetchArticleList()
    } catch (error: any) {
      ElMessage.error(error.message || '删除失败')
    }
  }).catch(() => {})
}

onMounted(() => {
  fetchArticleList()
})
</script>

<style scoped lang="scss">
.article-list-container {
  .toolbar {
    display: flex;
    justify-content: space-between;
    margin-bottom: 16px;

    .toolbar-left {
      display: flex;
      gap: 10px;
    }
  }

  .filter-card {
    margin-bottom: 16px;
  }

  .pagination {
    margin-top: 16px;
    display: flex;
    justify-content: flex-end;
  }
}
</style>
