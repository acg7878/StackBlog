package com.derek.stackblog.domain.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

/**
 * 评论创建DTO
 */
data class CommentDTO(
    @field:NotNull(message = "文章ID不能为空")
    val articleId: Long,
    
    val parentId: Long? = null,
    
    @field:NotBlank(message = "昵称不能为空")
    val nickname: String,
    
    @field:Email(message = "邮箱格式不正确")
    val email: String? = null,
    
    val qq: String? = null,
    
    val url: String? = null,
    
    @field:NotBlank(message = "评论内容不能为空")
    val content: String
)

/**
 * 评论响应DTO
 */
data class CommentResponseDTO(
    val id: Long,
    val articleId: Long,
    val userId: Long? = null,
    val parentId: Long? = null,
    val nickname: String,
    val avatar: String? = null,
    val email: String? = null,
    val url: String? = null,
    val content: String,
    val status: String,
    val support: Int,
    val oppose: Int,
    val createTime: LocalDateTime? = null,
    val replies: List<CommentResponseDTO> = emptyList()
)
