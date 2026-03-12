package com.derek.stackblog.service

import com.derek.stackblog.TestFixtures
import com.derek.stackblog.domain.dto.ArticleDTO
import com.derek.stackblog.repository.*
import io.mockk.*
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.DisplayName
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.util.*

@DisplayName("ArticleService 单元测试")
class ArticleServiceTest {
    
    private lateinit var articleRepository: ArticleRepository
    private lateinit var articleContentRepository: ArticleContentRepository
    private lateinit var articleTypeRepository: ArticleTypeRepository
    private lateinit var tagRepository: TagRepository
    private lateinit var userRepository: UserRepository
    private lateinit var articleService: ArticleService
    
    @BeforeEach
    fun setUp() {
        articleRepository = mockk()
        articleContentRepository = mockk()
        articleTypeRepository = mockk()
        tagRepository = mockk()
        userRepository = mockk()
        articleService = ArticleService(
            articleRepository,
            articleContentRepository,
            articleTypeRepository,
            tagRepository,
            userRepository
        )
    }
    
    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }
    
    // ==================== 创建文章测试 ====================
    
    @Test
    fun `should create article successfully`() {
        // Given
        val userId = 1L
        val dto = TestFixtures.createArticleDTO(title = "测试文章标题")
        val article = TestFixtures.createTestArticle(userId = userId, title = dto.title)
        val savedArticle = article.copy().apply { id = 1L }
        val content = TestFixtures.createTestArticleContent(articleId = 1L)
        val user = TestFixtures.createTestUser(id = userId)
        
        every { articleRepository.save(any()) } returns savedArticle
        every { articleContentRepository.save(any()) } returns content
        every { userRepository.findById(userId) } returns Optional.of(user)
        
        // When
        val result = articleService.createArticle(dto, userId)
        
        // Then
        assertThat(result.id).isEqualTo(1L)
        assertThat(result.title).isEqualTo(dto.title)
        assertThat(result.userId).isEqualTo(userId)
        assertThat(result.author).isEqualTo(user.nickname)
        
        verify(exactly = 1) { articleRepository.save(any()) }
        verify(exactly = 1) { articleContentRepository.save(any()) }
    }
    
    @Test
    fun `should create article with tags when tags provided`() {
        // Given
        val userId = 1L
        val tagIds = listOf(1L, 2L)
        val dto = TestFixtures.createArticleDTO(tags = tagIds)
        val article = TestFixtures.createTestArticle(userId = userId)
        val savedArticle = article.copy().apply { id = 1L }
        val tags = listOf(
            TestFixtures.createTestTag(id = 1L, name = "Kotlin"),
            TestFixtures.createTestTag(id = 2L, name = "Spring")
        )
        val user = TestFixtures.createTestUser(id = userId)
        
        every { articleRepository.save(any()) } returnsMany listOf(savedArticle, savedArticle)
        every { articleContentRepository.save(any()) } returns mockk()
        every { tagRepository.findAllById(tagIds) } returns tags
        every { userRepository.findById(userId) } returns Optional.of(user)
        
        // When
        val result = articleService.createArticle(dto, userId)
        
        // Then
        assertThat(result.tags).hasSize(2)
        assertThat(result.tags.map { it.name }).containsExactlyInAnyOrder("Kotlin", "Spring")
        
        verify(exactly = 2) { articleRepository.save(any()) }
        verify(exactly = 1) { tagRepository.findAllById(tagIds) }
    }
    
    // ==================== 更新文章测试 ====================
    
    @Test
    fun `should update article successfully when user is owner`() {
        // Given
        val articleId = 1L
        val userId = 1L
        val article = TestFixtures.createTestArticle(id = articleId, userId = userId)
        val content = TestFixtures.createTestArticleContent(articleId = articleId)
        val dto = TestFixtures.createArticleDTO(title = "更新后的标题")
        val user = TestFixtures.createTestUser(id = userId)
        
        every { articleRepository.findById(articleId) } returns Optional.of(article)
        every { articleContentRepository.findByArticleId(articleId) } returns Optional.of(content)
        every { articleContentRepository.save(any()) } returns content
        every { articleRepository.save(any()) } returns article
        every { userRepository.findById(userId) } returns Optional.of(user)
        
        // When
        val result = articleService.updateArticle(articleId, dto, userId)
        
        // Then
        assertThat(result.title).isEqualTo("更新后的标题")
        
        verify(exactly = 1) { articleRepository.save(any()) }
        verify(exactly = 1) { articleContentRepository.save(any()) }
    }
    
    @Test
    fun `should throw exception when user is not owner`() {
        // Given
        val articleId = 1L
        val ownerId = 1L
        val otherUserId = 2L
        val article = TestFixtures.createTestArticle(id = articleId, userId = ownerId)
        val dto = TestFixtures.createArticleDTO()
        
        every { articleRepository.findById(articleId) } returns Optional.of(article)
        
        // When & Then
        assertThatThrownBy { articleService.updateArticle(articleId, dto, otherUserId) }
            .isInstanceOf(IllegalAccessException::class.java)
            .hasMessageContaining("无权限修改此文章")
        
        verify(exactly = 0) { articleRepository.save(any()) }
    }
    
    @Test
    fun `should throw exception when article not found`() {
        // Given
        val articleId = 999L
        val userId = 1L
        val dto = TestFixtures.createArticleDTO()
        
        every { articleRepository.findById(articleId) } returns Optional.empty()
        
        // When & Then
        assertThatThrownBy { articleService.updateArticle(articleId, dto, userId) }
            .isInstanceOf(NoSuchElementException::class.java)
            .hasMessageContaining("文章不存在")
        
        verify(exactly = 1) { articleRepository.findById(articleId) }
    }
    
    // ==================== 删除文章测试 ====================
    
    @Test
    fun `should delete article successfully when user is owner`() {
        // Given
        val articleId = 1L
        val userId = 1L
        val article = TestFixtures.createTestArticle(id = articleId, userId = userId)
        
        every { articleRepository.findById(articleId) } returns Optional.of(article)
        every { articleRepository.deleteById(articleId) } just Runs
        every { articleContentRepository.deleteByArticleId(articleId) } just Runs
        
        // When
        articleService.deleteArticle(articleId, userId)
        
        // Then
        verify(exactly = 1) { articleRepository.deleteById(articleId) }
    }
    
    @Test
    fun `should throw exception when deleting article user is not owner`() {
        // Given
        val articleId = 1L
        val ownerId = 1L
        val otherUserId = 2L
        val article = TestFixtures.createTestArticle(id = articleId, userId = ownerId)
        
        every { articleRepository.findById(articleId) } returns Optional.of(article)
        
        // When & Then
        assertThatThrownBy { articleService.deleteArticle(articleId, otherUserId) }
            .isInstanceOf(IllegalAccessException::class.java)
            .hasMessageContaining("无权限删除此文章")
        
        verify(exactly = 0) { articleRepository.deleteById(any()) }
    }
    
    // ==================== 查询文章测试 ====================
    
    @Test
    fun `should get article detail with content when article exists`() {
        // Given
        val articleId = 1L
        val userId = 1L
        val article = TestFixtures.createPublishedArticle(id = articleId, userId = userId)
        val content = TestFixtures.createTestArticleContent(
            articleId = articleId,
            content = "文章详细内容"
        )
        val user = TestFixtures.createTestUser(id = userId)
        
        every { articleRepository.findById(articleId) } returns Optional.of(article)
        every { articleContentRepository.findByArticleId(articleId) } returns Optional.of(content)
        every { userRepository.findById(userId) } returns Optional.of(user)
        
        // When
        val result = articleService.getArticleDetail(articleId)
        
        // Then
        assertThat(result.id).isEqualTo(articleId)
        assertThat(result.content).isEqualTo("文章详细内容")
        assertThat(result.author).isEqualTo(user.nickname)
        
        verify(exactly = 1) { articleRepository.findById(articleId) }
        verify(exactly = 1) { articleContentRepository.findByArticleId(articleId) }
    }
    
    @Test
    fun `should throw exception when getting non-existent article`() {
        // Given
        val articleId = 999L
        every { articleRepository.findById(articleId) } returns Optional.empty()
        
        // When & Then
        assertThatThrownBy { articleService.getArticleDetail(articleId) }
            .isInstanceOf(NoSuchElementException::class.java)
            .hasMessageContaining("文章不存在")
        
        verify(exactly = 1) { articleRepository.findById(articleId) }
    }
    
    @Test
    fun `should find articles with query`() {
        // Given
        val query = com.derek.stackblog.domain.dto.ArticleQueryDTO(
            page = 0,
            size = 10,
            sortBy = "createTime",
            sortDirection = "DESC"
        )
        val articles = listOf(
            TestFixtures.createTestArticle(id = 1L, title = "文章1"),
            TestFixtures.createTestArticle(id = 2L, title = "文章2")
        )
        val page = PageImpl(articles, PageRequest.of(0, 10), articles.size.toLong())
        val user = TestFixtures.createTestUser(id = 1L)
        
        every { articleRepository.findAll(any<org.springframework.data.jpa.domain.Specification<com.derek.stackblog.domain.entity.Article>>(), any<org.springframework.data.domain.Pageable>()) } returns page
        every { userRepository.findById(any()) } returns Optional.of(user)
        
        // When
        val result = articleService.findArticles(query)
        
        // Then
        assertThat(result.content.size).isEqualTo(2)
        assertThat(result.totalElements).isEqualTo(2L)
        
        verify(exactly = 1) { articleRepository.findAll(any<org.springframework.data.jpa.domain.Specification<com.derek.stackblog.domain.entity.Article>>(), any<org.springframework.data.domain.Pageable>()) }
    }
    
    @Test
    fun `should return empty page when no articles found`() {
        // Given
        val query = com.derek.stackblog.domain.dto.ArticleQueryDTO(
            page = 0,
            size = 10,
            sortBy = "createTime",
            sortDirection = "DESC"
        )
        val page = PageImpl<com.derek.stackblog.domain.entity.Article>(emptyList(), PageRequest.of(0, 10), 0L)
        
        every { articleRepository.findAll(any<org.springframework.data.jpa.domain.Specification<com.derek.stackblog.domain.entity.Article>>(), any<org.springframework.data.domain.Pageable>()) } returns page
        
        // When
        val result = articleService.findArticles(query)
        
        // Then
        assertThat(result.content).isEmpty()
        assertThat(result.totalElements).isEqualTo(0L)
    }
}
