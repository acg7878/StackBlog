package com.derek.stackblog.domain.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * 用户注册DTO
 */
data class RegisterDTO(
    @field:NotBlank(message = "用户名不能为空")
    @field:Size(min = 3, max = 50, message = "用户名长度必须在3-50之间")
    val username: String,
    
    @field:NotBlank(message = "密码不能为空")
    @field:Size(min = 6, max = 50, message = "密码长度必须在6-50之间")
    val password: String,
    
    @field:NotBlank(message = "昵称不能为空")
    @field:Size(max = 50, message = "昵称长度不能超过50")
    val nickname: String,
    
    @field:Email(message = "邮箱格式不正确")
    val email: String? = null
)

/**
 * 用户登录DTO
 */
data class LoginDTO(
    @field:NotBlank(message = "用户名不能为空")
    val username: String,
    
    @field:NotBlank(message = "密码不能为空")
    val password: String
)

/**
 * 用户信息响应DTO
 */
data class UserResponseDTO(
    val id: Long,
    val username: String,
    val nickname: String,
    val email: String? = null,
    val mobile: String? = null,
    val avatar: String? = null,
    val gender: String? = null,
    val birthday: LocalDate? = null,
    val company: String? = null,
    val blog: String? = null,
    val location: String? = null,
    val score: Int,
    val experience: Int,
    val roles: List<String>,
    val createTime: LocalDateTime? = null
)

/**
 * 用户更新DTO
 */
data class UpdateUserDTO(
    val nickname: String? = null,
    val email: String? = null,
    val mobile: String? = null,
    val avatar: String? = null,
    val gender: String? = null,
    val birthday: LocalDate? = null,
    val company: String? = null,
    val blog: String? = null,
    val location: String? = null
)
