package com.derek.stackblog.repository

import com.derek.stackblog.domain.entity.ArticleContent
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

/**
 * 文章内容数据访问接口
 */
@Repository
interface ArticleContentRepository : JpaRepository<ArticleContent, Long> {
    
    fun findByArticleId(articleId: Long): Optional<ArticleContent>
    
    fun deleteByArticleId(articleId: Long)
}
