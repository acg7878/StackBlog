package com.derek.stackblog.domain.entity

import jakarta.persistence.*

/**
 * 文章内容实体（单独存储以优化查询性能）
 */
@Entity
@Table(name = "biz_article_content")
data class ArticleContent(
    
    @Column(name = "article_id", nullable = false, unique = true)
    var articleId: Long,
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", insertable = false, updatable = false)
    var article: Article? = null,
    
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    var content: String? = null, // HTML内容
    
    @Lob
    @Column(name = "content_md", columnDefinition = "LONGTEXT")
    var contentMd: String? = null // Markdown内容
    
) : BaseEntity()
