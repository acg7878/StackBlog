package com.derek.stackblog.domain.entity

import jakarta.persistence.*

/**
 * 文章实体
 */
@Entity
@Table(name = "biz_article", indexes = [
    Index(name = "idx_user_id", columnList = "user_id"),
    Index(name = "idx_type_id", columnList = "type_id"),
    Index(name = "idx_status", columnList = "status"),
    Index(name = "idx_create_time", columnList = "create_time")
])
data class Article(
    
    @Column(nullable = false, length = 200)
    var title: String,
    
    @Column(name = "user_id", nullable = false)
    var userId: Long,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    var user: User? = null,
    
    @Column(name = "cover_image", length = 500)
    var coverImage: String? = null,
    
    @Column(name = "qrcode_path", length = 500)
    var qrcodePath: String? = null,
    
    @Column(nullable = false)
    var top: Boolean = false,
    
    @Column(name = "type_id")
    var typeId: Long? = null,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", insertable = false, updatable = false)
    var type: ArticleType? = null,
    
    @Column(nullable = false)
    var status: Int = 0, // 0-草稿 1-已发布
    
    @Column(nullable = false)
    var recommended: Boolean = false,
    
    @Column(nullable = false)
    var original: Boolean = true,
    
    @Column(length = 500)
    var description: String? = null,
    
    @Column(length = 200)
    var keywords: String? = null,
    
    @Column(nullable = false)
    var comment: Boolean = true,
    
    @Column(length = 50)
    var password: String? = null,
    
    @Column(name = "required_auth", nullable = false)
    var requiredAuth: Boolean = false,
    
    @Column(name = "editor_type", length = 20)
    var editorType: String = "markdown", // markdown, richtext
    
    @OneToOne(mappedBy = "article", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var content: ArticleContent? = null,
    
    @ManyToMany(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinTable(
        name = "biz_article_tags",
        joinColumns = [JoinColumn(name = "article_id")],
        inverseJoinColumns = [JoinColumn(name = "tag_id")]
    )
    var tags: MutableSet<Tag> = mutableSetOf(),
    
    @Transient
    var lookCount: Int = 0,
    
    @Transient
    var commentCount: Int = 0,
    
    @Transient
    var loveCount: Int = 0
    
) : BaseEntity()
