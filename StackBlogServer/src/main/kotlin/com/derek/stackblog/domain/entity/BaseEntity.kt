package com.derek.stackblog.domain.entity

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

/**
 * 基础实体类
 * 包含所有实体的公共字段：id, createTime, updateTime
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    
    @CreatedDate
    @Column(name = "create_time", nullable = false, updatable = false)
    var createTime: LocalDateTime? = null
    
    @LastModifiedDate
    @Column(name = "update_time", nullable = false)
    var updateTime: LocalDateTime? = null
}
