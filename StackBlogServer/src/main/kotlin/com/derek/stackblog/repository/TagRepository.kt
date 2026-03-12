package com.derek.stackblog.repository

import com.derek.stackblog.domain.entity.Tag
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

/**
 * 标签数据访问接口
 */
@Repository
interface TagRepository : JpaRepository<Tag, Long> {
    
    fun findByName(name: String): Optional<Tag>
    
    fun findByNameIn(names: List<String>): List<Tag>
}
