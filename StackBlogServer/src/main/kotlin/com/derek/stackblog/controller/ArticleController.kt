package com.derek.stackblog.controller

import com.derek.stackblog.domain.dto.*
import com.derek.stackblog.service.ArticleService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.web.bind.annotation.*

/**
 * 文章控制器
 */
@RestController
@RequestMapping("/api/articles")
class ArticleController(
    private val articleService: ArticleService
) {
    
    /**
     * 创建文章
     */
    @PostMapping
    fun createArticle(
        @Valid @RequestBody dto: ArticleDTO,
        @RequestAttribute("userId") userId: Long
    ): ResponseDTO<ArticleResponseDTO> {
        val article = articleService.createArticle(dto, userId)
        return ResponseDTO.success(article, "文章创建成功")
    }
    
    /**
     * 更新文章
     */
    @PutMapping("/{id}")
    fun updateArticle(
        @PathVariable id: Long,
        @Valid @RequestBody dto: ArticleDTO,
        @RequestAttribute("userId") userId: Long
    ): ResponseDTO<ArticleResponseDTO> {
        val article = articleService.updateArticle(id, dto, userId)
        return ResponseDTO.success(article, "文章更新成功")
    }
    
    /**
     * 删除文章
     */
    @DeleteMapping("/{id}")
    fun deleteArticle(
        @PathVariable id: Long,
        @RequestAttribute("userId") userId: Long
    ): ResponseDTO<Unit> {
        articleService.deleteArticle(id, userId)
        return ResponseDTO.success(message = "文章删除成功")
    }
    
    /**
     * 获取文章详情
     */
    @GetMapping("/{id}")
    fun getArticle(@PathVariable id: Long): ResponseDTO<ArticleDetailResponseDTO> {
        val article = articleService.getArticleDetail(id)
        return ResponseDTO.success(article)
    }
    
    /**
     * 分页查询文章
     */
    @GetMapping
    fun getArticles(
        @RequestParam(required = false) title: String?,
        @RequestParam(required = false) typeId: Long?,
        @RequestParam(required = false) userId: Long?,
        @RequestParam(required = false) status: Int?,
        @RequestParam(required = false) recommended: Boolean?,
        @RequestParam(required = false) top: Boolean?,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(defaultValue = "createTime") sortBy: String,
        @RequestParam(defaultValue = "DESC") sortDirection: String
    ): ResponseDTO<Page<ArticleResponseDTO>> {
        val query = ArticleQueryDTO(
            title = title,
            typeId = typeId,
            userId = userId,
            status = status,
            recommended = recommended,
            top = top,
            page = page,
            size = size,
            sortBy = sortBy,
            sortDirection = sortDirection
        )
        val articles = articleService.findArticles(query)
        return ResponseDTO.success(articles)
    }
    
    /**
     * 获取推荐文章
     */
    @GetMapping("/recommended")
    fun getRecommendedArticles(
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseDTO<List<ArticleResponseDTO>> {
        val articles = articleService.getRecommendedArticles(size)
        return ResponseDTO.success(articles)
    }
    
    /**
     * 获取最新文章
     */
    @GetMapping("/recent")
    fun getRecentArticles(
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseDTO<List<ArticleResponseDTO>> {
        val articles = articleService.getRecentArticles(size)
        return ResponseDTO.success(articles)
    }
}
