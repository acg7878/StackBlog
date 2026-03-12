<template>
  <div class="about-page">
    <n-card title="关于本站" hoverable>
      <n-space vertical :size="24">
        <section>
          <h3>👋 欢迎</h3>
          <n-text>欢迎来到 StackBlog，这是一个基于现代化技术栈构建的博客系统。</n-text>
        </section>

        <n-divider />

        <section>
          <h3>🛠️ 技术栈</h3>
          <n-grid :x-gap="20" :cols="1" :md="2">
            <n-grid-item>
              <n-card title="前端" size="small">
                <n-ul>
                  <li>Vue 3 - 渐进式 JavaScript 框架</li>
                  <li>TypeScript - 类型安全</li>
                  <li>Vite 8.0 - 下一代前端构建工具</li>
                  <li>Naive UI - Vue 3 组件库</li>
                  <li>Pinia - Vue 状态管理</li>
                </n-ul>
              </n-card>
            </n-grid-item>
            <n-grid-item>
              <n-card title="后端" size="small">
                <n-ul>
                  <li>Kotlin - 现代化 JVM 语言</li>
                  <li>Spring Boot 4.0 - 企业级框架</li>
                  <li>Spring Security + JWT - 安全认证</li>
                  <li>MySQL 8 - 关系型数据库</li>
                  <li>Redis - 缓存中间件</li>
                </n-ul>
              </n-card>
            </n-grid-item>
          </n-grid>
        </section>

        <n-divider />

        <section>
          <h3>✨ 特性</h3>
          <n-ul>
            <li>📝 Markdown 编辑器支持</li>
            <li>🔍 文章搜索和筛选</li>
            <li>🏷️ 分类和标签管理</li>
            <li>💬 评论系统</li>
            <li>📊 数据统计</li>
            <li>🎨 响应式设计</li>
            <li>🔐 安全认证</li>
          </n-ul>
        </section>

        <n-divider />

        <section>
          <h3>📬 联系方式</h3>
          <n-card size="small">
            <n-p><n-text strong>GitHub:</n-text> <n-button text tag="a" href="https://github.com" target="_blank">github.com/stackblog</n-button></n-p>
            <n-p><n-text strong>Email:</n-text> admin@stackblog.com</n-p>
          </n-card>
        </section>

        <n-divider />

        <section>
          <h3>📄 版本信息</h3>
          <n-descriptions :column="1" bordered>
            <n-descriptions-item label="版本">v1.0.0</n-descriptions-item>
            <n-descriptions-item label="发布日期">2026-03-12</n-descriptions-item>
            <n-descriptions-item label="许可证">MIT License</n-descriptions-item>
          </n-descriptions>
        </section>
      </n-space>
    </n-card>
  </div>
</template>

<style scoped lang="scss">
.about-page {
  h3 {
    font-size: 18px;
    font-weight: 600;
    margin: 0 0 12px;
  }
}
</style>
