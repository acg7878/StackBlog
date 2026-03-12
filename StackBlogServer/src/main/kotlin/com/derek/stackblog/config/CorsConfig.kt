package com.derek.stackblog.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

/**
 * CORS 跨域配置
 */
@Configuration
class CorsConfig {
    
    @Bean
    fun corsFilter(): CorsFilter {
        val source = UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration()
        
        // 允许的源
        config.addAllowedOriginPattern("*")
        
        // 允许的请求头
        config.addAllowedHeader("*")
        
        // 允许的HTTP方法
        config.addAllowedMethod("*")
        
        // 是否允许携带凭证（如Cookie）
        config.allowCredentials = true
        
        // 预检请求的有效期（秒）
        config.maxAge = 3600L
        
        // 应用到所有路径
        source.registerCorsConfiguration("/**", config)
        
        return CorsFilter(source)
    }
}
