/**
 * Mock数据工具（用于前端独立开发和演示）
 */

import type { LoginResponse, UserResponseDTO, ArticleResponseDTO, PageResponse, CommentResponseDTO, ArticleType, Tag } from '../types'

// Mock延迟
const delay = (ms: number = 300) => new Promise(resolve => setTimeout(resolve, ms))

// Mock用户数据
export const mockUser: UserResponseDTO = {
  id: 1,
  username: 'admin',
  nickname: '管理员',
  email: 'admin@stackblog.com',
  avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
  createTime: '2026-01-01 10:00:00',
  roles: ['ROLE_ADMIN', 'ROLE_USER']
}

// Mock文章数据
export const mockArticles: ArticleResponseDTO[] = [
  {
    id: 1,
    userId: 1,
    title: '欢迎使用StackBlog博客系统',
    coverImage: '',
    typeId: 1,
    top: true,
    recommended: true,
    original: true,
    comment: true,
    viewCount: 1234,
    status: 1,
    description: '这是一篇欢迎文章，介绍了StackBlog的主要特性和功能。',
    keywords: 'StackBlog,博客,Vue,Kotlin',
    createTime: '2026-03-01 10:00:00',
    updateTime: '2026-03-10 15:30:00',
    author: '管理员',
    typeName: '技术',
    commentCount: 25,
    tags: [
      { id: 1, name: 'Vue', createTime: '2026-01-01 10:00:00' },
      { id: 2, name: 'Kotlin', createTime: '2026-01-01 10:00:00' }
    ]
  },
  {
    id: 2,
    userId: 1,
    title: 'Vue 3 Composition API 最佳实践',
    coverImage: '',
    typeId: 1,
    top: false,
    recommended: true,
    original: true,
    comment: true,
    viewCount: 856,
    status: 1,
    description: '深入探讨Vue 3 Composition API的使用技巧和最佳实践。',
    keywords: 'Vue3,Composition API',
    createTime: '2026-03-05 14:20:00',
    updateTime: '2026-03-08 09:15:00',
    author: '管理员',
    typeName: '技术',
    commentCount: 18,
    tags: [
      { id: 1, name: 'Vue', createTime: '2026-01-01 10:00:00' }
    ]
  },
  {
    id: 3,
    userId: 1,
    title: 'Kotlin协程入门指南',
    coverImage: '',
    typeId: 1,
    top: false,
    recommended: false,
    original: true,
    comment: true,
    viewCount: 567,
    status: 1,
    description: 'Kotlin协程的基本概念和使用方法介绍。',
    keywords: 'Kotlin,协程,异步编程',
    createTime: '2026-03-08 16:45:00',
    author: '管理员',
    typeName: '技术',
    commentCount: 12,
    tags: [
      { id: 2, name: 'Kotlin', createTime: '2026-01-01 10:00:00' }
    ]
  },
  {
    id: 4,
    userId: 1,
    title: 'Spring Boot微服务架构设计',
    coverImage: '',
    typeId: 1,
    top: false,
    recommended: true,
    original: true,
    comment: true,
    viewCount: 923,
    status: 1,
    description: '介绍Spring Boot微服务架构的设计原则和实践经验。',
    keywords: 'Spring Boot,微服务',
    createTime: '2026-03-10 11:30:00',
    author: '管理员',
    typeName: '技术',
    commentCount: 30,
    tags: [
      { id: 3, name: 'Spring Boot', createTime: '2026-01-01 10:00:00' }
    ]
  },
  {
    id: 5,
    userId: 1,
    title: 'TypeScript高级类型系统',
    coverImage: '',
    typeId: 1,
    top: false,
    recommended: false,
    original: true,
    comment: true,
    viewCount: 445,
    status: 0,
    description: '探索TypeScript的高级类型特性。',
    keywords: 'TypeScript,类型系统',
    createTime: '2026-03-11 09:20:00',
    author: '管理员',
    typeName: '技术',
    commentCount: 5,
    tags: [
      { id: 4, name: 'TypeScript', createTime: '2026-01-01 10:00:00' }
    ]
  }
]

