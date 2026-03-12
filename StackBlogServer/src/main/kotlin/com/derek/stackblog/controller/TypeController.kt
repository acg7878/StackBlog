package com.derek.stackblog.controller

import com.derek.stackblog.domain.dto.ResponseDTO
import com.derek.stackblog.domain.entity.ArticleType
import com.derek.stackblog.repository.ArticleTypeRepository
import org.springframework.web.bind.annotation.*

/**
 * 文章分类控制器
 */
@RestController
@RequestMapping("/api/types")
class TypeController(
    private val articleTypeRepository: ArticleTypeRepository
) {
    
    /**
     * 获取所有分类列表
     */
    @GetMapping
    fun getTypes(): ResponseDTO<List<ArticleType>> {
        val types = articleTypeRepository.findAll()
        return ResponseDTO.success(types)
    }
    
    /**
     * 根据ID获取分类
     */
    @GetMapping("/{id}")
    fun getType(@PathVariable id: Long): ResponseDTO<ArticleType> {
        val type = articleTypeRepository.findById(id)
            .orElseThrow { RuntimeException("分类不存在") }
        return ResponseDTO.success(type)
    }
    
    /**
     * 获取可用的分类列表
     */
    @GetMapping("/available")
    fun getAvailableTypes(): ResponseDTO<List<ArticleType>> {
        val types = articleTypeRepository.findByAvailable(true)
        return ResponseDTO.success(types)
    }
}
