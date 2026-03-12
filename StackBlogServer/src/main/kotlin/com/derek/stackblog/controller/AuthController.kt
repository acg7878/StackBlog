package com.derek.stackblog.controller

import com.derek.stackblog.domain.dto.*
import com.derek.stackblog.service.UserService
import com.derek.stackblog.security.JwtTokenProvider
import jakarta.validation.Valid
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

/**
 * 认证控制器
 */
@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val userService: UserService,
    private val authenticationManager: AuthenticationManager,
    private val jwtTokenProvider: JwtTokenProvider
) {
    
    /**
     * 用户注册
     */
    @PostMapping("/register")
    fun register(@Valid @RequestBody dto: RegisterDTO): ResponseDTO<UserResponseDTO> {
        val user = userService.register(dto)
        return ResponseDTO.success(user, "注册成功")
    }
    
    /**
     * 用户登录
     */
    @PostMapping("/login")
    fun login(@Valid @RequestBody dto: LoginDTO): ResponseDTO<Map<String, Any?>> {
        val authentication: Authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(dto.username, dto.password)
        )
        
        val token = jwtTokenProvider.generateToken(authentication)
        val user = userService.findByUsername(dto.username)
        
        val response: Map<String, Any?> = mapOf(
            "token" to token,
            "user" to user
        )
        
        return ResponseDTO.success(response, "登录成功")
    }
    
    /**
     * 获取当前用户信息
     */
    @GetMapping("/me")
    fun getCurrentUser(@RequestAttribute("userId") userId: Long): ResponseDTO<UserResponseDTO> {
        val user = userService.findById(userId)
        return ResponseDTO.success(user)
    }
}