// Mock评论数据
export const mockComments: CommentResponseDTO[] = [
  {
    id: 1,
    articleId: 1,
    userId: 2,
    nickname: '张三',
    email: 'zhangsan@example.com',
    content: '写得非常好，对我帮助很大！',
    status: 'PENDING',
    support: 15,
    oppose: 0,
    createTime: '2026-03-12 09:30:00'
  },
  {
    id: 2,
    articleId: 1,
    nickname: '李四',
    email: 'lisi@example.com',
    content: '期待更多这样的教程',
    status: 'APPROVED',
    support: 8,
    oppose: 1,
    createTime: '2026-03-12 10:15:00'
  },
  {
    id: 3,
    articleId: 2,
    userId: 3,
    nickname: '王五',
    email: 'wangwu@example.com',
    content: 'Composition API确实很强大',
    status: 'APPROVED',
    support: 12,
    oppose: 0,
    createTime: '2026-03-12 11:00:00'
  },
  {
    id: 4,
    articleId: 2,
    nickname: '赵六',
    email: 'zhaoliu@example.com',
    content: '有没有完整的示例代码？',
    status: 'PENDING',
    support: 3,
    oppose: 0,
    createTime: '2026-03-12 14:20:00'
  },
  {
    id: 5,
    articleId: 1,
    nickname: '孙七',
    email: 'sunqi@example.com',
    content: '这是垃圾内容',
    status: 'REJECTED',
    support: 0,
    oppose: 10,
    createTime: '2026-03-12 15:45:00'
  }
]

// Mock分类数据
export const mockCategories: ArticleType[] = [
  {
    id: 1,
    name: '技术',
    description: '技术相关文章',
    sort: 1,
    available: true,
    createTime: '2026-01-01 10:00:00'
  },
  {
    id: 2,
    name: '生活',
    description: '生活随笔',
    sort: 2,
    available: true,
    createTime: '2026-01-01 10:00:00'
  },
  {
    id: 3,
    name: '读书',
    description: '读书笔记',
    sort: 3,
    available: false,
    createTime: '2026-01-01 10:00:00'
  }
]

// Mock标签数据
export const mockTags: Tag[] = [
  { id: 1, name: 'Vue', description: 'Vue.js框架', createTime: '2026-01-01 10:00:00' },
  { id: 2, name: 'Kotlin', description: 'Kotlin编程语言', createTime: '2026-01-01 10:00:00' },
  { id: 3, name: 'Spring Boot', description: 'Spring Boot框架', createTime: '2026-01-01 10:00:00' },
  { id: 4, name: 'TypeScript', description: 'TypeScript语言', createTime: '2026-01-01 10:00:00' },
  { id: 5, name: 'React', description: 'React框架', createTime: '2026-01-01 10:00:00' }
]

/**
 * Mock API方法
 */
