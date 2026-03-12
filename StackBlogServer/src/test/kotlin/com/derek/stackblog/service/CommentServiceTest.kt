package com.derek.stackblog.service

import com.derek.stackblog.TestFixtures
import com.derek.stackblog.domain.dto.CommentDTO
import com.derek.stackblog.repository.CommentRepository
import com.derek.stackblog.repository.UserRepository
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

@DisplayName("CommentService 单元测试")
class CommentServiceTest {
    
    private lateinit var commentRepository: CommentRepository
    private lateinit var userRepository: UserRepository
    private lateinit var commentService: CommentService
    
    @BeforeEach
    fun setUp() {
        commentRepository = mockk()
        userRepository = mockk()
        commentService = CommentService(commentRepository, userRepository)
    }
    
    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }
    
    // ==================== 创建评论测试 ====================
    
    @Test
    fun `should create comment successfully for guest user`() {
        // Given
        val dto = TestFixtures.createCommentDTO()
        val ip = "127.0.0.1"
        val savedComment = TestFixtures.createTestComment(
            id = 1L,
            articleId = dto.articleId,
            nickname = dto.nickname,
            email = dto.email ?: "guest@example.com",
            content = dto.content,
            status = "PENDING",
            ip = ip
        )
        
        every { commentRepository.save(any()) } returns savedComment
        
        // When
        val result = commentService.createComment(dto, null, ip)
        
        // Then
        assertThat(result.id).isEqualTo(1L)
        assertThat(result.nickname).isEqualTo(dto.nickname)
        assertThat(result.content).isEqualTo(dto.content)
        assertThat(result.status).isEqualTo("PENDING")
        assertThat(result.userId).isNull()
        
        verify(exactly = 1) { commentRepository.save(any()) }
    }
    
    @Test
    fun `should create comment for authenticated user`() {
        // Given
        val userId = 1L
        val dto = TestFixtures.createCommentDTO()
        val ip = "127.0.0.1"
        val savedComment = TestFixtures.createTestComment(
            id = 1L,
            articleId = dto.articleId,
            userId = userId,
            nickname = dto.nickname,
            content = dto.content,
            status = "PENDING",
            ip = ip
        )
        
        every { commentRepository.save(any()) } returns savedComment
        
        // When
        val result = commentService.createComment(dto, userId, ip)
        
        // Then
        assertThat(result.userId).isEqualTo(userId)
        assertThat(result.status).isEqualTo("PENDING")
        
        verify(exactly = 1) { commentRepository.save(any()) }
    }
    
    @Test
    fun `should create reply comment when parentId provided`() {
        // Given
        val parentCommentId = 1L
        val dto = TestFixtures.createCommentDTO(parentId = parentCommentId)
        val ip = "127.0.0.1"
        val savedComment = TestFixtures.createTestComment(
            id = 2L,
            articleId = dto.articleId,
            content = dto.content,
            ip = ip
        ).apply {
            parentId = parentCommentId
        }
        
        every { commentRepository.save(any()) } returns savedComment
        
        // When
        val result = commentService.createComment(dto, null, ip)
        
        // Then
        assertThat(result.parentId).isEqualTo(parentCommentId)
        
        verify(exactly = 1) { commentRepository.save(any()) }
    }
    
    // ==================== 查询评论测试 ====================
    
    @Test
    fun `should list comments by article with pagination`() {
        // Given
        val articleId = 1L
        val pageable = PageRequest.of(0, 10)
        val comments = listOf(
            TestFixtures.createApprovedComment(id = 1L, articleId = articleId),
            TestFixtures.createApprovedComment(id = 2L, articleId = articleId)
        )
        val page = PageImpl(comments, pageable, comments.size.toLong())
        
        every { commentRepository.findByArticleIdAndParentIdIsNull(articleId, pageable) } returns page
        every { commentRepository.findByParentId(any()) } returns emptyList()
        
        // When
        val result = commentService.getArticleComments(articleId, pageable)
        
        // Then
        assertThat(result.content).hasSize(2)
        assertThat(result.content[0].articleId).isEqualTo(articleId)
        assertThat(result.content[1].articleId).isEqualTo(articleId)
        
        verify(exactly = 1) { commentRepository.findByArticleIdAndParentIdIsNull(articleId, pageable) }
    }
    
    @Test
    fun `should return empty page when no comments`() {
        // Given
        val articleId = 1L
        val pageable = PageRequest.of(0, 10)
        val page = PageImpl<com.derek.stackblog.domain.entity.Comment>(emptyList(), pageable, 0L)
        
        every { commentRepository.findByArticleIdAndParentIdIsNull(articleId, pageable) } returns page
        
        // When
        val result = commentService.getArticleComments(articleId, pageable)
        
        // Then
        assertThat(result.content).isEmpty()
        assertThat(result.totalElements).isEqualTo(0L)
    }
    
    // ==================== 删除评论测试 ====================
    
    @Test
    fun `should delete comment successfully`() {
        // Given
        val commentId = 1L
        
        every { commentRepository.deleteById(commentId) } just Runs
        
        // When
        commentService.deleteComment(commentId)
        
        // Then
        verify(exactly = 1) { commentRepository.deleteById(commentId) }
    }
    
    // ==================== 审核评论测试 ====================
    
    @Test
    fun `should approve comment successfully`() {
        // Given
        val commentId = 1L
        val comment = TestFixtures.createTestComment(id = commentId, status = "PENDING")
        val approvedComment = comment.copy().apply {
            id = commentId
            status = "APPROVED"
        }
        
        every { commentRepository.findById(commentId) } returns Optional.of(comment)
        every { commentRepository.save(any()) } returns approvedComment
        
        // When
        val result = commentService.approveComment(commentId)
        
        // Then
        assertThat(result.status).isEqualTo("APPROVED")
        
        verify(exactly = 1) { commentRepository.save(any()) }
    }
    
    @Test
    fun `should reject comment successfully`() {
        // Given
        val commentId = 1L
        val comment = TestFixtures.createTestComment(id = commentId, status = "PENDING")
        val rejectedComment = comment.copy().apply {
            id = commentId
            status = "REJECTED"
        }
        
        every { commentRepository.findById(commentId) } returns Optional.of(comment)
        every { commentRepository.save(any()) } returns rejectedComment
        
        // When
        val result = commentService.rejectComment(commentId)
        
        // Then
        assertThat(result.status).isEqualTo("REJECTED")
        
        verify(exactly = 1) { commentRepository.save(any()) }
    }
    
    @Test
    fun `should throw exception when comment not found for approval`() {
        // Given
        val commentId = 999L
        every { commentRepository.findById(commentId) } returns Optional.empty()
        
        // When & Then
        assertThatThrownBy { commentService.approveComment(commentId) }
            .isInstanceOf(NoSuchElementException::class.java)
            .hasMessageContaining("评论不存在")
        
        verify(exactly = 0) { commentRepository.save(any()) }
    }
}
