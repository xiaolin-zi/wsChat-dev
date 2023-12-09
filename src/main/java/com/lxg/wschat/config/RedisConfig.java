package com.lxg.wschat.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@EnableCaching // 开启缓存
@Configuration // 配置类
public class RedisConfig extends CachingConfigurerSupport {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {

        RedisTemplate<String, Object> template = new RedisTemplate<>();

        // 1. 创建了一个 RedisSerializer 对象，用于对 Redis 中的 key 进行序列化，这里使用的是 StringRedisSerializer，即将 key 序列化为字符串。
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();

        // 创建了一个 Jackson2JsonRedisSerializer 对象，用于对 Redis 中的 value 进行序列化，
        // 这里序列化的对象类型为 Object，即可以序列化任何类型的对象。同时，还需要配置一个 ObjectMapper 对象，
        // 用于指定序列化和反序列化时的一些特性，比如可见性、类型信息等
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        // ObjectMapper是Jackson的一个工作类，顾名思义他的作用是将JSON映射到Java对象即反序列化，或将Java对象映射到JSON即序列化
        ObjectMapper om = new ObjectMapper();
        /// 设置序列化时的可见性，第一个参数是选择序列化哪些属性，比如时序列化setter?还是filed?h第二个参数是选择哪些修饰符权限的属性来序列化，比如private或者public，这里的any是指对所有权限修饰的属性都可见(可序列化)
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 用于指定序列化输入的类型，比如非final类型的对象，必须指定序列化的类型，否则会报错
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        // 将ObjectMapper对象设置到Jackson2JsonRedisSerializer中，后续序列化时会使用该ObjectMapper对象
        jackson2JsonRedisSerializer.setObjectMapper(om);

        template.setConnectionFactory(factory);
        // key序列化方式，设置 RedisTemplate 的 key 序列化方式为 redisSerializer。
        template.setKeySerializer(redisSerializer);
        // value序列化 设置 RedisTemplate 的 value 序列化方式为 jackson2JsonRedisSerializer。
        template.setValueSerializer(jackson2JsonRedisSerializer);
        // value hashmap序列化，设置 RedisTemplate 的 hash value 序列化方式为 jackson2JsonRedisSerializer。
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        // 返回的 RedisTemplate 对象可以用于对 Redis 数据库进行各种操作，比如读取、写入、删除等。
        return template;
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        // 创建一个Redis序列化器，用于将字符串进行序列化
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();
        // 创建一个Jackson2JsonRedis序列化器，用于将对象进行序列化
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);

        // 解决查询缓存转换异常的问题
        ObjectMapper om = new ObjectMapper();

        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);

        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);

        jackson2JsonRedisSerializer.setObjectMapper(om);

        // 配置序列化（解决乱码的问题）,过期时间600秒
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(600))
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer))
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer))
            .disableCachingNullValues();// 对空数据不操作

        // 使用以上配置创建一个Redis缓存管理器
        RedisCacheManager cacheManager = RedisCacheManager.builder(factory).cacheDefaults(config).build();
        return cacheManager;
    }
}