export const mockApi = {
  // ============ 认证相关 ============
  async login(username: string, password: string): Promise<LoginResponse> {
    await delay()
    if (username === 'admin' && password === '123456') {
      return {
        token: 'mock-jwt-token-' + Date.now(),
        user: mockUser
      }
    }
    throw new Error('用户名或密码错误')
  },

  async getCurrentUser(): Promise<UserResponseDTO> {
    await delay()
    return mockUser
  },

  // ============ 文章相关 ============
  async getArticleList(params: any): Promise<PageResponse<ArticleResponseDTO>> {
    await delay()
    
    let filteredArticles = [...mockArticles]
    
    if (params.title) {
      filteredArticles = filteredArticles.filter(article => 
        article.title.includes(params.title)
      )
    }
    
    if (params.status !== undefined) {
      filteredArticles = filteredArticles.filter(article => 
        article.status === params.status
      )
    }
    
    if (params.recommended !== undefined) {
      filteredArticles = filteredArticles.filter(article => 
        article.recommended === params.recommended
      )
    }
    
    if (params.top !== undefined) {
      filteredArticles = filteredArticles.filter(article => 
        article.top === params.top
      )
    }
    
    const page = params.page || 0
    const size = params.size || 10
    const start = page * size
    const end = start + size
    
    return {
      content: filteredArticles.slice(start, end),
      totalElements: filteredArticles.length,
      totalPages: Math.ceil(filteredArticles.length / size),
      number: page,
      size: size,
      first: page === 0,
      last: end >= filteredArticles.length
    }
  },

  async getArticleDetail(id: number) {
    await delay()
    const article = mockArticles.find(a => a.id === id)
    if (!article) throw new Error('文章不存在')
    
    // 根据文章ID生成不同的示例内容
    const contentMap: Record<number, string> = {
      1: `
        <h2>欢迎来到StackBlog</h2>
        <p>StackBlog是一个基于现代化技术栈构建的博客系统，旨在为开发者提供一个优雅、高效的内容发布平台。</p>
        
        <h3>主要特性</h3>
        <ul>
          <li>📝 支持Markdown编辑，让写作更专注</li>
          <li>🎨 响应式设计，完美适配各种设备</li>
          <li>🔍 强大的搜索和筛选功能</li>
          <li>💬 完善的评论系统</li>
          <li>🏷️ 灵活的分类和标签管理</li>
          <li>🚀 极速的页面加载体验</li>
        </ul>
        
        <h3>技术栈</h3>
        <p>前端采用<strong>Vue 3 + TypeScript</strong>构建，后端使用<strong>Kotlin + Spring Boot</strong>，数据库选用<strong>MySQL 8</strong>。</p>
        
        <pre><code class="language-typescript">// 示例代码
import { defineComponent } from 'vue'

export default defineComponent({
  name: 'StackBlog',
  setup() {
    return {
      message: 'Hello, StackBlog!'
    }
  }
})</code></pre>
        
        <h3>开始使用</h3>
        <p>访问我们的GitHub仓库获取完整源码，欢迎Star和Fork！</p>
      `,
      2: `
        <h2>Vue 3 Composition API</h2>
        <p>Vue 3 带来了全新的Composition API，它提供了一种更灵活、更符合逻辑的方式来组织组件代码。</p>
        
        <h3>为什么选择Composition API？</h3>
        <ol>
          <li><strong>更好的代码组织</strong>：相关逻辑可以聚合在一起</li>
          <li><strong>更好的类型推导</strong>：TypeScript支持更完善</li>
          <li><strong>更灵活的代码复用</strong>：通过组合函数实现</li>
        </ol>
        
        <h3>基本用法</h3>
        <pre><code class="language-typescript">import { ref, computed, onMounted } from 'vue'

export default {
  setup() {
    const count = ref(0)
    const doubleCount = computed(() => count.value * 2)
    
    const increment = () => {
      count.value++
    }
    
    onMounted(() => {
      console.log('Component mounted!')
    })
    
    return {
      count,
      doubleCount,
      increment
    }
  }
}</code></pre>
        
        <h3>最佳实践</h3>
        <p>推荐使用<code>&lt;script setup&gt;</code>语法糖，它提供了更简洁的写法：</p>
        <pre><code class="language-vue">&lt;script setup lang="ts"&gt;
import { ref } from 'vue'

const count = ref(0)
const increment = () => count.value++
&lt;/script&gt;</code></pre>
      `,
      3: `
        <h2>Kotlin协程入门</h2>
        <p>Kotlin协程是一种轻量级的并发解决方案，可以用更简洁的方式处理异步任务。</p>
        
        <h3>什么是协程？</h3>
        <p>协程是一种可以暂停和恢复执行的函数，它允许我们以同步的方式编写异步代码。</p>
        
        <h3>基本示例</h3>
        <pre><code class="language-kotlin">import kotlinx.coroutines.*

fun main() = runBlocking {
    launch {
        delay(1000L)
        println("World!")
    }
    println("Hello")
}</code></pre>
        
        <h3>协程的优势</h3>
        <ul>
          <li>轻量级：可以创建数十万个协程</li>
          <li>内存占用小：比线程更高效</li>
          <li>结构化并发：自动管理协程生命周期</li>
          <li>取消机制：支持协程取消和超时</li>
        </ul>
        
        <blockquote>
          <p>协程让异步编程变得简单而优雅。</p>
        </blockquote>
      `,
      4: `
        <h2>Spring Boot微服务架构</h2>
        <p>微服务架构是当前主流的软件架构模式，Spring Boot为构建微服务提供了强大的支持。</p>
        
        <h3>微服务的核心原则</h3>
        <ol>
          <li><strong>单一职责</strong>：每个服务只负责一个业务功能</li>
          <li><strong>独立部署</strong>：服务可以独立发布和扩展</li>
          <li><strong>去中心化</strong>：数据管理去中心化</li>
          <li><strong>技术多样性</strong>：可以使用不同的技术栈</li>
        </ol>
        
        <h3>Spring Boot微服务技术栈</h3>
        <ul>
          <li><strong>Spring Cloud</strong>：微服务治理</li>
          <li><strong>Eureka</strong>：服务注册与发现</li>
          <li><strong>Ribbon</strong>：客户端负载均衡</li>
          <li><strong>Feign</strong>：声明式服务调用</li>
          <li><strong>Hystrix</strong>：熔断器</li>
          <li><strong>Gateway</strong>：API网关</li>
        </ul>
        
        <h3>示例代码</h3>
        <pre><code class="language-kotlin">@SpringBootApplication
@EnableDiscoveryClient
class UserServiceApplication

fun main(args: Array&lt;String&gt;) {
    runApplication&lt;UserServiceApplication&gt;(*args)
}</code></pre>
      `,
      5: `
        <h2>TypeScript高级类型系统</h2>
        <p>TypeScript的类型系统非常强大，掌握高级类型可以让代码更加健壮和优雅。</p>
        
        <h3>联合类型和交叉类型</h3>
        <pre><code class="language-typescript">// 联合类型
type Status = 'success' | 'error' | 'pending'

// 交叉类型
type User = { name: string } & { age: number }</code></pre>
        
        <h3>条件类型</h3>
        <pre><code class="language-typescript">type IsString&lt;T&gt; = T extends string ? true : false

type A = IsString&lt;string&gt;  // true
type B = IsString&lt;number&gt;  // false</code></pre>
        
        <h3>映射类型</h3>
        <pre><code class="language-typescript">type Readonly&lt;T&gt; = {
  readonly [P in keyof T]: T[P]
}

type Optional&lt;T&gt; = {
  [P in keyof T]?: T[P]
}</code></pre>
        
        <p>这只是TypeScript类型系统的冰山一角，还有更多强大的特性等待探索！</p>
      `
    }
    
    const content = contentMap[id] || `
      <h2>${article.title}</h2>
      <p>${article.description}</p>
      <p>这是一篇关于${article.typeName}的文章，作者是${article.author}。</p>
    `
    
    return {
      ...article,
      content,
      contentMd: content.replace(/<[^>]+>/g, '') // 简单转换
    }
  },

  async createArticle(data: any): Promise<ArticleResponseDTO> {
    await delay(500)
    
    const newArticle: ArticleResponseDTO = {
      id: mockArticles.length + 1,
      userId: 1,
      title: data.title,
      coverImage: data.coverImage || '',
      typeId: data.typeId,
      top: data.top || false,
      recommended: data.recommended || false,
      original: data.original || true,
      comment: data.comment !== false,
      viewCount: 0,
      status: data.status || 0,
      description: data.description || '',
      keywords: '',
      createTime: new Date().toLocaleString('zh-CN'),
      author: '管理员',
      typeName: '技术',
      commentCount: 0,
      tags: []
    }
    
    mockArticles.unshift(newArticle)
    return newArticle
  },

  async updateArticle(id: number, data: any): Promise<ArticleResponseDTO> {
    await delay(500)
    
    const index = mockArticles.findIndex(a => a.id === id)
    if (index === -1) throw new Error('文章不存在')
    
    mockArticles[index] = {
      ...mockArticles[index],
      ...data,
      updateTime: new Date().toLocaleString('zh-CN')
    }
    
    return mockArticles[index]
  },

  async deleteArticle(id: number): Promise<void> {
    await delay(300)
    
    const index = mockArticles.findIndex(a => a.id === id)
    if (index === -1) throw new Error('文章不存在')
    
    mockArticles.splice(index, 1)
  },

  // ============ 评论相关 ============
  async getCommentList(params: any): Promise<PageResponse<CommentResponseDTO>> {
    await delay()
    
    let filteredComments = [...mockComments]
    
    if (params.status) {
      filteredComments = filteredComments.filter(comment => 
        comment.status === params.status
      )
    }
    
    if (params.articleId) {
      filteredComments = filteredComments.filter(comment => 
        comment.articleId === params.articleId
      )
    }
    
    const page = params.page || 0
    const size = params.size || 10
    const start = page * size
    const end = start + size
    
    return {
      content: filteredComments.slice(start, end),
      totalElements: filteredComments.length,
      totalPages: Math.ceil(filteredComments.length / size),
      number: page,
      size: size,
      first: page === 0,
      last: end >= filteredComments.length
    }
  },

  async approveComment(id: number): Promise<CommentResponseDTO> {
    await delay()
    
    const comment = mockComments.find(c => c.id === id)
    if (!comment) throw new Error('评论不存在')
    
    comment.status = 'APPROVED'
    return comment
  },

  async rejectComment(id: number): Promise<CommentResponseDTO> {
    await delay()
    
    const comment = mockComments.find(c => c.id === id)
    if (!comment) throw new Error('评论不存在')
    
    comment.status = 'REJECTED'
    return comment
  },

  async deleteComment(id: number): Promise<void> {
    await delay(300)
    
    const index = mockComments.findIndex(c => c.id === id)
    if (index === -1) throw new Error('评论不存在')
    
    mockComments.splice(index, 1)
  },

  // ============ 分类相关 ============
  async getCategoryList(): Promise<ArticleType[]> {
    await delay()
    return [...mockCategories]
  },

  async getCategoryById(id: number): Promise<ArticleType> {
    await delay()
    const category = mockCategories.find(c => c.id === id)
    if (!category) throw new Error('分类不存在')
    return { ...category }
  },

  async createCategory(data: any): Promise<ArticleType> {
    await delay()
    
    const newCategory: ArticleType = {
      id: mockCategories.length + 1,
      name: data.name,
      description: data.description || '',
      sort: data.sort || mockCategories.length + 1,
      available: data.available !== false,
      createTime: new Date().toLocaleString('zh-CN')
    }
    
    mockCategories.push(newCategory)
    return newCategory
  },

  async updateCategory(id: number, data: any): Promise<ArticleType> {
    await delay()
    
    const index = mockCategories.findIndex(c => c.id === id)
    if (index === -1) throw new Error('分类不存在')
    
    mockCategories[index] = {
      ...mockCategories[index],
      ...data
    }
    
    return mockCategories[index]
  },

  async deleteCategory(id: number): Promise<void> {
    await delay()
    
    const index = mockCategories.findIndex(c => c.id === id)
    if (index === -1) throw new Error('分类不存在')
    
    mockCategories.splice(index, 1)
  },

  // ============ 标签相关 ============
  async getTagList(): Promise<Tag[]> {
    await delay()
    return [...mockTags]
  },

  async getTagById(id: number): Promise<Tag> {
    await delay()
    const tag = mockTags.find(t => t.id === id)
    if (!tag) throw new Error('标签不存在')
    return { ...tag }
  },

  async createTag(data: any): Promise<Tag> {
    await delay()
    
    const newTag: Tag = {
      id: mockTags.length + 1,
      name: data.name,
      description: data.description || '',
      createTime: new Date().toLocaleString('zh-CN')
    }
    
    mockTags.push(newTag)
    return newTag
  },

  async updateTag(id: number, data: any): Promise<Tag> {
    await delay()
    
    const index = mockTags.findIndex(t => t.id === id)
    if (index === -1) throw new Error('标签不存在')
    
    mockTags[index] = {
      ...mockTags[index],
      ...data
    }
    
    return mockTags[index]
  },

  async deleteTag(id: number): Promise<void> {
    await delay()
    
    const index = mockTags.findIndex(t => t.id === id)
    if (index === -1) throw new Error('标签不存在')
    
    mockTags.splice(index, 1)
  }
}
