package com.derek.stackblog.repository

import com.derek.stackblog.domain.entity.ArticleType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * 文章分类数据访问接口
 */
@Repository
interface ArticleTypeRepository : JpaRepository<ArticleType, Long> {
    
    fun findByParentIdIsNull(): List<ArticleType>
    
    fun findByParentId(parentId: Long): List<ArticleType>
    
    fun findByAvailable(available: Boolean): List<ArticleType>
}
