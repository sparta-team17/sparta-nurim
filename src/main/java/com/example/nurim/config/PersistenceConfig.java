package com.example.nurim.config;

import com.example.nurim.domain.auth.repository.RefreshTokenRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableJpaAuditing
@EnableRedisRepositories(basePackageClasses = RefreshTokenRepository.class)
public class PersistenceConfig {
}
