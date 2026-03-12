package com.derek.stackblog.repository

import com.derek.stackblog.domain.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import java.util.*

/**
 * 用户数据访问接口
 */
@Repository
interface UserRepository : JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    
    fun findByUsername(username: String): Optional<User>
    
    fun findByEmail(email: String): Optional<User>
    
    fun existsByUsername(username: String): Boolean
    
    fun existsByEmail(email: String): Boolean
}
