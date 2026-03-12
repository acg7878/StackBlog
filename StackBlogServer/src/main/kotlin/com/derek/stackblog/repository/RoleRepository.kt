package com.derek.stackblog.repository

import com.derek.stackblog.domain.entity.Role
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

/**
 * 角色数据访问接口
 */
@Repository
interface RoleRepository : JpaRepository<Role, Long> {
    
    fun findByName(name: String): Optional<Role>
}
