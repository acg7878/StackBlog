package com.derek.stackblog.domain.entity

import jakarta.persistence.*

/**
 * 评论实体
 */
@Entity
@Table(name = "biz_comment", indexes = [
    Index(name = "idx_article_id", columnList = "article_id"),
    Index(name = "idx_user_id", columnList = "user_id"),
    Index(name = "idx_parent_id", columnList = "parent_id")
])
data class Comment(
    
    @Column(name = "user_id")
    var userId: Long? = null,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    var user: User? = null,
    
    @Column(name = "article_id", nullable = false)
    var articleId: Long,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", insertable = false, updatable = false)
    var article: Article? = null,
    
    @Column(name = "parent_id")
    var parentId: Long? = null,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", insertable = false, updatable = false)
    var parent: Comment? = null,
    
    @Column(length = 20)
    var qq: String? = null,
    
    @Column(nullable = false, length = 50)
    var nickname: String,
    
    @Column(length = 500)
    var avatar: String? = null,
    
    @Column(length = 100)
    var email: String? = null,
    
    @Column(length = 200)
    var url: String? = null,
    
    @Column(nullable = false, length = 20)
    var status: String = "APPROVED", // PENDING, APPROVED, REJECTED
    
    @Column(length = 50)
    var ip: String? = null,
    
    @Column(length = 20)
    var lng: String? = null,
    
    @Column(length = 20)
    var lat: String? = null,
    
    @Column(length = 100)
    var address: String? = null,
    
    @Column(length = 50)
    var os: String? = null,
    
    @Column(name = "os_short_name", length = 20)
    var osShortName: String? = null,
    
    @Column(length = 50)
    var browser: String? = null,
    
    @Column(name = "browser_short_name", length = 20)
    var browserShortName: String? = null,
    
    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    var content: String,
    
    @Column(length = 500)
    var remark: String? = null,
    
    var support: Int = 0,
    
    var oppose: Int = 0
    
) : BaseEntity()
