package com.derek.stackblog.controller

import com.derek.stackblog.domain.dto.ResponseDTO
import com.derek.stackblog.domain.entity.Tag
import com.derek.stackblog.repository.TagRepository
import org.springframework.web.bind.annotation.*

/**
 * 标签控制器
 */
@RestController
@RequestMapping("/api/tags")
class TagController(
    private val tagRepository: TagRepository
) {
    
    /**
     * 获取所有标签列表
     */
    @GetMapping
    fun getTags(): ResponseDTO<List<Tag>> {
        val tags = tagRepository.findAll()
        return ResponseDTO.success(tags)
    }
    
    /**
     * 根据ID获取标签
     */
    @GetMapping("/{id}")
    fun getTag(@PathVariable id: Long): ResponseDTO<Tag> {
        val tag = tagRepository.findById(id)
            .orElseThrow { RuntimeException("标签不存在") }
        return ResponseDTO.success(tag)
    }
}
