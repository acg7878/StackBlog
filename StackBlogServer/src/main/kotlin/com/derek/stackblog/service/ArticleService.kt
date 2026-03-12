package com.derek.stackblog.service

import com.derek.stackblog.domain.dto.*
import com.derek.stackblog.domain.entity.Article
import com.derek.stackblog.domain.entity.ArticleContent
import com.derek.stackblog.domain.entity.Tag
import com.derek.stackblog.repository.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import jakarta.persistence.criteria.Predicate

/**
 * 文章服务
 */
@Service
@Transactional
class ArticleService(
    private val articleRepository: ArticleRepository,
    private val articleContentRepository: ArticleContentRepository,
    private val articleTypeRepository: ArticleTypeRepository,
    private val tagRepository: TagRepository,
    private val userRepository: UserRepository
) {
    
    /**
     * 创建文章
     */
    fun createArticle(dto: ArticleDTO, userId: Long): ArticleResponseDTO {
        val article = Article(
            title = dto.title,
            userId = userId,
            coverImage = dto.coverImage,
            typeId = dto.typeId,
            top = dto.top ?: false,
            recommended = dto.recommended ?: false,
            original = dto.original ?: true,
            description = dto.description,
            keywords = dto.keywords,
            comment = dto.comment ?: true,
            password = dto.password,
            requiredAuth = dto.requiredAuth ?: false,
            editorType = dto.editorType ?: "markdown",
            status = dto.status ?: 0
        )
        
        // 保存文章
        val savedArticle = articleRepository.save(article)
        
        // 保存文章内容
        val content = ArticleContent(
            articleId = savedArticle.id!!,
            content = dto.content,
            contentMd = dto.contentMd
        )
        articleContentRepository.save(content)
        
        // 关联标签
        dto.tags?.takeIf { it.isNotEmpty() }?.let { tagIds ->
            val tags = tagRepository.findAllById(tagIds)
            savedArticle.tags.addAll(tags)
            articleRepository.save(savedArticle)
        }
        
        return toResponseDTO(savedArticle)
    }
    
    /**
     * 更新文章
     */
    fun updateArticle(id: Long, dto: ArticleDTO, userId: Long): ArticleResponseDTO {
        val article = articleRepository.findById(id).orElseThrow {
            throw NoSuchElementException("文章不存在")
        }
        
        // 验证权限
        if (article.userId != userId) {
            throw IllegalAccessException("无权限修改此文章")
        }
        
        article.title = dto.title
        article.coverImage = dto.coverImage
        article.typeId = dto.typeId
        article.top = dto.top ?: article.top
        article.recommended = dto.recommended ?: article.recommended
        article.original = dto.original ?: article.original
        article.description = dto.description
        article.keywords = dto.keywords
        article.comment = dto.comment ?: article.comment
        article.password = dto.password
        article.requiredAuth = dto.requiredAuth ?: article.requiredAuth
        article.editorType = dto.editorType ?: article.editorType
        article.status = dto.status ?: article.status
        
        // 更新文章内容
        articleContentRepository.findByArticleId(id).ifPresent { content ->
            content.content = dto.content
            content.contentMd = dto.contentMd
            articleContentRepository.save(content)
        }
        
        // 更新标签
        article.tags.clear()
        dto.tags?.takeIf { it.isNotEmpty() }?.let { tagIds ->
            val tags = tagRepository.findAllById(tagIds)
            article.tags.addAll(tags)
        }
        
        val savedArticle = articleRepository.save(article)
        return toResponseDTO(savedArticle)
    }
    
    /**
     * 删除文章
     */
    fun deleteArticle(id: Long, userId: Long) {
        val article = articleRepository.findById(id).orElseThrow {
            throw NoSuchElementException("文章不存在")
        }
        
        // 验证权限
        if (article.userId != userId) {
            throw IllegalAccessException("无权限删除此文章")
        }
        
        articleContentRepository.deleteByArticleId(id)
        articleRepository.deleteById(id)
    }
    
    /**
     * 获取文章详情
     */
    fun getArticleDetail(id: Long): ArticleDetailResponseDTO {
        val article = articleRepository.findById(id).orElseThrow {
            throw NoSuchElementException("文章不存在")
        }
        
        val content = articleContentRepository.findByArticleId(id).orElse(null)
        
        return toDetailResponseDTO(article, content)
    }
    
    /**
     * 分页查询文章
     */
    fun findArticles(query: ArticleQueryDTO): Page<ArticleResponseDTO> {
        val sort = if (query.sortDirection.uppercase() == "ASC") {
            Sort.by(query.sortBy).ascending()
        } else {
            Sort.by(query.sortBy).descending()
        }
        
        val pageable = PageRequest.of(query.page, query.size, sort)
        
        val spec = Specification<Article> { root, _, cb ->
            val predicates = mutableListOf<Predicate>()
            
            query.title?.let {
                predicates.add(cb.like(root.get("title"), "%$it%"))
            }
            
            query.typeId?.let {
                predicates.add(cb.equal(root.get<Long>("typeId"), it))
            }
            
            query.tagId?.let { tagId ->
                val tagsJoin = root.join<Article, Tag>("tags")
                predicates.add(cb.equal(tagsJoin.get<Long>("id"), tagId))
            }
            
            query.userId?.let {
                predicates.add(cb.equal(root.get<Long>("userId"), it))
            }
            
            query.status?.let {
                predicates.add(cb.equal(root.get<Int>("status"), it))
            }
            
            query.recommended?.let {
                predicates.add(cb.equal(root.get<Boolean>("recommended"), it))
            }
            
            query.top?.let {
                predicates.add(cb.equal(root.get<Boolean>("top"), it))
            }
            
            cb.and(*predicates.toTypedArray())
        }
        
        return articleRepository.findAll(spec, pageable).map { toResponseDTO(it) }
    }
    
    /**
     * 获取推荐文章
     */
    fun getRecommendedArticles(size: Int): List<ArticleResponseDTO> {
        val pageable = PageRequest.of(0, size)
        return articleRepository.findByRecommendedTrueAndStatus(1, pageable)
            .content.map { toResponseDTO(it) }
    }
    
    /**
     * 获取最新文章
     */
    fun getRecentArticles(size: Int): List<ArticleResponseDTO> {
        val pageable = PageRequest.of(0, size)
        return articleRepository.findRecentPublished(pageable)
            .content.map { toResponseDTO(it) }
    }
    
    /**
     * 转换为响应DTO
     */
    private fun toResponseDTO(article: Article): ArticleResponseDTO {
        val user = userRepository.findById(article.userId).orElse(null)
        val type = article.typeId?.let { articleTypeRepository.findById(it).orElse(null) }
        
        return ArticleResponseDTO(
            id = article.id!!,
            title = article.title,
            coverImage = article.coverImage,
            typeId = article.typeId,
            typeName = type?.name,
            userId = article.userId,
            author = user?.nickname ?: "Unknown",
            authorAvatar = user?.avatar,
            description = article.description,
            keywords = article.keywords,
            tags = article.tags.map { TagResponseDTO(it.id!!, it.name, it.description) },
            top = article.top,
            recommended = article.recommended,
            original = article.original,
            comment = article.comment,
            status = article.status,
            lookCount = article.lookCount,
            commentCount = article.commentCount,
            loveCount = article.loveCount,
            createTime = article.createTime,
            updateTime = article.updateTime
        )
    }
    
    /**
     * 转换为详情响应DTO
     */
    private fun toDetailResponseDTO(article: Article, content: ArticleContent?): ArticleDetailResponseDTO {
        val user = userRepository.findById(article.userId).orElse(null)
        val type = article.typeId?.let { articleTypeRepository.findById(it).orElse(null) }
        
        return ArticleDetailResponseDTO(
            id = article.id!!,
            title = article.title,
            coverImage = article.coverImage,
            typeId = article.typeId,
            typeName = type?.name,
            userId = article.userId,
            author = user?.nickname ?: "Unknown",
            authorAvatar = user?.avatar,
            content = content?.content ?: "",
            contentMd = content?.contentMd,
            description = article.description,
            keywords = article.keywords,
            tags = article.tags.map { TagResponseDTO(it.id!!, it.name, it.description) },
            top = article.top,
            recommended = article.recommended,
            original = article.original,
            comment = article.comment,
            status = article.status,
            editorType = article.editorType,
            lookCount = article.lookCount,
            commentCount = article.commentCount,
            loveCount = article.loveCount,
            createTime = article.createTime,
            updateTime = article.updateTime
        )
    }
}
