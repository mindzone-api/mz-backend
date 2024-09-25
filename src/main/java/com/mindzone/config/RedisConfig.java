package com.mindzone.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;

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
        RedisCacheConfiguration cacheConfig = myDefaultCacheConfig(Duration.ofMinutes(10)).disableCachingNullValues();

        return RedisCacheManager.builder(redisConnectionFactory())
                .cacheDefaults(cacheConfig)
                .withCacheConfiguration("user", myDefaultCacheConfig(Duration.ofMinutes(5)))
                .withCacheConfiguration("chat", myDefaultCacheConfig(Duration.ofMinutes(20)))
                .withCacheConfiguration("prescription", myDefaultCacheConfig(Duration.ofMinutes(10)))
                .withCacheConfiguration("questionnaire", myDefaultCacheConfig(Duration.ofMinutes(10)))
                .withCacheConfiguration("report", myDefaultCacheConfig(Duration.ofMinutes(10)))
                .withCacheConfiguration("therapy", myDefaultCacheConfig(Duration.ofMinutes(15)))
                .build();
    }

    private RedisCacheConfiguration myDefaultCacheConfig(Duration duration) {
        return RedisCacheConfiguration
                .defaultCacheConfig()
                .entryTtl(duration)
                .serializeValuesWith(SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
    }
}
