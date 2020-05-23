package com.raiink.inv.configure;

import java.io.Serializable;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @description: redis配置类
 * @author: hulei
 * @create: 2020-05-16 23:10:34
 */
@Configuration
public class RedisConfig implements Serializable {
  // 指定redisTemplate的序列化方式
  @Bean("myRedisTemplate")
  public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
    RedisTemplate<String, Object> template = new RedisTemplate();
    template.setConnectionFactory(factory);
    Jackson2JsonRedisSerializer jackson2JsonRedisSerializer =
        new Jackson2JsonRedisSerializer(Object.class);
    ObjectMapper om = new ObjectMapper();
    om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
    om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
    jackson2JsonRedisSerializer.setObjectMapper(om);
    StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
    // key采用String的序列化方式
    template.setKeySerializer(stringRedisSerializer);
    // hash的key也采用String的序列化方式
    template.setHashKeySerializer(stringRedisSerializer);
    // value序列化方式采用jackson
    template.setValueSerializer(jackson2JsonRedisSerializer);
    // hash的value序列化方式采用jackson
    template.setHashValueSerializer(jackson2JsonRedisSerializer);
    template.afterPropertiesSet();
    return template;
  }

  /**
   * @description 配置redis注解的失效时间，指定序列化方式
   * @param redisConnectionFactory
   * @return org.springframework.cache.CacheManager
   * @author hulei
   * @date 2020-05-17 08:18:13
   */
  @Bean
  public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
    return new RedisCacheManager(
        RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory),
        this.getRedisCacheConfigurationWithTtl(2 * 60), // 默认策略，未配置的 key 会使用这个
        this.getRedisCacheConfigurationMap() // 指定 key 策略
        );
  }

  // 对于特定的redis注解，此处设置失效时间
  private Map<String, RedisCacheConfiguration> getRedisCacheConfigurationMap() {
    Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = new HashMap<>();
    redisCacheConfigurationMap.put("invCacheAll", this.getRedisCacheConfigurationWithTtl(3 * 60));
    return redisCacheConfigurationMap;
  }

  // 其他的redis注解，使用默认的失效时间
  private RedisCacheConfiguration getRedisCacheConfigurationWithTtl(Integer seconds) {
    Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer =
        new Jackson2JsonRedisSerializer<>(Object.class);
    ObjectMapper om = new ObjectMapper();
    om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
    om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
    jackson2JsonRedisSerializer.setObjectMapper(om);

    RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
    redisCacheConfiguration =
        redisCacheConfiguration
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    jackson2JsonRedisSerializer))
            .entryTtl(Duration.ofSeconds(seconds));

    return redisCacheConfiguration;
  }
}
