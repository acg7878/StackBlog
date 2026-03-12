package com.derek.stackblog.domain.entity

import jakarta.persistence.*

/**
 * 角色实体
 */
@Entity
@Table(name = "sys_role")
data class Role(
    
    @Column(nullable = false, unique = true, length = 50)
    var name: String,
    
    @Column(length = 200)
    var description: String? = null,
    
    @Column(nullable = false)
    var available: Boolean = true,
    
    @ManyToMany(mappedBy = "roles")
    var users: MutableSet<User> = mutableSetOf(),
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "sys_role_resources",
        joinColumns = [JoinColumn(name = "role_id")],
        inverseJoinColumns = [JoinColumn(name = "resources_id")]
    )
    var resources: MutableSet<Resource> = mutableSetOf()
    
) : BaseEntity()
