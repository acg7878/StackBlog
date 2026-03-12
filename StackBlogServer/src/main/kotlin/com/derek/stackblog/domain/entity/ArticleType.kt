package com.derek.stackblog.domain.entity

import jakarta.persistence.*

/**
 * 文章分类实体
 */
@Entity
@Table(name = "biz_type")
data class ArticleType(
    
    @Column(name = "parent_id")
    var parentId: Long? = null,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", insertable = false, updatable = false)
    var parent: ArticleType? = null,
    
    @Column(nullable = false, length = 50)
    var name: String,
    
    @Column(length = 200)
    var description: String? = null,
    
    var sort: Int = 0,
    
    @Column(nullable = false)
    var available: Boolean = true,
    
    @Column(length = 50)
    var icon: String? = null,
    
    @Column(length = 20)
    var position: String? = null, // 位置：navbar, sidebar
    
    @OneToMany(mappedBy = "parent")
    var children: MutableList<ArticleType> = mutableListOf()
    
) : BaseEntity()
