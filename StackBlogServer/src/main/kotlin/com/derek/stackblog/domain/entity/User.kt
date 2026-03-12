package com.derek.stackblog.domain.entity

import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * 用户实体
 */
@Entity
@Table(name = "sys_user", indexes = [
    Index(name = "idx_username", columnList = "username"),
    Index(name = "idx_email", columnList = "email")
])
data class User(
    
    @Column(nullable = false, unique = true, length = 50)
    var username: String,
    
    @Column(nullable = false, length = 255)
    var password: String,
    
    @Column(nullable = false, length = 50)
    var nickname: String,
    
    @Column(length = 20)
    var mobile: String? = null,
    
    @Column(unique = true, length = 100)
    var email: String? = null,
    
    @Column(length = 20)
    var qq: String? = null,
    
    var birthday: LocalDate? = null,
    
    @Column(length = 10)
    var gender: String? = null, // MALE, FEMALE, UNKNOWN
    
    @Column(length = 500)
    var avatar: String? = null,
    
    @Column(name = "user_type", length = 20)
    var userType: String? = "NORMAL", // NORMAL, ADMIN
    
    @Column(length = 100)
    var company: String? = null,
    
    @Column(length = 200)
    var blog: String? = null,
    
    @Column(length = 100)
    var location: String? = null,
    
    @Column(length = 20)
    var source: String? = "REGISTER", // REGISTER, OAUTH
    
    var privacy: Int = 1, // 隐私设置
    
    var notification: Int = 1, // 通知设置
    
    var score: Int = 0,
    
    var experience: Int = 0,
    
    @Column(name = "reg_ip", length = 50)
    var regIp: String? = null,
    
    @Column(name = "last_login_ip", length = 50)
    var lastLoginIp: String? = null,
    
    @Column(name = "last_login_time")
    var lastLoginTime: LocalDateTime? = null,
    
    @Column(name = "login_count")
    var loginCount: Int = 0,
    
    @Column(length = 500)
    var remark: String? = null,
    
    @Column(nullable = false)
    var status: Int = 1, // 1-正常 0-禁用
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "sys_user_role",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")]
    )
    var roles: MutableSet<Role> = mutableSetOf()
    
) : BaseEntity()
