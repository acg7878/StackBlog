package com.derek.stackblog.repository

import com.derek.stackblog.domain.entity.Comment
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

/**
 * 评论数据访问接口
 */
@Repository
interface CommentRepository : JpaRepository<Comment, Long> {
    
    fun findByArticleId(articleId: Long, pageable: Pageable): Page<Comment>
    
    fun findByArticleIdAndParentIdIsNull(articleId: Long, pageable: Pageable): Page<Comment>
    
    fun findByParentId(parentId: Long): List<Comment>
    
    fun findByStatus(status: String, pageable: Pageable): Page<Comment>
    
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.articleId = ?1")
    fun countByArticleId(articleId: Long): Long
}
