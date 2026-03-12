package com.derek.stackblog.domain.entity

import jakarta.persistence.*

/**
 * 标签实体
 */
@Entity
@Table(name = "biz_tags")
data class Tag(
    
    @Column(nullable = false, unique = true, length = 50)
    var name: String,
    
    @Column(length = 200)
    var description: String? = null,
    
    @ManyToMany(mappedBy = "tags")
    var articles: MutableSet<Article> = mutableSetOf()
    
) : BaseEntity()
