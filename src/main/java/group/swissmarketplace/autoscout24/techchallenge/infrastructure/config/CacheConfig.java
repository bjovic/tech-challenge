package group.swissmarketplace.autoscout24.techchallenge.infrastructure.config;

import group.swissmarketplace.autoscout24.techchallenge.domain.model.CarListingSearchCriteria;
import group.swissmarketplace.autoscout24.techchallenge.domain.model.PageInformation;
import group.swissmarketplace.autoscout24.techchallenge.infrastructure.repository.elasticsearch.query.CarListingSearchQueryBuilder;
import java.lang.reflect.Method;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfig {

  @Value("${app.cache.car-listings.ttl}")
  private Duration entryTtl;

  @Bean("cacheManager")
  public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
    final var defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
        .entryTtl(entryTtl)
        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
            new GenericJackson2JsonRedisSerializer())
        );
    return RedisCacheManager.builder(redisConnectionFactory)
        .withInitialCacheConfigurations(Map.of(CacheNames.CAR_LISTINGS, defaultCacheConfig))
        .build();
  }

  @Bean("carListingSearchKeyGenerator")
  public KeyGenerator carListingSearchKeyGenerator() {
    return (target, method, params) -> {
      CarListingSearchCriteria criteria = (CarListingSearchCriteria) params[0];
      PageInformation pageInformation = (PageInformation) params[1];
      return CarListingSearchQueryBuilder.buildCacheKey(criteria, pageInformation);
    };
  }
}
