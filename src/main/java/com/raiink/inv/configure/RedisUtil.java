package com.raiink.inv.configure;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;

/**
 * @description: redis工具类
 * @author: hulei
 * @create: 2020-05-16 18:41:06
 */
public class RedisUtil {
  private static RedisTemplate<String, Object> redisTemplate =
      SpringUtil.getApplicationContext().getBean("myRedisTemplate", RedisTemplate.class);

  /**
   * @description 按s设置key的失效时间
   * @param key 键
   * @param time 失效时间
   * @return java.lang.Boolean
   * @author hulei
   * @date 2020-05-16 19:43:50
   */
  public static Boolean expire(String key, long time) {
    return redisTemplate.expire(key, time, TimeUnit.SECONDS);
  }

  /**
   * @description 删除redis的数据
   * @param key 键
   * @return java.lang.Boolean
   * @author hulei
   * @date 2020-05-16 19:21:38
   */
  public static Boolean del(String key) {
    return redisTemplate.delete(key);
  }

  /**
   * @description 塞入redis的string类型
   * @param key 键
   * @param value 值
   * @author hulei
   * @date 2020-05-16 19:18:56
   */
  public static void set(String key, Object value) {
    redisTemplate.opsForValue().set(key, value, 60, TimeUnit.SECONDS);
  }

  /**
   * @description 获取redis的string类型
   * @param key 键
   * @return java.lang.Object
   * @author hulei
   * @date 2020-05-16 19:19:34
   */
  public static Object get(String key) {
    return redisTemplate.opsForValue().get(key);
  }
}
