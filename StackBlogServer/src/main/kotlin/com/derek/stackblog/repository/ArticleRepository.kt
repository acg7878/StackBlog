package com.derek.stackblog.repository

import com.derek.stackblog.domain.entity.Article
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

/**
 * 文章数据访问接口
 */
@Repository
interface ArticleRepository : JpaRepository<Article, Long>, JpaSpecificationExecutor<Article> {
    
    fun findByUserId(userId: Long, pageable: Pageable): Page<Article>
    
    fun findByTypeId(typeId: Long, pageable: Pageable): Page<Article>
    
    fun findByStatus(status: Int, pageable: Pageable): Page<Article>
    
    fun findByRecommendedTrueAndStatus(status: Int, pageable: Pageable): Page<Article>
    
    fun findByTopTrueAndStatus(status: Int, pageable: Pageable): Page<Article>
    
    @Query("SELECT a FROM Article a WHERE a.status = 1 ORDER BY a.createTime DESC")
    fun findRecentPublished(pageable: Pageable): Page<Article>
    
    @Query("SELECT a FROM Article a WHERE a.status = 1 ORDER BY RAND()")
    fun findRandomPublished(pageable: Pageable): Page<Article>
    
    @Query("SELECT COUNT(a) FROM Article a WHERE a.userId = ?1")
    fun countByUserId(userId: Long): Long
}
