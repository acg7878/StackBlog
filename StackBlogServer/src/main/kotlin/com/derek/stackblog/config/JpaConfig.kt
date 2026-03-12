package com.derek.stackblog.config

import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

/**
 * JPA配置
 */
@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = ["com.derek.stackblog.repository"])
class JpaConfig
