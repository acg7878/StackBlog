package com.derek.stackblog.domain.entity

import jakarta.persistence.*

/**
 * 资源实体（菜单/权限）
 */
@Entity
@Table(name = "sys_resources")
data class Resource(
    
    @Column(nullable = false, unique = true, length = 100)
    var name: String,
    
    @Column(nullable = false, length = 20)
    var type: String, // MENU, BUTTON
    
    @Column(length = 200)
    var url: String? = null,
    
    @Column(length = 100)
    var permission: String? = null,
    
    @Column(name = "parent_id")
    var parentId: Long? = null,
    
    var sort: Int = 0,
    
    @Column(length = 50)
    var icon: String? = null,
    
    @Column(nullable = false)
    var available: Boolean = true,
    
    @ManyToMany(mappedBy = "resources")
    var roles: MutableSet<Role> = mutableSetOf()
    
) : BaseEntity()
