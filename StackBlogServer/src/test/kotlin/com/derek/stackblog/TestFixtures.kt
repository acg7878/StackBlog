package com.derek.stackblog

import com.derek.stackblog.domain.dto.*
import com.derek.stackblog.domain.entity.*
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * 测试数据固件 - 提供可复用的测试数据构建器
 */
object TestFixtures {
    
    // ==================== User ====================
    
    fun createTestUser(
        id: Long? = null,
        username: String = "testuser",
        password: String = "\$2a\$10\$hashedpassword",
        nickname: String = "测试用户",
        email: String? = "test@example.com"
    ) = User(
        username = username,
        password = password,
        nickname = nickname,
        email = email
    ).apply {
        id?.let { this.id = it }
    }
    
    fun createAdminUser(id: Long? = null) = createTestUser(
        id = id,
        username = "admin",
        nickname = "管理员",
        email = "admin@example.com"
    )
    
    // ==================== Role ====================
    
    fun createTestRole(
        id: Long? = null,
        name: String = "ROLE_USER",
        description: String = "普通用户"
    ) = Role(
        name = name,
        description = description
    ).apply {
        id?.let { this.id = it }
    }
    
    // ==================== Article ====================
    
    fun createTestArticle(
        id: Long? = null,
        title: String = "测试文章",
        userId: Long = 1L,
        top: Boolean = false,
        recommended: Boolean = false,
        status: Int = 1
    ) = Article(
        title = title,
        userId = userId,
        top = top,
        recommended = recommended,
        status = status
    ).apply {
        id?.let { this.id = it }
    }
    
    fun createPublishedArticle(id: Long? = null, userId: Long = 1L) = 
        createTestArticle(id = id, userId = userId, status = 1)
    
    fun createDraftArticle(id: Long? = null, userId: Long = 1L) = 
        createTestArticle(id = id, userId = userId, status = 0)
    
    // ==================== ArticleContent ====================
    
    fun createTestArticleContent(
        id: Long? = null,
        articleId: Long = 1L,
        content: String = "这是测试文章的内容",
        contentMd: String? = null
    ) = ArticleContent(
        articleId = articleId,
        content = content,
        contentMd = contentMd
    ).apply {
        id?.let { this.id = it }
    }
    
    // ==================== Comment ====================
    
    fun createTestComment(
        id: Long? = null,
        articleId: Long = 1L,
        userId: Long? = null,
        nickname: String = "访客",
        email: String = "guest@example.com",
        content: String = "这是一条测试评论",
        status: String = "PENDING",
        ip: String? = null
    ) = Comment(
        articleId = articleId,
        userId = userId,
        nickname = nickname,
        email = email,
        content = content,
        status = status,
        ip = ip
    ).apply {
        id?.let { this.id = it }
    }
    
    fun createApprovedComment(id: Long? = null, articleId: Long = 1L) = 
        createTestComment(id = id, articleId = articleId, status = "APPROVED")
    
    // ==================== Tag ====================
    
    fun createTestTag(
        id: Long? = null,
        name: String = "测试标签",
        description: String? = null
    ) = Tag(
        name = name,
        description = description
    ).apply {
        id?.let { this.id = it }
    }
    
    // ==================== ArticleType ====================
    
    fun createTestArticleType(
        id: Long? = null,
        name: String = "技术分享",
        description: String? = null
    ) = ArticleType(
        name = name,
        description = description
    ).apply {
        id?.let { this.id = it }
    }
    
    // ==================== DTOs ====================
    
    fun createRegisterDTO(
        username: String = "newuser",
        password: String = "password123",
        nickname: String = "新用户",
        email: String? = "new@example.com"
    ) = RegisterDTO(
        username = username,
        password = password,
        nickname = nickname,
        email = email
    )
    
    fun createLoginDTO(
        username: String = "testuser",
        password: String = "password123"
    ) = LoginDTO(
        username = username,
        password = password
    )
    
    fun createArticleDTO(
        title: String = "测试文章标题",
        content: String = "测试文章内容",
        description: String? = null,
        tags: List<Long>? = null,
        top: Boolean? = false,
        recommended: Boolean? = false,
        status: Int? = 1
    ) = ArticleDTO(
        title = title,
        content = content,
        description = description,
        tags = tags,
        top = top,
        recommended = recommended,
        status = status
    )
    
    fun createCommentDTO(
        articleId: Long = 1L,
        nickname: String = "测试访客",
        email: String? = "guest@test.com",
        content: String = "测试评论内容",
        parentId: Long? = null
    ) = CommentDTO(
        articleId = articleId,
        nickname = nickname,
        email = email,
        content = content,
        parentId = parentId
    )
}
