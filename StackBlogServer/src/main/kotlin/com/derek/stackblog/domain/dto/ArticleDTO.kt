package com.derek.stackblog.domain.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

/**
 * 文章创建/更新DTO
 */
data class ArticleDTO(
    val id: Long? = null,
    
    @field:NotBlank(message = "标题不能为空")
    @field:Size(max = 200, message = "标题长度不能超过200")
    val title: String,
    
    val coverImage: String? = null,
    
    val typeId: Long? = null,
    
    @field:NotBlank(message = "文章内容不能为空")
    val content: String,
    
    val contentMd: String? = null,
    
    val description: String? = null,
    
    val keywords: String? = null,
    
    val tags: List<Long>? = null,
    
    val top: Boolean? = false,
    
    val recommended: Boolean? = false,
    
    val original: Boolean? = true,
    
    val comment: Boolean? = true,
    
    val password: String? = null,
    
    val requiredAuth: Boolean? = false,
    
    val editorType: String? = "markdown",
    
    val status: Int? = 0 // 0-草稿 1-已发布
)

/**
 * 文章响应DTO
 */
data class ArticleResponseDTO(
    val id: Long,
    val title: String,
    val coverImage: String? = null,
    val typeId: Long? = null,
    val typeName: String? = null,
    val userId: Long,
    val author: String,
    val authorAvatar: String? = null,
    val description: String? = null,
    val keywords: String? = null,
    val tags: List<TagResponseDTO> = emptyList(),
    val top: Boolean,
    val recommended: Boolean,
    val original: Boolean,
    val comment: Boolean,
    val status: Int,
    val lookCount: Int = 0,
    val commentCount: Int = 0,
    val loveCount: Int = 0,
    val createTime: LocalDateTime? = null,
    val updateTime: LocalDateTime? = null
)

/**
 * 文章详情响应DTO（包含内容）
 */
data class ArticleDetailResponseDTO(
    val id: Long,
    val title: String,
    val coverImage: String? = null,
    val typeId: Long? = null,
    val typeName: String? = null,
    val userId: Long,
    val author: String,
    val authorAvatar: String? = null,
    val content: String,
    val contentMd: String? = null,
    val description: String? = null,
    val keywords: String? = null,
    val tags: List<TagResponseDTO> = emptyList(),
    val top: Boolean,
    val recommended: Boolean,
    val original: Boolean,
    val comment: Boolean,
    val status: Int,
    val editorType: String,
    val lookCount: Int = 0,
    val commentCount: Int = 0,
    val loveCount: Int = 0,
    val createTime: LocalDateTime? = null,
    val updateTime: LocalDateTime? = null
)

/**
 * 文章查询条件DTO
 */
data class ArticleQueryDTO(
    val title: String? = null,
    val typeId: Long? = null,
    val tagId: Long? = null,
    val userId: Long? = null,
    val status: Int? = null,
    val recommended: Boolean? = null,
    val top: Boolean? = null,
    val page: Int = 0,
    val size: Int = 10,
    val sortBy: String = "createTime",
    val sortDirection: String = "DESC"
)

/**
 * 标签响应DTO
 */
data class TagResponseDTO(
    val id: Long,
    val name: String,
    val description: String? = null
)
