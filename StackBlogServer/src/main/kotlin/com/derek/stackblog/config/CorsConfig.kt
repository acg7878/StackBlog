package com.derek.stackblog.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

/**
 * CORS跨域配置
 */
@Configuration
class CorsConfig {
    
    @Bean
    fun corsFilter(): CorsFilter {
        val source = UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration()
        
        config.allowCredentials = true
        config.addAllowedOriginPattern("*") // 允许所有源
        config.addAllowedHeader("*") // 允许所有请求头
        config.addAllowedMethod("*") // 允许所有HTTP方法
        config.maxAge = 3600L // 预检请求缓存时间
        
        source.registerCorsConfiguration("/**", config)
        return CorsFilter(source)
    }
}
