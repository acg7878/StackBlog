package com.derek.stackblog.service

import com.derek.stackblog.domain.dto.*
import com.derek.stackblog.domain.entity.User
import com.derek.stackblog.repository.RoleRepository
import com.derek.stackblog.repository.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * 用户服务
 */
@Service
@Transactional
class UserService(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val passwordEncoder: PasswordEncoder
) {
    
    /**
     * 用户注册
     */
    fun register(dto: RegisterDTO): UserResponseDTO {
        // 检查用户名是否存在
        if (userRepository.existsByUsername(dto.username)) {
            throw IllegalArgumentException("用户名已存在")
        }
        
        // 检查邮箱是否存在
        dto.email?.let { email ->
            if (userRepository.existsByEmail(email)) {
                throw IllegalArgumentException("邮箱已被注册")
            }
        }
        
        // 创建用户
        val user = User(
            username = dto.username,
            password = passwordEncoder.encode(dto.password)!!,
            nickname = dto.nickname,
            email = dto.email
        )
        
        // 分配默认角色
        roleRepository.findByName("ROLE_USER").ifPresent { role ->
            user.roles.add(role)
        }
        
        val savedUser = userRepository.save(user)
        return toResponseDTO(savedUser)
    }
    
    /**
     * 根据用户名查找用户
     */
    fun findByUsername(username: String): User? {
        return userRepository.findByUsername(username).orElse(null)
    }
    
    /**
     * 根据ID查找用户
     */
    fun findById(id: Long): UserResponseDTO {
        val user = userRepository.findById(id).orElseThrow {
            throw NoSuchElementException("用户不存在")
        }
        return toResponseDTO(user)
    }
    
    /**
     * 更新用户信息
     */
    fun updateUser(id: Long, dto: UpdateUserDTO): UserResponseDTO {
        val user = userRepository.findById(id).orElseThrow {
            throw NoSuchElementException("用户不存在")
        }
        
        dto.nickname?.let { user.nickname = it }
        dto.email?.let { email ->
            // 检查邮箱是否被其他用户使用
            if (userRepository.existsByEmail(email) && user.email != email) {
                throw IllegalArgumentException("邮箱已被使用")
            }
            user.email = email
        }
        dto.mobile?.let { user.mobile = it }
        dto.avatar?.let { user.avatar = it }
        dto.gender?.let { user.gender = it }
        dto.birthday?.let { user.birthday = it }
        dto.company?.let { user.company = it }
        dto.blog?.let { user.blog = it }
        dto.location?.let { user.location = it }
        
        val savedUser = userRepository.save(user)
        return toResponseDTO(savedUser)
    }
    
    /**
     * 获取用户列表
     */
    fun findAll(pageable: Pageable): Page<UserResponseDTO> {
        return userRepository.findAll(pageable).map { toResponseDTO(it) }
    }
    
    /**
     * 删除用户
     */
    fun deleteUser(id: Long) {
        userRepository.deleteById(id)
    }
    
    /**
     * 转换为响应DTO
     */
    private fun toResponseDTO(user: User): UserResponseDTO {
        return UserResponseDTO(
            id = user.id!!,
            username = user.username,
            nickname = user.nickname,
            email = user.email,
            mobile = user.mobile,
            avatar = user.avatar,
            gender = user.gender,
            birthday = user.birthday,
            company = user.company,
            blog = user.blog,
            location = user.location,
            score = user.score,
            experience = user.experience,
            roles = user.roles.map { it.name },
            createTime = user.createTime
        )
    }
}
