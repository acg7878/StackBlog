package com.derek.stackblog.service

import com.derek.stackblog.TestFixtures
import com.derek.stackblog.domain.dto.RegisterDTO
import com.derek.stackblog.domain.dto.UpdateUserDTO
import com.derek.stackblog.repository.RoleRepository
import com.derek.stackblog.repository.UserRepository
import io.mockk.*
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.DisplayName
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.*

@DisplayName("UserService 单元测试")
class UserServiceTest {
    
    private lateinit var userRepository: UserRepository
    private lateinit var roleRepository: RoleRepository
    private lateinit var passwordEncoder: PasswordEncoder
    private lateinit var userService: UserService
    
    @BeforeEach
    fun setUp() {
        userRepository = mockk()
        roleRepository = mockk()
        passwordEncoder = mockk()
        userService = UserService(userRepository, roleRepository, passwordEncoder)
    }
    
    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }
    
    // ==================== 注册测试 ====================
    
    @Test
    fun `should register user successfully when username is unique`() {
        // Given
        val dto = TestFixtures.createRegisterDTO()
        val encodedPassword = "encoded_password"
        val userRole = TestFixtures.createTestRole(id = 1L)
        val savedUser = TestFixtures.createTestUser(
            id = 1L,
            username = dto.username,
            password = encodedPassword,
            nickname = dto.nickname,
            email = dto.email
        )
        
        every { userRepository.existsByUsername(dto.username) } returns false
        every { userRepository.existsByEmail(dto.email!!) } returns false
        every { passwordEncoder.encode(dto.password) } returns encodedPassword
        every { roleRepository.findByName("ROLE_USER") } returns Optional.of(userRole)
        every { userRepository.save(any()) } returns savedUser
        
        // When
        val result = userService.register(dto)
        
        // Then
        assertThat(result.id).isEqualTo(1L)
        assertThat(result.username).isEqualTo(dto.username)
        assertThat(result.nickname).isEqualTo(dto.nickname)
        assertThat(result.email).isEqualTo(dto.email)
        
        verify(exactly = 1) { userRepository.existsByUsername(dto.username) }
        verify(exactly = 1) { userRepository.existsByEmail(dto.email!!) }
        verify(exactly = 1) { passwordEncoder.encode(dto.password) }
        verify(exactly = 1) { userRepository.save(any()) }
    }
    
    @Test
    fun `should throw exception when username already exists`() {
        // Given
        val dto = TestFixtures.createRegisterDTO(username = "existinguser")
        every { userRepository.existsByUsername(dto.username) } returns true
        
        // When & Then
        assertThatThrownBy { userService.register(dto) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("用户名已存在")
        
        verify(exactly = 1) { userRepository.existsByUsername(dto.username) }
        verify(exactly = 0) { userRepository.save(any()) }
    }
    
    @Test
    fun `should throw exception when email already exists`() {
        // Given
        val dto = TestFixtures.createRegisterDTO(email = "existing@example.com")
        every { userRepository.existsByUsername(dto.username) } returns false
        every { userRepository.existsByEmail(dto.email!!) } returns true
        
        // When & Then
        assertThatThrownBy { userService.register(dto) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("邮箱已被注册")
        
        verify(exactly = 1) { userRepository.existsByEmail(dto.email!!) }
        verify(exactly = 0) { userRepository.save(any()) }
    }
    
    @Test
    fun `should register user without email when email is null`() {
        // Given
        val dto = TestFixtures.createRegisterDTO(email = null)
        val encodedPassword = "encoded_password"
        val savedUser = TestFixtures.createTestUser(
            id = 1L,
            username = dto.username,
            password = encodedPassword,
            email = null
        )
        
        every { userRepository.existsByUsername(dto.username) } returns false
        every { passwordEncoder.encode(dto.password) } returns encodedPassword
        every { roleRepository.findByName("ROLE_USER") } returns Optional.empty()
        every { userRepository.save(any()) } returns savedUser
        
        // When
        val result = userService.register(dto)
        
        // Then
        assertThat(result.email).isNull()
        verify(exactly = 0) { userRepository.existsByEmail(any()) }
    }
    
    // ==================== 查找用户测试 ====================
    
    @Test
    fun `should find user by username when user exists`() {
        // Given
        val username = "testuser"
        val user = TestFixtures.createTestUser(id = 1L, username = username)
        every { userRepository.findByUsername(username) } returns Optional.of(user)
        
        // When
        val result = userService.findByUsername(username)
        
        // Then
        assertThat(result).isNotNull
        assertThat(result?.username).isEqualTo(username)
        
        verify(exactly = 1) { userRepository.findByUsername(username) }
    }
    
    @Test
    fun `should return null when user does not exist`() {
        // Given
        val username = "nonexistent"
        every { userRepository.findByUsername(username) } returns Optional.empty()
        
        // When
        val result = userService.findByUsername(username)
        
        // Then
        assertThat(result).isNull()
        
        verify(exactly = 1) { userRepository.findByUsername(username) }
    }
    
    @Test
    fun `should find user by id when user exists`() {
        // Given
        val userId = 1L
        val user = TestFixtures.createTestUser(id = userId)
        every { userRepository.findById(userId) } returns Optional.of(user)
        
        // When
        val result = userService.findById(userId)
        
        // Then
        assertThat(result.id).isEqualTo(userId)
        assertThat(result.username).isEqualTo(user.username)
        
        verify(exactly = 1) { userRepository.findById(userId) }
    }
    
    @Test
    fun `should throw exception when finding user by id fails`() {
        // Given
        val userId = 999L
        every { userRepository.findById(userId) } returns Optional.empty()
        
        // When & Then
        assertThatThrownBy { userService.findById(userId) }
            .isInstanceOf(NoSuchElementException::class.java)
            .hasMessageContaining("用户不存在")
        
        verify(exactly = 1) { userRepository.findById(userId) }
    }
    
    // ==================== 更新用户测试 ====================
    
    @Test
    fun `should update user successfully when user exists`() {
        // Given
        val userId = 1L
        val user = TestFixtures.createTestUser(id = userId)
        val updateDTO = UpdateUserDTO(
            nickname = "新昵称",
            email = "newemail@example.com"
        )
        val updatedUser = user.copy().apply {
            id = userId  // 确保id不为null
            nickname = updateDTO.nickname!!
            email = updateDTO.email
        }
        
        every { userRepository.findById(userId) } returns Optional.of(user)
        every { userRepository.existsByEmail(updateDTO.email!!) } returns false
        every { userRepository.save(any()) } returns updatedUser
        
        // When
        val result = userService.updateUser(userId, updateDTO)
        
        // Then
        assertThat(result.nickname).isEqualTo("新昵称")
        assertThat(result.email).isEqualTo("newemail@example.com")
        
        verify(exactly = 1) { userRepository.save(any()) }
    }
    
    @Test
    fun `should throw exception when updating with existing email`() {
        // Given
        val userId = 1L
        val user = TestFixtures.createTestUser(id = userId, email = "old@example.com")
        val updateDTO = UpdateUserDTO(email = "existing@example.com")
        
        every { userRepository.findById(userId) } returns Optional.of(user)
        every { userRepository.existsByEmail(updateDTO.email!!) } returns true
        
        // When & Then
        assertThatThrownBy { userService.updateUser(userId, updateDTO) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("邮箱已被使用")
        
        verify(exactly = 0) { userRepository.save(any()) }
    }
    
    @Test
    fun `should allow updating with same email`() {
        // Given
        val userId = 1L
        val email = "same@example.com"
        val user = TestFixtures.createTestUser(id = userId, email = email)
        val updateDTO = UpdateUserDTO(nickname = "新昵称", email = email)
        
        every { userRepository.findById(userId) } returns Optional.of(user)
        every { userRepository.existsByEmail(email) } returns true
        every { userRepository.save(any()) } returns user
        
        // When
        val result = userService.updateUser(userId, updateDTO)
        
        // Then
        assertThat(result.email).isEqualTo(email)
        verify(exactly = 1) { userRepository.save(any()) }
    }
}
