package com.derek.stackblog.controller

import com.derek.stackblog.domain.dto.CommentDTO
import com.derek.stackblog.domain.dto.CommentResponseDTO
import com.derek.stackblog.domain.dto.ResponseDTO
import com.derek.stackblog.service.CommentService
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.*

/**
 * 评论控制器
 */
@RestController
@RequestMapping("/api/comments")
class CommentController(
    private val commentService: CommentService
) {
    
    /**
     * 创建评论
     */
    @PostMapping
    fun createComment(
        @Valid @RequestBody dto: CommentDTO,
        @RequestAttribute(value = "userId", required = false) userId: Long?,
        request: HttpServletRequest
    ): ResponseDTO<CommentResponseDTO> {
        val ip = request.remoteAddr
        val comment = commentService.createComment(dto, userId, ip)
        return ResponseDTO.success(comment, "评论提交成功，等待审核")
    }
    
    /**
     * 获取文章的评论列表
     */
    @GetMapping("/article/{articleId}")
    fun getArticleComments(
        @PathVariable articleId: Long,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseDTO<Page<CommentResponseDTO>> {
        val pageable = PageRequest.of(page, size)
        val comments = commentService.getArticleComments(articleId, pageable)
        return ResponseDTO.success(comments)
    }
    
    /**
     * 审核评论
     */
    @PutMapping("/{id}/approve")
    fun approveComment(@PathVariable id: Long): ResponseDTO<CommentResponseDTO> {
        val comment = commentService.approveComment(id)
        return ResponseDTO.success(comment, "评论已通过审核")
    }
    
    /**
     * 拒绝评论
     */
    @PutMapping("/{id}/reject")
    fun rejectComment(@PathVariable id: Long): ResponseDTO<CommentResponseDTO> {
        val comment = commentService.rejectComment(id)
        return ResponseDTO.success(comment, "评论已被拒绝")
    }
    
    /**
     * 删除评论
     */
    @DeleteMapping("/{id}")
    fun deleteComment(@PathVariable id: Long): ResponseDTO<Unit> {
        commentService.deleteComment(id)
        return ResponseDTO.success(message = "评论删除成功")
    }
    
    /**
     * 点赞评论
     */
    @PostMapping("/{id}/support")
    fun supportComment(@PathVariable id: Long): ResponseDTO<CommentResponseDTO> {
        val comment = commentService.supportComment(id)
        return ResponseDTO.success(comment, "点赞成功")
    }
}
