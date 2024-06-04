package org.example.books.config;

import org.example.books.config.properties.AppCacheProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableCaching
@EnableConfigurationProperties(AppCacheProperties.class)
public class CacheConfiguration {
  @Bean
  public CacheManager redisCacheManager(
      AppCacheProperties appCacheProperties, LettuceConnectionFactory lettuceConnectionFactory) {

    RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig();
    Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = new HashMap<>();

    appCacheProperties.getCacheNames().forEach(cacheName -> {
      var configuration = RedisCacheConfiguration
          .defaultCacheConfig()
          .entryTtl(appCacheProperties
              .getCaches()
              .get(cacheName)
              .getExpiry());
      redisCacheConfigurationMap.put(cacheName, configuration);
    });

    return RedisCacheManager.builder(lettuceConnectionFactory)
        .cacheDefaults(defaultConfig)
        .withInitialCacheConfigurations(redisCacheConfigurationMap)
        .build();
  }
}
