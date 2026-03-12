<template>
  <div class="dashboard-container">
    <h2 class="page-title">数据概览</h2>
    
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stat-cards">
      <el-col :xs="12" :sm="12" :md="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: #ecf5ff;">
              <el-icon :size="32" color="#409eff"><Document /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.articleCount }}</div>
              <div class="stat-label">文章总数</div>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :xs="12" :sm="12" :md="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: #f0f9ff;">
              <el-icon :size="32" color="#67c23a"><ChatDotRound /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.commentCount }}</div>
              <div class="stat-label">评论总数</div>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :xs="12" :sm="12" :md="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: #fef0f0;">
              <el-icon :size="32" color="#f56c6c"><User /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.userCount }}</div>
              <div class="stat-label">用户总数</div>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :xs="12" :sm="12" :md="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: #fdf6ec;">
              <el-icon :size="32" color="#e6a23c"><View /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ formatNumber(stats.viewCount) }}</div>
              <div class="stat-label">浏览总量</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20" class="chart-section">
      <el-col :xs="24" :sm="24" :md="16">
        <el-card shadow="hover">
          <template #header>
            <span>文章发布趋势</span>
          </template>
          <div class="chart-placeholder">
            <el-empty description="图表功能开发中..." />
          </div>
        </el-card>
      </el-col>

      <el-col :xs="24" :sm="24" :md="8">
        <el-card shadow="hover">
          <template #header>
            <span>分类统计</span>
          </template>
          <div class="chart-placeholder">
            <el-empty description="图表功能开发中..." />
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 快捷操作 -->
    <el-row :gutter="20">
      <el-col :xs="24" :sm="24" :md="12">
        <el-card shadow="hover">
          <template #header>
            <span>快捷操作</span>
          </template>
          <el-space wrap>
            <el-button type="primary" @click="router.push('/article/create')">
              <el-icon><Edit /></el-icon>
              写文章
            </el-button>
            <el-button @click="router.push('/comment')">
              <el-icon><ChatDotRound /></el-icon>
              评论管理
            </el-button>
            <el-button @click="router.push('/category')">
              <el-icon><Folder /></el-icon>
              分类管理
            </el-button>
            <el-button @click="router.push('/tag')">
              <el-icon><PriceTag /></el-icon>
              标签管理
            </el-button>
          </el-space>
        </el-card>
      </el-col>

      <el-col :xs="24" :sm="24" :md="12">
        <el-card shadow="hover">
          <template #header>
            <span>系统信息</span>
          </template>
          <el-descriptions :column="1" border>
            <el-descriptions-item label="系统版本">v1.0.0</el-descriptions-item>
            <el-descriptions-item label="技术栈">Vue 3 + TypeScript + Element Plus</el-descriptions-item>
            <el-descriptions-item label="后端框架">Kotlin + Spring Boot</el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Document, ChatDotRound, User, View, Edit, Folder, PriceTag } from '@element-plus/icons-vue'
import { articleApi, commentApi, categoryApi, tagApi } from '@stackblog/shared'

const router = useRouter()

// 统计数据
const stats = ref({
  articleCount: 0,
  commentCount: 0,
  userCount: 1,
  viewCount: 0
})

// 加载统计数据
const loadStats = async () => {
  try {
    // 获取文章统计
    const articleRes = await articleApi.getList({ page: 0, size: 1 })
    stats.value.articleCount = articleRes.totalElements
    
    // 计算总浏览量
    const allArticles = await articleApi.getList({ page: 0, size: 100 })
    stats.value.viewCount = allArticles.content.reduce((sum, article) => sum + article.viewCount, 0)
    
    // 获取评论统计
    const commentRes = await commentApi.getList({ page: 0, size: 1 })
    stats.value.commentCount = commentRes.totalElements
  } catch (error) {
    console.error('加载统计数据失败', error)
  }
}

// 格式化数字
const formatNumber = (num: number) => {
  if (num >= 10000) {
    return (num / 1000).toFixed(1) + 'K'
  }
  return num.toString()
}

onMounted(() => {
  loadStats()
})
</script>

<style scoped lang="scss">
.dashboard-container {
  .page-title {
    margin-bottom: 20px;
    font-size: 20px;
    font-weight: 500;
    color: #303133;
  }
}

.stat-cards {
  margin-bottom: 20px;
}

.stat-card {
  .stat-content {
    display: flex;
    align-items: center;
    gap: 16px;

    .stat-icon {
      width: 60px;
      height: 60px;
      display: flex;
      align-items: center;
      justify-content: center;
      border-radius: 8px;
    }

    .stat-info {
      flex: 1;

      .stat-value {
        font-size: 24px;
        font-weight: bold;
        color: #303133;
        margin-bottom: 4px;
      }

      .stat-label {
        font-size: 14px;
        color: #909399;
      }
    }
  }
}

.chart-section {
  margin-bottom: 20px;

  .chart-placeholder {
    height: 300px;
    display: flex;
    align-items: center;
    justify-content: center;
  }
}
</style>
