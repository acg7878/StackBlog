package com.derek.stackblog.security

import com.derek.stackblog.domain.entity.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

/**
 * 自定义用户详情
 */
class CustomUserDetails(
    val userId: Long,
    private val username: String,
    private val password: String,
    private val authorities: Collection<GrantedAuthority>,
    private val enabled: Boolean
) : UserDetails {
    
    companion object {
        fun create(user: User): CustomUserDetails {
            val authorities = user.roles.map { role ->
                SimpleGrantedAuthority(role.name)
            }
            
            return CustomUserDetails(
                userId = user.id!!,
                username = user.username,
                password = user.password,
                authorities = authorities,
                enabled = user.status == 1
            )
        }
    }
    
    override fun getAuthorities(): Collection<GrantedAuthority> = authorities
    
    override fun getPassword(): String = password
    
    override fun getUsername(): String = username
    
    override fun isAccountNonExpired(): Boolean = true
    
    override fun isAccountNonLocked(): Boolean = true
    
    override fun isCredentialsNonExpired(): Boolean = true
    
    override fun isEnabled(): Boolean = enabled
}
