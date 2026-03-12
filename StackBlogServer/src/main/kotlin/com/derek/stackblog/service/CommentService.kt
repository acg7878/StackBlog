package com.derek.stackblog.service

import com.derek.stackblog.domain.dto.CommentDTO
import com.derek.stackblog.domain.dto.CommentResponseDTO
import com.derek.stackblog.domain.entity.Comment
import com.derek.stackblog.repository.CommentRepository
import com.derek.stackblog.repository.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import jakarta.persistence.criteria.Predicate

/**
 * 评论服务
 */
@Service
@Transactional
class CommentService(
    private val commentRepository: CommentRepository,
    private val userRepository: UserRepository
) {
    
    /**
     * 创建评论
     */
    fun createComment(dto: CommentDTO, userId: Long?, ip: String?): CommentResponseDTO {
        val comment = Comment(
            articleId = dto.articleId,
            userId = userId,
            parentId = dto.parentId,
            nickname = dto.nickname,
            email = dto.email,
            qq = dto.qq,
            url = dto.url,
            content = dto.content,
            ip = ip,
            status = "PENDING" // 默认待审核
        )
        
        val savedComment = commentRepository.save(comment)
        return toResponseDTO(savedComment)
    }
    
    /**
     * 获取所有评论列表（管理后台）
     */
    fun getComments(status: String?, articleId: Long?, pageable: Pageable): Page<CommentResponseDTO> {
        val spec = Specification<Comment> { root, _, cb ->
            val predicates = mutableListOf<Predicate>()
            
            status?.let {
                predicates.add(cb.equal(root.get<String>("status"), it))
            }
            
            articleId?.let {
                predicates.add(cb.equal(root.get<Long>("articleId"), it))
            }
            
            cb.and(*predicates.toTypedArray())
        }
        
        return commentRepository.findAll(spec, pageable).map { toResponseDTO(it) }
    }
    
    /**
     * 获取文章的评论列表
     */
    fun getArticleComments(articleId: Long, pageable: Pageable): Page<CommentResponseDTO> {
        return commentRepository.findByArticleIdAndParentIdIsNull(articleId, pageable)
            .map { comment ->
                val replies = commentRepository.findByParentId(comment.id!!)
                    .map { toResponseDTO(it) }
                toResponseDTO(comment, replies)
            }
    }
    
    /**
     * 审核评论
     */
    fun approveComment(id: Long): CommentResponseDTO {
        val comment = commentRepository.findById(id).orElseThrow {
            throw NoSuchElementException("评论不存在")
        }
        comment.status = "APPROVED"
        val savedComment = commentRepository.save(comment)
        return toResponseDTO(savedComment)
    }
    
    /**
     * 拒绝评论
     */
    fun rejectComment(id: Long): CommentResponseDTO {
        val comment = commentRepository.findById(id).orElseThrow {
            throw NoSuchElementException("评论不存在")
        }
        comment.status = "REJECTED"
        val savedComment = commentRepository.save(comment)
        return toResponseDTO(savedComment)
    }
    
    /**
     * 删除评论
     */
    fun deleteComment(id: Long) {
        commentRepository.deleteById(id)
    }
    
    /**
     * 点赞评论
     */
    fun supportComment(id: Long): CommentResponseDTO {
        val comment = commentRepository.findById(id).orElseThrow {
            throw NoSuchElementException("评论不存在")
        }
        comment.support++
        val savedComment = commentRepository.save(comment)
        return toResponseDTO(savedComment)
    }
    
    /**
     * 转换为响应DTO
     */
    private fun toResponseDTO(comment: Comment, replies: List<CommentResponseDTO> = emptyList()): CommentResponseDTO {
        return CommentResponseDTO(
            id = comment.id!!,
            articleId = comment.articleId,
            userId = comment.userId,
            parentId = comment.parentId,
            nickname = comment.nickname,
            avatar = comment.avatar,
            email = comment.email,
            url = comment.url,
            content = comment.content,
            status = comment.status,
            support = comment.support,
            oppose = comment.oppose,
            createTime = comment.createTime,
            replies = replies
        )
    }
}
