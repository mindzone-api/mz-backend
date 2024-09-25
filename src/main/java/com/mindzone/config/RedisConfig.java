package com.mindzone.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
public class RedisConfig {


    private final String redisHost = System.getenv("REDIS_HOST");

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        int redisPort = 6379;
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(redisHost, redisPort);

        return new LettuceConnectionFactory(configuration);
    }

    @Bean
    public RedisCacheManager cacheManager() {
        RedisCacheConfiguration cacheConfig = defaultCacheConfig(Duration.ofMinutes(10)).disableCachingNullValues();

        return RedisCacheManager.builder(redisConnectionFactory())
                .cacheDefaults(cacheConfig)
                .withCacheConfiguration("user", defaultCacheConfig(Duration.ofMinutes(20)))
                .withCacheConfiguration("chat", defaultCacheConfig(Duration.ofMinutes(20)))
                .withCacheConfiguration("prescription", defaultCacheConfig(Duration.ofMinutes(10)))
                .withCacheConfiguration("questionnaire", defaultCacheConfig(Duration.ofMinutes(10)))
                .withCacheConfiguration("report", defaultCacheConfig(Duration.ofMinutes(10)))
                .withCacheConfiguration("therapy", defaultCacheConfig(Duration.ofMinutes(15)))
                .build();
    }

    private RedisCacheConfiguration defaultCacheConfig(Duration duration) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper)))
                .entryTtl(duration);
    }
}
