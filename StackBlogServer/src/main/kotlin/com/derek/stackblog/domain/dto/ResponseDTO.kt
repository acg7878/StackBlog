package com.derek.stackblog.domain.dto

/**
 * 统一响应格式
 */
data class ResponseDTO<T>(
    val code: Int,
    val message: String,
    val data: T? = null,
    val timestamp: Long = System.currentTimeMillis()
) {
    companion object {
        fun <T> success(data: T? = null, message: String = "操作成功"): ResponseDTO<T> {
            return ResponseDTO(200, message, data)
        }
        
        fun <T> error(code: Int = 500, message: String = "操作失败", data: T? = null): ResponseDTO<T> {
            return ResponseDTO(code, message, data)
        }
    }
}

/**
 * 分页响应
 */
data class PageResponseDTO<T>(
    val content: List<T>,
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int,
    val isFirst: Boolean,
    val isLast: Boolean
)
