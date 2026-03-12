<template>
  <n-layout-footer class="blog-footer" bordered>
    <div class="footer-container">
      <div class="footer-content">
        <div class="footer-info">
          <p>© 2026 StackBlog. 基于 Vue 3 + Kotlin 构建的现代化博客系统</p>
          <p class="footer-tech">
            <span>技术栈：Vue 3 · TypeScript · Naive UI · Kotlin · Spring Boot</span>
          </p>
        </div>
        <div class="footer-links">
          <n-button text tag="a" href="https://github.com" target="_blank" rel="noopener">
            GitHub
          </n-button>
          <span class="separator">|</span>
          <n-button text @click="router.push('/about')">关于</n-button>
          <span class="separator">|</span>
          <n-button text tag="a" href="http://localhost:3001" target="_blank">后台管理</n-button>
        </div>
      </div>
    </div>
  </n-layout-footer>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'

const router = useRouter()
</script>

<style scoped lang="scss">
.blog-footer {
  padding: 40px 0 30px;
  margin-top: 60px;

  .footer-container {
    max-width: 1200px;
    margin: 0 auto;
    padding: 0 20px;
  }

  .footer-content {
    text-align: center;
  }

  .footer-info {
    margin-bottom: 20px;

    p {
      color: #606266;
      font-size: 14px;
      line-height: 1.8;
      margin: 4px 0;
    }

    .footer-tech {
      color: #909399;
      font-size: 13px;
    }
  }

  .footer-links {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 12px;
    font-size: 14px;

    .separator {
      color: #dcdfe6;
    }
  }
}
</style>
