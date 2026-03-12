<template>
  <div class="comment-list">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>评论管理</span>
        </div>
      </template>

      <!-- 搜索栏 -->
      <el-form :inline="true" :model="queryForm">
        <el-form-item label="审核状态">
          <el-select v-model="queryForm.status" placeholder="全部" clearable @change="handleQuery">
            <el-option label="待审核" value="PENDING" />
            <el-option label="已通过" value="APPROVED" />
            <el-option label="已拒绝" value="REJECTED" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 评论列表 -->
      <el-table :data="commentList" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="nickname" label="评论者" width="120" />
        <el-table-column prop="content" label="评论内容" min-width="300" show-overflow-tooltip />
        <el-table-column label="所属文章" width="200">
          <template #default="{ row }">
            <span>{{ getArticleTitle(row.articleId) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.status === 'PENDING'" type="warning">待审核</el-tag>
            <el-tag v-else-if="row.status === 'APPROVED'" type="success">已通过</el-tag>
            <el-tag v-else-if="row.status === 'REJECTED'" type="danger">已拒绝</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="点赞/反对" width="100">
          <template #default="{ row }">
            <span style="color: #67c23a">{{ row.support }}</span> / 
            <span style="color: #f56c6c">{{ row.oppose }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="评论时间" width="160" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button 
              v-if="row.status === 'PENDING'" 
              type="success" 
              size="small" 
              @click="handleApprove(row)"
            >
              通过
            </el-button>
            <el-button 
              v-if="row.status === 'PENDING'" 
              type="warning" 
              size="small" 
              @click="handleReject(row)"
            >
              拒绝
            </el-button>
            <el-button 
              type="danger" 
              size="small" 
              @click="handleDelete(row)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
          v-model:current-page="queryForm.page"
          v-model:page-size="queryForm.size"
          :total="total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @current-change="handleQuery"
          @size-change="handleQuery"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { commentApi, articleApi } from '@stackblog/shared'
import type { CommentResponseDTO, ArticleResponseDTO } from '@stackblog/shared'

// 查询表单
const queryForm = ref({
  page: 1,
  size: 10,
  status: ''
})

// 数据
const commentList = ref<CommentResponseDTO[]>([])
const articleMap = ref<Map<number, string>>(new Map())
const total = ref(0)
const loading = ref(false)

// 获取文章标题
const getArticleTitle = (articleId: number) => {
  return articleMap.value.get(articleId) || '未知文章'
}

// 查询评论
const handleQuery = async () => {
  loading.value = true
  try {
    const params: any = {
      page: queryForm.value.page - 1,
      size: queryForm.value.size
    }
    if (queryForm.value.status) {
      params.status = queryForm.value.status
    }

    const res = await commentApi.getList(params)
    commentList.value = res.content
    total.value = res.totalElements
  } catch (error) {
    ElMessage.error('获取评论列表失败')
  } finally {
    loading.value = false
  }
}

// 重置查询
const handleReset = () => {
  queryForm.value = {
    page: 1,
    size: 10,
    status: ''
  }
  handleQuery()
}

// 审核通过
const handleApprove = async (row: CommentResponseDTO) => {
  try {
    await ElMessageBox.confirm('确认通过该评论？', '提示', {
      type: 'success'
    })
    await commentApi.approve(row.id)
    ElMessage.success('审核通过')
    handleQuery()
  } catch (error: any) {
    if (error !== 'cancel') {
      ElMessage.error('操作失败')
    }
  }
}

// 拒绝评论
const handleReject = async (row: CommentResponseDTO) => {
  try {
    await ElMessageBox.confirm('确认拒绝该评论？', '提示', {
      type: 'warning'
    })
    await commentApi.reject(row.id)
    ElMessage.success('已拒绝')
    handleQuery()
  } catch (error: any) {
    if (error !== 'cancel') {
      ElMessage.error('操作失败')
    }
  }
}

// 删除评论
const handleDelete = async (row: CommentResponseDTO) => {
  try {
    await ElMessageBox.confirm('确认删除该评论？删除后无法恢复！', '警告', {
      type: 'error'
    })
    await commentApi.delete(row.id)
    ElMessage.success('删除成功')
    handleQuery()
  } catch (error: any) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

// 加载文章标题映射
const loadArticles = async () => {
  try {
    const res = await articleApi.getList({ page: 0, size: 100 })
    res.content.forEach((article: ArticleResponseDTO) => {
      articleMap.value.set(article.id, article.title)
    })
  } catch (error) {
    console.error('加载文章列表失败', error)
  }
}

onMounted(() => {
  loadArticles()
  handleQuery()
})
</script>

<style scoped lang="scss">
.comment-list {
  padding: 20px;

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .pagination {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
  }
}
</style>
